package com.github.cmag.financemanager.es.repository;

import com.github.cmag.financemanager.es.index.TransactionIndex;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Transaction Repository for Elasticsearch.
 */
public interface TransactionEsRepository extends ElasticsearchRepository<TransactionIndex, String> {

  /**
   * Find the Transaction that are linked with the given billId.
   *
   * @param billId The bill id to search with.
   * @param userId The id of the logged in user.
   * @return A List of transactions.
   */
  List<TransactionIndex> findByBillIdAndUserId(String billId, String userId);

  /**
   * Find the Transaction that are linked with the given billId and do not have the given
   * transactionId.
   *
   * @param billId The bill id.
   * @param transactionId The transaction id.
   * @param userId The id of the logged in user.
   * @return A list of transactions.
   */
  List<TransactionIndex> findByBillIdAndIdNotAndUserId(String billId, String transactionId,
      String userId);

  /**
   * Find the transaction with the given type and between the given dates.
   *
   * @param type The transaction type.
   * @param from Date from.
   * @param to Date to.
   * @param userId The id of the logged in user.
   * @return A list of transaction.
   */
  List<TransactionIndex> findByTypeAndDateBetweenAndUserId(boolean type, LocalDate from,
      LocalDate to, String userId);

  /**
   * Find the transaction with date between the two given ones.
   *
   * @param from Date from.
   * @param to Date to.
   * @param userId The id of the logged in user.
   * @return A List of transactions.
   */
  List<TransactionIndex> findByDateBetweenAndUserId(LocalDate from, LocalDate to, String userId);
}
