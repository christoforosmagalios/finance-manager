import { Component, OnInit } from '@angular/core';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { GroupedTransactionDTO } from '../dto/grouped-transaction-dto';
import { TransactionItemDTO } from '../dto/transaction-item-dto';
import { Constants } from '../shared/constants/constants';
import { UtilsService } from '../shared/services/utils.service';
import { TransactionService } from '../transactions/transaction.service';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // The monthly pending amount.
  public pending: number = null;
  // The monthly spending amount.
  public spendings: number = null;
  // The monthly earnings amount.
  public earnings: number = null;
  // The annual balance amount.
  public balance: number = null;
  // Line chart title.
  public lineChartTitle: string;
  // Line chart data.
  public lineChartData: Array<TransactionItemDTO>;
  // Pie chart title.
  public pieChartTitle: string;
  // Pie chart data.
  public pieChartData: GroupedTransactionDTO;
  // Subscription.
  private subscription: Subscription;

  constructor(
    private dashboardService: DashboardService,
    private transactionService: TransactionService,
    private utils: UtilsService,
    private translate: TranslateService) {
      // Subscribe to transalte service in case language is changed.
      this.subscription = this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
        this.updateCharts();
      });
    }

  ngOnInit(): void {
    this.init();
  }

  /**
   * Initialize all the components.
   */
  init() {
    // Get the pending amount.
    this.dashboardService.getPending().subscribe(amount => {
      this.pending = amount;
    });

    // Get the spendings amount.
    this.dashboardService.getMonthlyTransactionAmount(true).subscribe(amount => {
      this.spendings = amount;
    });

    // Get the earnings amount.
    this.dashboardService.getMonthlyTransactionAmount(false).subscribe(amount => {
      this.earnings = amount;
    });

    // Get the annual balance.
    this.dashboardService.getAnnualBalance().subscribe(amount => {
      this.balance = amount;
    });

    // Update charts.
    this.updateCharts();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  /**
   * Update line and pie charts.
   */
  private updateCharts() {
    // Get the transaction data for the line chart.
    this.transactionService.getTransactionsPerDay(new Date().getMonth() + 1, new Date().getFullYear())
    .subscribe((data: Array<TransactionItemDTO>) => {
      this.lineChartData = data;
      this.lineChartTitle = this.utils.getMonth(new Date());
    });

    // Get the grouped expenses.
    this.transactionService.getGroupedExpenses(new Date().getMonth() + 1, new Date().getFullYear())
    .subscribe((data: GroupedTransactionDTO) => {
      this.pieChartData = data;
      this.pieChartTitle = this.utils.getMonth(new Date());
    });
  }
}
