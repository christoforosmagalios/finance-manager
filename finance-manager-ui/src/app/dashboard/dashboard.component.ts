import { Component, OnInit } from '@angular/core';
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

  constructor(
    private dashboardService: DashboardService,
    private transactionService: TransactionService,
    private utils: UtilsService) { }

  ngOnInit(): void {
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
    
    // Get the transaction data for the line chart.
    this.transactionService.getTransactionsPerDay().subscribe((data: Array<TransactionItemDTO>) => {
      this.lineChartData = data;
      this.lineChartTitle = this.getMonth(new Date());
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

}
