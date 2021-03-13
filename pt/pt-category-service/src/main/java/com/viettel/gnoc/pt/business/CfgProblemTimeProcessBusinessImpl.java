package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import com.viettel.gnoc.pt.repository.CfgProblemTimeProcessRepository;
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
public class CfgProblemTimeProcessBusinessImpl implements CfgProblemTimeProcessBusiness {

  @Autowired
  CfgProblemTimeProcessRepository cfgProblemTimeProcessRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public CfgProblemTimeProcessDTO findById(long id) {
    log.debug("Request to CfgProblemTimeProcessDTO findById: {}", id);
    return cfgProblemTimeProcessRepository.findById(id).toDTO();
  }

  @Override
  public ResultInSideDto onInsert(CfgProblemTimeProcessDTO cfgTimeTroubleProcessListDTO)
      throws Exception {
    log.debug("Request to CfgProblemTimeProcessDTO onInsert: {}", cfgTimeTroubleProcessListDTO);
    String msg = onValidate(cfgTimeTroubleProcessListDTO);
    if (msg.length() > 0) {
      throw new Exception(msg);
    }
    cfgTimeTroubleProcessListDTO.setLastUpdateTime(new Date());
    return cfgProblemTimeProcessRepository.onInsert(cfgTimeTroubleProcessListDTO);
  }

  @Override
  public ResultInSideDto onDeleteList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess) {
    log.debug("Request to CfgProblemTimeProcessDTO onDeleteList: {}", lstCfgProblemTimeProcess);
    return cfgProblemTimeProcessRepository.onDeleteList(lstCfgProblemTimeProcess);
  }

  @Override
  public ResultInSideDto onUpdateList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess,
      CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) throws Exception {
    log.debug("Request to CfgProblemTimeProcessDTO onUpdateList: {}", cfgProblemTimeProcessDTO);
    String msg = onValidate(cfgProblemTimeProcessDTO);
    if (msg.length() > 0) {
      throw new Exception(msg);
    }
    cfgProblemTimeProcessDTO.setLastUpdateTime(new Date());
    return cfgProblemTimeProcessRepository
        .onUpdateList(lstCfgProblemTimeProcess, cfgProblemTimeProcessDTO);
  }

  @Override
  public String getSequence() throws Exception {
    log.debug("Request to getSequence: {}");
    return cfgProblemTimeProcessRepository.getSequence();
  }

  @Override
  public Datatable getListDataSearch(CfgProblemTimeProcessDTO dto) {
    log.debug("Request to getListDataSearch: {}", dto);
    Datatable datatable = cfgProblemTimeProcessRepository.getDataTableCfgProblemTimeProcessDTO(dto);
    List<CfgProblemTimeProcessDTO> datas = (List<CfgProblemTimeProcessDTO>) datatable.getData();
    Map<String, CatItemDTO> map = getMapAllCatItem("2");
    for (CfgProblemTimeProcessDTO item : datas) {
      if (map.get(item.getPriorityCode()) != null) {
        item.setPriorityName(map.get(item.getPriorityCode()).getItemName());
        item.setTypeName(map.get(item.getTypeCode()).getItemName());
      }
    }
    datatable.setData(datas);
    return datatable;
  }

  @Override
  public File exportData(CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) throws Exception {
    log.debug("Request to exportData: {}", cfgProblemTimeProcessDTO);
    List<CfgProblemTimeProcessDTO> lstExport = cfgProblemTimeProcessRepository
        .getDataExportCfgProblemTimeProcessDTO(cfgProblemTimeProcessDTO);
    Map<String, CatItemDTO> map = getMapAllCatItem("2");
    for (CfgProblemTimeProcessDTO cptpdto : lstExport) {
      if (map.get(cptpdto.getPriorityCode()) != null) {
        cptpdto.setPriorityName(map.get(cptpdto.getPriorityCode()).getItemName());
        cptpdto.setTypeName(map.get(cptpdto.getTypeCode()).getItemName());
      }
    }
    return exportFileEx(lstExport);
  }

  @Override
  public CfgProblemTimeProcessDTO getCfgProblemTimeProcessByDTO(CfgProblemTimeProcessDTO dto) {
    log.debug("Request to getCfgProblemTimeProcessByDTO: {}", dto);
    CfgProblemTimeProcessDTO cfg = cfgProblemTimeProcessRepository
        .getCfgProblemTimeProcessByDTO(dto);
    if (cfg != null && dto.getCreateDatePT() != null) {
      Long locationId = dto.getLocationId();
      CatLocationDTO nationItem = catLocationRepository.getNationByLocationId(locationId);
      String nation = null;
      if (nationItem != null && StringUtils.isNotNullOrEmpty(nationItem.getLocationCode())) {
        nation = nationItem.getLocationCode();
      }
      Date dateTime = dto.getCreateDatePT();
      Date rcaFoundTime = DateUtil.addMinute(dateTime,
          Math.round(Float.valueOf(cfg.getRcaFoundTime()) * 60));
      String count = catItemBusiness
          .countDayOff(DateUtil.dateToStringWithPattern(dateTime, "dd/MM/yyyy"),
              DateUtil.dateToStringWithPattern(rcaFoundTime, "dd/MM/yyyy"), nation);
      rcaFoundTime = DateUtil.addMinute(rcaFoundTime, Math.round(Float.valueOf(count) * 24 * 60));

      Date waFoundTime = DateUtil.addMinute(dateTime,
          Math.round(Float.valueOf(cfg.getWaFoundTime()) * 60));
      count = catItemBusiness.countDayOff(DateUtil.dateToStringWithPattern(dateTime, "dd/MM/yyyy"),
          DateUtil.dateToStringWithPattern(waFoundTime, "dd/MM/yyyy"), nation);
      waFoundTime = DateUtil.addMinute(waFoundTime, Math.round(Float.valueOf(count) * 24 * 60));

      Date slFoundTime = DateUtil.addMinute(dateTime,
          Math.round(Float.valueOf(cfg.getSlFoundTime()) * 60));
      count = catItemBusiness.countDayOff(DateUtil.dateToStringWithPattern(dateTime, "dd/MM/yyyy"),
          DateUtil.dateToStringWithPattern(slFoundTime, "dd/MM/yyyy"), nation);
      slFoundTime = DateUtil.addMinute(slFoundTime, Math.round(Float.valueOf(count) * 24 * 60));

      cfg.setRcaFoundTime(DateUtil.dbUpdateDateTime2String(rcaFoundTime));
      cfg.setWaFoundTime(DateUtil.dbUpdateDateTime2String(waFoundTime));
      cfg.setSlFoundTime(DateUtil.dbUpdateDateTime2String(slFoundTime));
    }
    return cfg;
  }

  public Map<String, CatItemDTO> getMapAllCatItem(String type) {
    Map<String, CatItemDTO> map = new HashMap<String, CatItemDTO>();
    try {
      List<CatItemDTO> lstCatItemDTO = catItemBusiness
          .getListCatItemDTOLE("", "", "", new CatItemDTO(), 0, 0, "asc", "position");
      if (lstCatItemDTO != null) {
        for (CatItemDTO catItemDTO : lstCatItemDTO) {
          if (type != null && "1".equals(type)) {
            map.put(String.valueOf(catItemDTO.getItemId()), catItemDTO);
          } else if (type != null && "2".equals(type)) {
            map.put(catItemDTO.getItemCode(), catItemDTO);
          } else {
            map.put(catItemDTO.getItemName(), catItemDTO);
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return map;
  }

  public String onValidate(CfgProblemTimeProcessDTO cfgDTO) {
    Double dRCAFoundTime = Double.valueOf(cfgDTO.getRcaFoundTime());
    Double dWAFoundTime = Double.valueOf(cfgDTO.getWaFoundTime());
    Double dSLFoundTime = Double.valueOf(cfgDTO.getSlFoundTime());
    if (dRCAFoundTime >= dWAFoundTime) {
      return I18n.getValidation("problem.cfgProblem.cfgRcaFoundTimeInvalidLogic");
    } else if (dWAFoundTime >= dSLFoundTime) {
      return I18n.getValidation("problem.cfgProblem.cfgSlFoundTimeInvalidLogic");
    }
    return "";
  }

  private File exportFileEx(List<CfgProblemTimeProcessDTO> lstData) throws Exception {
    String title = I18n.getLanguage("cfgProblemTimeProcess.title_export");
    String fileNameOut = "ProblemTimeConfigReport";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = renderHeaderSheet("cfgCode", "typeName",
        "priorityName", "rcaFoundTime", "waFoundTime", "slFoundTime", "lastUpdateTime");
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstData
        , I18n.getLanguage("cfgProblemTimeProcess.title_export")
        , title
        , null
        , 7
        , 3
        , 6
        , true
        , "language.cfgProblemTimeProcess"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("cfgProblemTimeProcess.export.firstLeftHeader")
        , I18n.getLanguage("cfgProblemTimeProcess.export.secondLeftHeader")
        , I18n.getLanguage("cfgProblemTimeProcess.export.firstRightHeader")
        , I18n.getLanguage("cfgProblemTimeProcess.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("cfgProblemTimeProcess.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);

    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }
}
