package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import java.util.List;

public interface SRRoleRepository {

  Datatable getListSRRolePage(SRRoleDTO srRoleDTO);

  ResultInSideDto add(SRRoleDTO srRoleDTO);

  ResultInSideDto edit(SRRoleDTO srRoleDTO);

  ResultInSideDto delete(Long roleId);

  SRRoleDTO getDetail(Long roleId);

  Datatable getListDataExport(SRRoleDTO srRoleDTO);

  SRRoleDTO checkRoleExist(String country, String roleCode);

  List<SRRoleDTO> getListSRRoleByLocationCBB(SRRoleDTO srRoleDTO);

  ResultInSideDto insertOrUpdateList(List<SRRoleDTO> srRoleDTOList);

  List<SRRoleDTO> getListSRRoleDTO(SRRoleDTO srRoleDTO);

}
