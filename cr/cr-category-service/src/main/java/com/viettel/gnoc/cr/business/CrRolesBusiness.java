package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrRolesDTO;

/**
 * @author DungPV
 */
public interface CrRolesBusiness {

  Datatable getListCrRoles(CrRolesDTO crRolesDTO);

  CrRolesDTO getDetail(Long cmreId);

  ResultInSideDto deleteCrRoles(Long cmreId);

  ResultInSideDto deleteListCrRoles(CrRolesDTO crRolesDTO);

  ResultInSideDto addCrRoles(CrRolesDTO crRolesDTO);

  ResultInSideDto updateCrRoles(CrRolesDTO crRolesDTO);
}
