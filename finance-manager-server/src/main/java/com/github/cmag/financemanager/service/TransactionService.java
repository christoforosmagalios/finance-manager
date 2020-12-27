package com.github.cmag.financemanager.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.GroupedTransactionDTO;
import com.github.cmag.financemanager.dto.GroupedTransactionInfoDTO;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.dto.TransactionItemDTO;
import com.github.cmag.financemanager.es.index.TransactionIndex;
import com.github.cmag.financemanager.es.repository.TransactionEsRepository;
import com.github.cmag.financemanager.mapper.TransactionCategoryMapper;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.model.master.data.TransactionCategory;
import com.github.cmag.financemanager.repository.TransactionCategoryRepository;
import com.github.cmag.financemanager.repository.TransactionRepository;
import com.github.cmag.financemanager.util.Utils;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Transaction Service.
 */
@Service
@Transactional
public class TransactionService extends BaseService<TransactionDTO, Transaction> {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionCategoryRepository transactionCategoryRepository;

  @Autowired
  private TransactionEsRepository es;

  @Autowired
  private BillService billService;

  @Autowired
  private UserService userService;

  @Autowired
  private TransactionMapper mapper;

  @Autowired
  private TransactionCategoryMapper transactionCategoryMapper;

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  /**
   * Save the given transaction. In case the transaction is linked with a bill, update the bill to
   * paid.
   *
   * @param transactionDTO The transaction to be saved.
   * @return The saved transaction.
   */
  @Override
  public TransactionDTO save(TransactionDTO transactionDTO) {

    // In case the transaction is connected to a bill and the transaction category is not the
    // Bills category, set the proper transaction category.
    if (!Objects.isNull(transactionDTO.getBill())
        && !transactionDTO.getTransactionCategory().getCode()
        .equals(AppConstants.BILLS_TRANSACTION_CATEGORY_CODE)) {
      TransactionCategory transactionCategory
          = transactionCategoryRepository.findByCode(AppConstants.BILLS_TRANSACTION_CATEGORY_CODE);
      transactionDTO.setTransactionCategory(transactionCategoryMapper.map(transactionCategory));
    }

    transactionDTO.setUser(userService.getLoggedInUserDTO());
    TransactionDTO transaction = super.save(transactionDTO);
    if (!Objects.isNull(transactionDTO.getBill())) {
      this.billService.updateToPaid(transactionDTO.getBill());
    }
    es.save(mapper.mapToIndex(transaction));
    return transaction;
  }

  /**
   * Find all the transactions based on the given Pageable.
   *
   * @return A Page that contains the transactions as well as the total number of bills and other
   * information.
   */
  public Page<TransactionDTO> findAllPaginated(Pageable pageable) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by(AppConstants.UPDATED_ON).descending());
    }

    Criteria criteria = new Criteria("userId").is(userService.getLoggedInUserId());
    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    SearchHits<TransactionIndex> result = elasticsearchOperations.search(query, TransactionIndex.class);

    List<TransactionDTO> transactions = new ArrayList<>();
    List<SearchHit<TransactionIndex>> hits = result.getSearchHits();
    hits.forEach(hit -> {
      TransactionIndex item = hit.getContent();
      transactions.add(mapper.mapToDTO(item));
    });

    return new PageImpl<>(transactions);
  }

  /**
   * Find the transactions that are linked with the given bill id and do not have the given
   * transaction id.
   *
   * @param billId The Bill id.
   * @param transactionId The transaction Id.
   * @return A list of TransactionDTO.
   */
  public List<TransactionDTO> findTransactionsByBillId(String billId, String transactionId) {
    List<TransactionIndex> transactions;
    if (StringUtils.isBlank(transactionId)) {
      transactions = es.findByBillIdAndUserId(billId, userService.getLoggedInUserId());
    } else {
      transactions = es.findByBillIdAndIdNotAndUserId(billId, transactionId,
          userService.getLoggedInUserId());
    }
    return transactions.stream().map(t -> mapper.mapToDTO(t))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  /**
   * Delete and unindex the transaction with the given id.
   *
   * @param id The transaction id.
   */
  @Override
  public void delete(String id) {
    Transaction transaction = transactionRepository.getOne(id);
    if (transaction.getId().equals(userService.getLoggedInUserId())) {
      transactionRepository.delete(transaction);
      es.deleteById(id);
    } else {
      throw new FinanceManagerException(AppConstants.NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }
  }

  /**
   * Get the current month's transaction amount based on the given type.
   *
   * @param type The transaction type.
   * @return The monthly total spending amount.
   */
  public double getMonthlyTransactionAmount(boolean type) {
    long from = Utils.getFirstDayOfMonthInMil();
    long to = Utils.getLastDayOfMonthInMil();

    // Fetch transactions and sum their amount.
    return es.findByTypeAndDateBetweenAndUserId(type, from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();
  }

  /**
   * Calculate the annual balance.
   *
   * @return The annual balance.
   */
  public double getAnnualBalance() {
    long from = Utils.getFirstDayOfYearInMil();
    long to = Utils.getLastDayOfYearInMil();

    // Get the annual earnings.
    double earnings = es.findByTypeAndDateBetweenAndUserId(false, from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();
    // Get teh annual spendings.
    double spendings = es.findByTypeAndDateBetweenAndUserId(true, from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();

    return earnings - spendings;
  }

  /**
   * Get expenses grouped by category.
   *
   * @return A list of grouped expenses.
   */
  public GroupedTransactionDTO getGroupedExpenses() {
    long from = Utils.getFirstDayOfMonthInMil();
    long to = Utils.getLastDayOfMonthInMil();
    // Get the expense transactions.
    List<TransactionIndex> transactions = es.findByTypeAndDateBetweenAndUserId(true, from, to,
        userService.getLoggedInUserId());

    List<GroupedTransactionInfoDTO> transactionInfos = transactions.stream()
        .collect(groupingBy(TransactionIndex::getCategoryName))
        .entrySet().stream().map(x -> {
          double sumAmount = x.getValue().stream().mapToDouble(TransactionIndex::getAmount).sum();
          String color = x.getValue().get(0).getCategoryColor();
          return new GroupedTransactionInfoDTO(x.getKey(), color, sumAmount);
        }).collect(toList());

    return new GroupedTransactionDTO(new Date(from), new Date(to), transactionInfos);
  }

  public List<TransactionItemDTO> getTransactionsPerDay() {
    int from = Utils.getFirstDayOfMonthReversed();
    int to = Utils.getLastDayOfMonthReversed();

    List<TransactionIndex> transactions =
        es.findByDateReversedGreaterThanEqualAndDateReversedLessThanEqualAndUserId(from, to,
            userService.getLoggedInUserId());

    List<TransactionItemDTO> result = new ArrayList<>();
    while (from <= to) {
      double expense = filterByDateAndType(transactions, from, true);
      double income = filterByDateAndType(transactions, from, false);
      String day = Utils.getDayFromReversedIntegerDate(from);
      result.add(new TransactionItemDTO(day, income, expense));
      from++;
    }
    return result;
  }

  /**
   * Find the transactions in the given list with the given date and the given type and sum their
   * amount.
   *
   * @param transactions Transaction list.
   * @param date An integer date representation (format: yyyyMMdd).
   * @param type Transaction type.
   * @return The summed amount.
   */
  private double filterByDateAndType(List<TransactionIndex> transactions, final int date,
      final boolean type) {
    return transactions.stream()
        .filter(t -> (t.getDateReversed() == date && t.isType() == type))
        .mapToDouble(o -> o.getAmount()).sum();
  }
}
