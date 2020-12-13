package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.service.ElasticSearchService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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
}
