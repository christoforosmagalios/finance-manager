package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.service.TransactionService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction Rest controller.
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController extends BaseController<TransactionDTO, Transaction> {

  @Autowired
  private TransactionService transactionService;

  /**
   * Save the given TransactionDTO.
   *
   * @param dto The TransactionDTO to be saved.
   * @return The saved TransactionDTO.
   */
  @Override
  @PostMapping
  public TransactionDTO save(@RequestBody @Valid TransactionDTO dto) {
    return transactionService.save(dto);
  }
}
