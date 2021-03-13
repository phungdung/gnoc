package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.TroubleImportantEntity;
import com.viettel.gnoc.commons.repository.TroubleImportantRepository;
import com.viettel.gnoc.commons.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hungtv77
 */
@Service
@Transactional
@Slf4j
public class TroubleImportantBusinessImpl implements TroubleImportantBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  private TroubleImportantRepository troubleImportantRepository;

  @Override
  public Datatable searchListTroubleImportant(TroubleImportantDTO dto) {
    return troubleImportantRepository.searchListTroubleImportant(dto);
  }

  @Override
  public ResultInSideDto insert(TroubleImportantDTO dto) {
    return troubleImportantRepository.insert(dto);
  }

  @Override
  public ResultInSideDto update(TroubleImportantDTO dto) {
    return troubleImportantRepository.update(dto);
  }

  @Override
  public TroubleImportantDTO getDetailTrouble(Long id) {
    return troubleImportantRepository.getDetailTrouble(id);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return troubleImportantRepository.delete(id);
  }

  @Override
  public File exportData(TroubleImportantDTO troubleImportantDTO) throws Exception{
    String[] header = new String[]{"troubleName", "countryId", "priorityId", "classNetwork", "isAlarm", "startTimeString",
        "endTimeString", "processTimeString", "numberPakh", "numberPakhReal", "nature", "confirmTrouble", "serviceOwner",
        "unitId", "ptCode", "vender", "isNotAlarm", "triggerUnit", "week", "detectTimeString", "detectTroubleTimeString", "userCreate", "userDetect",
        "userProcess", "coordination", "arrayName", "groupReasonName"};
    List<TroubleImportantDTO> lstData = (List<TroubleImportantDTO>)troubleImportantRepository.searchListTroubleImportant(troubleImportantDTO).getData();
    for (TroubleImportantDTO data : lstData) {
      String start;
      if(data.getStartTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = DateTimeUtils.converClientDateToServerDate(dfm.format(data.getStartTime()), 0.0);
        data.setStartTimeString(start);
      }
      if(data.getEndTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = DateTimeUtils.converClientDateToServerDate(dfm.format(data.getEndTime()), 0.0);
        data.setEndTimeString(start);
      }
      if(data.getProcessTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = DateTimeUtils.converClientDateToServerDate(dfm.format(data.getProcessTime()), 0.0);
        data.setProcessTimeString(start);
      }
      if(data.getDetectTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = DateTimeUtils.converClientDateToServerDate(dfm.format(data.getDetectTime()), 0.0);
        data.setDetectTimeString(start);
      }
      if(data.getDetectTroubleTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = DateTimeUtils.converClientDateToServerDate(dfm.format(data.getDetectTroubleTime()), 0.0);
        data.setDetectTroubleTimeString(start);
      }
    }
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(lstData, header, DateUtil.date2ddMMyyyyHHMMss(date));
  }

  public File handleFileExport(List<TroubleImportantDTO> list, String[] columnExport, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("trouble.export.sheetname");
    String title = I18n.getLanguage("trouble.export.title");
    String fileNameOut = I18n.getLanguage("trouble.export.fileNameOut");
    String headerPrefix = "language.trouble";
    String firstLeftHeader = I18n.getLanguage("trouble.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("trouble.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("trouble.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("trouble.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = I18n.getLanguage("trouble.export.exportDate", date);
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("history.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }
}
