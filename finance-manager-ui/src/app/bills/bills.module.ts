import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BillsComponent } from './bills/bills.component';
import { BillComponent } from './bill/bill.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DirectivesModule } from '../shared/directives/directives.module';
import { BillsRoutingModule } from './bills.routing.module';



@NgModule({
  declarations: [BillsComponent, BillComponent],
  imports: [
    CommonModule,
    NgbModule,
    FormsModule,
    BillsRoutingModule,
    DirectivesModule,
    TranslateModule.forChild({
      extend: true
    })
  ]
})
export class BillsModule { }
