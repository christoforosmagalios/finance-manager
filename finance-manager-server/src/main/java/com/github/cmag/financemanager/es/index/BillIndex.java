package com.github.cmag.financemanager.es.index;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate issuedOn;
  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate dueDate;
  private String description;
  private double amount;
  private boolean paid;
  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate paidOn;
  private String userId;
  private Date createdOn;
  private Date updatedOn;
}
