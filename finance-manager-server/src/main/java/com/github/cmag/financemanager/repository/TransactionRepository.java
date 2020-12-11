package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.Transaction;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * The Transaction Repository.
 */
@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

  List<Transaction> findByBillIdAndIdNot(String billId, String transactionId);
}
