package com.github.cmag.financemanager.es.repository;

import com.github.cmag.financemanager.es.index.BillIndex;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Bill Repository for Elasticsearch.
 */
public interface BillEsRepository extends ElasticsearchRepository<BillIndex, String> {

  List<BillIndex> findByCodeStartsWith(String text);
}
