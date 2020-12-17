import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Label, BaseChartDirective } from 'ng2-charts';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { TransactionService } from 'src/app/transactions/transaction.service';
import { GroupedTransactionDTO } from 'src/app/dto/grouped-transaction-dto';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { Constants } from 'src/app/shared/constants/constants';
import { GroupedTransactionInfoDTO } from 'src/app/dto/grouped-transaction-info-dto';

@Component({
  selector: 'app-expenditure-sources',
  templateUrl: './expenditure-sources.component.html',
  styleUrls: ['./expenditure-sources.component.css']
})
export class ExpenditureSourcesComponent implements OnInit {

  @ViewChild(BaseChartDirective) chart;
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

  constructor(
    private transactionService: TransactionService,
    private utils: UtilsService) { }

  ngOnInit(): void {
    this.getExpenses();
  }

  /**
   * Find the grouped expenses and add them to the pie chart.
   */
  private getExpenses() {
    // Get the grouped expenses.
    this.transactionService.getGroupedExpenses().subscribe((groupedTransaction: GroupedTransactionDTO) => {
      let transactionInfo = groupedTransaction.transactions;
      for (let i = 0; i < transactionInfo.length; i++) {
        if (transactionInfo[i].amount > 0) {
          // Add the grouped transaction info to the chart.
          this.addDataToChart(transactionInfo[i]);
        }
      }
      // Set the title.
      this.options.title.text = this.getMonth(groupedTransaction.to) + ": € " + this.total;
      // Refresh the chart.
      this.chart.refresh();
    });
  }

  /**
   * Extract the translated month word from the given date.
   * 
   * @param date The date.
   * @returns The translated name of the month.
   */
  private getMonth(date: Date) {
    let monthIndex = new Date(date).getMonth();
    return this.utils.translate(Constants.MONTHS[monthIndex]);
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

}
