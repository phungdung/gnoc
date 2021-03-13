package com.viettel.gnoc.commons.utils;

import com.viettel.gnoc.commons.config.I18n;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
@Slf4j
public class CommonImport {

  private static String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

  public static List getDataFromExcelFile(
      File file
      , int iSheet
      , int iBeginRow
      , int iFromCol
      , int iToCol
      , int rowBack
  ) throws IOException {
    List result = new ArrayList();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    FileInputStream fileInput = null;
    Workbook workbook = null;
    try {
      fileInput = new FileInputStream(file);
      workbook = WorkbookFactory.create(fileInput);
      Sheet worksheet = workbook.getSheetAt(iSheet);
      int irowBack = 0;

      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        Row row = worksheet.getRow(i);

        if (row != null) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null) {
              CellType cellType = cell.getCellType();
              switch (cellType) {
                case STRING:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
                case NUMERIC:
                  Double doubleValue = (Double) cell.getNumericCellValue();
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(doubleValue);
                    obj[iCount] = simpleDateFormat.format(date);
                    break;
                  }
                  List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                  if (lstValue.get(1).matches("[0]+")) {
                    obj[iCount] = lstValue.get(0);
                  } else {
                    DecimalFormat df = new DecimalFormat("##.##");
//                    obj[iCount] = df.format(doubleValue);
                    obj[iCount] = doubleValue;
                  }
                  break;
                case BLANK:
                  check++;
                  break;
                case FORMULA:
                  obj[iCount] = cell.getCellFormula().trim();
                  break;
                default:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
              }
            } else {
              obj[iCount] = null;
            }
            iCount += 1;
          }
          if (check != (iToCol - iFromCol + 1)) {
            boolean isOK = false;
            for (int k = 0; k < obj.length; k++) {
              if (!StringUtils.isStringNullOrEmpty(obj[k])) {
                isOK = true;
              }
            }
            if (isOK) {
              result.add(obj);
            }
          }
        } else {
          irowBack += 1;
        }
        if (irowBack == rowBack) {
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result = null;
    } finally {
      if (workbook != null) {
        workbook.close();
      }
      if (fileInput != null) {
        fileInput.close();
      }
    }
    return result;
  }

  public static List getDataFromExcelFileNew(
      File file
      , int iSheet
      , int iBeginRow
      , int iFromCol
      , int iToCol
      , int rowBack
  ) throws IOException {
    List result = new ArrayList();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    FileInputStream fileInput = null;
    Workbook workbook = null;
    try {
      fileInput = new FileInputStream(file);
      workbook = WorkbookFactory.create(fileInput);
      Sheet worksheet = workbook.getSheetAt(iSheet);

      int irowBack = 0;

      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        Row row = worksheet.getRow(i);

        if (row != null) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null) {
              CellType cellType = cell.getCellType();
              switch (cellType) {
                case STRING:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
                case NUMERIC:
                  Double doubleValue = (Double) cell.getNumericCellValue();
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(doubleValue);
                    obj[iCount] = simpleDateFormat.format(date);
                    break;
                  }
                  List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                  String value = lstValue.get(1);
                  if (lstValue.get(1).matches("[0]+")) {
                    obj[iCount] = lstValue.get(0);
                  } else if (value.contains("E")) {
                    List<String> lstValueTemp = DataUtil.splitCharE(value);
                    if (lstValueTemp.get(0).length() > Integer.valueOf(lstValueTemp.get(1))) {
                      value =
                          lstValueTemp.get(0).substring(0, Integer.valueOf(lstValueTemp.get(1)))
                              + "." + lstValueTemp.get(0)
                              .substring(Integer.valueOf(lstValueTemp.get(1)),
                                  lstValueTemp.get(0).length());
                      obj[iCount] = lstValue.get(0) + value;
                    } else if (lstValueTemp.get(0).length() == Integer
                        .valueOf(lstValueTemp.get(1))) {
                      value = lstValueTemp.get(0);
                      obj[iCount] = lstValue.get(0) + value;
                    } else if (lstValueTemp.get(0).length() < Integer
                        .valueOf(lstValueTemp.get(1))) {
                      int temp =
                          Integer.valueOf(lstValueTemp.get(1)) - lstValueTemp.get(0).length();
                      value = lstValueTemp.get(0);
                      for (int t = 0; t < temp; t++) {
                        value = value + "0";
                      }
                      obj[iCount] = lstValue.get(0) + value;
                    }
                  } else {
                    obj[iCount] = lstValue.get(0) + "." + value;
                  }
                  break;
                case BLANK:
                  check++;
                  break;
                case FORMULA:
                  obj[iCount] = cell.getCellFormula().trim();
                  break;
                default:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
              }
            } else {
              obj[iCount] = null;
            }
            iCount += 1;
          }
          if (check != (iToCol - iFromCol + 1)) {
            boolean isOK = false;
            for (int k = 0; k < obj.length; k++) {
              if (!StringUtils.isStringNullOrEmpty(obj[k])) {
                isOK = true;
              }
            }
            if (isOK) {
              result.add(obj);
            }
          }
        } else {
          irowBack += 1;
        }
        if (irowBack == rowBack) {
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result = null;
    } finally {
      if (workbook != null) {
        workbook.close();
      }
      if (fileInput != null) {
        fileInput.close();
      }
    }
    return result;
  }

  public static XSSFSheet createSheetAndTitle(
      XSSFWorkbook workbook,
      String[] arrHeader,
      Map<String, CellStyle> styles,
      Locale locale,
      String titleSheet, String sheetName) {
    XSSFSheet sheet = workbook.createSheet(I18n.getLanguage(sheetName, locale));
    List<String> listObjHeader = Arrays.asList(arrHeader);
    //Tao title
    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, listObjHeader.size() - 1));
    Row titleObjRow = sheet.createRow(1);
    titleObjRow.setHeightInPoints(45);
    Cell titleObjCell = titleObjRow.createCell(0);
    titleObjCell.setCellValue(I18n.getLanguage(titleSheet, locale));
    titleObjCell.setCellStyle(styles.get("title"));

    Row row = sheet.createRow(3);
    row.setHeightInPoints(18);
    for (int i = 0; i < listObjHeader.size(); i++) {
      Cell headerObjCell = row.createCell(i);
      headerObjCell.setCellStyle(styles.get("header"));
      XSSFRichTextString rt = new XSSFRichTextString(listObjHeader.get(i));
      headerObjCell.setCellValue(rt);
      sheet.setColumnWidth(i, 7000);
    }
    return sheet;
  }

  //ham nay chuan voi doc file xls, dang nummeric chuan
  public static List getDataFromExcel(File file, int iSheet, int iBeginRow, int iFromCol,
      int iToCol, int rowBack) throws FileNotFoundException, IOException {
    List lst = new ArrayList();
    FileInputStream flieInput = new FileInputStream(file);
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");

    HSSFWorkbook workbook;
    try {
      workbook = new HSSFWorkbook(flieInput);
      HSSFSheet worksheet = workbook.getSheetAt(iSheet);
      int irowBack = 0;
      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        HSSFRow row = worksheet.getRow(i);

        if (row != null) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null) {
              switch (cell.getCellType()) {
                case STRING:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
                case NUMERIC:
                  Double doubleValue = (Double) cell.getNumericCellValue();
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(doubleValue);
                    obj[iCount] = sp.format(date);
                    break;
                  }

                  List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                  if (lstValue.get(1).matches("[0]+")) {
                    obj[iCount] = lstValue.get(0);
                  } else {
                    DecimalFormat df = new DecimalFormat("######");
                    obj[iCount] = df.format(doubleValue);
                  }
                  break;
                case BLANK:
                  check++;
                  break;
                case ERROR:
                  obj[iCount] = "";
                  break;
                default:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
              }
            } else {
              obj[iCount] = null;
            }
            iCount += 1;
          }
          if (check != (iToCol - iFromCol + 1)) {
            lst.add(obj);
          }

        } else {
          irowBack += 1;
        }
        if (irowBack == rowBack) {
          break;
        }
      }
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
      lst = null;
    } finally {
      if (flieInput != null) {
        flieInput.close();
      }
    }
    return lst;
  }

  public static List readExcel(File file, int iSheet, int iBeginRow, int iFromCol, int iToCol,
      int rowBack)
      throws FileNotFoundException, IOException {
    List lst = new ArrayList();
    FileInputStream flieInput = new FileInputStream(file);
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
    try {
      HSSFWorkbook workbook = new HSSFWorkbook(flieInput);
      HSSFSheet worksheet = workbook.getSheetAt(iSheet);
      int irowBack = 0;
      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        HSSFRow row = worksheet.getRow(i);
        if (row != null) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null) {
              switch (cell.getCellType()) {
                case STRING:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
                case NUMERIC:
                  Double doubleValue = Double.valueOf(cell.getNumericCellValue());
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(doubleValue.doubleValue());
                    String dateFmt = cell.getCellStyle().getDataFormatString();
                    obj[iCount] = sp.format(date);
                  } else {
                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                    if (((String) lstValue.get(1)).matches("[0]+")) {
                      obj[iCount] = lstValue.get(0);
                    } else {
                      obj[iCount] = String.format("%.2f", new Object[]{doubleValue}).trim();
                    }
                  }
                  break;
                case BLANK:
                  check++;
                  break;
                case FORMULA:
                default:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
              }
            } else {
              obj[iCount] = null;
            }
            iCount++;
          }
          if (check != iToCol - iFromCol + 1) {
            lst.add(obj);
          }
        } else {
          irowBack++;
        }
        if (irowBack == rowBack) {
          break;
        }
      }
    } catch (IOException ex) {
      lst = null;
    }
    return lst;
  }
}
