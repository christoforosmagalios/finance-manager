package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.GroupedTransactionDTO;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.dto.TransactionFilterDTO;
import com.github.cmag.financemanager.dto.TransactionItemDTO;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.service.TransactionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  /**
   * Find all the entities by taking into account the given Pageable.
   *
   * @param pageable Contains pagination info.
   * @return The Page result.
   */
  @PostMapping("/paginated")
  public Page<TransactionDTO> findAll(Pageable pageable, @RequestBody TransactionFilterDTO filter) {
    return transactionService.findAllPaginated(pageable, filter);
  }

  /**
   * Get the current month's transaction amount based on the given type.
   *
   * @return The monthly total earning amount.
   */
  @GetMapping("/monthlyTransactionsAmount/{type}")
  public double getMonthlyTransactionAmount(@PathVariable boolean type) {
    return transactionService.getMonthlyTransactionAmount(type);
  }

  /**
   * Calculate the annual balance.
   *
   * @return The annual balance.
   */
  @GetMapping("/balance")
  public double getAnnualBalance() {
    return this.transactionService.getAnnualBalance();
  }

  /**
   * Get expenses grouped by category.
   *
   * @return A list of grouped expenses.
   */
  @GetMapping("/groupedExpenses")
  public GroupedTransactionDTO getGroupedExpenses() {
    return this.transactionService.getGroupedExpenses();
  }

  @GetMapping("/transactionsPerDay")
  public List<TransactionItemDTO> getTransactionsPerDay() {
    return this.transactionService.getTransactionsPerDay();
  }
}
