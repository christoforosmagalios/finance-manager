package com.github.cmag.financemanager.util.aspect;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.es.repository.BillEsRepository;
import com.github.cmag.financemanager.es.repository.TransactionEsRepository;
import com.github.cmag.financemanager.mapper.BillMapper;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Bill;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.repository.BillRepository;
import com.github.cmag.financemanager.repository.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Index/Unidex after performing CRUD operation in the Database.
 */
@Aspect
@Component
public class ElasticIndexAspect {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionEsRepository transactionEs;

  @Autowired
  private TransactionMapper transactionMapper;

  @Autowired
  private BillEsRepository billEs;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillMapper billMapper;

  /**
   * Index the transaction after an update in the Database.
   *
   * @param joinPoint The Join point.
   * @param transaction The save result.
   */
  @AfterReturning(pointcut = "execution(* com.github.cmag.financemanager.repository.TransactionRepository.save(..))", returning = "transaction")
  public void indexTransaction(JoinPoint joinPoint, Transaction transaction) {
    transactionEs.save(transactionMapper.mapToIndex(transaction));
  }

  /**
   * Index the bill after an update in the Database.
   *
   * @param joinPoint The Join point.
   * @param bill The save result.
   */
  @AfterReturning(pointcut = "execution(* com.github.cmag.financemanager.repository.BillRepository.save(..))", returning = "bill")
  public void indexBill(JoinPoint joinPoint, Bill bill) {
    billEs.save(billMapper.mapToIndex(bill));
  }

  /**
   * Unindex the transaction after deleting it from the database.
   *
   * @param joinPoint The Join Point.
   */
  @AfterReturning(pointcut = "execution(* com.github.cmag.financemanager.repository.TransactionRepository.deleteById(..))")
  public void deleteTransaction(JoinPoint joinPoint) {
    String id = joinPoint.getArgs().length > 0 ? (String) joinPoint.getArgs()[0] : null;
    if (!StringUtils.isBlank(id)) {
      transactionEs.deleteById(id);
    }
  }

  /**
   * Unindex the bill after deleting it from the database.
   *
   * @param joinPoint The Join Point.
   */
  @AfterReturning(pointcut = "execution(* com.github.cmag.financemanager.repository.BillRepository.deleteById(..))")
  public void deleteBill(JoinPoint joinPoint) {
    String id = joinPoint.getArgs().length > 0 ? (String) joinPoint.getArgs()[0] : null;
    if (!StringUtils.isBlank(id)) {
      billEs.deleteById(id);
    }
  }


}
