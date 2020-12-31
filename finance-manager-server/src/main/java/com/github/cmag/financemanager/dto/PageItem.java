package com.github.cmag.financemanager.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageItem<D extends BaseDTO> {

  private List<D> content;
  private long totalElements;
}
