package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MrITHisBusinessImpl implements MrITHisBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;
  private static final String MR_TYPE_HARD = "H";
  private static final String MR_TYPE_SOFT = "S";
  @Autowired
  MrITHisRepository mrITHisRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Override
  public Datatable getListMrSoftITHis(MrScheduleITHisDTO mrScheduleITHisDTO) {
    log.debug("Request to getList : {}", mrScheduleITHisDTO);
    return mrITHisRepository.getListMrSoftITHis(mrScheduleITHisDTO);
  }

  @Override
  public Datatable getListMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO) {
    log.debug("Request to getListMrHardITHis : {}", mrScheduleITHisDTO);
    return mrITHisRepository.getListMrHardITHis(mrScheduleITHisDTO);
  }

  @Override
  public File exportDataMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO) throws Exception {
    log.debug("Request to exportDataMrHardITHis : {}", mrScheduleITHisDTO);
    List<MrScheduleITHisDTO> mrScheduleITHisDTOS = mrITHisRepository
        .getDataExportMrHardITHis(mrScheduleITHisDTO);
    return handleFileExport(mrScheduleITHisDTOS, MR_TYPE_HARD,
        DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
  }

  @Override
  public ResultInSideDto insertList(List<MrScheduleITHisDTO> lst) {
    log.debug("Request to insertList : {}", lst);
    return mrITHisRepository.insertList(lst);
  }

  @Override
  public File exportData(MrScheduleITHisDTO mrScheduleITHisDTO) throws Exception {
    log.debug("Request to exportData : {}", mrScheduleITHisDTO);
    List<MrScheduleITHisDTO> mrScheduleITHisDTOS = mrITHisRepository
        .getDataExport(mrScheduleITHisDTO);
    return handleFileExport(mrScheduleITHisDTOS, MR_TYPE_SOFT,
        DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
  }

  public File handleFileExport(List<MrScheduleITHisDTO> list, String type, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    if (type.equals(MR_TYPE_SOFT)) {
      sheetName = I18n.getLanguage("mrScheduleITHisSoft.export.title");
      title = I18n.getLanguage("mrScheduleITHisSoft.export.title").toUpperCase();
      fileNameOut = I18n.getLanguage("mrScheduleITHisSoft.export.fileNameOut");
      lstHeaderSheet1 = getListHeaderSheet(
          new String[]{"nationName", "arrayCodeStr", "deviceType", "deviceCode", "deviceName",
              "mrDate"
              , "mrCode", "crId", "nodeStatus", "mrTypeName", "earliestTime", "lastestTime",
              "reponsibleUnitName", "considerName", "state", "unitName", "cycle", "mrArrayType",
              "note",});
    } else {
      sheetName = I18n.getLanguage("mrScheduleITHisHard.export.title");
      title = I18n.getLanguage("mrScheduleITHisHard.export.title").toUpperCase();
      fileNameOut = I18n.getLanguage("mrScheduleITHisHard.export.fileNameOut");
      lstHeaderSheet1 = getListHeaderSheet(
          new String[]{"nationName", "arrayCodeStr", "deviceType", "deviceCode", "deviceName",
              "mrDate"
              , "mrCode", "woId", "mrTypeName", "nodeStatus", "note"});
    }
    String subTitle = I18n.getLanguage("mrScheduleITHisSoft.export.exportDate", date);
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.mrScheduleITHisSoft",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("mrScheduleITHisSoft.export.firstLeftHeader"),
        I18n.getLanguage("mrScheduleITHisSoft.export.secondLeftHeader"),
        I18n.getLanguage("mrScheduleITHisSoft.export.firstRightHeader"),
        I18n.getLanguage("mrScheduleITHisSoft.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("mrScheduleITHisSoft.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    String rootPath = tempFolder + File.separator;
    return CommonExport.exportExcel(fileTemplate, fileNameOut, fileExports, rootPath, null);
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
