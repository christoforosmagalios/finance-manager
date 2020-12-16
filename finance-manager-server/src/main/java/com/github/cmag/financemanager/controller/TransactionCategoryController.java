package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.master.data.TransactionCategoryDTO;
import com.github.cmag.financemanager.model.master.data.TransactionCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TransactionCategory Rest controller.
 */
@RestController
@RequestMapping("/transactionCategory")
public class TransactionCategoryController extends BaseController<TransactionCategoryDTO, TransactionCategory> {

}
