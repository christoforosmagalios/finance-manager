package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.es.repository.BillEsRepository;
import com.github.cmag.financemanager.es.repository.TransactionEsRepository;
import com.github.cmag.financemanager.mapper.BillMapper;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.repository.BillRepository;
import com.github.cmag.financemanager.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ElasticSearchService {

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillEsRepository billEsRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionEsRepository transactionEsRepository;

  @Autowired
  private BillMapper billMapper;

  @Autowired
  private TransactionMapper transactionMapper;

  /**
   * Perform an elasticsearch full reindex.
   */
  public void reindex() {
    // Find all bills from the DB.
    List<Bill> bills = billRepository.findAll();
    // Delete all indexed bills.
    billEsRepository.deleteAll();
    // Index the bills.
    billEsRepository.saveAll(bills.stream().map(b -> billMapper.mapToIndex(b))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

    // Find all transactions from the DB.
    List<Transaction> transactions = transactionRepository.findAll();
    // Delete all indexed transactions.
    transactionEsRepository.deleteAll();
    // Index the transactions.
    transactionEsRepository.saveAll(transactions.stream().map(t -> transactionMapper.mapToIndex(t))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
  }

}
