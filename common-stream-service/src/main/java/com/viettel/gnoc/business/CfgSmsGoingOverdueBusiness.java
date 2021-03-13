package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueFullDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

public interface CfgSmsGoingOverdueBusiness {

  Datatable getListCfgSmsGoingOverdueDTO(
      CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO);

  String updateCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO);

  String deleteCfgSmsGoingOverdue(Long id);

  String deleteListCfgSmsGoingOverdue(List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueListDTO);

  CfgSmsGoingOverdueDTO findCfgSmsGoingOverdueById(Long id);

  ResultInSideDto insertCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO);

  List<String> getSequenseCfgSmsGoingOverdue(String seqName, int... size);

  List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  UsersInsideDto getUserInfo(Long userId);

  String insertOrUpdateCfg(List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS);

  ResultInSideDto deleteCfgSmsGoingOverdueAndUserList(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO);

  List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueDTO_allFields(String cfgName, String unitId,
      String userId, String levelId);

  Datatable getListCfgSmsUser(CfgSmsUserGoingOverdueFullDTO cfgSmsUserGoingOverdueFullDTO);

  ResultInSideDto updateCfgSmsGoingOverdue2(CfgSmsGoingOverdueDTO dto);

  Long getMaxLevelIDByUnitID(CfgSmsGoingOverdueDTO dto);
}
