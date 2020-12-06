package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.repository.BillRepository;
import java.io.IOException;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    }
    // Replace the new image with the old one.
    billDTO.setImgPath(fileService.copy(billDTO.getImgPath(), existingImg, imagesPath));

    return super.save(billDTO);
  }
}
