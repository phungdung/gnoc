package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgMapUnitGnocNimsRepository {

  Datatable getListCfgMapUnitGnocNimsPage(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto delete(Long id);

  ResultInSideDto insertImport(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto insertList(List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList);

  Datatable getListDataExport(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto add(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto edit(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  CfgMapUnitGnocNimsDTO findById(Long id);

  CfgMapUnitGnocNimsDTO checkCfgMapUnitGnocNimsExist(String unitNimsCode, String unitGnocCode, Long businessCode);

}
