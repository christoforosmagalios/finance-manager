import { Component, OnInit } from '@angular/core';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { ChartDataSets } from 'chart.js';
import { Subscription } from 'rxjs';
import { GroupedTransactionDTO } from '../dto/grouped-transaction-dto';
import { MonthOverviewDTO } from '../dto/month-overview-dto';
import { TransactionItemDTO } from '../dto/transaction-item-dto';
import { Constants } from '../shared/constants/constants';
import { UtilsService } from '../shared/services/utils.service';
import { TransactionService } from '../transactions/transaction.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {
  // Month overview object.
  monthOverview = new MonthOverviewDTO();
  // Annual overview.
  annualOverview = new Date().getFullYear();
  // Months.
  months: Array<string>;
  // Years.
  years: Array<number>;
  // Line chart title.
  lineChartTitle: string;
  // Line chart data.
  lineChartData: Array<TransactionItemDTO>;
  // Pie chart title.
  pieChartTitle: string;
  // Pie chart data.
  pieChartData: GroupedTransactionDTO;
  // Stacked Bar chart title.
  stackedBarChartTitle: string;
  // Stacked Bar data.
  stackedBarChartData: ChartDataSets[];
  // Subscription.
  private subscription: Subscription;
  
  constructor(
    private transactionService: TransactionService,
    private utils: UtilsService,
    private translate: TranslateService
  ) {
    // Subscribe to transalte service in case language is changed.
    this.subscription = this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.updateCharts();
    });
  }

  ngOnInit(): void {
    this.months = [...Constants.MONTHS];
    this.years = this.getYears();
    this.updateCharts();
  }

  updateCharts() {
    this.searchMonthly();
    this.searchAnnually();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  /**
   * Get the monthly data in order to update the charts.
   */
  searchMonthly() {
    // Get the transaction data for the line chart.
    this.transactionService.getTransactionsPerDay(+this.monthOverview.month + 1, this.monthOverview.year)
    .subscribe((data: Array<TransactionItemDTO>) => {
      this.lineChartData = data;
      this.lineChartTitle = this.utils.getMonthByIndex(this.monthOverview.month) + " " + this.monthOverview.year;
    });

    // Get the grouped expenses.
    this.transactionService.getGroupedExpenses(+this.monthOverview.month + 1, this.monthOverview.year)
    .subscribe((data: GroupedTransactionDTO) => {
      this.pieChartData = data;
      this.pieChartTitle = this.utils.getMonthByIndex(this.monthOverview.month) + " " + this.monthOverview.year;
    });
  }

  /**
   * Get the annual data in order to update the charts.
   */
  searchAnnually() {
    // Get the transactions grouped by month for the given year.
    this.transactionService.getAnnualTransactionsGroupedByMonth(this.annualOverview)
    .subscribe(data => {
      this.stackedBarChartData = data;
      this.stackedBarChartTitle = this.annualOverview + "";
    });
  }

  /**
   * Get a list of years starting from the current year until 50 years back.
   * 
   * @return A array of years.
   */
  private getYears() {
    let years = new Array();
    let currentYear = new Date().getFullYear();
    for (let i = currentYear; i > (currentYear - 50); i--) {
      years.push(i);
    }
    return years;
  }
}
