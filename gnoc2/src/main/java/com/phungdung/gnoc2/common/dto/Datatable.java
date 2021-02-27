package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Datatable {
  private int total;
  private int pages;
  private List<?> data;
}
