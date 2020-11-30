import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TransactionDTO } from 'src/app/dto/transaction-dto';
import { Location } from '@angular/common';
import { NgbDateParserFormatter, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateFRParserFormatter } from 'src/app/shared/services/datepicker.formatter.service';
import { CRUDService } from 'src/app/shared/services/crud.service';
import { CategoryDTO } from 'src/app/dto/category-dto';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { Constants } from 'src/app/shared/constants/constants';

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

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    public formatter: NgbDateParserFormatter,
    private crudService: CRUDService,
    public utils: UtilsService,
    private router: Router
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
    this.crudService.save(Constants.ENTITY.TRANSACTION, this.transaction)
    .subscribe(result => {
      this.utils.showSuccess("Saved successfully.");
      this.router.navigate(['/transactions']);
    });
  }

  /**
   * Navigate to previous state.
   */
  onCancel() {
    if(this.editMode && this.id) {
      this.transaction = {...this.originalTransaction};
      this.editMode = false;
    } else {
      this.location.back();
    }
  }

}
