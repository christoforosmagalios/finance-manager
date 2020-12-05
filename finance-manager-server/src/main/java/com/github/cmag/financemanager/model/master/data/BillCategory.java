package com.github.cmag.financemanager.model.master.data;

import com.github.cmag.financemanager.model.MasterDataEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * Bill Category entity.
 */
@Entity
@Data
@Table(name = "bill_category")
public class BillCategory extends MasterDataEntity {

}
