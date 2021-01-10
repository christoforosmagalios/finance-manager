package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.ChartDataSetsDTO;
import com.github.cmag.financemanager.dto.GroupedTransactionDTO;
import com.github.cmag.financemanager.dto.PageItem;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.dto.TransactionFilterDTO;
import com.github.cmag.financemanager.dto.TransactionItemDTO;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.service.TransactionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
  public PageItem findAll(Pageable pageable, @RequestBody TransactionFilterDTO filter) {
    return transactionService.findAllPaginated(pageable, filter);
  }

  /**
   * Get the transaction amount based on the given type, month and year.
   *
   * @param month The month.
   * @param year The year.
   * @param type The transaction type.
   * @return The monthly total earning amount.
   */
  @GetMapping("/monthlyTransactionsAmount/{month}/{year}/{type}")
  public double getMonthlyTransactionAmount(@PathVariable int month, @PathVariable int year,
      @PathVariable boolean type) {
    return transactionService.getTransactionAmount(month, year, type);
  }

  /**
   * Get the transaction amount based on the given year and type.
   *
   * @param year The year.
   * @param type The transaction type.
   * @return The monthly total earning amount.
   */
  @GetMapping("/annualTransactionsAmount/{year}/{type}")
  public double getAnnualTransactionAmount(@PathVariable int year, @PathVariable boolean type) {
    return transactionService.getAnnualTransactionAmount(year, type);
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
   * Get expenses grouped by category for the given month and year.
   *
   * @param month The month.
   * @param year The year.
   * @return A list of grouped expenses.
   */
  @GetMapping("/groupedExpenses/{month}/{year}")
  public GroupedTransactionDTO getGroupedExpenses(@PathVariable int month,
      @PathVariable int year) {
    return this.transactionService.getGroupedExpenses(month, year);
  }

  /**
   * Get grouped transaction per day for the given month and year.
   *
   * @param month The month.
   * @param year The year.
   * @return A list of grouped transactions.
   */
  @GetMapping("/transactionsPerDay/{month}/{year}")
  public List<TransactionItemDTO> getTransactionsPerDay(@PathVariable int month,
      @PathVariable int year) {
    return this.transactionService.getTransactionsPerDay(month, year);
  }

  /**
   * Get the total number of transactions for the logged in user.
   *
   * @return The transactions count.
   */
  @GetMapping("/totalNumberOfTransactions")
  public long getTotalNumberOfTransactions() {
    return transactionService.getTotalNumberOfTransactions();
  }

  /**
   * Get the annual transactions amount grouped by month.
   *
   * @param year The year.
   * @return A list of ChartDataSetsDTO.
   */
  @GetMapping("/annualTransactionsByMonth/{year}")
  public List<ChartDataSetsDTO> getAnnualTransactionsByMonth(@PathVariable int year) {
    return transactionService.getAnnualTransactionsByMonth(year);
  }
}
