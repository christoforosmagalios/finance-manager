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
 * A Transaction index representation for Elasticsearch.
 */
@Data
@Document(indexName = "transaction")
public class TransactionIndex {

  @Id
  @Field(type = FieldType.Text)
  private String id;

  @Field(type = FieldType.Text)
  private String categoryId;

  @Field(type = FieldType.Text, fielddata = true)
  private String categoryName;

  @Field(type = FieldType.Text)
  private String categoryColor;

  @Field(type = FieldType.Boolean)
  private boolean type;

  @Field(type = FieldType.Text, fielddata = true)
  private String description;

  @Field(type = FieldType.Float)
  private double amount;

  @Field(type = FieldType.Date, format = DateFormat.basic_date)
  private LocalDate date;

  @Field(type = FieldType.Text)
  private String billId;

  @Field(type = FieldType.Text, fielddata = true)
  private String billCode;

  @Field(type = FieldType.Text)
  private String userId;

  private Date createdOn;
  private Date updatedOn;

  @Field(type = FieldType.Text, fielddata = true)
  private String notes;
}
