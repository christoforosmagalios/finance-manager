package com.github.cmag.financemanager.es.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Transaction index representation for Elasticsearch.
 */
@Data
@Document(indexName = "transaction")
public class TransactionIndex {

  @Id
  private String id;
  private String categoryId;
  private String categoryName;
  private String categoryColor;
  private boolean type;
  private String description;
  private double amount;
  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate date;
  private String billId;
  private String billCode;
  private String userId;
  private Date createdOn;
  private Date updatedOn;
}
