package com.phungdung.gnoc2.common.untils;

import com.phungdung.gnoc2.common.dto.CellConfigExport;
import com.phungdung.gnoc2.common.dto.ConfigFileExport;
import com.phungdung.gnoc2.common.dto.ConfigHeaderExport;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonExport {
    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String DOC_FILE_EXTENTION = ".doc";
    public static final String DOCX_FILE_EXTENTION = ".docx";
    public static final String XLSM_FILE_EXTENTION = ".xlsm";
    public static final String XLS_FILE_EXTENTION = ".xls";

    public static File exportExcel(String pathTemplate, String fileNameOut,
                                   List<ConfigFileExport> config, String pathOut,
                                   String... exportChart) throws Exception {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = simpleDateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut
                + strCurTimeExp + ((exportChart != null && exportChart.length > 0) ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
        HSSFWorkbook hwb = null;
        InputStream fileTemplate = null;
        XSSFWorkbook workbook_temp = null;
        SXSSFWorkbook workbook = null;
        try {
            log.info("Start get template file!");
//            pathTemplate = StringUtils.removeSeparator(pathTemplate);
            Resource resource = new ClassPathResource(pathTemplate);
            fileTemplate = resource.getInputStream();
            workbook_temp = new XSSFWorkbook(fileTemplate);
            log.info("End get template file!");
            workbook = new SXSSFWorkbook(workbook_temp, 1000);
            hwb = new HSSFWorkbook();

            CellStyle cellStypeFormatNumber = workbook.createCellStyle();
            cellStypeFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cellStypeFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
            cellStypeFormatNumber.setBorderLeft(BorderStyle.THIN);
            cellStypeFormatNumber.setBorderBottom(BorderStyle.THIN);
            cellStypeFormatNumber.setBorderRight(BorderStyle.THIN);
            cellStypeFormatNumber.setBorderTop(BorderStyle.THIN);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setWrapText(true);

            Font xSSFFont = workbook.createFont();
            xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            xSSFFont.setFontHeightInPoints((short) 20);
            xSSFFont.setBold(true);
            xSSFFont.setColor(IndexedColors.BLACK.index);

            CellStyle cellStyleTitle = workbook.createCellStyle();
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
            cellStyleTitle.setFont(xSSFFont);

            Font xSSFFontHeader = workbook.createFont();
            xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
            xSSFFontHeader.setFontHeightInPoints((short) 10);
            xSSFFontHeader.setColor(IndexedColors.BLUE.index);
            xSSFFontHeader.setBold(true);

            Font subTitleFont = workbook.createFont();
            subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
            subTitleFont.setFontHeightInPoints((short) 10);
            subTitleFont.setColor(IndexedColors.BLACK.index);

            Font xssFontTopHeader = workbook.createFont();
            xssFontTopHeader.setFontName("Times New Roman");
            xssFontTopHeader.setFontHeightInPoints((short) 10);
            xssFontTopHeader.setColor(IndexedColors.BLACK.index);

            Font rowDataFont = workbook.createFont();
            rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
            rowDataFont.setFontHeightInPoints((short) 10);
            rowDataFont.setColor(IndexedColors.BLACK.index);

            CellStyle cellStyleTopHeader = workbook.createCellStyle();
            cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
            cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleTopHeader.setFont(xssFontTopHeader);

            CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
            cellStyleTopRightHeader.setAlignment(HorizontalAlignment.CENTER);
            cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleTopRightHeader.setFont(xssFontTopHeader);

            CellStyle cellStyleSubTitle = workbook.createCellStyle();
            cellStyleSubTitle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleSubTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleSubTitle.setFont(subTitleFont);

            CellStyle cellStyleHeader = workbook.createCellStyle();
            cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
            cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleHeader.setBorderLeft(BorderStyle.THIN);
            cellStyleHeader.setBorderBottom(BorderStyle.THIN);
            cellStyleHeader.setBorderRight(BorderStyle.THIN);
            cellStyleHeader.setBorderTop(BorderStyle.THIN);
            cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyleHeader.setWrapText(true);
            cellStyleHeader.setFont(xSSFFontHeader);

            CellStyle cellStyleLeft = workbook.createCellStyle();
            cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
            cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleLeft.setBorderLeft(BorderStyle.THIN);
            cellStyleLeft.setBorderBottom(BorderStyle.THIN);
            cellStyleLeft.setBorderRight(BorderStyle.THIN);
            cellStyleLeft.setBorderTop(BorderStyle.THIN);
            cellStyleLeft.setWrapText(true);
            cellStyleLeft.setFont(rowDataFont);

            CellStyle cellStyleRight = workbook.createCellStyle();
            cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
            cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleRight.setBorderLeft(BorderStyle.THIN);
            cellStyleRight.setBorderBottom(BorderStyle.THIN);
            cellStyleRight.setBorderRight(BorderStyle.THIN);
            cellStyleRight.setBorderTop(BorderStyle.THIN);
            cellStyleRight.setWrapText(true);
            cellStyleRight.setFont(rowDataFont);

            CellStyle cellStyleHeaderOver = workbook.createCellStyle();
            cellStyleHeaderOver.setAlignment(HorizontalAlignment.LEFT);
            cellStyleHeaderOver.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleHeaderOver.setBorderLeft(BorderStyle.THIN);
            cellStyleHeaderOver.setBorderBottom(BorderStyle.THIN);
            cellStyleHeaderOver.setBorderRight(BorderStyle.THIN);
            cellStyleHeaderOver.setBorderTop(BorderStyle.THIN);
            cellStyleHeaderOver.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            cellStyleHeaderOver.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyleHeaderOver.setWrapText(true);
            cellStyleHeaderOver.setFont(xSSFFontHeader);

            CellStyle cellStyleCenter = workbook.createCellStyle();
            cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
            cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleCenter.setBorderLeft(BorderStyle.THIN);
            cellStyleCenter.setBorderBottom(BorderStyle.THIN);
            cellStyleCenter.setBorderRight(BorderStyle.THIN);
            cellStyleCenter.setBorderTop(BorderStyle.THIN);
            cellStyleCenter.setWrapText(true);
            cellStyleCenter.setFont(rowDataFont);

            CellStyle cellRight = workbook.createCellStyle();
            cellRight.setAlignment(HorizontalAlignment.RIGHT);
            cellRight.setVerticalAlignment(VerticalAlignment.CENTER);
            cellRight.setWrapText(true);

            CellStyle cellLeft = workbook.createCellStyle();
            cellLeft.setAlignment(HorizontalAlignment.LEFT);
            cellLeft.setVerticalAlignment(VerticalAlignment.CENTER);
            cellLeft.setWrapText(true);

            CellStyle cellCenter = workbook.createCellStyle();
            cellCenter.setAlignment(HorizontalAlignment.CENTER);
            cellCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            cellCenter.setWrapText(true);

            for (ConfigFileExport item : config) {
                Map<String, String> fieldSplit = item.getFieldSplit();
                SXSSFSheet sheet;
                if (exportChart != null && exportChart.length > 0) {
                    sheet = workbook.getSheetAt(0);
                } else {
                    sheet = workbook.createSheet(item.getSheetName());
                }
                if (item.getCellTitleIndex() >= 3) {
//                    them template Header
                    Row headerFirstTitle = sheet.createRow(0);
                    Row headerSecondTitle = sheet.createRow(1);
                    int sizeHeader = 5;
                    Cell firstLeftHeader = headerFirstTitle.createCell(1);
                    firstLeftHeader.setCellStyle(cellStyleTopHeader);

                    Cell secondLeftHeader = headerSecondTitle.createCell(1);
                    secondLeftHeader.setCellStyle(cellStyleTopHeader);

                    Cell firstRightHeader = headerFirstTitle.createCell(-1);
                    firstRightHeader.setCellStyle(cellStyleTopHeader);

                    Cell secondRightHeader = headerSecondTitle.createCell(-1);
                    secondRightHeader.setCellStyle(cellStyleTopHeader);

                    firstLeftHeader.setCellValue(StringUtils.isStringNullOrEmtry(item.getFirstLeftHeaderTitle()) ? "" : item.getFirstLeftHeaderTitle());
                    secondLeftHeader.setCellValue(StringUtils.isStringNullOrEmtry(item.getSecondLeftHeaderTitle()) ? "" : item.getSecondLeftHeaderTitle());
                    firstRightHeader.setCellValue(StringUtils.isStringNullOrEmtry(item.getFirstRightHeaderTitle()) ? "" : item.getFirstRightHeaderTitle());
                    secondRightHeader.setCellValue(StringUtils.isStringNullOrEmtry(item.getSecondRightHeaderTitle()) ? "" : item.getSecondRightHeaderTitle());

                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, sizeHeader - 1));
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, sizeHeader - 1));
                    //end
                }
                //Title
                Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
                Cell mainCellTilte;
                if (item.getCustomTitle() != null && item.getCustomTitle().length > 0) {
                    mainCellTilte = rowMainTitle.createCell(0);
                } else {
                    mainCellTilte = rowMainTitle.createCell(1);
                }
                mainCellTilte.setCellValue(StringUtils.isStringNullOrEmtry(item.getTitle()) ? "" : item.getTitle());
                //Sub title
                int indexSubTitle = (StringUtils.isStringNullOrEmtry(item.getSubTitle())) ? item.getCellTitleIndex() + 1 : item.getCellTitleIndex() + 2;
                SXSSFRow rowSubTitle;
                if (item.getSubTitle() != null && item.getSubTitle().length() > 0) {
                    CellStyle styleTitle = workbook.createCellStyle();
//                        tao tieu de
                    Font xssFontTitle = workbook.createFont();
                    xssFontTitle.setFontName(HSSFFont.FONT_ARIAL);
                    xssFontTitle.setFontHeightInPoints((short) 22);
                    xssFontTitle.setColor(IndexedColors.WHITE.index);
                    xssFontTitle.setBold(true);

                    styleTitle.setBorderBottom(BorderStyle.THIN);
                    styleTitle.setBorderLeft(BorderStyle.THIN);
                    styleTitle.setBorderRight(BorderStyle.THIN);
                    styleTitle.setBorderTop(BorderStyle.THIN);
                    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

                    HSSFPalette palette = hwb.getCustomPalette();
                    String[] customTitle = item.getCustomTitle();
                    String[] bgColors = customTitle[2].split(",");
                    HSSFColor myColor = palette.findSimilarColor(Integer.valueOf(bgColors[0]), Integer.valueOf(bgColors[1])
                            , Integer.valueOf(bgColors[2]));
                    styleTitle.setFillForegroundColor(myColor.getIndex());
                    styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    styleTitle.setFont(xssFontTitle);

                    mainCellTilte.setCellStyle(styleTitle);
                    sheet.addMergedRegion(new CellRangeAddress(item.getCellTitleIndex(),
                            item.getCellTitleIndex() + Integer.valueOf(customTitle[3]), 0, item.getMergeTitleEndIndex()));

                    styleTitle.setAlignment(HorizontalAlignment.LEFT);
                    styleTitle.setIndention(Short.valueOf(customTitle[1]));
                    subTitleFont.setBold(true);
                    cellStyleSubTitle.setFont(subTitleFont);
                    cellStyleSubTitle.setAlignment(HorizontalAlignment.LEFT);
                    cellStyleSubTitle.setIndention(Short.valueOf(customTitle[1]));

                    indexSubTitle = (StringUtils.isStringNullOrEmtry(item.getSubTitle())) ? item.getCellTitleIndex()
                            + Integer.valueOf(customTitle[3]) : item.getCellTitleIndex() + 3;

                    rowSubTitle = sheet.createRow(indexSubTitle);

                    Cell cellTitle = rowSubTitle.createCell(0);
                    cellTitle.setCellValue(StringUtils.isStringNotNullOrEmtry(item.getSubTitle()) ? "" : item.getSubTitle());
                    cellTitle.setCellStyle(cellStyleTitle);
                    sheet.addMergedRegion(new CellRangeAddress(indexSubTitle, indexSubTitle, 0, item.getMergeTitleEndIndex()));
                } else {
                    mainCellTilte.setCellStyle(cellStyleTitle);
                    sheet.addMergedRegion(new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1, item.getMergeTitleEndIndex()));
                    rowSubTitle = sheet.createRow(indexSubTitle);
                    Cell cellTitle = rowSubTitle.createCell(1);
                    cellTitle.setCellValue(StringUtils.isStringNullOrEmtry(item.getSubTitle()) ? "" : item.getSubTitle());
                    cellTitle.setCellStyle(cellStyleSubTitle);
                    sheet.addMergedRegion(new CellRangeAddress(indexSubTitle, indexSubTitle, 1, item.getMergeTitleEndIndex()));
                }
                int indexRowData = 0;
                //<editor-fold defaultstate ="collapsed" desc = "Build header">
                if (item.isCreateHeader()) {
                    int index = -1;
                    Cell cellHeader;
                    Row rowHeader = sheet.createRow(item.getStartRow());
                    rowHeader.setHeight((short) 500);
                    Row rowHeaderSub = null;
                    for (ConfigHeaderExport header : item.getHeader()) {
                        if (fieldSplit != null) {
                            if (fieldSplit.get(header.getFieldName()) != null) {
                                String[] fieldSplitHeader = fieldSplit.get(header.getFieldName()).split(item.getSplitChar());
                                for (String field : fieldSplitHeader) {
                                    cellHeader = rowHeader.createCell(index + 2);
                                    cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                                    if (header.isHasMerge()) {
                                        CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(), item.getStartRow()
                                                + header.getMergeRow(), index + 2, index + 2 + header.getMergeColumn());
                                        sheet.addMergedRegion(cellRangeAddress);
                                        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                        if (header.getMergeRow() > 0) {
                                            indexRowData = header.getMergeRow();
                                        }
                                        if (header.getMergeColumn() > 0) {
                                            index++;
                                        }
                                        if (header.getSubHeader().length > 0) {
                                            if (rowHeaderSub == null) {
                                                rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                                            }
                                            int k = index + 1;
                                            int s = 0;
                                            for (String sub : header.getSubHeader()) {
                                                Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                                                cellHeaderSub1.setCellValue(I18n.getString(item.getHeaderPrefix() + "." + sub));
                                                cellHeaderSub1.setCellStyle(cellStyleHeader);
                                                k++;
                                                s++;
                                            }
                                        }
                                    }
                                    cellHeader.setCellStyle(cellStyleHeader);
                                    index++;
                                }
                            } else {
                                cellHeader = rowHeader.createCell(index + 2);
                                cellHeader.setCellValue(I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                                if (header.isHasMerge()) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(), item.getStartRow()
                                            + header.getMergeRow(), index + 2, index + 2 + header.getMergeColumn());
                                    sheet.addMergedRegion(cellRangeAddress);
                                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                    if (header.getMergeRow() > 0) {
                                        indexRowData = header.getMergeRow();
                                    }
                                    if (header.getMergeColumn() > 0) {
                                        index++;
                                    }
                                }
                                cellHeader.setCellStyle(cellStyleHeader);
                                index++;
                            }
                        } else {
                            cellHeader = rowHeader.createCell(index + 2);
                            cellHeader.setCellValue(I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                            if (header.isHasMerge()) {
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(), item.getStartRow()
                                        + header.getMergeRow(), index + 2, index + 2 + header.getMergeColumn());
                                sheet.addMergedRegion(cellRangeAddress);
                                RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                if (header.getMergeRow() > 0) {
                                    indexRowData = header.getMergeRow();
                                }
                                if (header.getMergeColumn() > 0) {
                                    index++;
                                }
                            }
                            cellHeader.setCellStyle(cellStyleHeader);
                            index++;
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate ="collapsed" desc = "Build other cell">
                if (item.getLstCreateCell() != null) {
                    Row row;
                    for (CellConfigExport cell : item.getLstCreateCell()) {
                        row = sheet.getRow(cell.getRow());
                        if (row == null) {
                            row = sheet.createRow(cell.getRow());
                        }
                        //row.setHeight((short)-1);
                        Cell newCell = row.createCell(cell.getColumn());
                        if ("NUMBER".equals((cell.getStyleFormat()))) {
                            newCell.setCellValue(Double.parseDouble(cell.getValue()));
                        } else {
                            newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
                        }

                        if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(), cell.getRow() + cell.getRowMerge(),
                                    cell.getColumn(), cell.getColumn() + cell.getColumnMerge());
                            sheet.addMergedRegion(cellRangeAddress);
                            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                        }
                        if ("HEAD".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleHeader);
                        }
                        if ("CENTER".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleCenter);
                        }
                        if ("LEFT".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleLeft);
                        }
                        if ("RIGHT".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleRight);
                        }
                        if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellCenter);
                        }
                        if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellLeft);
                        }
                        if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellRight);
                        }
                    }
                }
                //</editor-fold>

                if (item.getCustomColumnWidthNoMerge() != null && item.getCustomColumnWidthNoMerge().length > 0) {
                    String[] customWith = item.getCustomColumnWidthNoMerge();
                    for (int i = 0; i <= item.getHeader().size(); i++) {
//                            sheet.autoSizeColumn(i);
                        sheet.setColumnWidth(i, Integer.valueOf(customWith[i]));
                    }
                }

                //<editor-fold defaultstate ="collapsed" desc = "Fill data">
                if (item.getLstData() != null && !item.getLstData().isEmpty()) {
//                        init mapColumn
                    Object firstRow = item.getLstData().get(0);
                    Map<String, Field> mapField = new HashMap<>();
                    for (ConfigHeaderExport header : item.getHeader()) {
                        for (Field f : firstRow.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.getName().equals(header.getFieldName())) {
                                mapField.put(header.getFieldName(), f);
                            }
                            String[] replace = header.getReplace();
                            if (replace != null) {
                                if (replace.length > 2) {
                                    for (int n = 2; n < replace.length; n++) {
                                        if (f.getName().equals(replace[n])) {
                                            mapField.put(replace[n], f);
                                        }
                                    }
                                }
                            }
                        }
                        if (firstRow.getClass().getSuperclass() != null) {
                            for (Field f : firstRow.getClass().getSuperclass().getDeclaredFields()) {
                                f.setAccessible(true);
                                if (f.getName().equals(header.getFieldName())) {
                                    mapField.put(header.getFieldName(), f);
                                }
                                String[] replace = header.getReplace();
                                if (replace != null) {
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            if (f.getName().equals(replace[n])) {
                                                mapField.put(replace[n], f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
//                        fillData
                    Row row;
                    List lstData = item.getLstData();
                    List<ConfigHeaderExport> lstHeader = item.getHeader();
                    int startRow = item.getStartRow();
                    String splitChar = item.getSplitChar();
                    for (int i = 0; i < lstData.size(); i++) {
                        row = sheet.createRow(i + startRow + 1 + indexRowData);
                        row.setHeight((short) 250);

                        Cell cell = row.createCell(0);
                        cell.setCellValue(i + 1);
                        cell.setCellStyle(cellStyleCenter);
                        int j = 0;
                        for (int e = 0; e < lstHeader.size(); e++) {
                            ConfigHeaderExport head = lstHeader.get(e);
                            String header = head.getFieldName();
                            String align = head.getAlign();
                            Object obj = lstData.get(i);

                            Field f = mapField.get(header);

                            if (fieldSplit != null && fieldSplit.containsKey(header)) {
                                String[] arrHead = fieldSplit.get(header).split(splitChar);
                                String value = "";
                                Object tempValue = f.get(obj);
                                if (tempValue instanceof Date) {
                                    value = tempValue == null ? "" : DateUtils.converstDateToString((Date) tempValue, "dd/MM/yyy HH:mm:ss");
                                } else {
                                    value = tempValue == null ? "" : tempValue.toString();
                                }

                                String[] fieldSplitValue = value.split(splitChar);
                                for (int m = 0; m < arrHead.length; m++) {
                                    if (head.isHasMerge() && head.getSubHeader().length > 0) {
                                        int s = 0;
                                        for (String sub : head.getSubHeader()) {
                                            cell = row.createCell(j + 1);
                                            String[] replace = head.getReplace();
                                            if (replace != null) {
                                                List<String> more = new ArrayList<>();
                                                if (replace.length > 2) {
                                                    for (int n = 2; n < replace.length; n++) {
                                                        Object objStr = mapField.get(replace[n]).get(obj);
                                                        String valueStr = objStr == null ? "" : objStr.toString();
                                                        more.add(valueStr);
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    double numberValue = replaceNumberValue(replace[0], m, more, s);
                                                    if (Double.compare(numberValue, -888) == 0) {
                                                        cell.setCellValue("*");
                                                    } else if (Double.compare(numberValue, -999) == 0) {
                                                        cell.setCellValue("-");
                                                    } else {
                                                        cell.setCellValue(numberValue);
                                                    }
                                                } else {
                                                    cell.setCellValue(replaceStringValue(replace[0], m, more, s));
                                                }
                                                s++;
                                            } else {
                                                String subValue = "";
                                                for (Field subf : firstRow.getClass().getDeclaredFields()) {
                                                    subf.setAccessible(true);
                                                    if (subf.getName().equals(sub)) {
                                                        String[] arrSub = (subf.get(obj) == null ? null : subf.get(obj).toString().split(item.getSplitChar()));
                                                        if (arrSub != null) {
                                                            subValue = arrSub[m];
                                                        }
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    if (StringUtils.isStringNotNullOrEmtry(subValue)) {
                                                        cell.setCellValue(Double.parseDouble(subValue));
                                                    } else {
                                                        cell.setCellValue(subValue == null ? "" : subValue);
                                                    }
                                                } else {
                                                    if (subValue == null) {
                                                        cell.setCellValue("");
                                                    } else if (subValue.length() > 32767) {
                                                        cell.setCellValue(subValue.substring(0, 32766));
                                                    } else {
                                                        cell.setCellValue(subValue);
                                                    }
                                                }
                                            }
                                            if ("CENTER".equals(align)) {
                                                cell.setCellStyle(cellStyleCenter);
                                            }
                                            if ("LEFT".equals(align)) {
                                                cell.setCellStyle(cellStyleLeft);
                                            }
                                            if ("RIGHT".equals(align)) {
                                                cell.setCellStyle(cellStyleRight);
                                            }
                                            j++;
                                        }
                                    } else {
                                        if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                                            if (j > 0) {
                                                j++;
                                            }
                                            cell = row.createCell(j + 1);
                                        } else {
                                            cell = row.createCell(j + 1);
                                        }
                                        String[] replace = head.getReplace();
                                        if (replace != null) {
                                            Object valueReplace = mapField.get(replace[1]).get(obj);
                                            List<String> more = new ArrayList<>();
                                            if (replace.length > 2) {
                                                for (int n = 2; n < replace.length; n++) {
                                                    Object objStr = mapField.get(replace[n]).get(obj);
                                                    String valueStr = objStr == null ? "" : objStr.toString();
                                                    more.add(valueStr);
                                                }
                                            }
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                double numberValue = replaceNumberValue(replace[0], valueReplace, more, m);
                                                if (Double.compare(numberValue, -888) == 0) {
                                                    cell.setCellValue("*");
                                                } else if (Double.compare(numberValue, -999) == 0) {
                                                    cell.setCellValue("-");
                                                } else {
                                                    cell.setCellValue(numberValue);
                                                }
                                            } else {
                                                cell.setCellValue(replaceStringValue(replace[0], valueReplace, more, m));
                                            }
                                        } else {
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                if (StringUtils.isStringNotNullOrEmtry(fieldSplitValue[m])) {
                                                    cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                                                } else {
                                                    cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                                }
                                            } else {
                                                cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                            }
                                        }
                                        if ("CENTER".equals(align)) {
                                            cell.setCellStyle(cellStyleCenter);
                                        }
                                        if ("LEFT".equals(align)) {
                                            cell.setCellStyle(cellStyleLeft);
                                        }
                                        if ("RIGHT".equals(align)) {
                                            cell.setCellStyle(cellStyleRight);
                                        }
                                        j++;
                                    }
                                }
                            } else {
                                String value = "";
                                if (f != null) {
                                    Object tempValue = f.get(obj);
                                    if (tempValue instanceof Date) {
                                        value = tempValue == null ? "" : DateUtils.converstDateToString((Date) tempValue, "dd/MM/yyyy HH:mm:ss");
                                    } else {
                                        value = tempValue == null ? "" : tempValue.toString();
                                    }
                                }
                                if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                                    if (j > 0) {
                                        j++;
                                    }
                                    cell = row.createCell(j + 1);
                                } else {
                                    cell = row.createCell(j + 1);
                                }
                                String[] replace = head.getReplace();
                                if (replace != null) {
                                    Object valueReplace = mapField.get(replace[1]).get(obj);
                                    List<String> more = new ArrayList<>();
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            Object objStr = mapField.get(replace[n]).get(obj);
                                            String valueStr = objStr == null ? "" : objStr.toString();
                                            more.add(valueStr);
                                        }
                                    }
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        double numberValue = replaceNumberValue(replace[0], valueReplace, more, i);
                                        if (Double.compare(numberValue, -888) == 0) {
                                            cell.setCellValue("*");
                                        } else if (Double.compare(numberValue, -999) == 0) {
                                            cell.setCellValue("-");
                                        } else {
                                            cell.setCellValue(numberValue);
                                        }
                                    } else {
                                        cell.setCellValue(replaceStringValue(replace[0], valueReplace, more, i));
                                    }
                                } else {
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        if (StringUtils.isStringNotNullOrEmtry(value)) {
                                            cell.setCellValue(Double.parseDouble(value));
                                        } else {
                                            cell.setCellValue(value == null ? "" : value);
                                        }
                                    } else {
                                        if (value == null) {
                                            cell.setCellValue("");
                                        } else if (value.length() > 32767) {
                                            cell.setCellValue(value.substring(0, 32766));
                                        } else {
                                            cell.setCellValue(value);
                                        }
                                    }
                                }
                                if ("CENTER".equals(align)) {
                                    cell.setCellStyle(cellStyleCenter);
                                }
                                if ("LEFT".equals(align)) {
                                    cell.setCellStyle(cellStyleLeft);
                                }
                                if ("RIGHT".equals(align)) {
                                    cell.setCellStyle(cellStyleRight);
                                }
                                j++;
                            }
                        }
                        if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                            int b = 1;
                            int size = 0;
                            if (item.getCustomColumnWidth().length % 2 == 0) {
                                size = item.getCustomColumnWidth().length / 2;
                            } else {
                                size = item.getCustomColumnWidth().length / 2 + 1;
                            }
                            for (int a = 1; a < size; a++) {
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), b, b + 1);
                                if (b == 1) {
                                    b = a + 2;
                                } else {
                                    b += 2;
                                }
                                sheet.addMergedRegion(cellRangeAddress);
                                RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                            }
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate ="collapsed" desc = "Merge row">
                if (item.getLstCellMerge() != null) {
                    for (CellConfigExport cell : item.getLstCellMerge()) {
                        if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(), cell.getRow() + cell.getRowMerge()
                                    , cell.getColumn(), cell.getColumn() + cell.getColumnMerge());
                            sheet.addMergedRegion(cellRangeAddress);
                            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate ="collapsed" desc = "Auto size column">
                if (item.getCustomColumnWidthNoMerge() == null || item.getCustomColumnWidthNoMerge().length < 1) {
                    if (item.getLstData() != null && item.getLstData().size() < 10000) {
                        sheet.trackAllColumnsForAutoSizing();
                        for (int i = 0; i <= item.getHeader().size(); i++) {
                            sheet.autoSizeColumn(i);
                            if (sheet.getColumnWidth(i) > 20000) {
                                sheet.setColumnWidth(i, 20000);
                            }
                        }
                    }
                }
                //</editor-fold>

                if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                    for (int i = 0; i <= item.getCustomColumnWidth().length - 1; i++) {
                        sheet.autoSizeColumn(i);
                        String[] columnWidth = item.getCustomColumnWidth();
                        sheet.setColumnWidth(i, Integer.parseInt(columnWidth[i]));
                    }
                }

//                    hide column
                List<Integer> lstColumnHidden = item.getLstColumnHidden();
                if (lstColumnHidden != null) {
                    for (Integer index : lstColumnHidden) {
                        sheet.setColumnHidden(index, true);
                    }
                }
            }
            if (exportChart == null || exportChart.length == 0) {
                workbook.removeSheetAt(0);
            }

            if (exportChart != null && exportChart.length > 0) {
                //<editor-fold defaultstate ="collapsed" desc = "Ve bieu do">
                ConfigFileExport item = config.get(0);
                Sheet sheetConf = workbook_temp.getSheet("conf");

//                Dong bat dau du lieu chart
                Row rowStartConf = sheetConf.getRow(0);
                Cell cellStarConf = rowStartConf.getCell(1);
                cellStarConf.setCellValue(item.getStartRow() + 1);

//                Dong ket thuc du lieu cua chart
                Row rowEndConf = sheetConf.getRow(1);
                Cell cellEndConf = rowStartConf.getCell(1);
                cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

//                Cot bat dau du lieu cua chart
                String xStart = "";

//                Cot ket thuc du lieu cua chart
                String xEnd = "";

//                xAxis
                Row rowXvalue = sheetConf.getRow(2);
                Cell cellXvalue = rowXvalue.getCell(1);
                cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
                        Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

                //                Categories
                Row rowNameList = sheetConf.getRow(3);
                Cell cellNameList = rowNameList.getCell(1);
                cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
                        Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

                Row rowDataValue = sheetConf.getRow(4);
                Cell cellDataValue = rowDataValue.getCell(1);
                cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
                        Locale.forLanguageTag("vi")) + "!$" + xStart + "${i}:$" + xEnd + "${i}");
                //</editor-fold>
            }
            try {
                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (hwb != null) {
                try {
                    hwb.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (workbook_temp != null) {
                try {
                    workbook_temp.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (fileTemplate != null) {
                try {
                    fileTemplate.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
        return new File(pathOut);
    }

    private static double replaceNumberValue(String replace, Object obj, List<String> lst, int i) {
        double valueReturn = 0.0D;
        return valueReturn;
    }

    private static String replaceStringValue(String modul, Object valueReplace, List<String> more, int index) {
        String strReturn = "";
        return strReturn;
    }

    public static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("title", style);

        Font indexTitleFont = wb.createFont();
        indexTitleFont.setFontHeightInPoints((short) 12);
        indexTitleFont.setBold(true);
        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(indexTitleFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("indexTitle", style);

        Font noteFont = wb.createFont();
        noteFont.setFontHeightInPoints((short) 15);
        noteFont.setColor(IndexedColors.BLACK.index);
        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(noteFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.YELLOW.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("note", style);

        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLUE.index);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setFont(headerFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(false);
        styles.put("cell", style);

//        trai
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setWrapText(false);
        styles.put("cellLeft", style);

//        phai
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setWrapText(false);
        styles.put("cellRight", style);

        //        giua
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setWrapText(false);
        styles.put("cellCenter", style);

//        subTitle
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);
        styles.put("cellSubTitle", style);

        style = wb.createCellStyle();
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0"));
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        styles.put("cellNumber", style);

        return styles;
    }

    //    export theo template va co 2 sheet
    private static File exportFiltWithTemplateXLSX(String pathTemlate, String fileNameOut, ConfigFileExport config,
                                                   String pathOut, List<Integer> lstColumnHidden, ConfigFileExport configSheet2, String... title) throws Exception {
        return null;
    }

    public static Font setFontTopHeader(XSSFWorkbook workbook) {
        Font xssFontTopHeader = workbook.createFont();
        xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
        xssFontTopHeader.setFontHeightInPoints((short) 10);
        xssFontTopHeader.setColor(IndexedColors.BLACK.index);
        xssFontTopHeader.setBold(true);
        return xssFontTopHeader;
    }

    public static Font setFont(XSSFWorkbook workbook) {
        Font xssFontTopHeader = workbook.createFont();
        xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
        xssFontTopHeader.setFontHeightInPoints((short) 20);
        xssFontTopHeader.setColor(IndexedColors.BLACK.index);
        xssFontTopHeader.setBold(true);
        return xssFontTopHeader;
    }

    public static Font setFontSubTitle(XSSFWorkbook workbook) {
        Font xssFontTopHeader = workbook.createFont();
        xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
        xssFontTopHeader.setFontHeightInPoints((short) 10);
        xssFontTopHeader.setColor(IndexedColors.RED.index);
        xssFontTopHeader.setBold(true);
        return xssFontTopHeader;
    }

    public static Font setFontHeader(XSSFWorkbook workbook) {
        Font xssFontTopHeader = workbook.createFont();
        xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
        xssFontTopHeader.setFontHeightInPoints((short) 10);
        xssFontTopHeader.setColor(IndexedColors.BLUE.index);
        xssFontTopHeader.setBold(true);
        return xssFontTopHeader;
    }

    public static Font setRowDataFont(XSSFWorkbook workbook) {
        Font xssFontTopHeader = workbook.createFont();
        xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
        xssFontTopHeader.setFontHeightInPoints((short) 10);
        xssFontTopHeader.setColor(IndexedColors.BLACK.index);
        return xssFontTopHeader;
    }

    public static CellStyle setCellStyleTopHeader(XSSFWorkbook workbook) {
        Font font = setFontTopHeader(workbook);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle setCellStyleTopRightHeader(XSSFWorkbook workbook) {
        Font font = setFontTopHeader(workbook);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle setCellStyleTitle(XSSFWorkbook workbook) {
        Font font = setFontTopHeader(workbook);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle setCellStyleSubTitle(XSSFWorkbook workbook) {
        Font font = setFontTopHeader(workbook);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        font.setColor(IndexedColors.RED.index);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle setCellStyleHeader(XSSFWorkbook workbook) {
        Font font = setFontTopHeader(workbook);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
