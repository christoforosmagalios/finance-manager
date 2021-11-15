import { Component, OnInit } from '@angular/core';
import { NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { PageDTO } from 'src/app/dto/page-dto';
import { SortDTO } from 'src/app/dto/sort-dto';
import { TransactionCategoryDTO } from 'src/app/dto/transaction-category-dto';
import { TransactionDTO } from 'src/app/dto/transaction-dto';
import { TransactionsFilter } from 'src/app/dto/transactions-filter';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';
import { NgbDateFRParserFormatter } from 'src/app/shared/services/datepicker.formatter.service';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
})
export class TransactionsComponent implements OnInit {

  // The transactions.
  transactions = [];
  // Active page.
  page = 1;
  // Total number of transactions in the Database.
  collectionSize: number = 0;
  // Transactions per page.
  size: number = 10;
  // Max number of pages
  maxSize: number = 5;
  // The sort options to be used in pagination.
  sort: SortDTO = {
    direction: Constants.ORDER.DESC,
    name: "date"
  };
  // Filter area is collapsed.
  filterIsCollapsed = true;
  // Items per page options
  itemsPerPage = [];
  // Transactions filter.
  filter = new TransactionsFilter();
  // Transaction categories.
  transactionCategories: Array<TransactionCategoryDTO>;
  // Filter Date from.
  dateFrom: NgbDate;
  // Filter Date to.
  dateTo: NgbDate;

  constructor(
    private loader: LoaderService,
    private crudService: CRUDService,
    public formatter: NgbDateParserFormatter,
    public utils: UtilsService
  ) { }

  ngOnInit(): void {
    this.itemsPerPage = Constants.ITEMS_PER_PAGE;
    // Find all the categories.
    this.crudService.findAll(Constants.ENTITY.TRANSACTION_CATEGORY)
    .subscribe((categories: Array<TransactionCategoryDTO>) => {
      this.transactionCategories = categories;
    });
    // Find all transactions in order to display them.
    this.findAll();
  }

  findAll() {
    // Show loader.
    this.loader.show();
    // Find the Transactions.
    this.crudService.findAllPaginated(Constants.ENTITY.TRANSACTION, this.size, (this.page - 1), this.sort.name, this.sort.direction, this.filter)
    .subscribe((page: PageDTO<TransactionDTO>) => {
      this.transactions = page.content;
      this.collectionSize = page.totalElements;
      // Hide loader.
      this.loader.hide();
    });
  }

  /**
   * Delete the transaction with the given id.
   * 
   * @param id The transaction id.
   */
  delete(id: string) {
    this.crudService.delete(Constants.ENTITY.TRANSACTION, id)
    .subscribe(result => {
      this.findAll();
    });
  }

  /**
   * Update the sort object, and refetch the list.
   * 
   * @param newSort The new sort object.
   */
  getSort(newSort) {
    this.sort = {...newSort};
    this.findAll();
  }

  search() {
    this.findAll();
  }

  clearFilter() {
    this.dateFrom = undefined;
    this.dateTo = undefined;
    this.filter = new TransactionsFilter();
  }
}
