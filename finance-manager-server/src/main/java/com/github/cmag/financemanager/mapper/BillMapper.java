package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.SearchResultDTO;
import com.github.cmag.financemanager.es.index.BillIndex;
import com.github.cmag.financemanager.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillMapper extends BaseMapper<BillDTO, Bill> {

  /**
   * Map a BillIndex to a BillDTO.
   *
   * @param billIndex The BillIndex.
   * @return The mapped BillDTO.
   */
  @Mapping(source = "categoryName", target = "billCategory.name")
  @Mapping(source = "userId", target = "user.id")
  BillDTO mapToDTO(BillIndex billIndex);

  /**
   * Map a Bill to a BillIndex.
   *
   * @param bill The Bill.
   * @return The mapped BillIndex.
   */
  @Mapping(source = "billCategory.name", target = "categoryName")
  @Mapping(source = "billCategory.id", target = "categoryId")
  @Mapping(source = "user.id", target = "userId")
  BillIndex mapToIndex(Bill bill);

  /**
   * Map a BillDTO to a BillIndex.
   *
   * @param billDTO The BillDTO.
   * @return The mapped BillIndex.
   */
  @Mapping(source = "billCategory.name", target = "categoryName")
  @Mapping(source = "billCategory.id", target = "categoryId")
  @Mapping(source = "user.id", target = "userId")
  BillIndex mapToIndex(BillDTO billDTO);

  /**
   * Map a BillIndex to a SearchResultDTO.
   *
   * @param billIndex The BillIndex.
   * @param score The search score.
   * @return The mapped SearchResultDTO.
   */
  @Mapping(expression = "java(true)", target = "bill")
  SearchResultDTO mapToSearchResult(BillIndex billIndex, float score);
}
