package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import java.util.List;

public interface SRRoleActionRepository {

  Datatable getListSRRoleActionPage(SRRoleActionDTO srRoleActionDTO);

  List<SRRoleActionDTO> getListSRRoleActionDTO(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto add(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto update(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto deleteByRoleActionsId(Long roleActionId);

  ResultInSideDto deleteRoleAction(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto deleteRoleActionByFlowID(Long flowID);

  SRRoleActionDTO getDetail(Long roleActionId);

  List<SRRoleActionDTO> getListRoleActionsByFlowExecuteId(Long flowId);

  ResultInSideDto insertOrUpdate(SRRoleActionDTO srRoleActionDTO);

  ResultInSideDto insertOrUpdateList(List<SRRoleActionDTO> srRoleActionsDTOList);

  List<SRRoleActionDTO> getComboBoxStatus();

  List<SRRoleActionDTO> getComboBoxRoleType();

  List<SRRoleActionDTO> getComboBoxActions(String roleType);

  List<SRRoleActionDTO> getComboBoxGroupRole();
}
