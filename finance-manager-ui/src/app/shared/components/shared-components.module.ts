import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LineChartComponent } from './charts/line-chart/line-chart.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { ChartsModule } from 'ng2-charts';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';



@NgModule({
  declarations: [
    PieChartComponent,
    LineChartComponent,
  ],
  imports: [
    CommonModule,
    NgbModule,
    ChartsModule,
    TranslateModule.forChild({
      extend: true
    })
  ],
  exports: [
    PieChartComponent,
    LineChartComponent
  ]
})
export class SharedComponentsModule { }
