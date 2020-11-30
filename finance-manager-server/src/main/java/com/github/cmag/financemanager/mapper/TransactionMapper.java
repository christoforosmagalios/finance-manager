package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper extends BaseMapper<TransactionDTO, Transaction> {

}
