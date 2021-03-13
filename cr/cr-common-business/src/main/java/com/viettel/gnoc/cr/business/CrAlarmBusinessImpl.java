package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.incident.provider.WSIIMPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrAlarmBusinessImpl implements CrAlarmBusiness {

  private Map<String, String> nationMap = new HashMap<>();
  private final static String CR_ALARM_EXPORT = "NETWORK_NODE_EXPORT";
  @Value("${application.temp.folder}")
  private String tempFolder;
  @Value("${application.conf.iim_nationCodes}")
  private String nationCodeList;

  @Autowired
  CrAlarmRepository crAlarmRepository;

  @Autowired
  WSNocprov4Port wsNocprov4Port;

  @Autowired
  WSNIMSInfraPort wsnimsInfraPort;

  @Autowired
  WSIIMPort wsiimPort;

  @Override
  public List<CrAlarmInsiteDTO> getListAlarmByCr(CrInsiteDTO crInsiteDTO) {
    return crAlarmRepository.getListAlarmByCr(crInsiteDTO);
  }

  @Override
  public HashSet<String> getListFaultSrc(String nationCode) throws Exception {
    log.info("Request to getListFaultSrc : {}", nationCode);
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = wsNocprov4Port.getFautGroupInfo(nationCode);
    HashSet<String> faultSrcList = new HashSet<>();
    if (lstFaultGroupDTO == null || lstFaultGroupDTO.isEmpty()) {
      return null;
    }
    for (CrAlarmFaultGroupDTO data : lstFaultGroupDTO) {
      if (data.getFault_src() == null || data.getFault_src().trim().isEmpty()) {
        continue;
      }
      String faultSrc = data.getFault_src().trim();
      if (!faultSrcList.contains(faultSrc)) {
        faultSrcList.add(faultSrc);
      }
    }
    return faultSrcList;
  }

  @Override
  public List<CrAlarmFaultGroupDTO> getListGroupFaultSrc(String faultSrc, String nationCode)
      throws Exception {
    log.info("Request to getListGroupFaultSrc : {}");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = wsNocprov4Port.getFautGroupInfo(nationCode);
    HashMap<String, List<CrAlarmFaultGroupDTO>> groupMap = new HashMap<>();
    HashSet<String> faultSrcList = new HashSet<>();
    if (lstFaultGroupDTO == null || lstFaultGroupDTO.isEmpty()) {
      return null;
    }
    for (CrAlarmFaultGroupDTO data : lstFaultGroupDTO) {
      if (data.getFault_src() == null || data.getFault_src().trim().isEmpty()) {
        continue;
      }
      String fault = data.getFault_src().trim();
      if (faultSrcList.contains(fault)) {
        groupMap.get(fault).add(data);
      } else {
        faultSrcList.add(fault);
        List<CrAlarmFaultGroupDTO> lst = new ArrayList<>();
        lst.add(data);
        groupMap.put(fault, lst);
      }
    }
    return groupMap.get(faultSrc);
  }

  @Override
  public Datatable getAlarmList(CrAlarmInsiteDTO crAlarmDTO) throws Exception {
    log.info("Request to getAlarmList : {}", crAlarmDTO);
    Datatable datatable = new Datatable();
    List<CrAlarmInsiteDTO> lstAlarm = new ArrayList<>();
    int page = crAlarmDTO.getPage();
    int size = crAlarmDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crAlarmDTO.setSortName(null);
    crAlarmDTO.setSortType(null);
    crAlarmDTO.setPage(1);
    crAlarmDTO.setPageSize(Integer.MAX_VALUE);
    CrAlarmInsiteDTO dto = new CrAlarmInsiteDTO();
    dto.setFaultSrc(crAlarmDTO.getFaultSrc());
    dto.setFaultName(crAlarmDTO.getFaultName());
    dto.setFaultGroupId(crAlarmDTO.getFaultGroupId());
    dto.setNationCode(crAlarmDTO.getNationCode());
    dto.setNumberOccurences(crAlarmDTO.getNumberOccurences());
    dto.setProcessId(crAlarmDTO.getProcessId());
    try {
      lstAlarm = wsNocprov4Port.getAlarmList(dto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstAlarm != null && lstAlarm.size() > 0) {
      int totalSize = lstAlarm.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
      List<CrAlarmInsiteDTO> crSubList = (List<CrAlarmInsiteDTO>) DataUtil
          .subPageList(lstAlarm, page, size);
      crSubList.get(0).setPage(page);
      crSubList.get(0).setPageSize(pageSize);
      datatable.setData(crSubList);
    }
    return datatable;
  }

  @Override
  public List<CrAlarmSettingDTO> getAlarmSettingByVendor(CrAlarmSettingDTO crAlarmSettingDTO) {
    return crAlarmRepository.getAlarmSettingByVendor(crAlarmSettingDTO);
  }

  @Override
  public List<CrAlarmSettingDTO> getAlarmSettingByModule(CrAlarmSettingDTO crAlarmSettingDTO) {
    return crAlarmRepository.getAlarmSettingByModule(crAlarmSettingDTO);
  }

  @Override
  public List<CrAlarmSettingDTO> getListAlarmSettingByModule(
      List<CrAlarmSettingDTO> crAlarmSettingDTOS) {
    List<CrAlarmSettingDTO> results = new ArrayList<>();
    if (crAlarmSettingDTOS != null && crAlarmSettingDTOS.size() > 0) {
      crAlarmSettingDTOS.forEach(item -> {
        List<CrAlarmSettingDTO> lst = getAlarmSettingByModule(item);
        if (lst != null && lst.size() > 0) {
          results.addAll(lst);
        }
      });
    }
    return results;
  }

  @Override
  public File exportData(List<Long> lstCasId) {
    List<CrAlarmSettingDTO> lst = crAlarmRepository.getListAlarm(lstCasId);
    return exportFileEx(lst);
  }

  @Override
  public Datatable getVendorList(CrAlarmSettingDTO crAlarmSettingDTO) {
    Datatable datatable = new Datatable();
    int page = crAlarmSettingDTO.getPage();
    int size = crAlarmSettingDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crAlarmSettingDTO.setSortName(null);
    crAlarmSettingDTO.setSortType(null);
    crAlarmSettingDTO.setPage(1);
    crAlarmSettingDTO.setPageSize(Integer.MAX_VALUE);
    List<CatVendorBO> lstVendor = new ArrayList<>();
    try {
      lstVendor = wsnimsInfraPort
          .getVendorList(crAlarmSettingDTO.getVendorCode(),
              crAlarmSettingDTO.getVendorName());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstVendor != null && lstVendor.size() > 0) {
      int totalSize = lstVendor.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
      List<CatVendorBO> crSubList = (List<CatVendorBO>) DataUtil
          .subPageList(lstVendor, page, size);
      datatable.setData(crSubList);
    }
    return datatable;
  }

  @Override
  public Map<String, String> nationMap() {
    String[] nationArr = nationCodeList.split(";");
    for (String data : nationArr) {
      String[] info = data.split(":");
      if (info.length != 2) {
        continue;
      }
      nationMap.put(info[1].trim(), info[0].trim());
    }
    return nationMap;
  }

  @Override
  public Datatable setupModuleData(CrModuleDraftDTO crModuleDraftDTO) {
    Datatable datatable = new Datatable();
    int page = crModuleDraftDTO.getPage();
    int size = crModuleDraftDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crModuleDraftDTO.setSortName(null);
    crModuleDraftDTO.setSortType(null);
    crModuleDraftDTO.setPage(1);
    crModuleDraftDTO.setPageSize(Integer.MAX_VALUE);
    List<CrModuleDraftDTO> resultList = new ArrayList<>();
    try {
      resultList = wsiimPort
          .getIIMModules(crModuleDraftDTO.getSERVICE_CODE(), null,
              crModuleDraftDTO.getMODULE_CODE(), null, crModuleDraftDTO.getNationCode(),
              nationMap.get(crModuleDraftDTO.getNationCode()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (resultList != null && resultList.size() > 0) {
      int totalSize = resultList.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
      List<CrModuleDraftDTO> crSubList = (List<CrModuleDraftDTO>) DataUtil
          .subPageList(resultList, page, size);
      crSubList.get(0).setPage(page);
      crSubList.get(0).setPageSize(pageSize);
      datatable.setData(crSubList);
    }
    return datatable;
  }

  @Override
  public List<CrModuleDetailDTO> getListModuleByCr(CrInsiteDTO crInsiteDTO) {
    return crAlarmRepository.getListModuleByCr(crInsiteDTO);
  }

  @Override
  public List<CrVendorDetailDTO> getListVendorByCr(CrInsiteDTO crInsiteDTO) {
    return crAlarmRepository.getListVendorByCr(crInsiteDTO);
  }

  private File exportFileEx(List<CrAlarmSettingDTO> lstData) {
    String title = I18n.getLanguage("crAlarm.export.title");
    String fileNameOut = CR_ALARM_EXPORT;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String subTitle = I18n
        .getLanguage("crAlarm.export.exportDate", dateFormat.format(new Date()));
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = readerHeaderSheet(
        "faultSrc", "faultId", "faultName", "faultLevelCode",
        "deviceTypeCode", "vendorCode", "moduleCode", "nationCode");
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("crAlarm.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.crAlarm",
        lstHeaderSheet,
        filedSplit,
        ""
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = null;
    try {
      fileExport = CommonExport.exportExcel(
          fileTemplate
          , fileNameOut
          , fileExports
          , rootPath
          , new String[]{}
      );
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return fileExport;
  }

  @Override
  public Datatable getAlarmSetting(CrAlarmSettingDTO alarmSettingDTO) {
    return crAlarmRepository
        .getAlarmSetting(alarmSettingDTO);
  }

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public List<CrAlarmDTO> getListAlarmByCr(CrDTO crDto) {
    return crAlarmRepository.getListObjectByCr(crDto);
  }

  @Override
  public File exportDataNew(List<CrAlarmSettingDTO> lstCrAlarms) {
    return lstCrAlarms == null ? null : exportFileEx(lstCrAlarms);
  }
}
