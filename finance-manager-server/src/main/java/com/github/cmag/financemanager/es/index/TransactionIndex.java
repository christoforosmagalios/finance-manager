package com.github.cmag.financemanager.es.index;

import java.util.Date;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Transaction index representation for Elasticsearch.
 */
@Data
@Document(indexName = "transaction")
public class TransactionIndex {

  @Id
  private String id;
  private String categoryName;
  private String categoryColor;
  private boolean type;
  private String description;
  private double amount;
  private Date date;
  private String billId;
  private String billCode;
  private Date createdOn;
  private Date updatedOn;
}
