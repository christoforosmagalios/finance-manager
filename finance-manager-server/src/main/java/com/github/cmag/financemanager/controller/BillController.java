package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillInfoDTO;
import com.github.cmag.financemanager.dto.UploadDTO;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.service.BillService;
import com.github.cmag.financemanager.service.FileService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Value("${finance.manager.bill.images.temp.path}")
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
  public BillDTO saveBill(@RequestBody BillDTO billDTO) throws IOException {
    return billService.saveBill(billDTO);
  }

  /**
   * Find all the entities by taking into account the given Pageable.
   *
   * @param pageable Contains pagination info.
   * @return The Page result.
   */
  @GetMapping("/paginatedBills")
  public Page<BillInfoDTO> findAllPaginated(Pageable pageable) {
    return billService.findAllPaginated(pageable);
  }
}
