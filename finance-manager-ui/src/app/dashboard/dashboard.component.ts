import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // The monthly pending amount.
  pending: number;

  constructor(private dashboardService: DashboardService) { }

  ngOnInit(): void {
    // Get the pending amount.
    this.dashboardService.getPending().subscribe((amount: number) => {
      this.pending = amount;
    })
  }

}
