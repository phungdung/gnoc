package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrRolesDTO;

/**
 * @author DungPV
 */
public interface CrRolesRepository {

  BaseDto sqlSearch(CrRolesDTO crRolesDTO);

  Datatable getListCrRoles(CrRolesDTO crRolesDTO);

  CrRolesDTO getDetail(Long cmreId);

  ResultInSideDto deleteCrRoles(Long id);

  ResultInSideDto addOrEdit(CrRolesDTO crRolesDTO);
}
