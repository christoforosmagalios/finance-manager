import { Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { BaseChartDirective, Color, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';
import { Constants } from 'src/app/shared/constants/constants';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit {
  // Chart object.
  @ViewChild(BaseChartDirective, {static: false}) chart;
  // Chart title.
  @Input() title;
  // Chart data.
  @Input() lineChartData;

  // Line chart data.
  public data: ChartDataSets[] = [];
  // Line chart labels.
  public labels: Label[] = [];
  // Line chart options.
  public options: (ChartOptions) = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      xAxes: [{
        ticks: {
          fontColor: "#00000090"
        }
      }],
      yAxes: [
        {
          id: 'y-axis-0',
          position: 'left',
          ticks: {
            fontColor: "#00000090",
            callback: (value, i, values) => {
              return value + " €";
            }
          }
        }
      ]
    },
    elements : {
      point: {
        radius: 4
      }
    },
    title: {
      display: true,
      fontColor: "#00000090",
      fontFamily: Constants.FONT_FAMILY,
      fontSize: 18,
      position: "top"
    },
    legend: {
      labels: {
        fontColor: "#00000090",
        fontFamily: Constants.FONT_FAMILY,
        fontSize: 15
      },
      position: "bottom"
    },
    plugins : {
      datalabels: {
        color: "white",
        formatter: (value, ctx) => {
          return "";
        },
      }
    }
  };
  // Line chart colors.
  public colors: Color[] = [
    { 
      backgroundColor: '#2f5e8720',
      borderColor: '#1cc88a',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#000',
      pointHoverBackgroundColor: '#000',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }, { 
      backgroundColor: '#b83d5320',
      borderColor: '#b83d53',
      pointBackgroundColor: 'rgba(77,83,96,1)',
      pointBorderColor: '#000',
      pointHoverBackgroundColor: '#000',
      pointHoverBorderColor: 'rgba(77,83,96,1)'
    }
  ];
  // Chart type.
  public type = 'line';
  // Line chart plugins.
  public plugins = [pluginAnnotations];

  constructor(private utils: UtilsService) { }

  ngOnInit(): void {
  }

  /**
   * Clear the already existing chart data and create the new ones.
   */
  private createChart() {
    this.clearChart();

    let incomes = [];
    let expenses = [];
    let labels = [];
    // Iterate through the data.
    for(let i = 0; i < this.lineChartData.length; i++) {
      incomes.push(this.lineChartData[i].income);
      expenses.push(this.lineChartData[i].expense);
      labels.push(this.lineChartData[i].label);
    }

    // Add the lables.
    this.data.push(
      { data: incomes, label: this.utils.translate("income")},
      { data: expenses, label: this.utils.translate("expenses")}
    );
    this.labels = labels;
    // Set the title.
    this.options.title.text = this.title;
    // Refresh the chart.
    this.chart.refresh();
  }

  /**
   * Clear the chart.
   */
  private clearChart() {
    this.data = [];
    this.labels = [];
  }

  /**
   * Listen to input changes. If the lineChartData or the title has benn changed,
   * create the new line chart.
   * 
   * @param changes SimpleChanges
   */
  ngOnChanges(changes: SimpleChanges) {
    if ((changes.lineChartData || changes.title) && this.lineChartData && this.lineChartData.length > 0) {
        this.createChart();
    }
  }
}
