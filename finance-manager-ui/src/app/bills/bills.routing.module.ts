import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "../shared/guards/auth.guard";
import { BillComponent } from './bill/bill.component';
import { BillsComponent } from './bills/bills.component';

const billsRoutes: Routes = [
    { path: 'bills', component: BillsComponent, canActivate: [AuthGuard] },
    { path: 'bill/:id', component: BillComponent, canActivate: [AuthGuard] },
    { path: 'bill', component: BillComponent, canActivate: [AuthGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(billsRoutes)],
    exports: [RouterModule]
  })
  export class BillsRoutingModule { }