package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.model.Bill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BillMapper extends BaseMapper<BillDTO, Bill> {

}
