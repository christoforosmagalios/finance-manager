package com.github.cmag.financemanager.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.GroupedTransactionDTO;
import com.github.cmag.financemanager.dto.GroupedTransactionInfoDTO;
import com.github.cmag.financemanager.dto.PageItem;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.dto.TransactionFilterDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * The Transaction Service.
 */
@Service
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

  @Autowired
  private RestHighLevelClient restHighLevelClient;

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
    return transaction;
  }

  /**
   * Find all the transactions based on the given Pageable.
   *
   * @param filter The Transaction filter.
   * @param pageable The Pageable object.
   * @return A Page that contains the transactions as well as the total number of bills and other
   * information.
   */
  public PageItem<TransactionDTO> findAllPaginated(Pageable pageable, TransactionFilterDTO filter) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by("categoryName").descending());
    }

    // Get the search criteria.
    Criteria criteria = getCriteria(filter);
    // Initialize the search query with the criteria and the pageable.
    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    // Find the transactions.
    SearchHits<TransactionIndex> result = elasticsearchOperations
        .search(query, TransactionIndex.class);

    List<TransactionDTO> transactions = new ArrayList<>();
    // Map the transactions to dto representations.
    result.getSearchHits().forEach(hit -> {
      transactions.add(mapper.mapToDTO(hit.getContent()));
    });
    // Create a new page item based on the results.
    return new PageItem<>(transactions, result.getTotalHits());
  }

  /**
   * Construct the criteria for the transaction search operation.
   *
   * @param filter Contains the selected filters.
   * @return The search criteria.
   */
  private Criteria getCriteria(TransactionFilterDTO filter) {
    // Initialize the criteria with the logged in user id.
    Criteria criteria = new Criteria(AppConstants.USER_ID).is(userService.getLoggedInUserId());
    // If the filter contains a specific bill code, add it to the criteria.
    if (!StringUtils.isBlank(filter.getBillCode())) {
      criteria.and(new Criteria(AppConstants.T_BILL_CODE).startsWith(filter.getBillCode()));
    }
    // If the filter contains a specific category, add it to the criteria.
    if (!Objects.isNull(filter.getTransactionCategory())) {
      criteria.and(
          new Criteria(AppConstants.T_CATEGORY_ID).is(filter.getTransactionCategory().getId()));
    }
    // If the filter contains a specific type, add it to the criteria.
    if (!Objects.isNull(filter.getType())) {
      criteria.and(new Criteria(AppConstants.T_TYPE).is(filter.getType()));
    }
    // If the filter contains a specific amount from, add it to the criteria.
    if (filter.getAmountFrom() > 0) {
      criteria.and(new Criteria(AppConstants.T_AMOUNT).greaterThanEqual(filter.getAmountFrom()));
    }
    // If the filter contains a specific amount to, add it to the criteria.
    if (filter.getAmountTo() > 0) {
      criteria.and(new Criteria(AppConstants.T_AMOUNT).lessThanEqual(filter.getAmountTo()));
    }
    // If the filter contains a specific date from, add it to the criteria.
    if (!Objects.isNull(filter.getDateFrom())) {
      criteria.and(new Criteria(AppConstants.T_DATE)
          .greaterThanEqual(Utils.getDateReversed(filter.getDateFrom())));
    }
    // If the filter contains a specific date from, add it to the criteria.
    if (!Objects.isNull(filter.getDateTo())) {
      criteria.and(new Criteria(AppConstants.T_DATE)
          .lessThanEqual(Utils.getDateReversed(filter.getDateTo())));
    }
    return criteria;
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
    if (transaction.getUser().getId().equals(userService.getLoggedInUserId())) {
      transactionRepository.deleteById(transaction.getId());
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
    LocalDate from = Utils.getFirstDayOfMonth();
    LocalDate to = Utils.getLastDayOfMonth();

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
    LocalDate from = Utils.getFirstDayOfYear();
    LocalDate to = Utils.getLastDayOfYear();

    // Get the annual earnings.
    double earnings = es
        .findByTypeAndDateBetweenAndUserId(false, from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();
    // Get teh annual spendings.
    double spendings = es
        .findByTypeAndDateBetweenAndUserId(true, from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();

    return earnings - spendings;
  }

  /**
   * Get expenses grouped by category.
   *
   * @return A list of grouped expenses.
   */
  public GroupedTransactionDTO getGroupedExpenses() {
    LocalDate from = Utils.getFirstDayOfMonth();
    LocalDate to = Utils.getLastDayOfMonth();
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

    return new GroupedTransactionDTO(from, to, transactionInfos);
  }

  public List<TransactionItemDTO> getTransactionsPerDay() {
    LocalDate from = Utils.getFirstDayOfMonth();
    LocalDate to = Utils.getLastDayOfMonth();

    List<TransactionIndex> transactions = es.findByDateBetweenAndUserId(from, to,
        userService.getLoggedInUserId());

    List<TransactionItemDTO> result = new ArrayList<>();
    while (!from.equals(to)) {
      double expense = filterByDateAndType(transactions, from, true);
      double income = filterByDateAndType(transactions, from, false);
      String day = String.valueOf(from.getDayOfMonth());
      result.add(new TransactionItemDTO(day, income, expense));
      from = from.plusDays(1);
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
  private double filterByDateAndType(List<TransactionIndex> transactions, final LocalDate date,
      final boolean type) {
    return transactions.stream()
        .filter(t -> (t.getDate().equals(date) && t.isType() == type))
        .mapToDouble(o -> o.getAmount()).sum();
  }
}
