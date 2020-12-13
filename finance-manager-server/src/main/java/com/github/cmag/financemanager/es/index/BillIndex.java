package com.github.cmag.financemanager.es.index;

import java.util.Date;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Bill index representation for Elasticsearch.
 */
@Data
@Document(indexName = "bill")
public class BillIndex {

  @Id
  private String id;
  private String billCategoryName;
  private String code;
  private Date issuedOn;
  private Date dueDate;
  private String description;
  private double amount;
  private boolean paid;
  private Date paidOn;
  private Date createdOn;
  private Date updatedOn;
}
