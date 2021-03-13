package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.HeaderHandler;
import com.viettel.gnoc.cr.dto.CrTemplateDataDTO;
import com.viettel.gnoc.cr.dto.TempImportDTOResult;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
import com.viettel.gnoc.cr.dto.V_TempImportColDTO;
import com.viettel.gnoc.cr.dto.V_WebserviceMethodDetailDTO;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import com.viettel.gnoc.cr.model.TempImportDataEntity;
import com.viettel.gnoc.cr.util.TemplateImportFileUtils;
import com.viettel.gnoc.wo.dto.HeaderForm;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class TemplateImportRepositoryImpl extends BaseRepository implements
    TemplateImportRepository {

  @Value("${application.upload.folder}")
  private String uploadFolder;
  TemplateImportFileUtils tifu = new TemplateImportFileUtils();
  String CR_IMPORT = "CR_IMPORT";
  String CR_IMPORT_RESULT = "CR_IMPORT_RESULT";
  private int allowedRow = 5;
  private int allowedRange = 405;

  @Override
  @Transactional
  public TemplateImportDTO validateTempImport(ExcelWriterUtils xls,
      TemplateImportDTO templateImportDTO1, String locale, String countryId) {

    Workbook workbook = null;
    try {
      String pathIn = templateImportDTO1.getLinkInput();
      // set cung tempImportId
      String tempImportId = templateImportDTO1.getTempImportId();
//      String tempImportId = "83";
      if (pathIn == null || "".equals(pathIn.trim())) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(templateImportDTO1.getFileName() + " : " + I18n
            .getChangeManagement("Cr.Not.exists.tmp", locale));
        return templateImportDTO1;
      }
      if (!pathIn.trim().toLowerCase().endsWith(".xls") && !pathIn.trim().toLowerCase().endsWith(".xlsx")) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(templateImportDTO1.getFileName() + " : " + I18n
            .getChangeManagement("Cr.tmp.notValid", locale));
        return templateImportDTO1;
      }
      List<TempImportDTOResult> listTempImport = getTempImport(tempImportId);
      if (listTempImport.isEmpty()) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(templateImportDTO1.getFileName() + " : " + I18n
            .getChangeManagement("Cr.Not.exists.tmp", locale));
        return templateImportDTO1;
      }
      List<V_TempImportColDTO> listVTempImportColDTOs = getTemplate(tempImportId);
      if (listVTempImportColDTOs.isEmpty()) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(templateImportDTO1.getFileName() + " : " + I18n
            .getChangeManagement("Cr.Not.exists.tmp", locale));
        return templateImportDTO1;
      }
      TempImportDTOResult tempImportDTO = listTempImport.get(0);
      String fileName = templateImportDTO1.getFileName();
      workbook = xls
          .readFileExcel(templateImportDTO1.getLinkInput());
      log.info("\n validateTempImport: " + (workbook != null ? workbook : "workbook is null"));
      if (workbook == null) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(I18n.getChangeManagement("Cr.Not.exists.tmp", locale));
        return templateImportDTO1;
      }
      Sheet sheet = workbook.getSheetAt(0);
      CellStyle styleColumnHeader = sheet.getRow(Constants.EXCEL_PARAM.ROW_HEADER).getCell(0)
          .getCellStyle();
      String validateLength = validateImportedFile(sheet, locale);
      if (!validateLength.equals(Constants.CR_RETURN_MESSAGE.OK)) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(validateLength);
        return templateImportDTO1;
      }
      String validateTemplate = validateHeaderOfFile(xls, fileName,
          sheet,
          tempImportDTO,
          listVTempImportColDTOs, locale);
      if (validateTemplate != null && !"".equals(validateTemplate)) {
        templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO1.setMessage(templateImportDTO1.getFileName() + " : " + validateTemplate);
        return templateImportDTO1;
      }
      if (tempImportDTO.getIsValidateInput() != null
          && tempImportDTO.getIsValidateInput()
          .equals(Constants.IMPORT_FILE_TYPE.IS_VALIDATE_INPUT)) {
        List<Map<String, String>> lsRowWithValue0
            = getRowWithValueFromSheet(xls,
            sheet,
            listVTempImportColDTOs);
        List inputList0 = new ArrayList();
        List<V_WebserviceMethodDetailDTO> listWebservice0 = getWebserviceFormFile(tempImportDTO);
        if (listWebservice0.isEmpty()) {
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          templateImportDTO1.setMessage(I18n.getChangeManagement("cr.ws.notExist", locale));
          return templateImportDTO1;
        }
        V_WebserviceMethodDetailDTO v_WebserviceMethodDetailDTO = listWebservice0.get(0);
        // /* OLD START
        int i = 0;
        for (Map<String, String> map0 : lsRowWithValue0) {
          map0.put(Constants.TEMP_IMPORT_UTILS.ID_KEY, i + "");
          Object input = tifu.parseDataToWebserviceBO(map0,
              listVTempImportColDTOs,
              v_WebserviceMethodDetailDTO);
          if (input != null) {
            inputList0.add(input);
          }
          i++;
        }

        try {
          String className = v_WebserviceMethodDetailDTO.getWebserviceClassPath();
          Class ws0 = Class.forName(className);
          URL baseUrl = ws0.getResource(".");
          URL url = new URL(baseUrl, v_WebserviceMethodDetailDTO.getUrl());
          Class[] paramType = new Class[2];
          paramType[0] = URL.class;
          paramType[1] = QName.class;
          Constructor constructor = ws0.getDeclaredConstructor(paramType);
          Object obj = constructor.newInstance(url,
              new QName(v_WebserviceMethodDetailDTO.getNamespaceUri(),
                  v_WebserviceMethodDetailDTO.getLocalpart()));
          Method method = obj.getClass().getMethod(v_WebserviceMethodDetailDTO.getGetPortMethod());
          Object wsPort = method.invoke(obj);
          String nationCode = "VNM";
          if (!StringUtils.isStringNullOrEmpty(countryId)) {
            nationCode = Constants.NATION_CODE.getGetText().get(Long.parseLong(countryId));
          }
          List<HeaderForm> lstHeader = new ArrayList<>();
          lstHeader.add(new HeaderForm("nationCode", nationCode == null ? "VNM" : nationCode));
          HeaderHandler.setHeaderHandler(wsPort, lstHeader);

          Class[] argTypes = new Class[1];
          argTypes[0] = List.class;

          try {
            Method method1 = wsPort.getClass()
                .getMethod(v_WebserviceMethodDetailDTO.getMethodName(), argTypes);
            inputList0 = (List) method1.invoke(wsPort, inputList0);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
            templateImportDTO1
                .setMessage(I18n.getChangeManagement("cr.ErroHappend.when.call.ws", locale));
            return templateImportDTO1;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        try {
          Map<String, String> lsResult = tifu.createUpdateResultMap1(
              inputList0,
              v_WebserviceMethodDetailDTO);
          Boolean rst = true;
          if (String.valueOf(lsResult.values()).contains("updateCellStatus.cellListNull")) {
            templateImportDTO1.setMessage(I18n.getChangeManagement("cr.file.is.empty", locale));
            templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
            return templateImportDTO1;
          }
          if (!lsResult.isEmpty()) {
            int j = 1;
            for (Map<String, String> row : lsRowWithValue0) {
              // them thong tin dong vao tung map
              String rowNumber = row.get(Constants.TEMP_IMPORT_UTILS.ID_KEY);
              String result = lsResult.get(rowNumber);
              if (result != null && !"".equals(result)) {
                row.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, result);
              }
            }
            int zr = Constants.EXCEL_PARAM.ROW_HEADER + 1;
            xls.createCell(sheet, 0, Constants.EXCEL_PARAM.ROW_ALERT, "");
            for (V_TempImportColDTO t0 : listVTempImportColDTOs) {
              xls.createCell(sheet, t0.getColPosition().intValue() - 1,
                  Constants.EXCEL_PARAM.ROW_HEADER, t0.getTitle(), styleColumnHeader);
              sheet.setColumnWidth(t0.getColPosition().intValue() - 1, 10000);
            }
            xls.createCell(sheet, listVTempImportColDTOs.size(), Constants.EXCEL_PARAM.ROW_HEADER,
                I18n.getChangeManagement("R3667.importResult", locale), styleColumnHeader);
            sheet.setColumnWidth(listVTempImportColDTOs.size(), 10000);
            for (Map<String, String> row0 : lsRowWithValue0) {
              String rowNumber = row0.get(Constants.TEMP_IMPORT_UTILS.ID_KEY);
              String result = lsResult.get(rowNumber);

              boolean checkMaxLength = true;
              for (Map.Entry<String, String> entry : row0.entrySet()) {
                if (StringUtils.isNotNullOrEmpty(entry.getValue()) && entry.getValue().length() > 4000) {
                  checkMaxLength = false;
                  break;
                }
              }
              if (!checkMaxLength) {
                row0.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, I18n.getChangeManagement("import.length.msg", locale));
                rst = false;
              } else if (tempImportDTO.getIsRevert() == null || tempImportDTO.getIsRevert().equals(1L)) {
                if (result == null || "".equals(result)) {
                  row0.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, "Import NOK");
                  rst = false;
                }
              } else if (tempImportDTO.getIsRevert() != null && tempImportDTO.getIsRevert()
                  .equals(2L)) {
                if (result != null && !"".equals(result)) {
                  rst = false;
                } else {
                  row0.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, "Import OK");
                }
              }

              for (int k = 0; k < row0.size() - 1; k++) {
                xls.createCell(sheet, k, zr, row0.get(k + ""));

              }
              xls.createCell(sheet,
                  listVTempImportColDTOs.size(), zr,
                  row0.get(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY));
              zr++;
            }
            if (!rst) {
              templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
              templateImportDTO1
                  .setMessage(I18n.getChangeManagement("errorValidateDetail", locale));
              Date date = new Date();
              String pathByDate = FileUtils.createPathByDate(date);
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(date);
              String filePath0 = uploadFolder
                  + File.separator + pathByDate
                  + File.separator;
              String fileNameExp = calendar.get(Calendar.HOUR_OF_DAY) + ""
                  + calendar.get(Calendar.MINUTE) + ""
                  + calendar.get(Calendar.SECOND) + ""
                  + calendar.get(Calendar.MILLISECOND) + "_" + tempImportDTO.getName();
              xls.saveToFileExcel(filePath0, fileNameExp);
              templateImportDTO1.setLinkOutput(filePath0 + fileNameExp);
              return templateImportDTO1;
            }
          } else {
            templateImportDTO1.setMessage(I18n.getChangeManagement("cr.file.is.empty", locale));
            templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
            return templateImportDTO1;
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
          templateImportDTO1.setMessage(I18n.getChangeManagement("cr.error.progress", locale));
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          return templateImportDTO1;
        }

      }
      if (tempImportDTO.getIsValidateOutput() != null
          && tempImportDTO.getIsValidateOutput()
          .equals(Constants.IMPORT_FILE_TYPE.IS_VALIDATE_OUTPUT)) {
        List<Map<String, String>> lsRowWithValue
            = getRowWithValueFromSheet(xls,
            sheet,
            listVTempImportColDTOs);
        if (!validateDataOfInOutPut(lsRowWithValue,
            listVTempImportColDTOs,
            Long.parseLong(templateImportDTO1.getTempImportId()),
            Long.parseLong(templateImportDTO1.getCrId()))) {
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          templateImportDTO1.setMessage(I18n.getChangeManagement("cr.in.out.notgoout", locale));
          return templateImportDTO1;
        }
        List inputList = new ArrayList();
        List<V_WebserviceMethodDetailDTO> listWebservice = getWebserviceFormFile(tempImportDTO);
        if (listWebservice.isEmpty()) {
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          templateImportDTO1.setMessage(I18n.getChangeManagement("cr.ws.notExist", locale));
          return templateImportDTO1;
        }
        V_WebserviceMethodDetailDTO v_WebserviceMethodDetailDTO = listWebservice.get(0);
        // OLD START /*
        int i = 0;
        for (Map<String, String> map : lsRowWithValue) {
          map.put(Constants.TEMP_IMPORT_UTILS.ID_KEY, i + "");
          Object input = tifu.parseDataToWebserviceBO(map,
              listVTempImportColDTOs,
              v_WebserviceMethodDetailDTO);
          if (input != null) {
            inputList.add(input);
          }
          i++;
        }
        Class ws = Class.forName(v_WebserviceMethodDetailDTO.getWebserviceClassPath());
        URL baseUrl = ws.getResource(".");
        URL url = new URL(baseUrl, v_WebserviceMethodDetailDTO.getUrl());
        Class[] paramType = new Class[2];
        paramType[0] = URL.class;
        paramType[1] = QName.class;
        Constructor constructor = ws.getDeclaredConstructor(paramType);
        Object obj = constructor.newInstance(url,
            new QName(v_WebserviceMethodDetailDTO.getNamespaceUri(),
                v_WebserviceMethodDetailDTO.getLocalpart()));
        Method method = obj.getClass().getMethod(v_WebserviceMethodDetailDTO.getGetPortMethod());

        Object wsPort = method.invoke(obj);
        String nationCode = "VNM";
        if (!StringUtils.isStringNullOrEmpty(countryId)) {
          nationCode = Constants.NATION_CODE.getGetText().get(Long.parseLong(countryId));
        }
        List<HeaderForm> lstHeader = new ArrayList<>();
        lstHeader.add(new HeaderForm("nationCode", nationCode == null ? "VNM" : nationCode));
        HeaderHandler.setHeaderHandler(wsPort, lstHeader);

        Class[] argTypes = new Class[4];
        argTypes[0] = String.class;
        argTypes[1] = String.class;
        argTypes[2] = String.class;
        argTypes[3] = List.class;
        try {
          Method method1 = wsPort.getClass()
              .getMethod(v_WebserviceMethodDetailDTO.getMethodName(), argTypes);
          inputList = (List) method1.invoke(wsPort, v_WebserviceMethodDetailDTO.getUserName(),
              v_WebserviceMethodDetailDTO.getPassword(),
              templateImportDTO1.getCrId(),
              inputList);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          templateImportDTO1
              .setMessage(I18n.getChangeManagement("cr.ErroHappend.when.call.ws", locale));
          return templateImportDTO1;
        }

        try {
          Map<String, String> lsResult = tifu.createUpdateResultMap1(
              inputList,
              v_WebserviceMethodDetailDTO);
          Boolean rst = true;
          if (String.valueOf(lsResult.values()).contains("updateCellStatus.cellListNull")) {
            templateImportDTO1.setMessage(I18n.getChangeManagement("cr.file.is.empty", locale));
            templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
            return templateImportDTO1;
          }
          if (!lsResult.isEmpty()) {
            int j = 1;
            for (Map<String, String> row : lsRowWithValue) {
              // them thong tin dong vao tung map
              String rowNumber = row.get(Constants.TEMP_IMPORT_UTILS.ID_KEY);
              String result = lsResult.get(rowNumber);
              if (result != null) {
                row.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, result);
              }
            }
            int zr = Constants.EXCEL_PARAM.ROW_HEADER + 1;
            xls.createCell(sheet, 0, Constants.EXCEL_PARAM.ROW_ALERT, "");
            for (V_TempImportColDTO t : listVTempImportColDTOs) {
              xls.createCell(sheet, t.getColPosition().intValue() - 1,
                  Constants.EXCEL_PARAM.ROW_HEADER, t.getTitle(), styleColumnHeader);
              sheet.setColumnWidth(t.getColPosition().intValue() - 1, 10000);
            }
            xls.createCell(sheet, listVTempImportColDTOs.size(), Constants.EXCEL_PARAM.ROW_HEADER,
                I18n.getChangeManagement("R3667.importResult", locale), styleColumnHeader);
            sheet.setColumnWidth(listVTempImportColDTOs.size(), 10000);
            for (Map<String, String> row : lsRowWithValue) {
              String rowNumber = row.get(Constants.TEMP_IMPORT_UTILS.ID_KEY);
              String result = lsResult.get(rowNumber);

              boolean checkMaxLength = true;
              for (Map.Entry<String, String> entry : row.entrySet()) {
                if (StringUtils.isNotNullOrEmpty(entry.getValue()) && entry.getValue().length() > 4000) {
                  checkMaxLength = false;
                  break;
                }
              }
              if (!checkMaxLength) {
                row.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, I18n.getChangeManagement("import.length.msg", locale));
                rst = false;
              } else if (tempImportDTO.getIsRevert() == null || tempImportDTO.getIsRevert().equals(1L)) {
                if (result == null) {
                  row.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, "Import NOK");
                  rst = false;
                }
              } else if (tempImportDTO.getIsRevert() != null && tempImportDTO.getIsRevert()
                  .equals(2L)) {
                if (result != null) {
                  rst = false;
                } else {
                  row.put(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY, "Import OK");
                }
              }

              for (int k = 0; k < row.size() - 1; k++) {
                xls.createCell(sheet, k, zr, row.get(k + ""));

              }
              xls.createCell(sheet,
                  listVTempImportColDTOs.size(), zr,
                  row.get(Constants.TEMP_IMPORT_UTILS.RETURN_VALUE_KEY));
              zr++;
            }
            if (!rst) {
              templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
              templateImportDTO1
                  .setMessage(I18n.getChangeManagement("errorValidateDetail", locale));
              Date date = new Date();
              String pathByDate = FileUtils.createPathByDate(date);
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(date);
              String filePath0 = uploadFolder
                  + File.separator + pathByDate
                  + File.separator + calendar.get(Calendar.HOUR_OF_DAY) + ""
                  + calendar.get(Calendar.MINUTE) + ""
                  + calendar.get(Calendar.SECOND) + ""
                  + calendar.get(Calendar.MILLISECOND);
              String fileNameExp = tempImportDTO.getName();
              templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
              templateImportDTO1
                  .setMessage(I18n.getChangeManagement("errorValidateDetail", locale));

              xls.saveToFileExcel(filePath0, fileNameExp);
              templateImportDTO1.setLinkOutput(filePath0 + fileNameExp);
              return templateImportDTO1;
            }
          } else {
            templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
            templateImportDTO1.setMessage(I18n.getChangeManagement("cr.file.is.empty", locale));
            return templateImportDTO1;
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
          templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
          templateImportDTO1.setMessage(I18n.getChangeManagement("cr.error.progress", locale));
          return templateImportDTO1;
        }

      }
      templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.SUCCESS);
      templateImportDTO1.setMessage(null);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      templateImportDTO1.setResult(Constants.CR_RETURN_MESSAGE.NOK);
      templateImportDTO1.setMessage(I18n.getChangeManagement("cr.Error.spc", locale));
      return templateImportDTO1;
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return templateImportDTO1;
  }

  @Override
  public List<TempImportDTOResult> getTempImport(String tempImportId) {
    try {
      if (StringUtils.isStringNullOrEmpty(tempImportId)) {
        return null;
      }
      String sql = SQLBuilder.getSqlQueryById(
          SQLBuilder.SQL_MODULE_TEMPLATE, "get-template-import");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("p_leeLocale", I18n.getLocale());
      parameters.put("temImportId", tempImportId);
      List<TempImportDTOResult> list = getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<>(
          TempImportDTOResult.class));
      if (list != null && list.isEmpty() && !"vi_VN".equalsIgnoreCase(I18n.getLocale())) {
        parameters.put("p_leeLocale", "vi_VN");
        list = getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<>(
            TempImportDTOResult.class));
      }
      return list;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<V_TempImportColDTO> getTemplate(String tempImportId) {
    try {
      if (StringUtils.isStringNullOrEmpty(tempImportId)) {
        return null;
      }
      String sql = SQLBuilder.getSqlQueryById(
          SQLBuilder.SQL_MODULE_TEMPLATE, "get-template");
      Map<String, Object> parameters = new HashMap<>();
      if (tempImportId != null) {
        parameters.put("temImportId", tempImportId);
      }
      return getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<>(
          V_TempImportColDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public String validateImportedFile(Sheet sheet, String locale) {
    String result = Constants.CR_RETURN_MESSAGE.OK;
    String maxImport = getMaxImport();
    if (!validateLength(maxImport, sheet)) {
      result = I18n.getChangeManagement("too.much.row", locale);
      result = "Total: " + (sheet.getLastRowNum() - allowedRow) + ". " + result
          .replace("#COUNT", (allowedRange - allowedRow) + "");
    }
    return result;
  }

  public String getMaxImport() {
    String ret = null;
    try {
      String sql = SQLBuilder.getSqlQueryById(
          SQLBuilder.SQL_MODULE_TEMPLATE, "get-maxImport");

      List<TemplateRelationsDTO> obj;
      obj = getNamedParameterJdbcTemplate()
          .query(sql, BeanPropertyRowMapper.newInstance(TemplateRelationsDTO.class));

      if (obj != null && !obj.isEmpty()) {
        ret = obj.get(0).getTrsName();
        if (StringUtils.isStringNullOrEmpty(ret)) {
          ret = "1000";
        }
      } else {
        ret = "1000";
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ret;
  }

  public boolean validateLength(String maxImport, Sheet sheet) {
    boolean result = true;
    try {
      allowedRange = Integer.valueOf(maxImport).intValue();
      int rows = sheet.getLastRowNum();
      if (rows > allowedRange) {
        return false;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return result;
  }

  public String validateHeaderOfFile(ExcelWriterUtils xls,
      String fileName,
      Sheet sheet,
      TempImportDTOResult tempImportDTO,
      List<V_TempImportColDTO> lsTemplateImportColOfFile,
      String locale) {
    String result = "";
    try {
      String title = ExcelWriterUtils.getCellStrContent(sheet, 0, Constants.EXCEL_PARAM.ROW_TITLE);
      if (title == null || (tempImportDTO.getTitle() != null
          && !title.trim().equalsIgnoreCase(tempImportDTO.getTitle().trim()))) {
        result = I18n.getChangeManagement("cr.template.Header", locale);
        return result;
      }
      if (!lsTemplateImportColOfFile.isEmpty()) {
        for (int i = 0; i < lsTemplateImportColOfFile.size(); i++) {
          String header = ExcelWriterUtils.getCellStrContent(sheet,
              lsTemplateImportColOfFile.get(i).getColPosition().intValue() - 1,
              Constants.EXCEL_PARAM.ROW_HEADER);
          if (!header.trim().equalsIgnoreCase(lsTemplateImportColOfFile.get(i).getTitle().trim())) {
            result = I18n.getChangeManagement("cr.template.column", locale);
            result += header + " ";
            result += I18n.getChangeManagement("cr.template.notvalid", locale);
          }
        }
      }
    } catch (Exception e) {
      result = I18n.getChangeManagement("cr.Error.spc");
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public List<Map<String, String>> getRowWithValueFromSheet(ExcelWriterUtils xls, Sheet sheet,
      List<V_TempImportColDTO> lsTemplateImportColOfFile) {
    List<Map<String, String>> ls1 = new ArrayList<Map<String, String>>();
    List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
    try {
      int lastRow = sheet.getLastRowNum();
      List<Cell> lstCell
          = getUnMergeCellList(sheet, lastRow, lsTemplateImportColOfFile);
      if (!lstCell.isEmpty()) {
        for (int i = Constants.EXCEL_PARAM.ROW_DATA; i <= lastRow; i++) {
          Map<String, String> map = new HashMap<String, String>();
          int col = 0;
          for (V_TempImportColDTO t : lsTemplateImportColOfFile) {
            col = t.getColPosition().intValue() - 1;
            String cellValue = tifu.getCellValue(sheet, lstCell, i, col);
            map.put(col + "", cellValue);

          }
          ls.add(map);
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

  public List<Cell> getUnMergeCellList(Sheet sheet, int lastRow,
      List<V_TempImportColDTO> lsUnMergeCol) {
    List<Cell> ls = new ArrayList<Cell>();
    try {
      for (V_TempImportColDTO t : lsUnMergeCol) {
        for (int j = Constants.EXCEL_PARAM.ROW_DATA; j <= lastRow; j++) {
          if (sheet.getRow(j) != null) {
            if (sheet.getRow(j).getCell(t.getColPosition().intValue() - 1) != null) {
              ls.add(sheet.getRow(j).getCell(t.getColPosition().intValue() - 1));
            } else {
              ls.add(sheet.getRow(j).createCell(t.getColPosition().intValue() - 1));
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ls;
  }

  public boolean validateDataOfInOutPut(
      List<Map<String, String>> lsRowWithValue,
      List<V_TempImportColDTO> listVTempImportColDTOs,
      Long templateImportId,
      Long crId) {
    boolean result = true;
    try {
      List<TemplateRelationsDTO> listTempHost = getRelations(templateImportId);//host
      if (listTempHost.isEmpty()) {
        return true;
      }
      TemplateRelationsDTO templateHostDTO = listTempHost.get(0);
      List<TemplateRelationsDTO> listMappingHost
          = getMappingColRelations(templateHostDTO.getTrsId());
      if (listMappingHost.isEmpty()) {
        return true;
      }
      Map<String, String> mapRelation = new HashMap<String, String>();
      List<Long> listIn = new ArrayList<Long>();
      List<Long> listOutPut = new ArrayList<Long>();
      ;
      for (TemplateRelationsDTO templateHostDTO1 : listMappingHost) {
        mapRelation.put(templateHostDTO1.getHostColId().toString(),
            templateHostDTO1.getTempColId().toString());
        if (true && !listIn.contains(templateHostDTO1.getHostColId())) {
          listIn.add(templateHostDTO1.getHostColId());
        }
        if (true && !listOutPut.contains(templateHostDTO1.getTempColId())) {
          listOutPut.add(templateHostDTO1.getTempColId());
        }
      }
      Long templateInput = Long.valueOf(templateHostDTO.getHostTemplateId());
      List<Map<String, String>> listInputData = convertInputToListMapByRow(crId,
          templateInput, listIn);
      List<Map<String, String>> listOutputData = convertOutputToListMapByRow(
          lsRowWithValue, listVTempImportColDTOs, listOutPut);
      for (Map<String, String> outPutMap : listOutputData) {
        if (true && !compareMap(outPutMap, listInputData, mapRelation)) {
          return false;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public List<Map<String, String>> convertOutputToListMapByRow(
      List<Map<String, String>> lsRowWithValue,
      List<V_TempImportColDTO> listVTempImportColDTOs,
      List<Long> listColOut) {
    List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
    try {
      for (int i = 0; i < lsRowWithValue.size(); i++) {
        Map<String, String> map = lsRowWithValue.get(i);
        Map<String, String> mapOut = new HashMap<String, String>();
        int col = 0;
        for (V_TempImportColDTO t : listVTempImportColDTOs) {
          col = t.getColPosition().intValue() - 1;
          if (listColOut.contains(t.getTempImportColId())) {
            String value = map.get(col + "") == null ? "" : map.get(col + "").trim();
            Long tempImportColId = t.getTempImportColId();
            mapOut.put(tempImportColId.toString(), value.toLowerCase());
          }
        }
        if (true && !mapOut.isEmpty()) {
          lst.add(mapOut);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }


  public List<TemplateRelationsDTO> getRelations(
      Long tempImportId) {
    List<TemplateRelationsDTO> lst = new ArrayList<>();
    try {
      String sql = "select trs_id as trsId, "
          + " trs_name as trsName, "
          + " host_template_id as hostTemplateId, "
          + " client_template_id as clientTemplateId, "
          + " relation_type as relationType "
          + " from template_relations "
          + " where client_template_id =  :temImportId";
      Map<String, Object> parameters = new HashMap<>();
      if (tempImportId != null) {
        parameters.put("temImportId", tempImportId);
      }
      lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(
              TemplateRelationsDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<TemplateRelationsDTO> getMappingColRelations(
      Long templateRealtionId) {
    List<TemplateRelationsDTO> lst = new ArrayList<TemplateRelationsDTO>();
    try {
      String sql = "select rds.HOST_COL_ID as hostColId, "
          + " rds.TEMP_COL_ID as tempColId, "
          + " trs_id as trsId "
          + " from relation_details rds "
          + " where trs_id =  :trsId";

      Map<String, Object> parameters = new HashMap<>();
      if (templateRealtionId != null) {
        parameters.put("trsId", templateRealtionId);
      }
      lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(
              TemplateRelationsDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<Map<String, String>> convertInputToListMapByRow(
      Long crId,
      Long templateId,
      List<Long> listCol) {
    List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
    try {
      List<CrTemplateDataDTO> listData = getListDataByTempImport(
          crId,
          templateId,
          listCol);
      List<String> listRowOrder = new ArrayList<String>();
      for (CrTemplateDataDTO crTemplateDTO : listData) {
        String order = crTemplateDTO.getTempImportRowOrder();
        if (true && !listRowOrder.contains(order.trim().toLowerCase())) {
          listRowOrder.add(order.trim().toLowerCase());
        }
      }
      for (String rowOrder : listRowOrder) {
        Map<String, String> map = new HashMap<String, String>();
        for (CrTemplateDataDTO crTemplateDTO : listData) {
          String order = crTemplateDTO.getTempImportRowOrder();
          if (order.equals(rowOrder)) {
            map.put(crTemplateDTO.getTempImportColumnId().trim().toLowerCase(),
                crTemplateDTO.getTempImportColValue().trim().toLowerCase());
          }
        }
        if (true && !map.isEmpty()) {
          lst.add(map);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }
//  public List<CrTemplateDataDTO> getListDataByTempImport(Session session,
//      CrOutputDataDTO crOutputDataDTO) {
//    List<CrTemplateDataDTO> list;
//    list = new ArrayList<CrTemplateDataDTO>();
//    try {
//      String sql = " select tida.cr_id crId, "
//          + " tida.TEMP_IMPORT_COL_ID tempImportColumnId, "
//          + " tida.TEMP_IMPORT_ID tempImportId, "
//          + " ticl.CODE tempImportColCode, "
//          + " ticl.TITLE tempImportColName, "
//          + " tida.TEMP_IMPORT_VALUE tempImportColValue, "
//          + " tida.ROW_ORDER tempImportRowOrder, "
//          + " ticl.COL_POSITION tempImportColumnPos "
//          + " from TEMP_IMPORT_DATA tida, "
//          + " TEMP_IMPORT_COL ticl "
//          + " where cr_id = :crId "
//          + " and tida.TEMP_IMPORT_ID = :tempImportId "
//          + " and tida.TEMP_IMPORT_COL_ID = ticl.TEMP_IMPORT_COL_ID "
//          + " order by tida.CR_ID,tida.TEMP_IMPORT_ID, "
//          + " tida.ROW_ORDER,ticl.COL_POSITION";
//      Query query = session.createSQLQuery(sql)
//          .addScalar("crId", new StringType())
//          .addScalar("tempImportColumnId", new StringType())
//          .addScalar("tempImportId", new StringType())
//          .addScalar("tempImportColName", new StringType())
//          .addScalar("tempImportColCode", new StringType())
//          .addScalar("tempImportColValue", new StringType())
//          .addScalar("tempImportRowOrder", new StringType())
//          .addScalar("tempImportColumnPos", new StringType())
//          .setResultTransformer(Transformers.aliasToBean(CrTemplateDataDTO.class));
//      query.setParameter(0, Long.parseLong(crOutputDataDTO.getCrId()));
//      query.setParameter(1, Long.parseLong(crOutputDataDTO.getTempImportId()));
//      list = query.list();
//      for (CrTemplateDataDTO crTemplateDataDTO : list) {
//        crTemplateDataDTO.setTempImportCode(crOutputDataDTO.getTempImportCode());
//        crTemplateDataDTO.setTempImportName(crOutputDataDTO.getTempImportName());
//      }
//    } catch (Exception e) {
//      com.viettel.gnoc.cr.utils.LogUtils.printLog(e);
//    }
//    return list;
//  }

  public List<CrTemplateDataDTO> getListDataByTempImport(
      Long crId,
      Long templateId,
      List<Long> listCol) {
    List<CrTemplateDataDTO> list;
    list = new ArrayList<CrTemplateDataDTO>();
    if (listCol != null) {
      listCol.add(0L);
    } else {
      listCol = new ArrayList<>();
    }
    try {
      String sql = " select tida.cr_id crId, "
          + " tida.TEMP_IMPORT_COL_ID tempImportColumnId, "
          + " tida.TEMP_IMPORT_ID tempImportId, "
          + " ticl.CODE tempImportColCode, "
          + " ticl.TITLE tempImportColName, "
          + " tida.TEMP_IMPORT_VALUE tempImportColValue, "
          + " tida.ROW_ORDER tempImportRowOrder, "
          + " ticl.COL_POSITION tempImportColumnPos "
          + " from TEMP_IMPORT_DATA tida, "
          + " TEMP_IMPORT_COL ticl "
          + " where cr_id = :crId "
          + " and tida.TEMP_IMPORT_ID = :tempImportId "
          + " and tida.TEMP_IMPORT_COL_ID = ticl.TEMP_IMPORT_COL_ID "
          + " and ticl.TEMP_IMPORT_COL_ID in (:listCol)";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("crId", crId);
      parameters.put("tempImportId", templateId);
      parameters.put("listCol", listCol);
      sql += " order by tida.CR_ID,tida.TEMP_IMPORT_ID, "
          + " tida.ROW_ORDER,ticl.COL_POSITION";
      list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(
              CrTemplateDataDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public boolean compareMap(Map<String, String> mapOut,
      List<Map<String, String>> listMapIn,
      Map<String, String> mapRelations) {
    boolean result = false;
    boolean temp = true;
    for (Map<String, String> mapIn : listMapIn) {
      for (Iterator<String> ir = mapIn.keySet().iterator();
          ir.hasNext(); ) {
        String colIn = ir.next();
        String colOut = mapRelations.get(colIn);
        String val = mapIn.get(colIn) == null ? "" : mapIn.get(colIn);
        String valOut = mapOut.get(colOut) == null ? "" : mapOut.get(colOut);
        if (val.trim().equals(valOut.trim())) {
          temp = true;
        } else {
          temp = false;
          continue;
        }
      }
      if (temp) {
        result = true;
        break;
      }
    }
    return result;
  }


  public List<V_WebserviceMethodDetailDTO> getWebserviceFormFile(
      TempImportDTOResult tempImportDTO) {
    List<V_WebserviceMethodDetailDTO> lst = new ArrayList<V_WebserviceMethodDetailDTO>();
    try {
      if (tempImportDTO.getWebserviceMethodId() == null) {
        return lst;
      }
      String sql = " select vwmdl.WEBSERVICE_METHOD_ID webserviceMethodId, "
          + " vwmdl.WEBSERVICE_ID webserviceId, "
          + " vwmdl.METHOD_NAME methodName, "
          + " vwmdl.SUCESS_RETURN_VALUE sucessReturnValue, "
          + " vwmdl.WEBSERVICE_NAME webserviceName, "
          + " vwmdl.URL url , "
          + " vwmdl.NAMESPACE_URI namespaceUri, "
          + " vwmdl.LOCALPART localpart, "
          + " vwmdl.WEBSERVICE_CLASS_PATH webserviceClassPath, "
          + " vwmdl.GET_PORT_METHOD getPortMethod, "
          + " vwmdl.CLASS_PATH classPath, "
          + " vwmdl.ID_FIELD idField, "
          + " vwmdl.RETURN_VALUE_FIELD returnValueField, "
          + " vwmdl.USER_NAME userName, "
          + " vwmdl.PASSWORD password "
          + " from V_WEBSERVICE_METHOD_DETAIL vwmdl "
          + " where "
          + " vwmdl.WEBSERVICE_METHOD_ID = :webServiceMethodId";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("webServiceMethodId", tempImportDTO.getWebserviceMethodId());
      lst = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(V_WebserviceMethodDetailDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public void actionClearData(TemplateImportDTO templateImportDTO) {
//    templateImportDTO.setTempImportId("83");
    try {
      deleteByMultilParam(TempImportDataEntity.class,
          "crId", templateImportDTO.getCrId(),
          "tempImportId", templateImportDTO.getTempImportId());
    } catch (Exception he) {
      log.error(he.getMessage(), he);
    }
  }

  @Override
  public void actionClearFileAttInput(TemplateImportDTO templateImportDTO) {
    try {
//      templateImportDTO.setTempImportId("83");
      deleteByMultilParam(CrFilesAttachEntity.class,
          "crId", Long.parseLong(templateImportDTO.getCrId()),
          "tempImportId", Long.parseLong(templateImportDTO.getTempImportId()),
          "fileType", templateImportDTO.getFileType());
    } catch (Exception he) {
      log.error(he.getMessage(), he);
    }
  }

  @Override
  public TemplateImportDTO insertIntoTempImport(ExcelWriterUtils xls,
      TemplateImportDTO templateImportDTO, String locale) {
    try {
      String tempImportId = templateImportDTO.getTempImportId();
//      String tempImportId = "83";
      Workbook workbook = xls.readFileExcel(templateImportDTO.getLinkInput());
      if (workbook == null) {
        templateImportDTO.setResult(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO.setMessage(I18n.getChangeManagement("Cr.tmp.notValid.OR"));
        return templateImportDTO;
      }
      Sheet sheet = workbook.getSheetAt(0);
      List<V_TempImportColDTO> listVTempImportColDTOs = getTemplate(tempImportId);
      List<Map<String, String>> lsRowWithValue
          = getRowWithValueFromSheet(xls,
          sheet, listVTempImportColDTOs);
      if (!lsRowWithValue.isEmpty()) {
        for (int i = 0; i < lsRowWithValue.size(); i++) {
          Map<String, String> map = lsRowWithValue.get(i);
          insertRowOfTaskCr(Long.valueOf(templateImportDTO.getCrId()),
              map, i,
              listVTempImportColDTOs);
          if (i % 100 == 0) {
//            EntityManager.flush();
          }
        }
//        EntityManager.flush();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      templateImportDTO.setResult(Constants.CR_RETURN_MESSAGE.NOK);
      templateImportDTO.setMessage(I18n.getChangeManagement(("cr.Error.spc")));
      return templateImportDTO;
    }
    return templateImportDTO;
  }

  private void insertRowOfTaskCr(
      Long crId,
      Map<String, String> map, int i,
      List<V_TempImportColDTO> lsTemplateImportColOfFile) {
    try {
      int col = 0;
      for (V_TempImportColDTO t : lsTemplateImportColOfFile) {
        TempImportDataEntity tempImportDataEntity = new TempImportDataEntity();

        col = t.getColPosition().intValue() - 1;
        String value = map.get(col + "") == null ? "" : map.get(col + "").trim();
        Long tempImportId = t.getTempImportId();
        Long tempImportColId = t.getTempImportColId();

        tempImportDataEntity.setTempImportId(tempImportId.toString());
        tempImportDataEntity.setTempImportColId(tempImportColId.toString());
        tempImportDataEntity.setTempImportValue(value);
        tempImportDataEntity.setCrId(crId.toString());
        tempImportDataEntity.setRowOrder(String.valueOf(i));
        getEntityManager().persist(tempImportDataEntity);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
