package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import java.util.List;

public interface SRRoleActionBusiness {

  Datatable getListSRRoleActionPage(SRRoleActionDTO srRoleActionDTO);

  List<SRRoleActionDTO> getListSRRoleActionDTO(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto insert(SRFlowExecuteDTO srFlowExecuteDTO);

  ResultInSideDto update(SRFlowExecuteDTO srFlowExecuteDTO);

  ResultInSideDto deleteByRoleActionsId(Long roleActionId);

  SRRoleActionDTO getDetail(Long roleActionId);

  List<SRRoleActionDTO> getComboBoxStatus();

  List<SRRoleActionDTO> getComboBoxRoleType();

  List<SRRoleActionDTO> getComboBoxActions(String roleType);

  List<SRRoleActionDTO> getComboBoxGroupRole();
}
