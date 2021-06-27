package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.AddressDTO;
import com.github.cmag.financemanager.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper<AddressDTO, Address> {

}
