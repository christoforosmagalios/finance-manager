import { Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Label, BaseChartDirective } from 'ng2-charts';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { Constants } from 'src/app/shared/constants/constants';
import { GroupedTransactionInfoDTO } from 'src/app/dto/grouped-transaction-info-dto';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.css']
})
export class PieChartComponent implements OnInit {
  // Chart Object.
  @ViewChild(BaseChartDirective) chart;
  // Chart title.
  @Input() title;
  // Chart data.
  @Input() pieChartData;
  // Pie chart labels.
  public labels: Label[] = [];
  // Pie chart data.
  public data: number[] = [];
  // Chart type.
  public type: ChartType = 'pie';
  // Total amount of exoenses.
  public total: number = 0;
  // Pie color object.
  public colors = [
    {
      backgroundColor: [],
      borderColor: [],
      borderWidth: 1
    }
  ];
  // Pie chart plugins.
  public plugins = [pluginDataLabels];
  // Pie chart options.
  public options: ChartOptions = {
    title: {
      display: true,
      fontColor: "#00000090",
      fontFamily: Constants.FONT_FAMILY,
      fontSize: 18,
      position: "top"
    },
    responsive: true,
    legend: {
      position: 'left',
      labels: {
        fontColor: "#00000090",
        fontSize: 15
      }
    },
    plugins: {
      datalabels: {
        color: "#00000090",
        formatter: this.formatter
      }
    }
  };

  constructor(private utils: UtilsService) { }

  ngOnInit(): void {
  }

  /**
   * Find the grouped expenses and add them to the pie chart.
   */
  private createChart() {
    this.clearChart();
      let transactionInfo = this.pieChartData.transactions;
      for (let i = 0; i < transactionInfo.length; i++) {
        if (transactionInfo[i].amount > 0) {
          // Add the grouped transaction info to the chart.
          this.addDataToChart(transactionInfo[i]);
        }
      }
      // Set the title.
      this.options.title.text = this.title + ": € " + this.total;
      // Refresh the chart.
      this.chart.refresh();
  }

  /**
   * Clear the chart data.
   */
  private clearChart() {
    this.labels = [];
    this.data = [];
    this.colors[0].backgroundColor = [];
    this.colors[0].borderColor = [];

    this.total = 0;
  }

  /**
   * Add the given grouped transaction info to the pie chart.
   * 
   * @param transactionInfo The grouped transaction info.
   */
  private addDataToChart(transactionInfo: GroupedTransactionInfoDTO) {
    // Add the label.
    this.labels.push(this.utils.translate(transactionInfo.category));
    // Add the data.
    this.data.push(transactionInfo.amount);
    // Set the background color.
    this.colors[0].backgroundColor.push(transactionInfo.color + "60");
    // Set the border color.
    this.colors[0].borderColor.push(transactionInfo.color);

    this.total += transactionInfo.amount;
  }

  /**
   * Calculate the pie display value.
   * 
   * @param value The pie value.
   * @param ctx Contextual information
   * @returns The value to be displayed inside the pie.
   */
  private formatter(value: number, ctx: any) {          
    let sum = 0;
    let dataArr = ctx.chart.data.datasets[0].data;
    for(let i = 0; i < dataArr.length; i++) {
      sum += +dataArr[i];
    }
    return (value*100 / sum).toFixed(2)+"%";
  }

  /**
   * Listen to input changes. If the lineChartData or the title has benn changed,
   * create the new line chart.
   * 
   * @param changes SimpleChanges
   */
  ngOnChanges(changes: SimpleChanges) {
    if ((changes.pieChartData || changes.title) && this.pieChartData 
    && this.pieChartData.transactions && this.pieChartData.transactions.length > 0) {
        this.createChart();
    }
  }
}
