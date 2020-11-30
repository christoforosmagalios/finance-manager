package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.BaseDTO;
import com.github.cmag.financemanager.model.BaseEntity;
import com.github.cmag.financemanager.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Base controller that contains basic CRUD methods. To be extended from other Rest Controllers.
 */
public class BaseController<D extends BaseDTO, E extends BaseEntity> {

  @Autowired
  private BaseService<D, E> baseService;

  /**
   * Find the requested entity with the given id.
   *
   * @param id The id of the entity.
   * @return The DTO representation of the entity.
   */
  @GetMapping("{id}")
  public D findOne(@PathVariable String id) {
    return baseService.findOne(id);
  }

  /**
   * Save the given DTO.
   *
   * @param dto The DTO to be saved.
   * @return The saved DTO.
   */
  @PostMapping
  public D save(@RequestBody D dto) {
    return baseService.save(dto);
  }

  /**
   * Find all the requested entities.
   *
   * @return A List of DTO representations of the requested entity.
   */
  @GetMapping
  public List<D> findAll() {
    return baseService.findAll();
  }

  /**
   * Delete the entity with the given id.
   *
   * @param id The id of the entity to be deleted.
   */
  @DeleteMapping("{id}")
  public void delete(@PathVariable String id) {
    baseService.delete(id);
  }
}
