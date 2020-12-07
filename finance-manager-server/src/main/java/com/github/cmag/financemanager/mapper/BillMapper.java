package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.BillInfoDTO;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.service.FileService;
import liquibase.util.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BillMapper extends BaseMapper<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  /**
   * Map a Bill to a BillInfoDTO.
   *
   * @param bill The Bill to be mapped.
   * @return A BillInfoDTO.
   */
  public abstract BillInfoDTO mapToInfo(Bill bill);

  /**
   * Get the bill image and convert it to base64.
   *
   * @param bill The Bill source.
   * @param billDTO The BillDTO target.
   */
  @AfterMapping
  public void constructBase64(Bill bill, @MappingTarget BillDTO billDTO) {
    if (StringUtils.isNotEmpty(bill.getImgPath())) {
      String base64 = fileService.getBase64(bill.getImgPath(), bill.getImgType());
      billDTO.setBase64(base64);
    }
  }
}
