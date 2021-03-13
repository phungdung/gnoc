package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.util.List;


public interface SRRoleUserRepository {

  Datatable getListSRRoleUserPage(SRRoleUserInSideDTO srRoleUserDTO);

  List<SRRoleUserInSideDTO> getListSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO);

  List<SRRoleUserInSideDTO> getListSRRoleUserByUnitId(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto add(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto edit(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto delete(Long roleUserId);

  SRRoleUserInSideDTO getDetail(Long roleUserId);

  ResultInSideDto insertOrUpdate(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto insertOrUpdateList(List<SRRoleUserInSideDTO> srRoleUserDTOList);

  Datatable getListDataExport(SRRoleUserInSideDTO srRoleUserDTO);

  SRRoleUserInSideDTO checkRoleUserExist(String country, String roleCode, String username,
      Long unitId);

  SRRoleUserInSideDTO checkRoleUserExistByUnitId(SRRoleUserInSideDTO srRoleUserDTO);

  List<SRRoleUserInSideDTO> getListUser(String unitId, String country, String username);
}
