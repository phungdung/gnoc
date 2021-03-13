package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface CfgInfoTtSpmRepository {

  List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmDTO(CfgInfoTtSpmDTO cfgInfoTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);


  String updateCfgInfoTtSpm(CfgInfoTtSpmDTO dto);

  String deleteCfgInfoTtSpm(Long id);

  String deleteListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> dto);

  CfgInfoTtSpmDTO findCfgInfoTtSpmById(Long id);

  ResultInSideDto insertCfgInfoTtSpm(CfgInfoTtSpmDTO cfgInfoTtSpmDTO);

  String insertOrUpdateListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS);

  List<String> getSequenseCfgInfoTtSpm(String seqName, int[] i);

  List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  Datatable getListCfgInfoTtSpmDTO2(CfgInfoTtSpmDTO cfgInfoTtSpmDTO);
}
