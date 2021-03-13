package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgTimeTroubleProcessBusiness {

  Datatable getListCfgTimeTroubleProcessDTO(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  CfgTimeTroubleProcessDTO findCfgTimeTroubleProcessById(Long id);

  ResultInSideDto insertCfgTimeTroubleProcess(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  ResultInSideDto updateCfgTimeTroubleProcess(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  ResultInSideDto insertOrUpdateListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO);

  ResultInSideDto deleteCfgTimeTroubleProcess(Long id);

  ResultInSideDto deleteListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO);

  List<String> getSequenseCfgTimeTroubleProcess(int... size);

  List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  File exportData(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) throws Exception;

  File getTemplate() throws Exception;

  List<CatItemDTO> getListSubCategory(Long typeId);

  ResultInSideDto importData(MultipartFile fileImport);

  List<CatItemDTO> getItemDTO(String typeId, String alarmId, String nationCode);
}
