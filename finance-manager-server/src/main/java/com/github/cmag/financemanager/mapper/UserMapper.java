package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.RegisterDTO;
import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends BaseMapper<UserDTO, User> {

  public abstract User map(RegisterDTO registerDTO);
}
