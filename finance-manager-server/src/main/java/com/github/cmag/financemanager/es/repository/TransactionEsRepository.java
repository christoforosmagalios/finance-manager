package com.github.cmag.financemanager.es.repository;

import com.github.cmag.financemanager.es.index.TransactionIndex;
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
   * @return A List of transactions.
   */
  List<TransactionIndex> findByBillId(String billId);

  /**
   * Find the Transaction that are linked with the given billId and do not have the given
   * transactionId.
   *
   * @param billId        The bill id.
   * @param transactionId The transaction id.
   * @return A list of transactions.
   */
  List<TransactionIndex> findByBillIdAndIdNot(String billId, String transactionId);

  /**
   * Find the transaction with the given type and between the given dates.
   *
   * @param type The transaction type.
   * @param from From date (in milliseconds)
   * @param to   To date (in milliseconds)
   * @return A list of transaction.
   */
  List<TransactionIndex> findByTypeAndDateBetween(boolean type, long from, long to);
}
