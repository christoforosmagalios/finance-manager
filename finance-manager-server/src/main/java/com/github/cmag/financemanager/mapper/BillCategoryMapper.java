package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.master.data.BillCategoryDTO;
import com.github.cmag.financemanager.model.master.data.BillCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillCategoryMapper extends BaseMapper<BillCategoryDTO, BillCategory> {

}
