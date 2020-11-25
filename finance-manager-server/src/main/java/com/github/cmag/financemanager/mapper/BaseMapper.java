package com.github.cmag.financemanager.mapper;

import com.github.cmag.financemanager.dto.BaseDTO;
import com.github.cmag.financemanager.model.BaseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Mapper with some default conversions.
 *
 * @param <D> A DTO that extends the BaseDTO.
 * @param <E> An Entity that extends the BaseEntity.
 */
public abstract class BaseMapper<D extends BaseDTO, E extends BaseEntity> {

  /**
   * Convert the given DTO to an Entity.
   *
   * @param dto The DTO to be converted.
   * @return The converted Entity.
   */
  public abstract E map(D dto);

  /**
   * Convert the given Entity to a DTO.
   *
   * @param entity The entity to be converted.
   * @return The converted DTO.
   */
  public abstract D map(E entity);

  /**
   * Convert the given list of entities to DTOs.
   *
   * @param entities A list of entities to be converted.
   * @return The list of DTOs.
   */
  public List<D> mapToDTOs(List<E> entities) {
    return entities.stream().map(this::map)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  /**
   * Convert the given list of DTOs to entities.
   *
   * @param dtos A list of DTOs to be converted.
   * @return The list of entities.
   */
  public List<E> mapToEntities(List<D> dtos) {
    return dtos.stream().map(this::map)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

}
