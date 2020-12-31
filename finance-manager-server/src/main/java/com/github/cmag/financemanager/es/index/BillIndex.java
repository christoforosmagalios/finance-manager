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
  @Field(type = FieldType.Text)
  private String id;

  @Field(type = FieldType.Text, fielddata = true)
  private String billCategoryName;

  @Field(type = FieldType.Text, fielddata = true)
  private String code;

  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate issuedOn;

  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate dueDate;

  @Field(type = FieldType.Text, fielddata = true)
  private String description;

  @Field(type = FieldType.Float)
  private double amount;

  @Field(type = FieldType.Boolean)
  private boolean paid;

  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate paidOn;

  @Field(type = FieldType.Text)
  private String userId;

  private Date createdOn;
  private Date updatedOn;
}
