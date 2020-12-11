package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillInfoDTO;
import com.github.cmag.financemanager.mapper.BillMapper;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.repository.BillRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import liquibase.util.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The Bill Service.
 */
@Service
public class BillService extends BaseService<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillMapper mapper;

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

    return super.save(billDTO);
  }

  /**
   * Find all the bills based on the given Pageable.
   *
   * @return A Page that contains the bills as well as the total number of bills and other
   * information.
   */
  public Page<BillInfoDTO> findAllPaginated(Pageable pageable) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by(AppConstants.UPDATED_ON).descending());
    }

    Page<Bill> page = billRepository.findAll(pageable);
    return page.map(bill -> mapper.mapToInfo(bill));
  }

  /**
   * Find the bills whose code start with the given text.
   *
   * @param text The text to search with.
   * @return A List of BillInfoDTO.
   */
  public List<BillInfoDTO> filterByCode(String text) {
    // Do not search for bills if the given text is null or less than 3 characters.
    if (StringUtils.isEmpty(text) || text.length() < 3) {
      return new ArrayList<>();
    }

    // Find the bills whose code start with the given text.
    return billRepository.findByCodeStartsWith(text).stream()
        .map(bill -> mapper.mapToInfo(bill))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

  }

  /**
   * Update the given bill to paid.
   *
   * @param billInfoDTO The bill to be updated.
   */
  public void updateToPaid(BillInfoDTO billInfoDTO) {
    Bill bill = billRepository.getOne(billInfoDTO.getId());
    if (!bill.isPaid()) {
      bill.setPaid(true);
      bill.setPaidOn(new Date());
      billRepository.save(bill);
    }
  }
}
