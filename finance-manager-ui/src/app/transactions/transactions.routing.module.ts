import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { TransactionComponent } from './transaction/transaction.component';
import { TransactionsComponent } from './transactions/transactions.component';

const transactionsRoutes: Routes = [
    { path: 'transactions', component: TransactionsComponent },
    { path: 'transaction/:id', component: TransactionComponent },
    { path: 'transaction', component: TransactionComponent }
];

@NgModule({
    imports: [RouterModule.forChild(transactionsRoutes)],
    exports: [RouterModule]
  })
  export class TransactionsRoutingModule { }