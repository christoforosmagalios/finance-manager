package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.SearchResultDTO;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.es.index.TransactionIndex;
import com.github.cmag.financemanager.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper extends BaseMapper<TransactionDTO, Transaction> {

  /**
   * Map a Transaction to a TransactionIndex.
   *
   * @param transaction The Transaction.
   * @return The mapped TransactionIndex.
   */
  @Mapping(source = "bill.id", target = "billId")
  @Mapping(source = "bill.code", target = "billCode")
  @Mapping(source = "transactionCategory.id", target = "categoryId")
  @Mapping(source = "transactionCategory.name", target = "categoryName")
  @Mapping(source = "transactionCategory.color", target = "categoryColor")
  @Mapping(source = "user.id", target = "userId")
  public abstract TransactionIndex mapToIndex(Transaction transaction);

  /**
   * Map a TransactionDTO to a TransactionIndex.
   *
   * @param transactionDTO The TransactionDTO.
   * @return The mapped TransactionIndex.
   */
  @Mapping(source = "bill.id", target = "billId")
  @Mapping(source = "bill.code", target = "billCode")
  @Mapping(source = "transactionCategory.id", target = "categoryId")
  @Mapping(source = "transactionCategory.name", target = "categoryName")
  @Mapping(source = "transactionCategory.color", target = "categoryColor")
  @Mapping(source = "user.id", target = "userId")
  public abstract TransactionIndex mapToIndex(TransactionDTO transactionDTO);

  /**
   * Map a TransactionIndex to a TransactionDTO.
   *
   * @param transactionIndex The TransactionIndex.
   * @return The mapped TransactionDTO.
   */
  @Mapping(source = "billId", target = "bill.id")
  @Mapping(source = "billCode", target = "bill.code")
  @Mapping(source = "userId", target = "user.id")
  @Mapping(source = "categoryId", target = "transactionCategory.id")
  @Mapping(source = "categoryName", target = "transactionCategory.name")
  public abstract TransactionDTO mapToDTO(TransactionIndex transactionIndex);

  /**
   * Map a TransactionIndex to a SearchResultDTO.
   *
   * @param transactionIndex The TransactionIndex.
   * @param score The search score.
   * @return The mapped SearchResultDTO.
   */
  @Mapping(expression = "java(false)", target = "bill")
  public abstract SearchResultDTO mapToSearchResult(TransactionIndex transactionIndex, float score);
}
