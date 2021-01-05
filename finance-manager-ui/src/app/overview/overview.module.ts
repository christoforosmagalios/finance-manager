import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OverviewComponent } from './overview.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedComponentsModule } from '../shared/components/shared-components.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [OverviewComponent],
  imports: [
    CommonModule,
    NgbModule,
    SharedComponentsModule,
    FormsModule,
    TranslateModule.forChild({
      extend: true
    })
  ]
})
export class OverviewModule { }
