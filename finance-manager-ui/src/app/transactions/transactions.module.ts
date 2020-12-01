import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionsComponent } from './transactions/transactions.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TransactionComponent } from './transaction/transaction.component';
import { FormsModule } from '@angular/forms';
import { TransactionsRoutingModule } from './transactions.routing.module';
import { DirectivesModule } from '../shared/directives/directives.module';



@NgModule({
  declarations: [TransactionsComponent, TransactionComponent],
  imports: [
    CommonModule,
    NgbModule,
    FormsModule,
    TransactionsRoutingModule,
    DirectivesModule
  ]
})
export class TransactionsModule { }
