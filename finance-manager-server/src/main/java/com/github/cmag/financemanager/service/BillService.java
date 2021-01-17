package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillFilterDTO;
import com.github.cmag.financemanager.dto.PageItem;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.es.index.BillIndex;
import com.github.cmag.financemanager.es.repository.BillEsRepository;
import com.github.cmag.financemanager.mapper.BillMapper;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.repository.BillRepository;
import com.github.cmag.financemanager.util.Utils;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * The Bill Service.
 */
@Service
public class BillService extends BaseService<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  @Autowired
  private UserService userService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillEsRepository es;

  @Autowired
  private BillMapper mapper;

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  @Value("${finance.manager.bill.images.path}")
  private String imagesPath;

  /**
   * Copy the bill image file and save the bill.
   *
   * @param billDTO The bill to be saved.
   * @return The saved billDTO.
   */
  public BillDTO saveBill(BillDTO billDTO) throws IOException {

    // Find the old image path.
    String existingImg = null;
    if (StringUtils.isNotEmpty(billDTO.getId())) {
      existingImg = billRepository.getImgUrl(billDTO.getId());
    } else {
      billDTO.setCode(RandomStringUtils.randomAlphanumeric(6).toUpperCase());
    }
    // Replace the new image with the old one.
    billDTO.setImgPath(fileService.copy(billDTO.getImgPath(), existingImg, imagesPath));

    billDTO.setUser(userService.getLoggedInUserDTO());
    BillDTO bill = super.save(billDTO);
    if (bill.isPaid()) {
      transactionService.createTransactionForBill(bill);
    } else {
      transactionService.deleteLinkedTransactions(bill);
    }
    return bill;
  }

  /**
   * Find all the bills based on the given Pageable.
   *
   * @return A Page that contains the bills as well as the total number of bills and other
   * information.
   */
  public PageItem<BillDTO> findAllPaginated(Pageable pageable, BillFilterDTO filter) {
    // Get the search criteria.
    Criteria criteria = getCriteria(filter);
    // Initialize the search query with the criteria and the pageable.
    Query query = new CriteriaQuery(criteria).setPageable(pageable);
    // Find the bills.
    SearchHits<BillIndex> result = elasticsearchOperations.search(query, BillIndex.class);

    List<BillDTO> bills = new ArrayList<>();
    // Map the bills to dto representations.
    result.getSearchHits().forEach(hit -> {
      bills.add(mapper.mapToDTO(hit.getContent()));
    });
    // Create a new page item based on the results.
    return new PageItem<>(bills, result.getTotalHits());
  }

  /**
   * Construct the criteria for the bill search operation.
   *
   * @param filter Contains the selected filters.
   * @return The search criteria.
   */
  private Criteria getCriteria(BillFilterDTO filter) {
    // Initialize the criteria with the logged in user id.
    Criteria criteria = new Criteria(AppConstants.USER_ID).is(userService.getLoggedInUserId());

    // If the filter contains a specific type, add it to the criteria.
    if (!Objects.isNull(filter.getPaid())) {
      criteria.and(new Criteria(AppConstants.B_PAID).is(filter.getPaid()));
    }
    // If the filter contains a specific bill code, add it to the criteria.
    if (!StringUtils.isBlank(filter.getCode())) {
      criteria.and(new Criteria(AppConstants.B_CODE).startsWith(filter.getCode()));
    }
    // If the filter contains a specific category, add it to the criteria.
    if (!Objects.isNull(filter.getBillCategory())) {
      criteria.and(
          new Criteria(AppConstants.B_CATEGORY_ID).is(filter.getBillCategory().getId()));
    }
    // If the filter contains a specific amount from, add it to the criteria.
    if (filter.getAmountFrom() > 0) {
      criteria.and(new Criteria(AppConstants.B_AMOUNT).greaterThanEqual(filter.getAmountFrom()));
    }
    // If the filter contains a specific amount to, add it to the criteria.
    if (filter.getAmountTo() > 0) {
      criteria.and(new Criteria(AppConstants.B_AMOUNT).lessThanEqual(filter.getAmountTo()));
    }
    // If the filter contains a specific issue date from, add it to the criteria.
    if (!Objects.isNull(filter.getIssueDateFrom())) {
      criteria.and(new Criteria(AppConstants.B_ISSUE_DATE)
          .greaterThanEqual(Utils.getDateReversed(filter.getIssueDateFrom())));
    }
    // If the filter contains a specific issue date from, add it to the criteria.
    if (!Objects.isNull(filter.getIssueDateTo())) {
      criteria.and(new Criteria(AppConstants.B_ISSUE_DATE)
          .lessThanEqual(Utils.getDateReversed(filter.getIssueDateTo())));
    }
    // If the filter contains a specific due date from, add it to the criteria.
    if (!Objects.isNull(filter.getDueDateFrom())) {
      criteria.and(new Criteria(AppConstants.B_DUE_DATE)
          .greaterThanEqual(Utils.getDateReversed(filter.getDueDateFrom())));
    }
    // If the filter contains a specific due date from, add it to the criteria.
    if (!Objects.isNull(filter.getDueDateTo())) {
      criteria.and(new Criteria(AppConstants.B_DUE_DATE)
          .lessThanEqual(Utils.getDateReversed(filter.getDueDateTo())));
    }
    return criteria;
  }

  /**
   * Find the bills whose code start with the given text.
   *
   * @param text The text to search with.
   * @return A List of BillDTO.
   */
  public List<BillDTO> filterByCode(String text) {
    // Do not search for bills if the given text is null or less than 3 characters.
    if (StringUtils.isBlank(text) || text.length() < 3) {
      return new ArrayList<>();
    }

    // Find the bills whose code start with the given text.
    return es.findByCodeStartsWithAndUserId(text, userService.getLoggedInUserId()).stream()
        .map(bill -> mapper.mapToDTO(bill))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

  }

  /**
   * Update the given bill to paid.
   *
   * @param billDTO The bill to be updated.
   */
  public void updateToPaid(BillDTO billDTO) {
    Bill bill = billRepository.getOne(billDTO.getId());
    if (!bill.isPaid()) {
      bill.setPaid(true);
      bill.setPaidOn(LocalDate.now());
      save(mapper.map(bill));
    }
  }

  /**
   * Delete the bill with the given id.
   *
   * @param id The bill id.
   */
  @Override
  public void delete(String id) {
    Bill bill = billRepository.getOne(id);
    // Get transactions linked to this bill.
    List<TransactionDTO> transactions = transactionService.findTransactionsByBillId(id, null);
    // In case a transaction is linked to this bill, throw exception.
    if (!transactions.isEmpty()) {
      throw new FinanceManagerException(AppConstants.BILL_IS_LINKED, HttpStatus.BAD_REQUEST);
    } else if (bill.getUser().getId().equals(userService.getLoggedInUserId())) {
      billRepository.deleteById(bill.getId());
    } else {
      throw new FinanceManagerException(AppConstants.NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }
  }

  /**
   * Get the current month's pending amount for the unpaid bills.
   *
   * @return The pending amount.
   */
  public double getPendingAmount() {
    return getPendingAmount(userService.getLoggedInUserId());
  }

  /**
   * Get the current month's pending amount for the unpaid bills.
   *
   * @param userId The id of the User.
   * @return The pending amount.
   */
  public double getPendingAmount(String userId) {
    LocalDate to = Utils.getLastDayOfMonth();

    // Fetch bills and sum their amount.
    return es.findByPaidFalseAndDueDateLessThanEqualAndUserIdOrderByDueDateDesc(to,
        userId).stream().mapToDouble(o -> o.getAmount()).sum();
  }

  /**
   * Get the bills that expire in the next 2 weeks for the logged in user.
   *
   * @return A list of bills.
   */
  public List<BillDTO> findBillsThatExpireSoon() {
    return findBillsThatExpireSoonByUserId(userService.getLoggedInUserId());
  }

  /**
   * Get the bills that expire in the next 2 weeks for the user with the given id.
   *
   * @return A list of bills.
   */
  public List<BillDTO> findBillsThatExpireSoonByUserId(String userId) {
    // Find the bills that expire in the next 2 weeks.
    List<BillIndex> bills = es.findByPaidFalseAndDueDateLessThanEqualAndUserIdOrderByDueDateDesc(
        LocalDate.now().plusWeeks(2), userId);
    // Convert them to DTOs and return the bills.
    return bills.stream().map(b -> mapper.mapToDTO(b)).collect(Collectors.toList());
  }

  /**
   * Set the given bill to paid and create a transaction.
   *
   * @param id The id of the Bill to be paid.
   */
  public void setToPaid(String id) {
    BillDTO bill = findOne(id);
    bill.setPaid(true);
    save(bill);
    transactionService.createTransactionForBill(bill);
  }

  /**
   * Get the total number of bills for the logged in user.
   *
   * @return The bills count.
   */
  public long getTotalNumberOfBills() {
    return es.countByUserId(userService.getLoggedInUserId());
  }
}
