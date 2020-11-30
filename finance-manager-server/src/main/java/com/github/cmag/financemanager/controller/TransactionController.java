package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.model.Transaction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction Rest controller.
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController extends BaseController<TransactionDTO, Transaction> {

}
