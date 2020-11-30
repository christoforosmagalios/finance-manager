package com.github.cmag.financemanager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * Category entity.
 */
@Entity
@Data
@Table(name = "category")
public class Category extends MasterDataEntity {

}
