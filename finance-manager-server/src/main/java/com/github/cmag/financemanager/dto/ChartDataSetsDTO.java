package com.github.cmag.financemanager.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representation of Chart Data Sets used by chart.js.
 */
@Data
@AllArgsConstructor
public class ChartDataSetsDTO {

  private String label;
  private List<Double> data;
  private String backgroundColor;
  private String hoverBackgroundColor;

  public ChartDataSetsDTO(String label, String color) {
    this.label = label;
    this.backgroundColor = color + "60";
    this.hoverBackgroundColor = color + "90";
    this.data = new ArrayList<>();
  }

}
