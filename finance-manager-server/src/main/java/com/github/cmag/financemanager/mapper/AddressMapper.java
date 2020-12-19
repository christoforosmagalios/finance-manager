package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.AddressDTO;
import com.github.cmag.financemanager.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AddressMapper extends BaseMapper<AddressDTO, Address> {

}
