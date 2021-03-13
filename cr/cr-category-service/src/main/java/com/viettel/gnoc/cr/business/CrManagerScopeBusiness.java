package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import java.io.File;
import java.util.List;

public interface CrManagerScopeBusiness {

  ResultInSideDto insertCrManagerScope(CrManagerScopeDTO crManagerScopeDTO);

  ResultInSideDto updateCrManagerScope(CrManagerScopeDTO crManagerScopeDTO);

  ResultInSideDto deleteCrManagerScope(Long id);

  ResultInSideDto deleteListCrManagerScope(List<CrManagerScopeDTO> crManagerScopeDTOS);

  CrManagerScopeDTO findCrManagerScopeDTOById(Long id);

  ResultInSideDto updateLisCrManagerScope(List<CrManagerScopeDTO> crManagerScopeDTOS);

  Datatable getListCrManagerScopeSearch(CrManagerScopeDTO crManagerScopeDTO);

  File exportData(CrManagerScopeDTO crManagerScopeDTO) throws Exception;
}
