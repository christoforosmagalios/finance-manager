package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillFilterDTO;
import com.github.cmag.financemanager.dto.PageItem;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * The Bill Service.
 */
@Service
public class BillService extends BaseService<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  @Autowired
  private UserService userService;

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
    es.save(mapper.mapToIndex(bill));
    return bill;
  }

  /**
   * Find all the bills based on the given Pageable.
   *
   * @return A Page that contains the bills as well as the total number of bills and other
   * information.
   */
  public PageItem<BillDTO> findAllPaginated(Pageable pageable, BillFilterDTO filter) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by(AppConstants.B_ISSUED_ON).descending());
    }

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
      super.save(mapper.map(bill));
      es.save(mapper.mapToIndex(bill));
    }
  }

  /**
   * Delete and unindex the bill with the given id.
   *
   * @param id The bill id.
   */
  @Override
  public void delete(String id) {
    Bill bill = billRepository.getOne(id);
    if (bill.getId().equals(userService.getLoggedInUserId())) {
      billRepository.delete(bill);
      es.deleteById(id);
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
    LocalDate from = Utils.getFirstDayOfMonth();
    LocalDate to = Utils.getLastDayOfMonth();

    // Fetch bills and sum their amount.
    return es.findByPaidFalseAndDueDateBetweenAndUserId(from, to, userService.getLoggedInUserId())
        .stream().mapToDouble(o -> o.getAmount()).sum();
  }
}
