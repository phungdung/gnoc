package com.viettel.gnoc.wo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.business.CfgFtOnTimeBusiness;
import com.viettel.gnoc.wo.business.CfgMapUnitGnocNimsBusiness;
import com.viettel.gnoc.wo.business.CfgSupportCaseBusiness;
import com.viettel.gnoc.wo.business.CfgWoHighTempBusiness;
import com.viettel.gnoc.wo.business.MapProvinceCdBusiness;
import com.viettel.gnoc.wo.business.WoCdGroupBusiness;
import com.viettel.gnoc.wo.business.WoConfigPropertyBusiness;
import com.viettel.gnoc.wo.business.WoMaterialBusiness;
import com.viettel.gnoc.wo.business.WoTypeBusiness;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.PT_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  protected WoConfigPropertyBusiness woConfigPropertyBusiness;

  @Autowired
  protected WoMaterialBusiness woMaterialBusiness;

  @Autowired
  protected WoTypeBusiness woTypeBusiness;

  @Autowired
  protected CfgMapUnitGnocNimsBusiness cfgMapUnitGnocNimsBusiness;

  @Autowired
  protected MapProvinceCdBusiness mapProvinceCdBusiness;

  @Autowired
  protected CfgSupportCaseBusiness cfgSupportCaseBusiness;

  @Autowired
  protected WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  CfgFtOnTimeBusiness cfgFtOnTimeBusiness;

  @Autowired
  CfgWoHighTempBusiness cfgWoHighTempBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file = null;
    switch (moduleName) {
      case "WO_CONFIG_PROPERTY":
        WoConfigPropertyDTO configPropertyDTO = mapper
            .readValue(formDataJson, WoConfigPropertyDTO.class);
        file = woConfigPropertyBusiness.exportData(configPropertyDTO);
        break;

      case "WO_MATERIAL_THRES":
        MaterialThresInsideDTO materialThresDTO = mapper
            .readValue(formDataJson, MaterialThresInsideDTO.class);
        file = woMaterialBusiness.exportData(materialThresDTO);
        break;
      case "WO_TYPE":
        WoTypeInsideDTO woTypeInsideDTO = mapper.readValue(formDataJson, WoTypeInsideDTO.class);
        file = woTypeBusiness.exportData(woTypeInsideDTO);
        break;
      case "WO_CFG_MAP_UNIT_GNOC_NIMS":
        CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO = mapper
            .readValue(formDataJson, CfgMapUnitGnocNimsDTO.class);
        file = cfgMapUnitGnocNimsBusiness.exportData(cfgMapUnitGnocNimsDTO);
        break;
      case "MAP_PROVINCE_CD":
        MapProvinceCdDTO mapProvinceCdDTO = mapper.readValue(formDataJson, MapProvinceCdDTO.class);
        file = mapProvinceCdBusiness.exportData(mapProvinceCdDTO);
        break;
      case "CFG_SUPPORT_CASE":
        CfgSupportCaseDTO cfgSupportCaseDTO = mapper
            .readValue(formDataJson, CfgSupportCaseDTO.class);
        file = cfgSupportCaseBusiness.exportData(cfgSupportCaseDTO);
        break;

      case "WO_CD_GROUP":
        WoCdGroupInsideDTO woCdGroupInsideDTO = mapper
            .readValue(formDataJson, WoCdGroupInsideDTO.class);
        file = woCdGroupBusiness.exportData(woCdGroupInsideDTO);
        break;

      case "WO_CD":
        WoCdDTO woCdDTO = mapper.readValue(formDataJson, WoCdDTO.class);
        file = woCdGroupBusiness.exportDataWoCd(woCdDTO);
        break;

      case "CFG_FT_ONTIME":
        CfgFtOnTimeDTO cfgFtOnTimeDTO = mapper.readValue(formDataJson, CfgFtOnTimeDTO.class);
        file = cfgFtOnTimeBusiness.exportSearchData(cfgFtOnTimeDTO);
        break;

      case "CFG_WO_HIGH_TEMP":
        CfgWoHighTempDTO cfgWoHighTempDTO = mapper.readValue(formDataJson, CfgWoHighTempDTO.class);
        file = cfgWoHighTempBusiness.exportData(cfgWoHighTempDTO);
        break;

      default:
        file = null;
        break;
    }
    if (file != null) {
      return FileUtils.responseSourceFromFile(file);
    }
    return null;
  }
}
