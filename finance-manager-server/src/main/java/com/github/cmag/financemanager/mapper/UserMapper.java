package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.UserDTO;
import com.github.cmag.financemanager.dto.UserDetailsDTO;
import com.github.cmag.financemanager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends BaseMapper<UserDTO, User> {

  /**
   * Map a UserDetailsDTO to a User.
   *
   * @param userDetailsDTO The UserDetailsDTO.
   * @return The mapped User.
   */
  public abstract User map(UserDetailsDTO userDetailsDTO);
}
