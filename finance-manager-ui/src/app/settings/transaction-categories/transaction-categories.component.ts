import { Component, OnInit } from '@angular/core';
import { TransactionCategoryDTO } from 'src/app/dto/transaction-category-dto';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';

@Component({
  selector: 'app-transaction-categories',
  templateUrl: './transaction-categories.component.html',
  styleUrls: ['./transaction-categories.component.css']
})
export class TransactionCategoriesComponent implements OnInit {
  // List of transaction categories.
  transactionCatogories: TransactionCategoryDTO[];

  constructor(private crud: CRUDService) { }

  ngOnInit(): void {
    this.crud.findAll(Constants.ENTITY.TRANSACTION_CATEGORY).subscribe((res: TransactionCategoryDTO[]) => {
      this.transactionCatogories = res;
    });
  }

}
