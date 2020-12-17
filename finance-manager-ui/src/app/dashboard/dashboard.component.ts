import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // The monthly pending amount.
  pending: number = null;
  // The monthly spending amount.
  spendings: number = null;
  // The monthly earnings amount.
  earnings: number = null;
  // The annual balance amount.
  balance: number = null;

  constructor(private dashboardService: DashboardService) { }

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
  }

}
