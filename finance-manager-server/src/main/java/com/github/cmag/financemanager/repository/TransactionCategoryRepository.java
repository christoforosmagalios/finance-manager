package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.master.data.TransactionCategory;
import org.springframework.stereotype.Repository;

/**
 * The TransactionCategory Repository.
 */
@Repository
public interface TransactionCategoryRepository extends BaseRepository<TransactionCategory> {

  /**
   * Find the transaction category with the given code.
   *
   * @param code The category code.
   * @return The transaction category.
   */
  TransactionCategory findByCode(String code);
}
