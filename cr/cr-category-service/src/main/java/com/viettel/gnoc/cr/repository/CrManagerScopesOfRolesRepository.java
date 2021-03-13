package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrManagerRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrManagerScopesOfRolesRepository {

  BaseDto sqlSearch(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  Datatable getListManagerScopesOfRoles(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  ResultInSideDto addOrEdit(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  ResultInSideDto deleteManagerScopesOfRoles(Long cmsorsId);

  List<CrManagerScopesOfRolesDTO> getListDataExport(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  CrManagerScopesOfRolesDTO getDetail(Long crId);

  List<CrManagerScopeDTO> getListManagerScopeCBB();

  List<CrManagerRolesDTO> getListCrManagerRolesCBB();
}
