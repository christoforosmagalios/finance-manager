import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TransactionDTO } from '../../dto/transaction-dto';
import { NgbDateParserFormatter, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateFRParserFormatter } from '../../shared/services/datepicker.formatter.service';
import { CRUDService } from '../../shared/services/crud.service';
import { TransactionCategoryDTO } from '../../dto/transaction-category-dto';
import { UtilsService } from '../../shared/services/utils.service';
import { Constants } from '../../shared/constants/constants';
import { Observable, of } from 'rxjs';
import { BillService } from '../../bills/bill.service';
import { tap, debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { BillDTO } from '../../dto/bill-dto';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
})
export class TransactionComponent implements OnInit {
  // The id of the transaction to be edited.
  id: string;
  // True if edit mode, false otherwise.
  editMode = false;
  // The transaction.
  transaction = new TransactionDTO(this.utils.getNewDate());
  // The original edited transaction.
  originalTransaction = new TransactionDTO(this.utils.getNewDate());
  // Transaction date.
  date: NgbDate;
  // List of transaction categories.
  categories: Array<TransactionCategoryDTO>;
  // Is currently searching for bill.
  searching = false;
  // Bill search failed.
  searchFailed = false;
  // A ngbTypeahead Formatter for the result list.
  billSearchFormatter = (result: BillDTO) => result.code + " - " + result.description;
  // A ngbTypeahead Formatter for the input display.
  billInputFormatter = (result: BillDTO) => result.code;
  // A bill object used as a model for the ngbTypeahead.
  bill = new BillDTO(this.utils.getNewDate());
  // Error object.
  errors = {
    transactionCategory: null,
    type: null,
    notes: null,
    description: null,
    amount: null,
    date: null,
    bill: null
  };

  constructor(
    private route: ActivatedRoute,
    public formatter: NgbDateParserFormatter,
    private crudService: CRUDService,
    public utils: UtilsService,
    private router: Router,
    private billService: BillService
  ) { }

  ngOnInit(): void {
    // Get the params.
    this.route.params.subscribe((params: Params) => {
      this.id = params['id'];
      // Initialize the form.
      this.initForm();
    });
  }

  /**
   * Initialize the transaction Form.
   */
  private initForm() {
    // Find all the categories.
    this.crudService.findAll(Constants.ENTITY.TRANSACTION_CATEGORY)
    .subscribe((categories: Array<TransactionCategoryDTO>) => {
      this.categories = categories;
    });

    // In case of edit mode fetch the transaction from the database.
    if (!this.id) {
      this.transaction.date = this.utils.getNewDate();
      this.setDate(new Date(this.transaction.date));
      this.editMode = true;
    } else {
      this.crudService.findOne(Constants.ENTITY.TRANSACTION, this.id)
      .subscribe((transaction: TransactionDTO) => {
        this.transaction = transaction;
        this.setDate(new Date(this.transaction.date));
        this.transaction.bill && (this.bill = {...this.transaction.bill});
        this.originalTransaction = {...this.transaction};
      });
    }
  }

  /**
   * Create a NgbDate from the given Date.
   * 
   * @param date A Date.
   */
  private setDate(date: Date) {
    this.date = new NgbDate(date.getFullYear(), date.getMonth() + 1,  date.getDate());
  }

  /**
   * Submit the transaction form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {

    this.transaction.bill = null;
    if (this.bill && this.bill.id) {
      this.transaction.bill = this.bill;
    }

    // Clear the form errors.
    this.utils.clearErrors(this.errors);
    // Save the transaction.
    this.crudService.save(Constants.ENTITY.TRANSACTION, this.transaction)
    .subscribe(result => {
      // Show success notification.
      this.utils.showSuccess("transaction.saved.successfully");
      // Navigate to Transactions view.
      this.router.navigate(['/transactions']);
    }, error => {
      // Mark the validation errors.
      this.utils.markErrors(this.errors, error);
    });
  }

  /**
   * Navigate to previous state.
   */
  onCancel() {
    if(this.editMode && this.id) {
      this.transaction = {...this.originalTransaction};
      this.editMode = false;
      this.utils.clearErrors(this.errors);
    } else {
      this.utils.goBack();
    }
  }

  /**
   * When the selected bill value changes to empty,
   * assign to the bill object a new BillDTO instead of an empty string.
   */
  onBillChange() {
    if (!this.bill) {
      this.bill = new BillDTO(this.utils.getNewDate());
    }
  }

  /**
   * Listens to the bill model change.
   * If the bill exists and has an id, then set the transaction amount equal to the bill amount.
   */
  onModelBillChange() {
    if (this.bill && this.bill.id) {
      this.transaction.amount = this.bill.amount;
      this.transaction.transactionCategory = this.findBillsCategory();
    }
  }

  /**
   * Find the bills transaction category.
   * 
   * @returns The transaction category.
   */
  private findBillsCategory() {
    return this.categories.find(c => c.code === Constants.BILLS_TRANSACTION_CATEGORY);
  }

  /**
   * If the length of the given text is greater than 2, 
   * search for bills whose code starts with the given text. 
   * 
   * @param text$ The text to search with.
   */
  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        term.length < 3 ? [] : this.billService.findByCode(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
    )

}
