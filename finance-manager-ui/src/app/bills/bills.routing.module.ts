import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { BillComponent } from './bill/bill.component';
import { BillsComponent } from './bills/bills.component';

const billsRoutes: Routes = [
    { path: 'bills', component: BillsComponent },
    { path: 'bill/:id', component: BillComponent },
    { path: 'bill', component: BillComponent }
];

@NgModule({
    imports: [RouterModule.forChild(billsRoutes)],
    exports: [RouterModule]
  })
  export class BillsRoutingModule { }