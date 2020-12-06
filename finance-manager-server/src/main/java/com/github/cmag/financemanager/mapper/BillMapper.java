package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BillDTO;
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

  @AfterMapping
  public void constructBase64(Bill bill, @MappingTarget BillDTO billDTO) {
    if (StringUtils.isNotEmpty(bill.getImgPath())) {
      String base64 = fileService.getBase64(bill.getImgPath(), bill.getImgType());
      billDTO.setBase64(base64);
    }
  }
}
