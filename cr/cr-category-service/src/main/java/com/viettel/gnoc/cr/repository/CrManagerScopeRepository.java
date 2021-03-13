package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrManagerScopeRepository {

  ResultInSideDto insertCrManagerScope(CrManagerScopeDTO crManagerScopeDTO);

  ResultInSideDto updateCrManagerScope(CrManagerScopeDTO crManagerScopeDTO);

  ResultInSideDto deleteCrManagerScope(Long id);

  ResultInSideDto deleteListCrManagerScope(List<CrManagerScopeDTO> groupUnitDetailListDTO);

  CrManagerScopeDTO findCrManagerScopeDTOById(Long id);

  ResultInSideDto updateLisCrManagerScope(List<Long> ids);

  Datatable getListCrManagerScopeSearch(CrManagerScopeDTO crManagerScopeDTO);
}
