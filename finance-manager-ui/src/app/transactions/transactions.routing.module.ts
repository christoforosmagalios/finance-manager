import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "../shared/guards/auth.guard";
import { TransactionComponent } from './transaction/transaction.component';
import { TransactionsComponent } from './transactions/transactions.component';

const transactionsRoutes: Routes = [
    { path: 'transactions', component: TransactionsComponent, canActivate: [AuthGuard] },
    { path: 'transaction/:id', component: TransactionComponent, canActivate: [AuthGuard] },
    { path: 'transaction', component: TransactionComponent, canActivate: [AuthGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(transactionsRoutes)],
    exports: [RouterModule]
  })
  export class TransactionsRoutingModule { }