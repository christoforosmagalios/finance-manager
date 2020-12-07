import { Component, OnInit } from '@angular/core';
import { BillDTO } from 'src/app/dto/bill-dto';
import { PageDTO } from 'src/app/dto/page-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';
import { BillService } from '../bill.service';

@Component({
  selector: 'app-bills',
  templateUrl: './bills.component.html',
  styleUrls: ['./bills.component.css']
})
export class BillsComponent implements OnInit {

  // The bills.
  bills = [];
  // Active page.
  page = 1;
  // Total number of bills in the Database.
  collectionSize: number = 0;
  // Bills per page.
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
    private crudService: CRUDService,
    private billService: BillService
  ) { }

  ngOnInit(): void {
    this.itemsPerPage = Constants.ITEMS_PER_PAGE;
    // Find all bills in order to display them.
    this.findAll();
  }

  findAll() {
    // Show loader.
    this.loader.show();
    // Find the Bills.
    this.billService.findAllPaginated(this.size, (this.page - 1), this.sort.name, this.sort.direction)
    .subscribe((page: PageDTO<BillDTO>) => {
      this.bills = page.content;
      this.collectionSize = page.totalElements;
      // Hide loader.
      this.loader.hide();
    });
  }

  /**
   * Delete the bill with the given id.
   * 
   * @param id The bill id.
   */
  delete(id: string) {
    this.crudService.delete(Constants.ENTITY.BILL, id)
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
