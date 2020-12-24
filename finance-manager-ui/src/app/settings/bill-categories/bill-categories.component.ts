import { Component, OnInit } from '@angular/core';
import { BillCategoryDTO } from 'src/app/dto/bill-category-dto';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';

@Component({
  selector: 'app-bill-categories',
  templateUrl: './bill-categories.component.html',
  styleUrls: ['./bill-categories.component.css']
})
export class BillCategoriesComponent implements OnInit {
  // List of bill categories.
  billCatogories: BillCategoryDTO[];

  constructor(private crud: CRUDService) { }

  ngOnInit(): void {
    this.crud.findAll(Constants.ENTITY.BILL_CATEGORY).subscribe((res: BillCategoryDTO[]) => {
      this.billCatogories = res;
    });
  }

}
