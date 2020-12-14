package com.github.cmag.financemanager.es.repository;

import com.github.cmag.financemanager.es.index.BillIndex;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Bill Repository for Elasticsearch.
 */
public interface BillEsRepository extends ElasticsearchRepository<BillIndex, String> {

  /**
   * Find the bills whose code start with the given text.
   *
   * @param text The text to search with.
   * @return List of bills.
   */
  List<BillIndex> findByCodeStartsWith(String text);

  /**
   * Find the unpaid bills between the given dates.
   *
   * @param start Start date (in milliseconds)
   * @param end End date (in milliseconds)
   * @return List of Bills.
   */
  List<BillIndex> findByPaidFalseAndDueDateBetween(long start, long end);
}
