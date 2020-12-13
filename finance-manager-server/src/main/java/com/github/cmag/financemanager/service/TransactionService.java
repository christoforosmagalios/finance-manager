package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.es.index.TransactionIndex;
import com.github.cmag.financemanager.es.repository.TransactionEsRepository;
import com.github.cmag.financemanager.mapper.TransactionMapper;
import com.github.cmag.financemanager.model.Transaction;
import com.github.cmag.financemanager.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  private TransactionEsRepository es;

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
    TransactionDTO transaction = super.save(transactionDTO);
    if (!Objects.isNull(transactionDTO.getBill())) {
      this.billService.updateToPaid(transactionDTO.getBill());
    }
    es.save(mapper.mapToIndex(transactionDTO));
    return transaction;
  }

  /**
   * Find all the transactions based on the given Pageable.
   *
   * @return A Page that contains the transactions as well as the total number of bills and other
   * information.
   */
  public Page<TransactionDTO> findAllPaginated(Pageable pageable) {
    // If unsorted, set default sorting.
    if (!pageable.getSort().isSorted()) {
      pageable = PageRequest.of(pageable.getPageNumber(),
          pageable.getPageSize(), Sort.by(AppConstants.UPDATED_ON).descending());
    }

    Page<TransactionIndex> page = es.findAll(pageable);
    return page.map(transaction -> mapper.mapToDTO(transaction));
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
    List<TransactionIndex> transactions;
    if (StringUtils.isBlank(transactionId)) {
      transactions = es.findByBillId(billId);
    } else {
      transactions = es.findByBillIdAndIdNot(billId, transactionId);
    }
    return transactions.stream().map(t -> mapper.mapToDTO(t))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  /**
   * Delete and unindex the transaction with the given id.
   *
   * @param id The transaction id.
   */
  @Override
  public void delete(String id) {
    transactionRepository.deleteById(id);
    es.deleteById(id);
  }
}
