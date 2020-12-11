package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.repository.TransactionRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Transaction Service.
 */
@Service
@Transactional
public class TransactionService extends BaseService<TransactionDTO, Transaction> {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private BillService billService;

  @Autowired
  private TransactionMapper mapper;

  /**
   * Save the given transaction. In case the transaction is linked with a bill, update the bill to
   * paid.
   *
   * @param transactionDTO The transaction to be saved.
   * @return The saved transaction.
   */
  public TransactionDTO save(TransactionDTO transactionDTO) {
    TransactionDTO result = super.save(transactionDTO);
    if (!Objects.isNull(transactionDTO.getBill())) {
      this.billService.updateToPaid(transactionDTO.getBill());
    }
    return result;
  }

  /**
   * Find the transactions that are linked with the given bill id and do not have the given
   * transaction id.
   *
   * @param billId The Bill id.
   * @param transactionId The transaction Id.
   * @return A list of TransactionDTO.
   */
  public List<TransactionDTO> findTransactionsByBillId(String billId, String transactionId) {
    return this.mapper.mapToDTOs(transactionRepository.findByBillIdAndIdNot(billId, transactionId));
  }
}
