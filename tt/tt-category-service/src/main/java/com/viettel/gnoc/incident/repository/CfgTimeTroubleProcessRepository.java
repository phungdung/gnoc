package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTimeTroubleProcessRepository {

  Datatable getListCfgTimeTroubleProcessDTO(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  CfgTimeTroubleProcessDTO findCfgTimeTroubleProcessById(Long id);

  ResultInSideDto insertCfgTimeTroubleProcess(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  ResultInSideDto updateCfgTimeTroubleProcess(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  ResultInSideDto insertOrUpdateListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO);

  ResultInSideDto deleteCfgTimeTroubleProcess(Long id);

  List<String> getSequenseCfgTimeTroubleProcess(String seqName, int... size);

  List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessExport(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  List<CatItemDTO> getListSubCategory(Long typeId);

  CfgTimeTroubleProcessDTO checkCfgTimeTroubleProcessExit(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(Long typeId, Long subCategoryId,
      Long priority, String country);

  List<CatItemDTO> getItemDTO(String typeId, String alarmId, String nationCode);
}
