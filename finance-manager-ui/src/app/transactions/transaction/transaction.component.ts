import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TransactionDTO } from '../../dto/transaction-dto';
import { Location } from '@angular/common';
import { NgbDateParserFormatter, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateFRParserFormatter } from '../../shared/services/datepicker.formatter.service';
import { CRUDService } from '../../shared/services/crud.service';
import { CategoryDTO } from '../..//dto/category-dto';
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
  transaction = new TransactionDTO();
  // The original edited transaction.
  originalTransaction = new TransactionDTO();
  // Transaction date.
  date: NgbDate;
  // List of transaction categories.
  categories: Array<CategoryDTO>;
  // Is currently searching for bill.
  searching = false;
  // Bill search failed.
  searchFailed = false;
  // A ngbTypeahead Formatter for the result list.
  billSearchFormatter = (result: BillDTO) => result.code + " - " + result.description;
  // A ngbTypeahead Formatter for the input display.
  billInputFormatter = (result: BillDTO) => result.code;
  // A bill object used as a model for the ngbTypeahead.
  bill = new BillDTO();
  // Error object.
  errors = {
    category: null,
    type: null,
    notes: null,
    description: null,
    amount: null,
    date: null
  };

  constructor(
    private route: ActivatedRoute,
    private location: Location,
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
    this.crudService.findAll(Constants.ENTITY.CATEGORY)
    .subscribe((categories: Array<CategoryDTO>) => {
      this.categories = categories;
    });

    // In case of edit mode fetch the transaction from the database.
    if (!this.id) {
      this.transaction.date = new Date();
      this.setDate(this.transaction.date);
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
   * Handle date selection.
   * 
   * @param date The new date.
   */
  onDateSelection(date: NgbDate) {
    this.transaction.date = new Date(date.year + "-" + date.month + "-" + date.day);
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

    this.clearErrors();

    this.crudService.save(Constants.ENTITY.TRANSACTION, this.transaction)
    .subscribe(result => {
      this.utils.showSuccess("Saved successfully.");
      this.router.navigate(['/transactions']);
    }, error => {
      if (error && error.errors instanceof Array) {
        for (let e of error.errors) {
          this.errors[e.field] = e.defaultMessage;
        }
      }
    });
  }

  /**
   * Clear any previous validation errors.
   */
  private clearErrors() {
    for (let property in this.errors) {
      this.errors[property] = null;
    }
  }

  /**
   * Navigate to previous state.
   */
  onCancel() {
    if(this.editMode && this.id) {
      this.transaction = {...this.originalTransaction};
      this.editMode = false;
      this.clearErrors();
    } else {
      this.location.back();
    }
  }

  /**
   * When the selected bill value changes to empty,
   * assign to the bill object a new BillDTO instead of an empty string.
   */
  onBillChange() {
    if (!this.bill) {
      this.bill = new BillDTO();
    }
  }

  /**
   * Listens to the bill model change.
   * If the bill exists and has an id, then set the transaction amount equal to the bill amount.
   */
  onModelBillChange() {
    if (this.bill && this.bill.id) {
      this.transaction.amount = this.bill.amount;
    }
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
