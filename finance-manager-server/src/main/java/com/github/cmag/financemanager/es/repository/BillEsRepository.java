package com.github.cmag.financemanager.es.repository;

import com.github.cmag.financemanager.es.index.BillIndex;
import java.time.LocalDate;
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
   * @param userId The id of the logged in user.
   * @return List of bills.
   */
  List<BillIndex> findByCodeStartsWithAndUserId(String text, String userId);

  /**
   * Find the unpaid bills between the given dates.
   *
   * @param to Date to.
   * @param userId The id of the logged in user.
   * @return List of Bills.
   */
  List<BillIndex> findByPaidFalseAndDueDateLessThanEqualAndUserIdOrderByDueDateDesc(LocalDate to,
      String userId);

  /**
   * Get the total number of bills for the user with the given id.
   *
   * @param userId The user id.
   * @return The count.
   */
  long countByUserId(String userId);
}
