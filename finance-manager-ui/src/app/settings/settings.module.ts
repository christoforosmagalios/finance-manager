import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsComponent } from './settings.component';
import { SettingsRoutingModule } from './seetings.routing.module';
import { AddressComponent } from './address/address.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DirectivesModule } from '../shared/directives/directives.module';
import { FormsModule } from '@angular/forms';
import { AddressItemComponent } from './address/address-item/address-item.component';
import { TransactionCategoriesComponent } from './transaction-categories/transaction-categories.component';
import { BillCategoriesComponent } from './bill-categories/bill-categories.component';
import { DataComponent } from './data/data.component';



@NgModule({
  declarations: [SettingsComponent, AddressComponent, AddressItemComponent, TransactionCategoriesComponent, BillCategoriesComponent, DataComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    NgbModule,
    FormsModule,
    DirectivesModule,
    TranslateModule.forChild({
      extend: true
    })
  ]
})
export class SettingsModule { }
