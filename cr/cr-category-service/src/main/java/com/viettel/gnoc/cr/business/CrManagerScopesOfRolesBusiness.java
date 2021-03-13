package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrManagerRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import java.io.File;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrManagerScopesOfRolesBusiness {

  Datatable getListManagerScopesOfRoles(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  ResultInSideDto addManagerScopesOfRoles(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  ResultInSideDto updateManagerScopesOfRoles(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  ResultInSideDto deleteManagerScopesOfRoles(Long crManagerScopesOfRolesId);

  ResultInSideDto deleteListManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO);

  File exportData(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) throws Exception;

  CrManagerScopesOfRolesDTO getDetail(Long cmsorsId);

  List<CrManagerScopeDTO> getListManagerScopeCBB();

  List<CrManagerRolesDTO> getListCrManagerRolesCBB();
}
