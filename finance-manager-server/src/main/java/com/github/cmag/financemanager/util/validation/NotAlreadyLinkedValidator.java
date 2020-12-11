package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.dto.TransactionDTO;
import com.github.cmag.financemanager.service.TransactionService;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Constrain Validator. Check that the given bill is not already linked with another
 * transaction.
 */
public class NotAlreadyLinkedValidator implements
    ConstraintValidator<NotAlreadyLinked, TransactionDTO> {

  @Autowired
  private TransactionService transactionService;
  private String field;
  private String message;

  /**
   * Initialize the constrain validator.
   *
   * @param notAlreadyLinked NotAlreadyLinked
   */
  @Override
  public void initialize(NotAlreadyLinked notAlreadyLinked) {
    message = notAlreadyLinked.message();
    field = notAlreadyLinked.field();
  }

  /**
   * Perform the validation check. If a bill exists and it is already linked with another
   * transaction return false.
   *
   * @param transactionDTO The transaction dto.
   * @param context The validator context.
   * @return true if valid, false otherwise.
   */
  @Override
  public boolean isValid(TransactionDTO transactionDTO, ConstraintValidatorContext context) {

    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();

    // In case the bill is null, no validation is required.
    if (Objects.isNull(transactionDTO.getBill())) {
      return true;
    }

    // Find the transactions that are linked with the bill.
    List<TransactionDTO> transactionDTOS = transactionService
        .findTransactionsByBillId(transactionDTO.getBill().getId(), transactionDTO.getId());
    return transactionDTOS.size() == 0;
  }
}
