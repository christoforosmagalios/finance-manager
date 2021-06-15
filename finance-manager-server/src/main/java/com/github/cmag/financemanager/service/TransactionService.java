package com.github.cmag.financemanager.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.ChartDataSetsDTO;
import com.github.cmag.financemanager.dto.GroupedTransactionDTO;
import com.github.cmag.financemanager.dto.GroupedTransactionInfoDTO;
import com.github.cmag.financemanager.dto.PageItem;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.dto.TransactionFilterDTO;
import com.github.cmag.financemanager.dto.TransactionItemDTO;
import com.github.cmag.financemanager.dto.master.data.TransactionCategoryDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
@Slf4j
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
      log.error("User " + userService.getLoggedInUserId() + " is not allowed to delete this transaction.");
      throw new FinanceManagerException(AppConstants.NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }
  }

  /**
   * Get the current transaction amount based on the given month, year, type and user.
   *
   * @param month The Month.
   * @param year The Year.
   * @param type The transaction type.
   * @param userId The Id of the user.
   * @return The monthly total spending amount.
   */
  public double getTransactionAmount(int month, int year, boolean type, String userId) {
    LocalDate from = Utils.getFirstDayOfMonth(month, year);
    LocalDate to = Utils.getLastDayOfMonth(month, year);
    // Fetch transactions and sum their amount.
    return findTransactionsSum(from, to, type, userId);
  }

  /**
   * Get the current transaction amount based on the given month, year and type.
   *
   * @param month The Month.
   * @param year The Year.
   * @param type The transaction type.
   * @return The monthly total spending amount.
   */
  public double getTransactionAmount(int month, int year, boolean type) {
    return getTransactionAmount(month, year, type, userService.getLoggedInUserId());
  }

  /**
   * Get the current transaction amount based on the given year and type.
   *
   * @param year The Year.
   * @param type The transaction type.
   * @param userId The Id of the user.
   * @return The monthly total spending amount.
   */
  public double getAnnualTransactionAmount(int year, boolean type, String userId) {
    LocalDate from = Utils.getFirstDayOfYear(year);
    LocalDate to = Utils.getLastDayOfYear(year);
    // Fetch transactions and sum their amount.
    return findTransactionsSum(from, to, type, userId);
  }

  /**
   * Get the current transaction amount based on the given year and type.
   *
   * @param year The Year.
   * @param type The transaction type.
   * @return The monthly total spending amount.
   */
  public double getAnnualTransactionAmount(int year, boolean type) {
    return getAnnualTransactionAmount(year, type, userService.getLoggedInUserId());
  }

  /**
   * Find the transactions between the given dates based on the given type and return their sum.
   *
   * @param from Date from.
   * @param to Date to.
   * @param type The transaction type.
   * @param userId The Id of the user.
   * @return The sum of the transactions.
   */
  private double findTransactionsSum(LocalDate from, LocalDate to, boolean type, String userId) {
    // Fetch transactions and sum their amount.
    return es.findByTypeAndDateBetweenAndUserId(type, from, to, userId)
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
   * Get expenses grouped by category for the given month and year.
   *
   * @param month The month.
   * @param year The year.
   * @return A list of grouped expenses.
   */
  public GroupedTransactionDTO getGroupedExpenses(int month, int year) {
    LocalDate from = Utils.getFirstDayOfMonth(month, year);
    LocalDate to = Utils.getLastDayOfMonth(month, year);
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

  /**
   * Get the grouped transactions per day for the given month and year.
   *
   * @param month The month.
   * @param year The year.
   * @return A list of grouped transactions.
   */
  public List<TransactionItemDTO> getTransactionsPerDay(int month, int year) {
    LocalDate from = Utils.getFirstDayOfMonth(month, year);
    LocalDate to = Utils.getLastDayOfMonth(month, year);

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

  /**
   * If the given bill is not already linked with a transaction, create a new transaction for the
   * given bill.
   *
   * @param billDTO The Bill.
   */
  public void createTransactionForBill(BillDTO billDTO) {
    // Find the transactions that are linked to the given bill.
    List<TransactionDTO> transactions = findTransactionsByBillId(billDTO.getId(), null);
    // If no transaction is linked with the given bill, create a new transaction.
    if (transactions.isEmpty()) {
      TransactionDTO transaction = new TransactionDTO();
      transaction.setUser(userService.getLoggedInUserDTO());
      TransactionCategoryDTO category = transactionCategoryMapper.map(transactionCategoryRepository
          .findByCode(AppConstants.BILLS_TRANSACTION_CATEGORY_CODE));
      transaction.setTransactionCategory(category);
      transaction.setAmount(billDTO.getAmount());
      transaction.setBill(billDTO);
      transaction.setDate(LocalDate.now());
      transaction.setDescription(billDTO.getDescription());
      transaction.setType(true);
      super.save(transaction);
    }
  }

  /**
   * Delete the transactions that are linked to the given bill.
   *
   * @param bill The Bill.
   */
  public void deleteLinkedTransactions(BillDTO bill) {
    List<TransactionDTO> transactions = findTransactionsByBillId(bill.getId(), null);
    for (TransactionDTO t : transactions) {
      delete(t.getId());
    }
  }

  /**
   * Get the total number of transactions for the logged in user.
   *
   * @return The transactions count.
   */
  public long getTotalNumberOfTransactions() {
    return es.countByUserId(userService.getLoggedInUserId());
  }

  /**
   * Get the transactions for the given year grouped by month and by type.
   *
   * @param year The year.
   * @return The grouped transactions.
   */
  public List<ChartDataSetsDTO> getAnnualTransactionsByMonth(int year) {
    List<ChartDataSetsDTO> result = new ArrayList<>();
    result.add(new ChartDataSetsDTO("income", "#1cc88a"));
    result.add(new ChartDataSetsDTO("expenses", "#b83d53"));

    // Get income and expenses for each month.
    for (int i = 1; i < 13; i++) {
      // Get income.
      result.get(0).getData().add(getTransactionAmount(i, year, false));
      // Get expenses.
      result.get(1).getData().add(-1 * getTransactionAmount(i, year, true));
    }
    return result;
  }
}
