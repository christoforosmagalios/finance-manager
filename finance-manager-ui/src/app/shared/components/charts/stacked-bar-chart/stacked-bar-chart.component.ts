import { Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective, Label } from 'ng2-charts';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { Constants } from 'src/app/shared/constants/constants';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-stacked-bar-chart',
  templateUrl: './stacked-bar-chart.component.html',
  styleUrls: ['./stacked-bar-chart.component.css']
})
export class StackedBarChartComponent implements OnInit {
  // Chart Object.
  @ViewChild(BaseChartDirective) chart;
  // Chart title.
  @Input() title;
  // Chart data.
  @Input() stackedBarChartData: ChartDataSets[] = [];
  // Chart labels.
  public labels: Label[] = [];
  // Chart data.
  public data: ChartDataSets[] = [];
  // Chart type.
  public type: ChartType = 'bar';
  // Chart plugins.
  public plugins = [pluginDataLabels];
  // Chart options.
  public options: ChartOptions = {
    scales: {
      xAxes: [{
          stacked: true
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          fontColor: "#00000090",
          callback: (value, i, values) => {
            return this.utils.formatNumber(+value) + " €";
          }
        }
      }]
    },
    title: {
      display: true,
      fontColor: "#00000090",
      fontFamily: Constants.FONT_FAMILY,
      fontSize: 18,
      position: "top"
    },
    responsive: true,
    legend: {
      position: 'bottom',
      labels: {
        fontColor: "#00000090",
        fontSize: 15
      }
    },
    plugins : {
      datalabels: {
        formatter: (value, ctx) => {
          return this.utils.formatNumber(value) + " €";
        },
      }
    },
    tooltips: {
      callbacks: {
        label: (tooltipItem, data) => {
            return this.utils.formatNumber(+tooltipItem.value) + " €";
        }
      }
    }
  };
  
  constructor(
    private utils: UtilsService
  ) { }

  ngOnInit(): void {
  }

  /**
   * Listen to input changes. If the stackedBarChartData or the title has been changed,
   * create the new stacked bar chart.
   * 
   * @param changes SimpleChanges
   */
  ngOnChanges(changes: SimpleChanges) {
    if ((changes.stackedBarChartData || changes.title) && this.stackedBarChartData && this.stackedBarChartData.length > 0) {
        this.data = [...this.stackedBarChartData.map(e => { 
          e.label = this.utils.translate(e.label);
          return e;
        })];
        // Set the title.
        this.options.title.text = this.title;
        // Set the labels.
        this.labels = [...Constants.MONTHS.map(e => { 
          return this.utils.translate(e);
        })];
        // Refresh the chart.
        this.chart.refresh();
    }
  }

}
