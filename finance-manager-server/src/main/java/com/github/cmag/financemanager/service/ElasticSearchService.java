package com.github.cmag.financemanager.service;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import com.github.cmag.financemanager.dto.SearchResultDTO;
import com.github.cmag.financemanager.es.index.BillIndex;
import com.github.cmag.financemanager.es.index.TransactionIndex;
import com.github.cmag.financemanager.es.repository.BillEsRepository;
import com.github.cmag.financemanager.es.repository.TransactionEsRepository;
import com.github.cmag.financemanager.mapper.BillMapper;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.repository.BillRepository;
import com.github.cmag.financemanager.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ElasticSearchService {

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillEsRepository billEsRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionEsRepository transactionEsRepository;

  @Autowired
  private BillMapper billMapper;

  @Autowired
  private TransactionMapper transactionMapper;

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  /**
   * Perform an elasticsearch full reindex.
   */
  public void reindex() {
    // Find all bills from the DB.
    List<Bill> bills = billRepository.findAll();
    // Delete all indexed bills.
    billEsRepository.deleteAll();
    // Index the bills.
    billEsRepository.saveAll(bills.stream().map(b -> billMapper.mapToIndex(b))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

    // Find all transactions from the DB.
    List<Transaction> transactions = transactionRepository.findAll();
    // Delete all indexed transactions.
    transactionEsRepository.deleteAll();
    // Index the transactions.
    transactionEsRepository.saveAll(transactions.stream().map(t -> transactionMapper.mapToIndex(t))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
  }

  /**
   * Search for Bills and Transactions that contain the given text parameter in their description or
   * notes. Sort the results based on their match score.
   *
   * @param text The text to search with.
   * @return A list of Bills or Transactions.
   */
  public List<SearchResultDTO> search(String text) {
    // Initialize a Page request. Set a limit of 5 items per search.
    PageRequest page = PageRequest.of(0, 5);
    // Construct the query.
    NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(multiMatchQuery(text).field("description").field("notes")
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
            .fuzziness(Fuzziness.ONE)).withPageable(page).build();

    // Search for Bills.
    SearchHits<BillIndex> bills = elasticsearchOperations.search(searchQuery, BillIndex.class);
    // Search for Transactions.
    SearchHits<TransactionIndex> transactions = elasticsearchOperations
        .search(searchQuery, TransactionIndex.class);
    // Initialize the result list.
    List<SearchResultDTO> results = new ArrayList<>();
    // Map the bills and transactions to search results.
    bills.getSearchHits().forEach(
        hit -> results.add(billMapper.mapToSearchResult(hit.getContent(), hit.getScore())));
    transactions.getSearchHits().forEach(hit -> results
        .add(transactionMapper.mapToSearchResult(hit.getContent(), hit.getScore())));
    // Sort the results based on their score.
    results.sort(Comparator.comparing(SearchResultDTO::getScore).reversed());
    return results;
  }

}
