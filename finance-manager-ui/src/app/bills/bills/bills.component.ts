import { Component, OnInit } from '@angular/core';
import { NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { BillCategoryDTO } from 'src/app/dto/bill-category-dto';
import { BillDTO } from 'src/app/dto/bill-dto';
import { BillsFilter } from 'src/app/dto/bills-filter';
import { PageDTO } from 'src/app/dto/page-dto';
import { SortDTO } from 'src/app/dto/sort-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';
import { NgbDateFRParserFormatter } from 'src/app/shared/services/datepicker.formatter.service';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-bills',
  templateUrl: './bills.component.html',
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
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
  sort: SortDTO = {
    direction: Constants.ORDER.DESC,
    name: "issuedOn"
  };
  // Items per page options
  itemsPerPage = [];
  // Filter area is collapsed.
  filterIsCollapsed = true;
  // Bills filter.
  filter = new BillsFilter();
  // Bill categories.
  billCategories: Array<BillCategoryDTO>;
  // Filter issue Date from.
  issueDateFrom: NgbDate;
  // Filter issue Date to.
  issueDateTo: NgbDate;
  // Filter due Date from.
  dueDateFrom: NgbDate;
  // Filter due Date to.
  dueDateTo: NgbDate;

  constructor(
    private loader: LoaderService,
    private crudService: CRUDService,
    public formatter: NgbDateParserFormatter,
    public utils: UtilsService
  ) { }

  ngOnInit(): void {
    this.itemsPerPage = Constants.ITEMS_PER_PAGE;
    this.crudService.findAll(Constants.ENTITY.BILL_CATEGORY)
    .subscribe((categories: Array<BillCategoryDTO>) => {
      this.billCategories = categories;
    });
    // Find all bills in order to display them.
    this.findAll();
  }

  findAll() {
    // Show loader.
    this.loader.show();
    // Find the Bills.
    this.crudService.findAllPaginated(Constants.ENTITY.BILL, this.size, (this.page - 1), this.sort.name, this.sort.direction, this.filter)
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

  search() {
    this.findAll();
  }

  clearFilter() {
    this.issueDateFrom = undefined;
    this.issueDateTo = undefined;
    this.dueDateFrom = undefined;
    this.dueDateTo = undefined;
    this.filter = new BillsFilter();
  }

}
