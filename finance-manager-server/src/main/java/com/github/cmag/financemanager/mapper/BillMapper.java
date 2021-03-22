package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.SearchResultDTO;
import com.github.cmag.financemanager.es.index.BillIndex;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.service.FileService;
import liquibase.util.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BillMapper extends BaseMapper<BillDTO, Bill> {

  @Autowired
  private FileService fileService;

  /**
   * Map a BillIndex to a BillDTO.
   *
   * @param billIndex The BillIndex.
   * @return The mapped BillDTO.
   */
  @Mapping(source = "categoryName", target = "billCategory.name")
  @Mapping(source = "userId", target = "user.id")
  public abstract BillDTO mapToDTO(BillIndex billIndex);

  /**
   * Map a Bill to a BillIndex.
   *
   * @param bill The Bill.
   * @return The mapped BillIndex.
   */
  @Mapping(source = "billCategory.name", target = "categoryName")
  @Mapping(source = "billCategory.id", target = "categoryId")
  @Mapping(source = "user.id", target = "userId")
  public abstract BillIndex mapToIndex(Bill bill);

  /**
   * Map a BillDTO to a BillIndex.
   *
   * @param billDTO The BillDTO.
   * @return The mapped BillIndex.
   */
  @Mapping(source = "billCategory.name", target = "categoryName")
  @Mapping(source = "billCategory.id", target = "categoryId")
  @Mapping(source = "user.id", target = "userId")
  public abstract BillIndex mapToIndex(BillDTO billDTO);

  /**
   * Map a BillIndex to a SearchResultDTO.
   *
   * @param billIndex The BillIndex.
   * @param score The search score.
   * @return The mapped SearchResultDTO.
   */
  @Mapping(expression = "java(true)", target = "bill")
  public abstract SearchResultDTO mapToSearchResult(BillIndex billIndex, float score);

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
