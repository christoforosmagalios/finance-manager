import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LineChartComponent } from './charts/line-chart/line-chart.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { ChartsModule } from 'ng2-charts';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { StackedBarChartComponent } from './charts/stacked-bar-chart/stacked-bar-chart.component';



@NgModule({
  declarations: [
    PieChartComponent,
    LineChartComponent,
    StackedBarChartComponent,
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
    LineChartComponent,
    StackedBarChartComponent
  ]
})
export class SharedComponentsModule { }
