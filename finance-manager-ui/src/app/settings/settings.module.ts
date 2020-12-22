import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsComponent } from './settings.component';
import { SettingsRoutingModule } from './seetings.routing.module';
import { AddressComponent } from './address/address.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DirectivesModule } from '../shared/directives/directives.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [SettingsComponent, AddressComponent],
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
