package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_STATE_CODE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisSoftRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrScheduleTelHisSoftBusinessImpl implements MrScheduleTelHisSoftBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrScheduleTelHisSoftRepository mrScheduleTelHisSoftRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  CatItemRepository catItemRepository;

  Map<Long, String> mapMarket = new HashMap<>();
  Map<String, CatItemDTO> mapArray = new HashMap<>();
  Map<String, String> mapStateName = new HashMap<>();

  @Override
  public Datatable getListDataSoftSearchWeb(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    log.debug("Request to getListDataSoftSearchWeb: {}", mrScheduleTelHisDTO);
    Datatable datatable = mrScheduleTelHisSoftRepository
        .getListDataSoftSearchWeb(mrScheduleTelHisDTO);
    if (datatable != null) {
      List<MrScheduleTelHisDTO> list = (List<MrScheduleTelHisDTO>) datatable.getData();
      setMapMarket();
      setMapArray();
      setMapStateName();
      if (list != null && list.size() > 0) {
        for (MrScheduleTelHisDTO dto : list) {
          setValueDetailSoft(dto);
        }
      }
    }
    return datatable;
  }

  @Override
  public File exportDataMrScheduleTelHisSoft(MrScheduleTelHisDTO mrScheduleTelHisDTO)
      throws Exception {
    log.debug("Request to exportDataMrScheduleTelHisSoft: {}", mrScheduleTelHisDTO);
    List<MrScheduleTelHisDTO> list = mrScheduleTelHisSoftRepository
        .getListMrScheduleTelHisSoftExport(mrScheduleTelHisDTO);
    if (list != null && list.size() > 0) {
      setMapMarket();
      setMapArray();
      setMapStateName();
      for (MrScheduleTelHisDTO dto : list) {
        setValueDetailSoft(dto);
      }
    }
    String[] header = new String[]{"marketName", "region", "arrayName", "cycle", "mrComment",
        "deviceType", "deviceCode", "deviceName", "mrDate", "mrCode", "crNumber", "nodeStatus",
        "procedureName", "mrTypeName", "mrArrayType", "crId", "title", "earliestTime",
        "lastestTime", "reponsibleUnitName", "considerName", "unitName", "state"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date));
  }

  public File handleFileExport(List<MrScheduleTelHisDTO> list, String[] columnExport, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("mrScheduleTelHisSoft.export.sheetname");
    String title = I18n.getLanguage("mrScheduleTelHisSoft.export.title").toUpperCase();
    String fileNameOut = I18n.getLanguage("mrScheduleTelHisSoft.export.fileNameOut");
    String headerPrefix = "language.mrScheduleTelHisSoft";
    String firstLeftHeader = I18n.getLanguage("mrScheduleTelHisSoft.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("mrScheduleTelHisSoft.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("mrScheduleTelHisSoft.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("mrScheduleTelHisSoft.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = I18n.getLanguage("mrScheduleTelHisSoft.export.exportDate", date);
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
        I18n.getLanguage("mrScheduleTelHisSoft.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
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

  public void setValueDetailSoft(MrScheduleTelHisDTO dto) {
    dto.setMrTypeName(I18n.getLanguage("mrScheduleTelHisSoft.mrSoft"));
    if (StringUtils.isNotNullOrEmpty(dto.getMarketCode())) {
      dto.setMarketName(mapMarket.get(Long.valueOf(dto.getMarketCode())));
    }
    String arrayCode = dto.getArrayCode();
    if (arrayCode != null && mapArray.containsKey(arrayCode)) {
      dto.setArrayName(mapArray.get(arrayCode).getItemName());
    } else {
      dto.setArrayName("");
    }
    String nodeStatus = dto.getNodeStatus();
    if (StringUtils.isStringNullOrEmpty(nodeStatus)) {
      dto.setNodeStatus("");
    } else {
      switch (nodeStatus) {
        case "0":
          dto.setNodeStatus(I18n.getLanguage("mrScheduleTelHisSoft.nodeStatus.notDone"));
          break;
        case "1":
          dto.setNodeStatus(I18n.getLanguage("mrScheduleTelHisSoft.nodeStatus.done"));
          break;
      }
    }
    dto.setState(mapStateName.get(dto.getState()));
  }

  public void setMapMarket() {
    List<ItemDataCRInside> list = catLocationRepository.getListLocationByLevelCBB(null, 1L, null);
    if (list != null && !list.isEmpty()) {
      for (ItemDataCRInside dto : list) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
      }
    }
  }

  public void setMapArray() {
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataArray.getData();
    if (list != null && list.size() > 0) {
      for (CatItemDTO dto : list) {
        mapArray.put(dto.getItemValue(), dto);
      }
    }
  }

  public void setMapStateName() {
    mapStateName.put(MR_STATE_CODE.OPEN, I18n.getLanguage("mrMngt.state.open"));
    mapStateName
        .put(MR_STATE_CODE.INACTIVE_WAITTING, I18n.getLanguage("mrMngt.state.inactive_waitting"));
    mapStateName.put(MR_STATE_CODE.QUEUE, I18n.getLanguage("mrMngt.state.queue"));
    mapStateName.put(MR_STATE_CODE.ACTIVE, I18n.getLanguage("mrMngt.state.active"));
    mapStateName.put(MR_STATE_CODE.INACTIVE, I18n.getLanguage("mrMngt.state.inactive"));
    mapStateName.put(MR_STATE_CODE.CLOSE, I18n.getLanguage("mrMngt.state.close"));
    mapStateName.put(MR_STATE_CODE.CLOSE7, I18n.getLanguage("mrMngt.state.close7"));
    mapStateName.put(MR_STATE_CODE.CLOSE8, I18n.getLanguage("mrMngt.state.close8"));
    mapStateName.put(MR_STATE_CODE.CLOSE9, I18n.getLanguage("mrMngt.state.close9"));
    mapStateName.put(MR_STATE_CODE.CLOSE10, I18n.getLanguage("mrMngt.state.close10"));
    mapStateName.put(MR_STATE_CODE.CLOSE11, I18n.getLanguage("mrMngt.state.close11"));
    mapStateName.put(MR_STATE_CODE.CLOSE12, I18n.getLanguage("mrMngt.state.close12"));
    mapStateName.put(MR_STATE_CODE.CLOSE13, I18n.getLanguage("mrMngt.state.close13"));
    mapStateName.put(MR_STATE_CODE.CLOSE14, I18n.getLanguage("mrMngt.state.close14"));
    mapStateName.put(MR_STATE_CODE.CLOSE15, I18n.getLanguage("mrMngt.state.close15"));
  }
}
