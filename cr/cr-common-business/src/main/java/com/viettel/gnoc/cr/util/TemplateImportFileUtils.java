package com.viettel.gnoc.cr.util;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.dto.V_TempImportColDTO;
import com.viettel.gnoc.cr.dto.V_WebserviceMethodDetailDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class TemplateImportFileUtils {

  public Logger log = Logger.getLogger(TemplateImportFileUtils.class);

  /**
   *
   */
  public List<TempImportColDTO> getMergColInTemplateFile(List<TempImportColDTO> lsAll) {
    List<TempImportColDTO> ls = new ArrayList<TempImportColDTO>();
    try {
      for (TempImportColDTO t : lsAll) {
        if (t.getIsMerge() != null && t.getIsMerge().equals(1l)) {
          ls.add(t);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<CellRangeAddress> getMerRegListOfCol(List<CellRangeAddress> lsMerReg, Long position) {
    List<CellRangeAddress> ls = new ArrayList<CellRangeAddress>();
    try {
      for (int i = 0; i < lsMerReg.size(); i++) {
        if (lsMerReg.get(i).getFirstColumn() <= (position.intValue() - 1)
            && lsMerReg.get(i).getLastColumn() >= (position.intValue() - 1)
            && lsMerReg.get(i).getFirstRow() == Constants.EXCEL_PARAM.ROW_DATA) {
          ls.add(lsMerReg.get(i));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<TempImportColDTO> getUnMergColInTemplateFile(List<TempImportColDTO> lsAll) {
    List<TempImportColDTO> ls = new ArrayList<TempImportColDTO>();
    try {
      for (TempImportColDTO t : lsAll) {
        if (t.getIsMerge() != null && t.getIsMerge().equals(0l)) {
          ls.add(t);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<CellRangeAddress> getListOfMegeRegion(Sheet sheet, int lastCol) {
    List<CellRangeAddress> ls = new ArrayList<CellRangeAddress>();
    try {

      for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
        if (sheet.getMergedRegion(i).getFirstColumn() <= lastCol) {
          ls.add(sheet.getMergedRegion(i));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<Cell> getUnMergeCellList(Sheet sheet, int lastRow,
      List<TempImportColDTO> lsUnMergeCol) {
    List<Cell> ls = new ArrayList<Cell>();
    try {
      for (TempImportColDTO t : lsUnMergeCol) {
        for (int j = Constants.EXCEL_PARAM.ROW_DATA; j <= lastRow; j++) {
          if (sheet.getRow(j) != null) {
            if (sheet.getRow(j).getCell(t.getColPosition().intValue() - 1) == null) {
              ls.add(sheet.getRow(j).createCell(t.getColPosition().intValue() - 1));
            } else {
              ls.add(sheet.getRow(j).getCell(t.getColPosition().intValue() - 1));
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<Cell> getMergeCellList(Sheet sheet, List<TempImportColDTO> lsMergeCol) {
    List<Cell> ls = new ArrayList<Cell>();
    try {
      for (TempImportColDTO t : lsMergeCol) {
        int col = t.getColPosition().intValue() - 1;
        if (sheet.getRow(Constants.EXCEL_PARAM.ROW_DATA).getCell(col) == null) {
          ls.add(sheet.getRow(Constants.EXCEL_PARAM.ROW_DATA).createCell(col));
        } else {
          ls.add(sheet.getRow(Constants.EXCEL_PARAM.ROW_DATA).getCell(col));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  /**
   *
   */
  public List<Map<String, String>> getRowWithValueFromSheet(ExcelWriterUtils xls, Sheet sheet,
      List<TempImportColDTO> lsTemplateImportColOfFile) {
    List<Map<String, String>> ls1 = new ArrayList<Map<String, String>>();
    List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
    try {
      int lastCol = lsTemplateImportColOfFile.size() - 1;
      int lastRow = Constants.EXCEL_PARAM.ROW_DATA;

      List<TempImportColDTO> lsMergeCol = getMergColInTemplateFile(lsTemplateImportColOfFile);
      List<TempImportColDTO> lsUnMergeCol = getUnMergColInTemplateFile(lsTemplateImportColOfFile);
      // validate cau truc cell merge
      // -- kiem tra sheet co mergeRegion
      // ---- co mergeRegion
      List<CellRangeAddress> lsMergeRegion = getListOfMegeRegion(sheet, lastCol);
      if (!lsMergeRegion.isEmpty()) {
        //-- kiem tra file mau co merge col ko
        // -- co merge col trong file mau
        if (lsMergeCol.isEmpty()) {
          lastRow = sheet.getLastRowNum();
        } else {
          // kiem tra vung merge tren tung cot merger
          for (int i = 0; i < lsMergeCol.size(); i++) {
            List<CellRangeAddress> lsFirtRowMerReg = getMerRegListOfCol(lsMergeRegion,
                lsMergeCol.get(i).getColPosition());
            if (lsFirtRowMerReg.isEmpty()) {
              lsMergeCol.get(i).setLastRow(Constants.EXCEL_PARAM.ROW_DATA);
            } else {
              if (lastRow < lsFirtRowMerReg.get(0).getLastRow()) {
                lastRow = lsFirtRowMerReg.get(0).getLastRow();
              }
              lsMergeCol.get(i).setLastRow(lsFirtRowMerReg.get(0).getLastRow());
            }
          }
        }
      }

      // validate maxlength cua cell
      List<Cell> lsUnMergerCell = getUnMergeCellList(sheet, lastRow, lsUnMergeCol);
      List<Cell> lsMergerCell = getMergeCellList(sheet, lsMergeCol);
      if (!lsUnMergeCol.isEmpty()) {
        for (int i = Constants.EXCEL_PARAM.ROW_DATA; i <= lastRow; i++) {
          Map<String, String> map = new HashMap<String, String>();
          int col = 0;
          for (TempImportColDTO t : lsUnMergeCol) {
            col = t.getColPosition().intValue() - 1;
            String cellValue = getCellValue(sheet, lsUnMergerCell, i, col);
            map.put(col + "", cellValue);

          }
          ls.add(map);

        }
      }
      if (!lsMergerCell.isEmpty()) {
        for (Cell cell : lsMergerCell) {
          List<Map<String, String>> lsTemp = new ArrayList<Map<String, String>>();
          lsTemp.addAll(ls);
          ls.clear();
          String cellValue = ExcelWriterUtils
              .getCellStrContent(sheet, cell.getColumnIndex(), cell.getRowIndex());
          if ("".equals(cellValue)) {
            for (Map<String, String> mp : lsTemp) {
              Map<String, String> mpN;
              mpN = mp;
              mpN.put(cell.getColumnIndex() + "", "");
              ls.add(mpN);
            }
          } else {
            String[] cellValueList = cellValue.split(",");
            if (cellValueList.length > 0) {
              for (int i = 0; i < cellValueList.length; i++) {
                if (lsTemp.isEmpty()) {
                  Map<String, String> mpN = new HashMap<String, String>();
                  mpN.put(cell.getColumnIndex() + "", cellValueList[i]);
                  ls.add(mpN);
                } else {
                  for (Map<String, String> mp : lsTemp) {
                    Map<String, String> mpN = new HashMap<String, String>();
                    mpN.putAll(mp);
                    mpN.put(cell.getColumnIndex() + "", cellValueList[i]);
                    ls.add(mpN);
                  }
                }
              }
            }
          }
        }
      }
      for (Map<String, String> map : ls) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
          String key = iterator.next();
          if (!"".equals(map.get(key))) {
            ls1.add(map);
            break;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls1;
  }

  public String getCellValue(Sheet sheet, List<Cell> lsUnMergerCell, int row, int col) {
    String value = "";
    try {
      if (!lsUnMergerCell.isEmpty()) {
        for (Cell cell : lsUnMergerCell) {
          if (cell.getRowIndex() == row && cell.getColumnIndex() == col) {
            return ExcelWriterUtils
                .getCellStrContent(sheet, cell.getColumnIndex(), cell.getRowIndex());
          }

        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return value;
  }

  /**
   *
   */
  public Object parseDataToWebserviceBO(Map<String, String> row,
      List<V_TempImportColDTO> listCol,
      V_WebserviceMethodDetailDTO wsMethod) {
    Object objInput = null;
    try {
      // khai bao + khoi tao object
      Class objInputClass = Class.forName(wsMethod.getClassPath());
      objInput = objInputClass.newInstance();
      // lay cac truong (fields) thuoc doi tuong
      Field fields[] = objInputClass.getDeclaredFields();
      Field superFields[] = objInputClass.getSuperclass().getDeclaredFields();
      Field allField[] = new Field[fields.length + superFields.length];
      System.arraycopy(fields, 0, allField, 0, fields.length);
      System.arraycopy(superFields, 0, allField, fields.length, superFields.length);
      // set gia tri vao cac truong thuoc doi tuong
      for (Field f : allField) {
        f.setAccessible(true);
        V_TempImportColDTO col = getColPosByName(listCol, f.getName());
        if (col != null) {
          Object value = row.get((col.getColPosition() - 1) + "");

          if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.STRING)) {
            log.info("TEST CHANGE VALUE: " + row.get((col.getColPosition() - 1) + "").toString());
            f.set(objInput, row.get((col.getColPosition() - 1) + "").toString());


          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.INTEGER)) {
            f.set(objInput, Integer.parseInt(row.get((col.getColPosition() - 1) + "").toString()));
            log.info(row.get((col.getColPosition() - 1) + "").toString());
          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.LONG)) {
            try {
              f.set(objInput, Long.parseLong(row.get((col.getColPosition() - 1) + "").toString()));
              log.info(row.get((col.getColPosition() - 1) + "").toString());
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
            }
          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.DOUBLE)) {
            try {
              f.set(objInput,
                  Double.parseDouble(row.get((col.getColPosition() - 1) + "").toString()));
              log.info(row.get((col.getColPosition() - 1) + "").toString());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }

          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.FLOAT)) {
            try {
              f.set(objInput,
                  Float.parseFloat(row.get((col.getColPosition() - 1) + "").toString()));
              log.info(row.get((col.getColPosition() - 1) + "").toString());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }

          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.BOOLEAN)) {
            try {
              f.set(objInput,
                  Boolean.parseBoolean(row.get((col.getColPosition() - 1) + "").toString()));
              log.info(row.get((col.getColPosition() - 1) + "").toString());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          } else if (col.getDataType().equals(Constants.TEMP_IMPORT_UTILS.CHAR)) {
            f.set(objInput, row.get((col.getColPosition() - 1) + "").toString());
          }
          f.set(objInput, value);
        }
        // set gia tri cho truong id
        if (f.getName().equalsIgnoreCase(wsMethod.getIdField())) {
          f.set(objInput, row.get(Constants.TEMP_IMPORT_UTILS.ID_KEY));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return objInput;
  }

  /**
   *
   */
  public Boolean validateDataCellInMergeRegion(String locale,
      String filePath,
      ExcelWriterUtils xls,
      Sheet sheet, int lastCol) {
    Boolean check = false;
    try {
      int lastDataRow = sheet.getLastRowNum();
      CellRangeAddress mRegion = null;
      for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
        mRegion = sheet.getMergedRegion(i);
        int fromRow = mRegion.getFirstRow();
        int toRow = mRegion.getLastRow();
        int fromCol = mRegion.getFirstColumn();
        int toCol = mRegion.getLastColumn();
        //Do something here with the infos
        if (toCol <= lastCol
            && ((fromRow <= lastDataRow && fromRow >= Constants.EXCEL_PARAM.ROW_DATA)
            || (toRow <= lastDataRow && toRow >= Constants.EXCEL_PARAM.ROW_DATA))) {
          String comment = I18n.getChangeManagement("not.allow.merge.in.area", locale);
          xls.createCell(sheet, 0, sheet.getLastRowNum() + 2,
              I18n.getChangeManagement("label.import.error.column", locale) + " " +
                  fromCol + ": " + comment);
//                    xls.saveToFileExcel(req.getRealPath(filePath));
//                    xls.saveToFileExcel(filePath);
          check = true;
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return check;
  }

  /**
   *
   */
  public V_TempImportColDTO getColPosByName(List<V_TempImportColDTO> listCol, String name) {
    V_TempImportColDTO colPos = null;
    try {
      for (V_TempImportColDTO v : listCol) {
        if (v.getParameterName() != null && v.getParameterName().equalsIgnoreCase(name)) {
          colPos = v;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return colPos;
  }

  /**
   *
   */

  public Map<String, String> createUpdateResultMap1(List inputList,
      V_WebserviceMethodDetailDTO wsMethod) {
    Map<String, String> map = new HashMap<String, String>();
    try {
      for (Object obj : inputList) {
        Field idField = obj.getClass().getDeclaredField(wsMethod.getIdField());
        Field resultField = obj.getClass().getDeclaredField(wsMethod.getReturnValueField());
        idField.setAccessible(true);
        resultField.setAccessible(true);
        String key = idField.get(obj) == null ? null : idField.get(obj).toString();
        String result = resultField.get(obj) == null ? null : resultField.get(obj).toString();
        map.put(key, result);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return map;
  }
}
