package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.BaseDTO;
import com.github.cmag.financemanager.mapper.BaseMapper;
import com.github.cmag.financemanager.model.BaseEntity;
import com.github.cmag.financemanager.repository.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * A base Spring {@link Service}.
 *
 * @param <D> A DTO that extends the BaseDTO.
 * @param <E> An Entity that extends the BaseEntity.
 */
@Service
public abstract class BaseService<D extends BaseDTO, E extends BaseEntity> {

  @Autowired
  private BaseRepository<E> baseRepository;

  @Autowired
  private BaseMapper<D, E> mapper;

  /**
   * Find all the data matching the given Entity.
   *
   * @return A list of DTO representations of the matched Entities.
   */
  public List<D> findAll() {
    return mapper.mapToDTOs(baseRepository.findAll());
  }

  /**
   * Find all the data based on the given Pageable.
   *
   * @return A Page that contains the requested Entities as well as the total number of entities and
   * other information.
   */
  public Page<D> findAll(Pageable pageable) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by(AppConstants.UPDATED_ON).descending());
    }

    Page<E> page = baseRepository.findAll(pageable);
    return page.map(this::entityToDto);
  }

  /**
   * Find one Entity based on the given id.
   *
   * @param id The id of the entity.
   * @return The DTO representation of the entity if found, null otherwise.
   */
  public D findOne(String id) {
    Optional<E> result = baseRepository.findById(id);
    if (result.isPresent()) {
      return mapper.map(result.get());
    }
    return null;
  }

  /**
   * Helper method that converts the given entity to its DTO representation.
   *
   * @param entity The entity to be converted.
   * @return The DTO representation of the given entity,
   */
  private D entityToDto(E entity) {
    return mapper.map(entity);
  }

  /**
   * Save the given DTO.
   *
   * @param dto The DTO to be saved.
   * @return The saved DTO.
   */
  public D save(D dto) {
    E entity = mapper.map(dto);
    return mapper.map(baseRepository.save(entity));

  }

  /**
   * Delete the entity with the given id.
   *
   * @param id The entity id.
   */
  public void delete(String id) {
    baseRepository.deleteById(id);
  }

}
