package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Bill Repository.
 */
@Repository
public interface BillRepository extends BaseRepository<Bill> {

  @Query("select imgPath from Bill where id = ?1")
  String getImgUrl(String id);

  List<Bill> findByCodeStartsWith(String text);
}
