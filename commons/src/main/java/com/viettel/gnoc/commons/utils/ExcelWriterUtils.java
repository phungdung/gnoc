package com.viettel.gnoc.commons.utils;

import com.google.common.base.Splitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by VTN-PTPM-NV23 on 12/20/2017.
 */
@Slf4j
public class ExcelWriterUtils {

  private Workbook workbook;
  private FileOutputStream fileOut;

  /**
   * Method to create a workbook to work with excel
   *
   * @param filePathName ThuanNHT
   */
  public void createWorkBook(String filePathName) {
    if (filePathName.endsWith(".xls") || filePathName.endsWith(".XLS")) {
      workbook = new HSSFWorkbook();
    } else if (filePathName.endsWith(".xlsx") || filePathName.endsWith(".XLSX")) {
      workbook = new XSSFWorkbook();
    }
  }

  /**
   * Method to create a new excel(xls,xlsx) file with file Name
   *
   * @param fileName ThuanNHT
   */
  public void saveToFileExcel(String pathName, String fileName) {
    try {
      File f = new File(pathName);
      if (!f.exists()) {
        boolean b = f.mkdirs();
      }
      // R3292_EDIT_DUNGNV50_13122012_START
      fileOut = new FileOutputStream(pathName + fileName);
      // R3292_EDIT_DUNGNV50_13122012_END
      workbook.write(fileOut);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        fileOut.close();
        workbook = null;
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
  }

  public void saveToFileExcel(String filePathName) {
    try {
      fileOut = new FileOutputStream(filePathName);
      workbook.write(fileOut);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        fileOut.close();
        workbook = null;
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
  }

  /**
   * Method to create a new excel(xls,xlsx) file with file Name
   *
   * @param fileName ThuanNHT
   */
  public void saveToFileExcel(Workbook workBook, String pathName, String fileName) {
    try {
      File f = new File(pathName);
      if (!f.exists()) {
        boolean b = f.mkdirs();
      }
      // R3292_EDIT_DUNGNV50_13122012_START
      fileOut = new FileOutputStream(pathName + fileName);
      // R3292_EDIT_DUNGNV50_13122012_END
      workBook.write(fileOut);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        fileOut.close();
        workbook = null;
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
  }

  /**
   * method t create a row
   *
   * @return ThuanNHT
   */
  public Row createRow(Sheet sheet, int r) {
    Row row = sheet.createRow(r);
    return row;
  }

  /**
   * method to create a cell with value
   *
   * @param cellValue ThuanNHT
   */
  public Cell createCell(Row row, int column, String cellValue) {
    // Create a cell and put a value in it.
    Cell cell = row.createCell(column);
    cell.setCellValue(cellValue);
    return cell;
  }

  /**
   * method to create a cell with value
   *
   * @param cellValue ThuanNHT
   */
  public Cell createCell(Sheet sheet, int c, int r, String cellValue) {
    Row row = sheet.getRow(r);
    if (row == null) {
      row = sheet.createRow(r);
    }
    // Create a cell and put a value in it.
    Cell cell = row.createCell(c);
    cell.setCellValue(cellValue);
    return cell;
  }

  /**
   * method to create a cell with value with style
   *
   * @param cellValue ThuanNHT
   */
  public Cell createCell(Sheet sheet, int c, int r, String cellValue, CellStyle style) {
    Row row = sheet.getRow(r);
    if (row == null) {
      row = sheet.createRow(r);
    }
    // Create a cell and put a value in it.
    Cell cell = row.createCell(c);
    cell.setCellValue(cellValue);
    cell.setCellStyle(style);
    return cell;
  }

  /**
   * Method get primitive content Of cell
   */

  public static Object getCellContent(Sheet sheet, int c, int r) {
    Cell cell = getCellOfSheet(r, c, sheet);
    Object obj = null;
    if (cell == null) {
      return "";
    }
    switch (cell.getCellType()) {
      case STRING:
        obj = cell.getRichStringCellValue().getString();
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          obj = cell.getDateCellValue();
        } else {
          obj = cell.getNumericCellValue();
        }
        break;
      case BOOLEAN:
        obj = cell.getBooleanCellValue();
        break;
      case FORMULA:
        obj = cell.getCellFormula();
        break;
      default:
        obj = "";
        break;
    }
    return obj;
  }

  /**
   * Method set sheet is selected when is opened
   */
  public void setSheetSelected(int posSheet) {
    try {
      workbook.setActiveSheet(posSheet);
    } catch (IllegalArgumentException ex) {
      log.error(ex.getMessage(), ex);
      workbook.setActiveSheet(0);
    }
  }

  /**
   * method to merge cell
   *
   * @param firstRow based 0
   * @param lastRow based 0
   * @param firstCol based 0
   * @param lastCol based 0
   */
  public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
    sheet.addMergedRegion(new CellRangeAddress(
        firstRow, //first row (0-based)
        lastRow, //last row  (0-based)
        firstCol, //first column (0-based)
        lastCol //last column  (0-based)
    ));
  }

  /**
   * method to fill color background for cell
   *
   * @param colors:BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK, TURQUOISE, DARK_RED, GREEN,
   * DARK_BLUE, DARK_YELLOW, VIOLET, TEAL, GREY_25_PERCENT, GREY_50_PERCENT, CORNFLOWER_BLUE,
   * MAROON, LEMON_CHIFFON, ORCHID, CORAL, ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE,
   * LIGHT_TURQUOISE, LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER, TAN, LIGHT_BLUE, AQUA,
   * LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY, GREY_40_PERCENT, DARK_TEAL, SEA_GREEN, DARK_GREEN,
   * OLIVE_GREEN, BROWN, PLUM, INDIGO, GREY_80_PERCENT, AUTOMATIC;
   */
  public void fillAndColorCell(Cell cell, IndexedColors colors) {
    CellStyle style = workbook.createCellStyle();
    style.setFillBackgroundColor(colors.getIndex());
    cell.setCellStyle(style);
  }
  // datpk  lay object tu Row

  public static Object getCellContentRow(int c, Row row) {
    Cell cell = getCellOfSheetRow(c, row);
    if (cell == null) {
      return "";
    }
       /* switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";

        }*/
    return "";
  }

  /**
   * Method get text content Of cell
   */
  public static String getCellStrContent(Sheet sheet, int c, int r) {
    Cell cell = getCellOfSheet(r, c, sheet);
    if (cell == null) {
      return "";
    }
    String temp = getCellContent(sheet, c, r).toString().trim();
    if (temp.endsWith(".0")) {
      return temp.substring(0, temp.length() - 2);
    }
    return temp;
  }
  // datpk getStringconten tu Row

  public static String getCellStrContentRow(int c, Row row) {
    Cell cell = getCellOfSheetRow(c, row);
    if (cell == null) {
      return "";
    }
    String temp = getCellContentRow(c, row).toString().trim();
    if (temp.endsWith(".0")) {
      return temp.substring(0, temp.length() - 2);
    }
    return temp;
  }

  /**
   * method to create validation from array String.But String do not exceed 255 characters
   *
   * @param arrValidate * ThuanNHT
   */
  public void createDropDownlistValidateFromArr(Sheet sheet, String[] arrValidate, int firstRow,
      int lastRow, int firstCol, int lastCol) {
    CellRangeAddressList addressList = new CellRangeAddressList(
        firstRow, lastRow, firstCol, lastCol);
    DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(arrValidate);
    HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
    dataValidation.setSuppressDropDownArrow(false);
    HSSFSheet sh = (HSSFSheet) sheet;
    sh.addValidationData(dataValidation);
  }

  /**
   * Method to create validation from spread sheet via range
   *
   * @param lastCol * ThuanNHT
   */
  public void createDropDownListValidateFromSpreadSheet(String range, int firstRow, int lastRow,
      int firstCol, int lastCol, Sheet shet) {
    Name namedRange = workbook.createName();
    SecureRandom rd = new SecureRandom();
    String refName = ("List" + rd.nextInt()).toString().replace("-", "");
    namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
    namedRange.setRefersToFormula(range);
    DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
    CellRangeAddressList addressList = new CellRangeAddressList(
        firstRow, lastRow, firstCol, lastCol);
    HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
    dataValidation.setSuppressDropDownArrow(false);
    HSSFSheet sh = (HSSFSheet) shet;
    sh.addValidationData(dataValidation);
  }

  public void createDropDownListValidateFromSpreadSheet(String sheetName, String columnRangeName,
      int rowRangeStart, int rowRangeEnd, int firstRow, int lastRow, int firstCol, int lastCol,
      Sheet shet) {
    String range = "'" + sheetName + "'!$" + columnRangeName + "$" + rowRangeStart + ":" + "$"
        + columnRangeName + "$" + rowRangeEnd;
    Name namedRange = workbook.createName();
    SecureRandom rd = new SecureRandom();
    String refName = ("List" + rd.nextInt()).toString().replace("-", "");
    namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
    namedRange.setRefersToFormula(range);
    DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
    CellRangeAddressList addressList = new CellRangeAddressList(
        firstRow, lastRow, firstCol, lastCol);
    HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
    dataValidation.setSuppressDropDownArrow(false);
    HSSFSheet sh = (HSSFSheet) shet;
    sh.addValidationData(dataValidation);
  }

  public Sheet getSheetAt(int pos) {
    return workbook.getSheetAt(pos);
  }

  public Sheet getSheet(String name) {
    return workbook.getSheet(name);
  }

  /**
   * Method to read an excel file
   *
   * @return * ThuanNHT
   */
  public Workbook readFileExcel(String filePathName) {
    InputStream inp = null;
    try {
      // R3292_EDIT_DUNGNV50_13122012_START
      inp = new FileInputStream(filePathName);
      // R3292_EDIT_DUNGNV50_13122012_END
      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        if (inp != null) {
          inp.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return workbook;
  }

  /**
   * * ThuanNHT
   */
  public static Cell getCellOfSheet(int r, int c, Sheet sheet) {
    try {
      Row row = sheet.getRow(r);
      if (row == null) {
        return null;
      }
      return row.getCell(c);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * set style for cell
   */
  public void setCellStyle(Cell cell, short halign, short valign, short border, short borderColor,
      int fontHeight) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontHeightInPoints((short) fontHeight);
//        style.setAlignment(halign);
//        style.setVerticalAlignment(valign);
//        style.setBorderBottom(border);
    style.setBottomBorderColor(borderColor);
//        style.setBorderLeft(border);
    style.setLeftBorderColor(borderColor);
//        style.setBorderRight(border);
    style.setRightBorderColor(borderColor);
//        style.setBorderTop(border);
    style.setTopBorderColor(borderColor);
    cell.setCellStyle(style);
  }

  /**
   * Method to draw an image on excel file
   */
  public void drawImageOnSheet(String imgSrc, Sheet sheet, int colCorner, int rowCorner)
      throws IOException {
    InputStream is = null;
    try {
      is = new FileInputStream(imgSrc);
      byte[] bytes = IOUtils.toByteArray(is);
      int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
      if (imgSrc.endsWith(".jpg") || imgSrc.endsWith(".JPG") || imgSrc.endsWith(".jpeg") || imgSrc
          .endsWith(".JPEG")) {
        pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
      } else if (imgSrc.endsWith(".png") || imgSrc.endsWith(".PNG")) {
        pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
      }

      CreationHelper helper = workbook.getCreationHelper();
      // Create the drawing patriarch.  This is the top level container for all shapes.
      Drawing drawing = sheet.createDrawingPatriarch();
      //add a picture shape
      ClientAnchor anchor = helper.createClientAnchor();
      //set top-left corner of the picture,
      //subsequent call of Picture#resize() will operate relative to it
      anchor.setCol1(colCorner);
      anchor.setRow1(rowCorner);
      Picture pict = drawing.createPicture(anchor, pictureIdx);

      //auto-size picture relative to its top-left corner
      pict.resize();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      if (is != null) {
        is.close();
      }
    }

  }

  // datpk: lay cell tu Row
  public static Cell getCellOfSheetRow(int c, Row row) {
    try {
      if (row == null) {
        return null;
      }
      return row.getCell(c);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

  public static Boolean compareToLong(String str, Long t) {
    Boolean check = false;
    try {
      Double d = Double.valueOf(str);
      Long l = d.longValue();
      if (l.equals(t)) {
        check = true;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      check = false;
    }
    return check;
  }

  public static Boolean doubleIsLong(String str) {
    Boolean check = false;
    try {
      Double d = Double.valueOf(str);
      Long l = d.longValue();
      if (d.equals(Double.valueOf(l))) {
        check = true;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      check = false;
    }
    return check;
  }

  public static void main(String[] arg) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      ewu.readFileExcel("C:\\a.xlsx");
      ewu.readFileExcel("C:\\b.xlsx");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public Workbook getWorkbook() {
    return workbook;
  }

  public void setWorkbook(Workbook workbook) {
    this.workbook = workbook;
  }

  //R585445 sonvt19 Cau hinh file dinh kem khi tao Wo (thêm validate file excel trống) - start
  public static Boolean isEmptyExcelFile(String pathFileImport, int iSheet, String fileUpload) {
    FileInputStream fileInput = null;
    Workbook workbook = null;
    try {
      File file = new File(pathFileImport);
      fileInput = new FileInputStream(file);
      workbook = WorkbookFactory.create(fileInput);
      Sheet worksheet = workbook.getSheetAt(iSheet);
      if (worksheet != null) {
        if (worksheet.getPhysicalNumberOfRows() == 0) {
          return true;
        } else {
          return false;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
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
    }
    return false;
  }

  //R585445 sonvt19 Cau hinh file dinh kem khi tao Wo (thêm validate file excel trống) - end

  //sonvt19 Giao tiếp với NIMS - cấp phát IP Port - start
  public static int findHeadeRow(String pathFileImport, int iSheet, String fileUpload, int iRowBack)
      throws IOException {
    FileInputStream fileInput = null;
    int rowHeader = -1;
    Boolean headerFound = false;
    Workbook workbook = null;
    try {
      File file = new File(pathFileImport);
      fileInput = new FileInputStream(file);
      workbook = WorkbookFactory.create(fileInput);
      Sheet worksheet = workbook.getSheetAt(iSheet);
      Iterator<Row> rows = worksheet.iterator();
      if (fileUpload.endsWith(".xls") || fileUpload.endsWith(".XLS")) {
        for (int i = 0; i < iRowBack; i++) {
          rowHeader = i;
          if (!isCellEmpty(getCellOfSheet(i, 0, worksheet)) && !isCellEmpty(
              getCellOfSheet(i, 1, worksheet))) {
            headerFound = true;
            break;
          }

        }
      } else if (fileUpload.endsWith(".xlsx") || fileUpload.endsWith(".XLSX")) {
        while (rows.hasNext()) {
          Row row = rows.next();
          if (!isCellEmpty(row.getCell(0)) && !isCellEmpty(row.getCell(1))) {
            headerFound = true;
            break;
          }
          rowHeader++;
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
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
    }
    if (headerFound) {
      return rowHeader;
    }
    return -1;
  }

  public static int findLastColumn(String pathFileImport, int iSheet, String fileUpload,
      int rowHeader) throws IOException {
    FileInputStream fileInput = null;
    Workbook workbook = null;
    try {
      File file = new File(pathFileImport);
      fileInput = new FileInputStream(file);
      workbook = WorkbookFactory.create(fileInput);
      Sheet worksheet = workbook.getSheetAt(iSheet);
      if (worksheet != null) {
        return worksheet.getRow(rowHeader).getLastCellNum() - 1;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
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
    }
    return -1;
  }

  public static Boolean isCellEmpty(Cell cell) {
    if (cell == null) {
      return true;
    }
    if ("".equals(cell.getStringCellValue())) {
      return true;
    }
    return false;
  }

  public static boolean compareHeader(String source, String dest) {
    List<CellObject> titleSource = new ArrayList<>();
    InputStream inputStreamsSource = null;
    InputStream inputStreamsDest = null;
    Workbook hssfWorkbook = null;
    Workbook xssfWorkbook = null;
    try {
      inputStreamsSource = new FileInputStream(source);
      inputStreamsDest = new FileInputStream(dest);
      if (source.endsWith(".xls") || source.endsWith(".XLS")) {
        hssfWorkbook = WorkbookFactory.create(inputStreamsSource);
        Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
        int r = 0;
        while (hssfSheet != null && r <= hssfSheet.getLastRowNum()) {
          Row row = hssfSheet.getRow(r);
          int indexHeader = 0;
          while (row != null && indexHeader <= row.getLastCellNum()) {
            if (row.getCell(indexHeader) == null) {
              indexHeader++;
              continue;
            }
            CellObject obj = new CellObject();
            if (row.getCell(indexHeader).getCellType() == CellType.NUMERIC) {
              obj.setRow(r);
              obj.setCell(indexHeader);
              obj.setValue(row.getCell(indexHeader).getNumericCellValue() + "");
              titleSource.add(obj);
            }
            if (row.getCell(indexHeader).getCellType() == CellType.STRING) {
              obj.setRow(r);
              obj.setCell(indexHeader);
              obj.setValue(row.getCell(indexHeader).getStringCellValue());
              titleSource.add(obj);
            }
            indexHeader++;
          }
          r++;
        }
      } else if (source.endsWith(".xlsx") || source.endsWith(".XLSX")) {
        xssfWorkbook = WorkbookFactory.create(inputStreamsSource);
        Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
        int r = 0;
        while (xssfSheet != null && r <= xssfSheet.getLastRowNum()) {
          Row row = xssfSheet.getRow(r);
          int indexHeader = 0;
          while (row != null && indexHeader <= row.getLastCellNum()) {
            if (row.getCell(indexHeader) == null) {
              indexHeader++;
              continue;
            }
            CellObject obj = new CellObject();
            if (row.getCell(indexHeader).getCellType() == CellType.NUMERIC) {
              obj.setRow(r);
              obj.setCell(indexHeader);
              obj.setValue(row.getCell(indexHeader).getNumericCellValue() + "");
              titleSource.add(obj);
            }
            if (row.getCell(indexHeader).getCellType() == CellType.STRING) {
              obj.setRow(r);
              obj.setCell(indexHeader);
              obj.setValue(row.getCell(indexHeader).getStringCellValue());
              titleSource.add(obj);
            }
            indexHeader++;
          }
          r++;
        }
      }
      if (dest.endsWith(".xls") || dest.endsWith(".XLS")) {
        hssfWorkbook = WorkbookFactory.create(inputStreamsDest);
        Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
        for (int i = 0; i < titleSource.size(); i++) {
          Row row = hssfSheet.getRow(titleSource.get(i).getRow());
          if (row == null) {
            return false;
          } else {
            String sourceValue = titleSource.get(i).getValue();
            if (row.getCell(titleSource.get(i).getCell()) == null) {
              return false;
            }
            if (row.getCell(titleSource.get(i).getCell()).getCellType() == CellType.NUMERIC) {
              if (!sourceValue.equalsIgnoreCase(
                  row.getCell(titleSource.get(i).getCell()).getNumericCellValue() + "")) {
                return false;
              }
            }
            if (row.getCell(titleSource.get(i).getCell()).getCellType() == CellType.STRING) {
              if (!sourceValue.equalsIgnoreCase(
                  row.getCell(titleSource.get(i).getCell()).getStringCellValue())) {
                return false;
              }
            }
          }
        }
        return true;
      } else if (dest.endsWith(".xlsx") || dest.endsWith(".XLSX")) {
        ZipSecureFile.setMinInflateRatio(0);
        xssfWorkbook = WorkbookFactory.create(inputStreamsDest);
        Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
        for (int i = 0; i < titleSource.size(); i++) {
          Row row = xssfSheet.getRow(titleSource.get(i).getRow());
          if (row == null) {
            return false;
          } else {
            String sourceValue = titleSource.get(i).getValue();
            if (row.getCell(titleSource.get(i).getCell()) == null) {
              return false;
            }
            if (row.getCell(titleSource.get(i).getCell()).getCellType() == CellType.NUMERIC) {
              if (!sourceValue.equalsIgnoreCase(
                  row.getCell(titleSource.get(i).getCell()).getNumericCellValue() + "")) {
                return false;
              }
            }
            if (row.getCell(titleSource.get(i).getCell()).getCellType() == CellType.STRING) {
              if (!sourceValue.equalsIgnoreCase(
                  row.getCell(titleSource.get(i).getCell()).getStringCellValue())) {
                return false;
              }
            }
          }
        }
        return true;
      }
      return false;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    } finally {
      try {
        if (inputStreamsSource != null) {
          inputStreamsSource.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (inputStreamsDest != null) {
          inputStreamsDest.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (hssfWorkbook != null) {
          hssfWorkbook.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (xssfWorkbook != null) {
          xssfWorkbook.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public static boolean isRowEmpty(Row row) {
    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
      Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
      if (cell != null) {
        return false;
      }
    }
    return true;
  }

  //fix bug shift a row losing data excel
  public void shiftRow(Sheet sheet, int r, int numberOfLine) {
    try {
      sheet.shiftRows(r, sheet.getLastRowNum(), numberOfLine);
      for (int nRow = r; nRow <= sheet.getLastRowNum(); ++nRow) {
        final Row row = sheet.getRow(nRow);
        if (row != null) {
          String msg = "Row[rownum=" + row.getRowNum()
              + "] contains cell(s) included in a multi-cell array formula. "
              + "You cannot change part of an array.";
          for (Cell c : row) {
            ((XSSFCell) c).updateCellReferencesForShifting(msg);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static List readExcelAddBlankRowXSSF(File file, int iSheet, int iBeginRow, int iFromCol,
      int iToCol, int rowBack) throws FileNotFoundException, IOException {
    List lst = new ArrayList();
    FileInputStream fileInput = null;
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");

    XSSFWorkbook workbook = null;
    try {
      fileInput = new FileInputStream(file);
      workbook = new XSSFWorkbook(fileInput);
      XSSFSheet worksheet = workbook.getSheetAt(iSheet);
      int irowBack = 0;
      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        Row row = worksheet.getRow(i);

        if (row != null && true) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null && true) {
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
                  List<String> lstValue = Splitter.on(".").trimResults().omitEmptyStrings()
                      .splitToList(String.valueOf(doubleValue));
                  if (lstValue.get(1).matches("[0]+")) {
                    obj[iCount] = lstValue.get(0);
                  } else {
                    obj[iCount] = String.format("%.2f", doubleValue).trim();
                  }

                  break;
                case BLANK:
                  check++;
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
      try {
        if (fileInput != null) {
          fileInput.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (workbook != null) {
          workbook.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lst;
  }

  /**
   * Method to read an excel file
   *
   * @return * TienNV
   */
  public Workbook readFileExcelFromTemplate(String filePathName) {
    InputStream inp = null;
    try {
      String pathTemplate = StringUtils.removeSeparator(filePathName);
      Resource resource = new ClassPathResource(pathTemplate);
      inp = resource.getInputStream();
      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        if (inp != null) {
          inp.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return workbook;
  }

  /**
   * Method to read an excel file, with file big data, indexRow la vi tri bat dau chen du lieu, do
   * thang SXSSFWorkbook no luu du lieu vao temp, nen phai xoa truoc khi chuyen thanh sxssWB
   *
   * @author TienNV
   */
  public Workbook readFileExcelFromTemplate(String filePathName, int indexRowData) {
    InputStream inp = null;
    try {
      String pathTemplate = StringUtils.removeSeparator(filePathName);
      Resource resource = new ClassPathResource(pathTemplate);
      inp = resource.getInputStream();
      OPCPackage pkg = OPCPackage.open(inp);
      XSSFWorkbook xssfwb = new XSSFWorkbook(pkg);
      Sheet sheetOne = xssfwb.getSheetAt(0);
      log.info(" --- getLastRowNum : " + sheetOne.getLastRowNum());
//      log.info( " --- physicalNumber: " + sheetOne.getPhysicalNumberOfRows());
      for (int i = indexRowData; i <= sheetOne.getLastRowNum(); i++) {
        Row row = sheetOne.getRow(i);
        if (row != null) {
          log.info(" --- remove row empty --- " + i);
          sheetOne.removeRow(row);
        }
      }
      workbook = new SXSSFWorkbook(xssfwb, 100);//cu 100 ban ghi flush vao excel 1 lan
//      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        if (inp != null) {
          inp.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return workbook;
  }

  //add method readFile from Path
  public Workbook readFileExcelFromFile(File file, int indexRowData, int sheet) {
    InputStream inp = null;
    try {
      log.info("---start read file with flush---");
      OPCPackage pkg = OPCPackage.open(file);
      XSSFWorkbook xssfwb = new XSSFWorkbook(pkg);
      if (indexRowData > 0) {
        Sheet sheetOne = xssfwb.getSheetAt(sheet);
        log.info(" --- getLastRowNum : " + sheetOne.getLastRowNum());
//      log.info( " --- physicalNumber: " + sheetOne.getPhysicalNumberOfRows());
        for (int i = indexRowData; i <= sheetOne.getLastRowNum(); i++) {
          Row row = sheetOne.getRow(i);
          if (row != null) {
            log.info(" --- remove row empty --- " + i);
            sheetOne.removeRow(row);
          }
        }
      }
      workbook = new SXSSFWorkbook(xssfwb, 100);//cu 100 ban ghi flush vao excel 1 lan
//      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
//      if (inp != null) {
//        try {
//          inp.close();
//        } catch (IOException ex) {
//          log.error(ex.getMessage(), ex);
//        }
//      }
    }
    return workbook;
  }

}
