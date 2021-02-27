package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ConfigFileExport {
    private List lstData;
    private String sheetName;
    private String title;
    private String subTitle;
    private int startRow;
    private int cellTitleIndex;
    private int mergeTitleEndIndex;
    private boolean createHeader;
    private String headerPrefix;
    private List<ConfigHeaderExport> header;
    private Map<String, String> fieldSplit;
    private String splitChar;
    private List<CellConfigExport> lstCreateCell;
    private String langKey;
    private String firstLeftHeaderTitle;
    private String secondLeftHeaderTitle;
    private String firstRightHeaderTitle;
    private String secondRightHeaderTitle;
    private List<CellConfigExport> lstCellMerge;
    private String[] customTitle;
    private String[] customColumnWidth;
    private String[] customColumnWidthNoMerge;
    private List<Integer> lstColumnHidden;
    private Boolean isAutoSize;
    private Map<Integer, Integer> mapCustomColumnWidth;
}
