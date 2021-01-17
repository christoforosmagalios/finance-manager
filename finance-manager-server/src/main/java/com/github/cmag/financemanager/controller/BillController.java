package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillFilterDTO;
import com.github.cmag.financemanager.dto.PageItem;
import com.github.cmag.financemanager.dto.UploadDTO;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.service.BillService;
import com.github.cmag.financemanager.service.FileService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Bill Rest controller.
 */
@RestController
@RequestMapping("/bill")
public class BillController extends BaseController<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  @Autowired
  private BillService billService;

  @Value("${finance.manager.temp.path}")
  private String tempPath;

  /**
   * Save the given image and return the URL, the base64 and image type.
   *
   * @param multipartFile MultipartFile
   * @return UploadDTO
   */
  @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UploadDTO upload(@RequestParam("file") MultipartFile multipartFile)
      throws IOException {
    return fileService.upload(multipartFile, tempPath);
  }

  /**
   * Save the given bill.
   *
   * @param billDTO The bill to be saved.
   * @return The saved bill.
   */
  @PostMapping(path = "/save")
  public BillDTO saveBill(@Valid @RequestBody BillDTO billDTO) throws IOException {
    return billService.saveBill(billDTO);
  }

  /**
   * Find all the bills by taking into account the given Pageable.
   *
   * @param pageable Contains pagination info.
   * @return The Page result.
   */
  @PostMapping("/paginated")
  public PageItem<BillDTO> findAll(Pageable pageable, @RequestBody BillFilterDTO filter) {
    return billService.findAllPaginated(pageable, filter);
  }

  /**
   * Find the bills whose code start with the given text.
   *
   * @param text The text to search with.
   * @return A List of BillDTO.
   */
  @GetMapping("/findByCode/{text}")
  public List<BillDTO> findByCode(@PathVariable String text) {
    return billService.filterByCode(text);
  }

  /**
   * Get the current month's pending amount for the unpaid bills.
   *
   * @return The pending amount.
   */
  @GetMapping("/pending")
  public double getPendingAmount() {
    return billService.getPendingAmount();
  }

  /**
   * Find all the bills that expire soon.
   *
   * @return The List of Bills that expire soon.
   */
  @GetMapping("/expireSoon")
  public List<BillDTO> findBillsThatExpireSoon() {
    return billService.findBillsThatExpireSoon();
  }

  /**
   * Set the Bill with the given id to paid.
   *
   * @param id The id of the Bill to be paid.
   */
  @PostMapping("/setToPaid")
  public void setToPaid(@RequestBody String id) {
    billService.setToPaid(id);
  }

  /**
   * Get the total number of bills for the logged in user.
   *
   * @return The bills count.
   */
  @GetMapping("/totalNumberOfBills")
  public long getTotalNumberOfBills() {
    return billService.getTotalNumberOfBills();
  }
}
