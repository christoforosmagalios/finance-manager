package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.master.data.TransactionCategoryDTO;
import com.github.cmag.financemanager.model.master.data.TransactionCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TransactionCategoryMapper extends BaseMapper<TransactionCategoryDTO, TransactionCategory> {

}
