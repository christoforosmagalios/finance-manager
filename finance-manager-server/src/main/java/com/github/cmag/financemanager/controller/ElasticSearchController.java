package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.SearchResultDTO;
import com.github.cmag.financemanager.service.ElasticSearchService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Elasticsearch REst Controller.
 */
@RestController
@RequestMapping("/elasticsearch")
public class ElasticSearchController {

  @Autowired
  private ElasticSearchService elasticSearchService;

  /**
   * Perform a full reindex.
   */
  @PostMapping(path = "/reindex")
  public void reindex() throws IOException {
    elasticSearchService.reindex();
  }

  /**
   * Search for Bills and Transactions that contain the given text parameter in their description or
   * notes.
   *
   * @param text The text to search with.
   * @return A list of Search Results sorted by their score.
   */
  @GetMapping("/search/{text}")
  public List<SearchResultDTO> search(@PathVariable String text) {
    return elasticSearchService.search(text);
  }
}
