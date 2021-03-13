package com.viettel.gnoc.commons.utils;

import com.viettel.gnoc.commons.config.I18n;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
@Slf4j
public class CommonExport {

  public static final String XLSX_FILE_EXTENTION = ".xlsx";
  public static final String DOC_FILE_EXTENTION = ".doc";
  public static final String DOCX_FILE_EXTENTION = ".docx";
  public static final String XLSM_FILE_EXTENTION = ".xlsm";
  public static final String PDF_FILE_EXTENTION = ".pdf";
  public static final String XLS_FILE_EXTENTION = ".xls";

  public static File exportExcel(
      String pathTemplate,
      String fileNameOut,
      List<ConfigFileExport> config,
      String pathOut,
      String... exportChart
  ) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0
        ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
    HSSFWorkbook hwb = null;
    InputStream fileTemplate = null;
    XSSFWorkbook workbook_temp = null;
    SXSSFWorkbook workbook = null;
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      fileTemplate = resource.getInputStream();
      workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      workbook = new SXSSFWorkbook(workbook_temp, 1000);
      hwb = new HSSFWorkbook();

      //<editor-fold defaultstate="collapsed" desc="Declare style">
      CellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

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
      xssFontTopHeader.setBold(true);

      Font rowDataFont = workbook.createFont();
      rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
      rowDataFont.setFontHeightInPoints((short) 10);
      rowDataFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTopHeader = workbook.createCellStyle();
      cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
      cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
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
      //gnoc_cr
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
      cellStyleRight.setFont(rowDataFont);

      CellStyle right = workbook.createCellStyle();
      right.setAlignment(HorizontalAlignment.RIGHT);
      right.setVerticalAlignment(VerticalAlignment.CENTER);
      right.setWrapText(true);

      CellStyle left = workbook.createCellStyle();
      left.setAlignment(HorizontalAlignment.LEFT);
      left.setVerticalAlignment(VerticalAlignment.CENTER);
      left.setWrapText(true);

      CellStyle center = workbook.createCellStyle();
      center.setAlignment(HorizontalAlignment.CENTER);
      center.setVerticalAlignment(VerticalAlignment.CENTER);
      center.setWrapText(true);
      //</editor-fold>

      for (ConfigFileExport item : config) {

        Map<String, String> fieldSplit = item.getFieldSplit();
        SXSSFSheet sheet;
        if (exportChart != null && exportChart.length > 0) {
          sheet = workbook.getSheetAt(0);
        } else {
          sheet = workbook.createSheet(item.getSheetName());
        }

        if (item.getCellTitleIndex() >= 3) {
          //TienNV them template header
          Row headerFirstTitle = sheet.createRow(0);
          Row headerSecondTitle = sheet.createRow(1);
          int sizeheader = 5;
          Cell firstLeftHeader = headerFirstTitle.createCell(1);
          firstLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell secondLeftHeader = headerSecondTitle.createCell(1);
          secondLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheader - 1);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheader - 1);
          secondRightHeader.setCellStyle(cellStyleTopRightHeader);
          firstLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstLeftHeaderTitle()) ? ""
                  : item.getFirstLeftHeaderTitle());
          secondLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondLeftHeaderTitle()) ? ""
                  : item.getSecondLeftHeaderTitle());
          firstRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstRightHeaderTitle()) ? ""
                  : item.getFirstRightHeaderTitle());
          secondRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondRightHeaderTitle()) ? ""
                  : item.getSecondRightHeaderTitle());
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheader - 1,
              sizeheader));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheader - 1,
              sizeheader));
          //end tiennv
        }

        // Title
        Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());

        Cell mainCellTitle;
        if (item.getCustomTitle() != null && item.getCustomTitle().length > 0) {
          mainCellTitle = rowMainTitle.createCell(0);
        } else {
          mainCellTitle = rowMainTitle.createCell(1);
        }
        mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
        // Sub title
        int indexSubTitle =
            (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex() + 1
                : item.getCellTitleIndex() + 2;
        Row rowSubTitle;
        if (item.getCustomTitle() != null && item.getCustomTitle().length
            > 0) {//tiennv bo sung style cho CR, ai khong dung thi thoi
          CellStyle styleTitle = workbook.createCellStyle();
          //Tao tieu de
          Font xssFontTitle = workbook.createFont();
          xssFontTitle.setFontName(HSSFFont.FONT_ARIAL);
          xssFontTitle.setFontHeightInPoints((short) 22);
          xssFontTitle.setColor(IndexedColors.WHITE.index);
          xssFontTitle.setBold(true);
          styleTitle.setBorderBottom(BorderStyle.THIN);
          styleTitle.setBorderLeft(BorderStyle.THIN);
          styleTitle.setBorderTop(BorderStyle.THIN);
          styleTitle.setBorderRight(BorderStyle.THIN);
          styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
          HSSFPalette palette = hwb.getCustomPalette();
          String[] customTitle = item.getCustomTitle();
          String[] bgColors = customTitle[2].split(",");
          HSSFColor myColor = palette
              .findSimilarColor(Integer.valueOf(bgColors[0]), Integer.valueOf(bgColors[1]),
                  Integer.valueOf(bgColors[2]));
          styleTitle.setFillForegroundColor(myColor.getIndex());
          styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
          styleTitle.setFont(xssFontTitle);
          mainCellTitle.setCellStyle(styleTitle);
          sheet.addMergedRegion(
              new CellRangeAddress(item.getCellTitleIndex(),
                  item.getCellTitleIndex() + Integer.valueOf(customTitle[3]), 0,
                  item.getMergeTitleEndIndex()));
          styleTitle.setAlignment(HorizontalAlignment.LEFT);
          styleTitle.setIndention(Short.valueOf(customTitle[1]));
          subTitleFont.setBold(true);
          cellStyleSubTitle.setFont(subTitleFont);
          cellStyleSubTitle.setAlignment(HorizontalAlignment.LEFT);
          cellStyleSubTitle.setIndention(Short.valueOf(customTitle[1]));
          indexSubTitle =
              (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex()
                  + Integer.valueOf(customTitle[3])
                  : item.getCellTitleIndex() + 3;
          rowSubTitle = sheet.createRow(indexSubTitle);
          Cell cellTitle = rowSubTitle.createCell(0);
          cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
          cellTitle.setCellStyle(cellStyleSubTitle);
          sheet.addMergedRegion(
              new CellRangeAddress(indexSubTitle, indexSubTitle, 0,
                  item.getMergeTitleEndIndex()));
        } else {
          mainCellTitle.setCellStyle(cellStyleTitle);
          sheet.addMergedRegion(
              new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1,
                  item.getMergeTitleEndIndex()));
          rowSubTitle = sheet.createRow(indexSubTitle);
          Cell cellTitle = rowSubTitle.createCell(1);
          cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
          cellTitle.setCellStyle(cellStyleSubTitle);
          sheet.addMergedRegion(
              new CellRangeAddress(indexSubTitle, indexSubTitle, 1,
                  item.getMergeTitleEndIndex()));
        }

        int indexRowData = 0;
        //<editor-fold defaultstate="collapsed" desc="Build header">
        if (item.isCreatHeader()) {
          int index = -1;
          Cell cellHeader;
          Row rowHeader = sheet.createRow(item.getStartRow());
          rowHeader.setHeight((short) 500);
          Row rowHeaderSub = null;
          for (ConfigHeaderExport header : item.getHeader()) {
            if (fieldSplit != null) {
              if (fieldSplit.get(header.getFieldName()) != null) {
                String[] fieldSplitHead = fieldSplit.get(header.getFieldName())
                    .split(item.getSplitChar());
                for (String field : fieldSplitHead) {
                  cellHeader = rowHeader.createCell(index + 2);
                  cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                  if (header.isHasMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                        index + 2, index + 2 + header.getMergeColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress,
                        sheet);

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
                        cellHeaderSub1.setCellValue(
                            I18n.getString(item.getHeaderPrefix() + "." + sub));
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
                cellHeader.setCellValue(
                    I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                if (header.isHasMerge()) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(
                      item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                      index + 2, index + 2 + header.getMergeColumn());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil
                      .setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderRight(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

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
              cellHeader.setCellValue(
                  I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
              if (header.isHasMerge()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                    item.getStartRow() + header.getMergeRow(), index + 2,
                    index + 2 + header.getMergeColumn());
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

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

        //<editor-fold defaultstate="collapsed" desc="Build other cell">
        if (item.getLstCreatCell() != null) {
          Row row;
          for (CellConfigExport cell : item.getLstCreatCell()) {
            row = sheet.getRow(cell.getRow());
            if (row == null) {
              row = sheet.createRow(cell.getRow());
            }
            //row.setHeight((short) -1);
            Cell newCell = row.createCell(cell.getColumn());
            if ("NUMBER".equals(cell.getStyleFormat())) {
              newCell.setCellValue(Double.valueOf(cell.getValue()));
            } else {
              newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
            }

            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
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
              newCell.setCellStyle(center);
            }
            if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(left);
            }
            if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(right);
            }
          }
        }
        //</editor-fold>

        if (item.getCustomColumnWidthNoMerge() != null
            && item.getCustomColumnWidthNoMerge().length > 0) {
//          sheet.trackAllColumnsForAutoSizing();
          String[] customWith = item.getCustomColumnWidthNoMerge();
          for (int i = 0; i <= item.getHeader().size(); i++) {
//            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, Integer.valueOf(customWith[i]));
//            if (sheet.getColumnWidth(i) > 20000) {
//              sheet.setColumnWidth(i, 20000);
//            }
          }
        }

        //<editor-fold defaultstate="collapsed" desc="Fill data">
        if (item.getLstData() != null && !item.getLstData().isEmpty()) {
          //init mapColumn
          Object firstRow = item.getLstData().get(0);
          Map<String, Field> mapField = new HashMap<String, Field>();
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
              for (Field f : firstRow.getClass()
                  .getSuperclass().getDeclaredFields()) {
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

          //fillData
          Row row;
          List lstData = item.getLstData();
          List<ConfigHeaderExport> lstHeader = item.getHeader();
          int startRow = item.getStartRow();
          String splitChar = item.getSplitChar();
          for (int i = 0; i < lstData.size(); i++) {
            row = sheet.createRow(i + startRow + 1 + indexRowData);
            row.setHeight((short) 250);
            //row.setHeight((short) -1);
            Cell cell;

            cell = row.createCell(0);
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
                  value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
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
                          double numberValue = replaceNumberValue(replace[0], m,
                              more, s);
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
                            String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                obj).toString()).split(item.getSplitChar());
                            subValue = arrSub[m];
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          if (StringUtils.isNotNullOrEmpty(subValue)) {
                            cell.setCellValue(Double.valueOf(subValue));
                          } else {
                            cell.setCellValue(subValue == null ? "" : subValue);
                          }
                        } else {
                          if (subValue == null) {
                            cell.setCellValue("");
                          } else if (subValue.length() > 32767) {
                            cell.setCellValue(subValue.substring(0, 32766));
                          } else {
                            cell.setCellValue(subValue);//cut cho nay
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
                    if (item.getCustomColumnWidth() != null
                        && item.getCustomColumnWidth().length > 0) {
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
                        double numberValue = replaceNumberValue(replace[0],
                            valueReplace, more, m);
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
                        if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
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
                    value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
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
                    double numberValue = replaceNumberValue(replace[0], valueReplace, more,
                        i);
                    if (Double.compare(numberValue, -888) == 0) {
                      cell.setCellValue("*");
                    } else if (Double.compare(numberValue, -999) == 0) {
                      cell.setCellValue("-");
                    } else {
                      cell.setCellValue(numberValue);
                    }
                  } else {
                    cell.setCellValue(
                        replaceStringValue(replace[0], valueReplace, more, i));
                  }
                } else {
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    if (StringUtils.isNotNullOrEmpty(value)) {
                      cell.setCellValue(Double.valueOf(value));
                    } else {
                      cell.setCellValue(value == null ? "" : value);
                    }
                  } else {
                    if (value == null) {
                      cell.setCellValue("");
                    } else if (value.length() > 32767) {
                      cell.setCellValue(value.substring(0, 32766));
                    } else {
                      cell.setCellValue(value);//cut cho nay
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
                size = (item.getCustomColumnWidth().length / 2) + 1;
              }
              for (int a = 1; a < size; a++) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), b, b + 1);
                if (b == 1) {
                  b = a + 2;
                } else {
                  b += 2;
                }
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Merge row">
        if (item.getLstCellMerge() != null) {
          for (CellConfigExport cell : item.getLstCellMerge()) {
            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Auto size column">
        if (item.getCustomColumnWidthNoMerge() == null
            || item.getCustomColumnWidthNoMerge().length < 1) {
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
        //tiennv hide column
        List<Integer> lsColumnHidden = item.getLsColumnHidden();
        if (lsColumnHidden != null) {
          for (Integer index : lsColumnHidden) {
            sheet.setColumnHidden(index, true);
          }
        }
      }

      if (exportChart == null || exportChart.length == 0) {
        workbook.removeSheetAt(0);
      }

      if (exportChart != null && exportChart.length > 0) {
        //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
        ConfigFileExport item = config.get(0);
        Sheet sheetConf = workbook_temp.getSheet("conf");

        // Dong bat dau du lieu cua chart
        Row rowStartConf = sheetConf.getRow(0);
        Cell cellStartConf = rowStartConf.getCell(1);
        cellStartConf.setCellValue(item.getStartRow() + 1);

        // Dong ket thuc du lieu cua chart
        Row rowEndConf = sheetConf.getRow(1);
        Cell cellEndConf = rowEndConf.getCell(1);
        cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

        // Cot bat dau du lieu cua chart
        String xStart = "";

        // Cot ket thuc du lieu cua chart
        String xEnd = "";

        // xAxis
        Row rowXvalue = sheetConf.getRow(2);
        Cell cellXvalue = rowXvalue.getCell(1);
        cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

        // Categories
        Row rowNameList = sheetConf.getRow(3);
        Cell cellNameList = rowNameList.getCell(1);
        cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

        // Data
        Row rowDataValue = sheetConf.getRow(4);
        Cell cellDataValue = rowDataValue.getCell(1);
        cellDataValue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
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
    } catch (FileNotFoundException e) {
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

  public static double replaceNumberValue(String modul, Object valueReplace, List<String> more,
      int index) {
    double valueReturn = 0;
//    String strValue = valueReplace == null ? "" : valueReplace.toString();
//    DecimalFormat df = new DecimalFormat("#.##");
    return valueReturn;
  }

  public static String replaceStringValue(String modul, Object valueReplace, List<String> more,
      int index) {
    String strReturn = "";
//    String strValue = valueReplace == null ? "" : valueReplace.toString();
    return strReturn;
  }

  public static Map<String, CellStyle> createStyles(Workbook wb) {
    Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
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
    noteFont.setColor(IndexedColors.BLACK.getIndex());
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.LEFT);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFont(noteFont);
    style.setWrapText(true);
    styles.put("note", style);

    Font headerFont = wb.createFont();
    headerFont.setFontHeightInPoints((short) 12);
    headerFont.setColor(IndexedColors.BLUE.getIndex());
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setFont(headerFont);
    style.setWrapText(true);
    styles.put("header", style);

    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.LEFT);
    style.setWrapText(false);
//    style.setBorderRight(BorderStyle.THIN);
//    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//    style.setBorderLeft(BorderStyle.THIN);
//    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//    style.setBorderTop(BorderStyle.THIN);
//    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//    style.setBorderBottom(BorderStyle.THIN);
//    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    styles.put("cell", style);

    //trai
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.LEFT);
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setWrapText(false);
    styles.put("cellLeft", style);

    //phai
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.RIGHT);
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setWrapText(false);
    styles.put("cellRight", style);

    //giua
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setWrapText(false);
    styles.put("cellCenter", style);

    //subTitle
    style = wb.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setWrapText(false);
    styles.put("cellSubTitle", style);

    style = wb.createCellStyle();
    style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0"));
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    styles.put("cellnumber", style);

    return styles;
  }

  //export theo template va co 2 sheet
  public static File exportFileWithTemplateXLS(String pathTemplate, String fileNameOut,
      ConfigFileExport config, String pathOut, List<Integer> lsColumnHidden,
      ConfigFileExport configSheet2, String... title) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut
        + strCurTimeExp + XLS_FILE_EXTENTION;
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      HSSFWorkbook workbook = new HSSFWorkbook(fileTemplate);
      log.info("End get template file!");
//      XSSFWorkbook workbook = new XSSFWorkbook(workbook_temp, 1000);
      HSSFSheet worksheet = workbook.getSheetAt(0);

      HSSFCellStyle cellStyle;

      HSSFCellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      cellStyle = workbook.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(false);

      //Title of report
      HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

      HSSFFont hSSFFont = workbook.createFont();
      hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      hSSFFont.setFontHeightInPoints((short) 20);
      hSSFFont.setBold(true);
      hSSFFont.setColor(IndexedColors.BLACK.index);
//            hSSFFont.setColor(HSSFColor.GREEN.index);
      cellStyleTitle.setFont(hSSFFont);

      if (title != null && title.length > 0) {
        HSSFRow rowMainTitle = worksheet.createRow(config.getStartRow() - 4);
        HSSFCell mainCellTitle = rowMainTitle.createCell(config.getCellTitleIndex() - 2);
        mainCellTitle.setCellValue(title[0]);
        mainCellTitle.setCellStyle(cellStyleTitle);
        log.info("Number of merged regions = " + worksheet.getNumMergedRegions());
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
          CellRangeAddress range = worksheet.getMergedRegion(i);

          if (range.getFirstRow() == config.getStartRow() - 4) {
            log.info("Number of merged regions = " + i);
            worksheet.removeMergedRegion(i);
          }
        }
        worksheet.addMergedRegion(
            new CellRangeAddress(config.getStartRow() - 4, config.getStartRow() - 4,
                config.getCellTitleIndex() - 2, config.getCellTitleIndex() + 2));
      }
      //subTitle
      HSSFRow rowTitle = worksheet.createRow(config.getStartRow() - 2);
      HSSFCell cellTitle = rowTitle.createCell(config.getCellTitleIndex());
      cellTitle.setCellValue(config.getSubTitle());

      //header
      HSSFRow rowHeader = worksheet.createRow(config.getStartRow());
      rowHeader.setHeight((short) 500);

      HSSFCellStyle cellStyleHeader = workbook.createCellStyle();
      cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeader.setBorderLeft(BorderStyle.THIN);
      cellStyleHeader.setBorderBottom(BorderStyle.THIN);
      cellStyleHeader.setBorderRight(BorderStyle.THIN);
      cellStyleHeader.setBorderTop(BorderStyle.THIN);
      cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

      cellStyleHeader.setWrapText(false);
      HSSFFont hSSFFontHeader = workbook.createFont();
      hSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
      hSSFFontHeader.setFontHeightInPoints((short) 10);
      hSSFFontHeader.setBold(true);
      hSSFFontHeader.setColor(IndexedColors.BLUE.index);
      cellStyleHeader.setFont(hSSFFontHeader);

      List<ConfigHeaderExport> headerAlign = config.getHeader();
      for (int i = -1; i < headerAlign.size(); i++) {
        HSSFCell cellHeader = rowHeader.createCell(i + 1);
        if (i == -1) {
          cellHeader.setCellValue(I18n.getString(config.getHeaderPrefix() + ".stt"));
        } else {
          cellHeader.setCellValue(
              I18n.getString(config.getHeaderPrefix() + "." + headerAlign.get(i).getFieldName()));
        }
        cellHeader.setCellStyle(cellStyleHeader);
      }

      //trai
      HSSFCellStyle cellStyleLeft = workbook.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(false);
      //phai
      HSSFCellStyle cellStyleRight = workbook.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(false);
      //giua
      HSSFCellStyle cellStyleCenter = workbook.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(false);

      List lstDTO = config.getLstData();
      //Data
      if (lstDTO != null && !lstDTO.isEmpty()) {
        //init mapColumn
        Object firstRow = lstDTO.get(0);
        Map<String, Field> mapField = new HashMap<>();
        for (int j = 0; j < headerAlign.size(); j++) {
          for (Field f : firstRow.getClass().getDeclaredFields()) {
            String header = headerAlign.get(j).getFieldName();
            f.setAccessible(true);
            if (f.getName().equals(header)) {
              mapField.put(header, f);
            }
          }
        }

        //fillData
        for (int i = 0; i < lstDTO.size(); i++) {
          HSSFRow row = worksheet.createRow(i + config.getStartRow() + 1);
          for (int j = -1; j < headerAlign.size(); j++) {
            HSSFCell cell = row.createCell(j + 1);
            if (j == -1) {
              cell.setCellValue(i + 1);
              cell.setCellStyle(cellStyleCenter);
            } else {
              String header = headerAlign.get(j).getFieldName();
              String align = headerAlign.get(j).getAlign();
              Object obj = lstDTO.get(i);
              Field f = mapField.get(header);
//                            f.setAccessible(true);
              if (f.getName().equals(header)) {
                Object value = f.get(obj);
                cell.setCellValue(value == null ? "" : value.toString());
                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }
              }

            }
          }

        }
      }

      if (config.getIsAutoSize() != null) {
        //Set Width
        System.out.print("begin autoSizeColumn");
        for (int i = 0; i <= headerAlign.size(); i++) {
          worksheet.autoSizeColumn(i);
          if (worksheet.getColumnWidth(i) > 20000) {
            worksheet.setColumnWidth(i, 20000);
          }
        }
      }
      //begin vuhx hide column
      if (lsColumnHidden != null) {
        for (Integer index : lsColumnHidden) {
          worksheet.setColumnHidden(index, true);
        }
      }

      if (configSheet2 != null) {
        HSSFSheet worksheet2 = workbook.getSheetAt(1);
        Map<String, Field> mapFieldSheet2 = new HashMap<>();
        List<ConfigHeaderExport> lstHeader2 = configSheet2.getHeader();
        List<Object> dataSheet2 = configSheet2.getLstData();
        if (dataSheet2 != null && dataSheet2.size() > 0) {
          Object firstRowSheet2 = dataSheet2.get(0);
          for (int j = 0; j < lstHeader2.size(); j++) {
            for (Field f : firstRowSheet2.getClass().getDeclaredFields()) {
              String header = lstHeader2.get(j).getFieldName();
              f.setAccessible(true);
              if (f.getName().equals(header)) {
                mapFieldSheet2.put(header, f);
              }
            }
          }

          int l = 1;
          int k = configSheet2.getStartRow();
          for (Object item : dataSheet2) {
            HSSFRow row = worksheet2.createRow(k++);
            for (int j = -1; j < lstHeader2.size(); j++) {
              HSSFCell cell = row.createCell(j + 1);
              if (j == -1) {
                cell.setCellValue(String.valueOf(l++));
                cell.setCellStyle(cellStyleCenter);
              } else {
                String header = lstHeader2.get(j).getFieldName();
                Field f = mapFieldSheet2.get(header);
                if (f.getName().equals(header)) {
                  Object value = f.get(item);
                  cell.setCellValue(value == null ? "" : value.toString());
                  cell.setCellStyle(cellStyleLeft);
                }
              }
            }
          }
        }
      }

      try {

        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }

    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    }
//        File file = new File(pathOut);
//        return file;
    return new File(pathOut);

  }


  //export theo template va co 2 sheet
  public static File exportFileWithTemplateXLSX(String pathTemplate, String fileNameOut,
      ConfigFileExport config, String pathOut, List<Integer> lsColumnHidden,
      ConfigFileExport configSheet2, String... title) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    HSSFWorkbook hwb = new HSSFWorkbook();
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut
        + strCurTimeExp + XLSX_FILE_EXTENTION;
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      XSSFSheet worksheet = workbook_temp.getSheetAt(0);
      log.info( " --- getLastRowNum : " + worksheet.getLastRowNum());
//      log.info( " --- physicalNumber: " + sheetOne.getPhysicalNumberOfRows());
      for(int i = config.getStartRow(); i <= worksheet.getLastRowNum(); i++) {
        Row row = worksheet.getRow(i);
        if(row != null) {
          log.info(" --- remove row empty --- " + i );
          worksheet.removeRow(row);
        }
      }

//      XSSFWorkbook workbook = new XSSFWorkbook(workbook_temp, 1000);
      if (worksheet != null && config != null && StringUtils
          .isNotNullOrEmpty(config.getSheetName())) {
        workbook_temp.setSheetName(0, config.getSheetName());
      }
      CellStyle cellStyle;

      CellStyle cellStyleFormatNumber = workbook_temp.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      cellStyle = workbook_temp.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(false);

      //Title of report
      CellStyle cellStyleTitle = workbook_temp.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

      Font hSSFFont = workbook_temp.createFont();
      hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      hSSFFont.setFontHeightInPoints((short) 20);
      hSSFFont.setBold(true);
      hSSFFont.setColor(IndexedColors.BLACK.index);
//            hSSFFont.setColor(HSSFColor.GREEN.index);
      cellStyleTitle.setFont(hSSFFont);

      if (title != null && title.length > 0) {
        XSSFRow rowMainTitle = worksheet.createRow(config.getStartRow() - 4);
        XSSFCell mainCellTitle = rowMainTitle.createCell(config.getCellTitleIndex() - 2);
        mainCellTitle.setCellValue(title[0]);
        mainCellTitle.setCellStyle(cellStyleTitle);
        log.info("Number of merged regions = " + worksheet.getNumMergedRegions());
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
          CellRangeAddress range = worksheet.getMergedRegion(i);

          if (range.getFirstRow() == config.getStartRow() - 4) {
            log.info("Number of merged regions = " + i);
            worksheet.removeMergedRegion(i);
          }
        }
        worksheet.addMergedRegion(
            new CellRangeAddress(config.getStartRow() - 4, config.getStartRow() - 4,
                config.getCellTitleIndex() - 2, config.getCellTitleIndex() + 2));

      }
      //subTitle
      XSSFRow rowTitle = worksheet.createRow(config.getStartRow() - 2);
      XSSFCell cellTitle = rowTitle.createCell(config.getCellTitleIndex());
      cellTitle.setCellValue(config.getSubTitle());

      //header
      XSSFRow rowHeader = worksheet.createRow(config.getStartRow());
      rowHeader.setHeight((short) 500);

      CellStyle cellStyleHeader = workbook_temp.createCellStyle();
      cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeader.setBorderLeft(BorderStyle.THIN);
      cellStyleHeader.setBorderBottom(BorderStyle.THIN);
      cellStyleHeader.setBorderRight(BorderStyle.THIN);
      cellStyleHeader.setBorderTop(BorderStyle.THIN);

      HSSFPalette palette = hwb.getCustomPalette();
      HSSFColor myColor = palette
          .findSimilarColor(Integer.valueOf(204), Integer.valueOf(255),
              Integer.valueOf(204));
      cellStyleHeader.setFillForegroundColor(myColor.getIndex());
      cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//      cellStyleHeader.setFillForegroundColor(IndexedColors.BLUE.index); //tiennv con color nua ok
//      cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

      HSSFColor headerColor = palette
          .findSimilarColor(Integer.valueOf(0), Integer.valueOf(0),
              Integer.valueOf(255));

      cellStyleHeader.setWrapText(false);
      Font hSSFFontHeader = workbook_temp.createFont();
      hSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
      hSSFFontHeader.setFontHeightInPoints((short) 10);
      hSSFFontHeader.setBold(true);
      hSSFFontHeader.setColor(headerColor.getIndex());
      cellStyleHeader.setFont(hSSFFontHeader);

      List<ConfigHeaderExport> headerAlign = config.getHeader();
      for (int i = -1; i < headerAlign.size(); i++) {
        XSSFCell cellHeader = rowHeader.createCell(i + 1);
        if (i == -1) {
          cellHeader.setCellValue(I18n.getString(config.getHeaderPrefix() + ".stt"));
        } else {
          cellHeader.setCellValue(
              I18n.getString(config.getHeaderPrefix() + "." + headerAlign.get(i).getFieldName()));
        }
        cellHeader.setCellStyle(cellStyleHeader);
      }

      //trai
      CellStyle cellStyleLeft = workbook_temp.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(false);
      //phai
      CellStyle cellStyleRight = workbook_temp.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(false);
      //giua
      CellStyle cellStyleCenter = workbook_temp.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(false);

      //tiennv them workBook data
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);
      SXSSFSheet worksheetFinal = workbook.getSheetAt(0);

      List lstDTO = config.getLstData();
      //Data
      if (lstDTO != null && !lstDTO.isEmpty()) {
        //init mapColumn
        Object firstRow = lstDTO.get(0);
        Map<String, Field> mapField = new HashMap<>();
        for (int j = 0; j < headerAlign.size(); j++) {
          for (Field f : firstRow.getClass().getDeclaredFields()) {
            String header = headerAlign.get(j).getFieldName();
            f.setAccessible(true);
            if (f.getName().equals(header)) {
              mapField.put(header, f);
            }
          }
        }

        //fillData
        for (int i = 0; i < lstDTO.size(); i++) {
          SXSSFRow row = worksheetFinal.createRow(i + config.getStartRow() + 1);
          for (int j = -1; j < headerAlign.size(); j++) {
            SXSSFCell cell = row.createCell(j + 1);
            if (j == -1) {
              cell.setCellValue(i + 1);
              cell.setCellStyle(cellStyleCenter);
            } else {
              String header = headerAlign.get(j).getFieldName();
              String align = headerAlign.get(j).getAlign();
              Object obj = lstDTO.get(i);
              Field f = mapField.get(header);
//                            f.setAccessible(true);
              if (f.getName().equals(header)) {
                Object value = f.get(obj);
                cell.setCellValue(value == null ? "" : value.toString());
                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }
              }

            }
          }

        }
      }

      if (config.getIsAutoSize() != null) {
        //Set Width
        if (lstDTO != null && lstDTO.size() < 10000) {
          log.info("begin autoSizeColumn");
          worksheetFinal.trackAllColumnsForAutoSizing();
          for (int i = 0; i <= headerAlign.size(); i++) {
            worksheetFinal.autoSizeColumn(i);
            if (worksheetFinal.getColumnWidth(i) > 20000) {
              worksheetFinal.setColumnWidth(i, 20000);
            }
          }
        }
      }

      //begin vuhx hide column
      if (lsColumnHidden != null) {
        for (Integer index : lsColumnHidden) {
          worksheetFinal.setColumnHidden(index, true);
        }
      }

      if (configSheet2 != null) {
        SXSSFSheet worksheet2 = workbook.getSheetAt(1);
        Map<String, Field> mapFieldSheet2 = new HashMap<>();
        List<ConfigHeaderExport> lstHeader2 = configSheet2.getHeader();
        List<Object> dataSheet2 = configSheet2.getLstData();
        if (dataSheet2 != null && dataSheet2.size() > 0) {
          Object firstRowSheet2 = dataSheet2.get(0);
          for (int j = 0; j < lstHeader2.size(); j++) {
            for (Field f : firstRowSheet2.getClass().getDeclaredFields()) {
              String header = lstHeader2.get(j).getFieldName();
              f.setAccessible(true);
              if (f.getName().equals(header)) {
                mapFieldSheet2.put(header, f);
              }
            }
          }

          int l = 1;
          int k = configSheet2.getStartRow();
          for (Object item : dataSheet2) {
            SXSSFRow row = worksheet2.createRow(k++);
            for (int j = -1; j < lstHeader2.size(); j++) {
              SXSSFCell cell = row.createCell(j + 1);
              if (j == -1) {
                cell.setCellValue(String.valueOf(l++));
                cell.setCellStyle(cellStyleCenter);
              } else {
                String header = lstHeader2.get(j).getFieldName();
                Field f = mapFieldSheet2.get(header);
                if (f.getName().equals(header)) {
                  Object value = f.get(item);
                  if (value == null) {
                    cell.setCellValue("");
                  } else if (value.toString().length() > 32767) {
                    cell.setCellValue(value.toString().substring(0, 32766));
                  } else {
                    cell.setCellValue(value.toString());//cut cho nay
                  }
//                  cell.setCellValue(value == null ? "" : value.toString());
                  cell.setCellStyle(cellStyleLeft);
                }
              }
            }
          }
        }
      }

      try {

        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }

    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
      if (hwb != null) {
        try {
          hwb.close();
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    }
//        File file = new File(pathOut);
//        return file;
    return new File(pathOut);

  }

  public static Font setFontTopHeader(XSSFWorkbook workBook) {
    Font xssFontTopHeader = workBook.createFont();
//    xssFontTopHeader.setFontName("Times New Roman");
    xssFontTopHeader.setFontName(HSSFFont.FONT_ARIAL);
    xssFontTopHeader.setFontHeightInPoints((short) 10);
    xssFontTopHeader.setColor(IndexedColors.BLACK.index);
    xssFontTopHeader.setBold(true);
    return xssFontTopHeader;
  }

  public static Font setFont(XSSFWorkbook workBook) {
    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);
    return xSSFFont;
  }

  public static Font setFontSubTitle(XSSFWorkbook workBook) {
    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 10);
    xSSFFont.setItalic(true);
    xSSFFont.setColor(IndexedColors.RED.index);
    return xSSFFont;
  }

  public static Font setFontHeader(XSSFWorkbook workBook) {
    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);
    return xSSFFontHeader;
  }

  public static Font setRowDataFont(XSSFWorkbook workBook) {
    Font rowDataFont = workBook.createFont();
    rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
    rowDataFont.setFontHeightInPoints((short) 10);
    rowDataFont.setColor(IndexedColors.BLACK.index);
    return rowDataFont;
  }

  public static CellStyle setCellStyleTopHeader(XSSFWorkbook workBook) {
    Font xssFontTopHeader = setFontTopHeader(workBook);
    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);
    return cellStyleTopHeader;
  }

  public static CellStyle setCellStyleTopRightHeader(XSSFWorkbook workBook) {
    Font xssFontTopHeader = setFontTopHeader(workBook);
    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);
    return cellStyleTopHeader;
  }

  public static CellStyle setCellStyleTitle(XSSFWorkbook workBook) {
    Font xSSFFont = setFont(workBook);
    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);
    return cellStyleTitle;
  }

  public static CellStyle setCellStyleSubTitle(XSSFWorkbook workBook) {
    Font xSSFFont = setFontSubTitle(workBook);
    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    xSSFFont.setColor(IndexedColors.RED.getIndex());
    cellStyleTitle.setFont(xSSFFont);
    return cellStyleTitle;
  }

  public static CellStyle setCellStyleHeader(XSSFWorkbook workBook) {
    Font xSSFFontHeader = setFontHeader(workBook);
    CellStyle cellStyleHeader = workBook.createCellStyle();
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
    return cellStyleHeader;
  }
}
