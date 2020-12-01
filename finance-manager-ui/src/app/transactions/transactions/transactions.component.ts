import { Component, OnInit } from '@angular/core';
import { PageDTO } from 'src/app/dto/page-dto';
import { TransactionDTO } from 'src/app/dto/transaction-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
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
  // The sort options to be used in pagination.
  sort = {
    direction: "",
    name: ""
  };
  // Items per page options
  itemsPerPage = [];

  constructor(
    private loader: LoaderService,
    private crudService: CRUDService
  ) { }

  ngOnInit(): void {
    this.itemsPerPage = Constants.ITEMS_PER_PAGE;
    // Find all transactions in order to display them.
    this.findAll();
  }

  findAll() {
    // Show loader.
    this.loader.show();
    // Find the Transactions.
    this.crudService.findAllPaginated(Constants.ENTITY.TRANSACTION, this.size, (this.page - 1), this.sort.name, this.sort.direction)
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
}
