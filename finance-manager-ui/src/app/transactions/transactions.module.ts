import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionsComponent } from './transactions/transactions.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TransactionComponent } from './transaction/transaction.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [TransactionsComponent, TransactionComponent],
  imports: [
    CommonModule,
    NgbModule,
    FormsModule
  ]
})
export class TransactionsModule { }
