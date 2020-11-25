package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A base Spring {@link Repository}.
 *
 * @param <E> An Entity that extends the BaseEntity.
 */
@Repository
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, String> {

}
