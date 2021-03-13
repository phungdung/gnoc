package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.io.File;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrManagerUnitsOfScopeBusiness {

  Datatable getListUnitOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  CrManagerUnitsOfScopeDTO getDetail(Long cmnoseId);

  ResultInSideDto deleteCrManagerUnitsOfScope(Long cmnoseId);

  ResultInSideDto deleteListCrManagerUnitsOfScope(
      CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  File exportData(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) throws Exception;

  ResultInSideDto addCrManagerUnitsOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  ResultInSideDto updateCrManagerUnitsOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(Long impactSegmentId);

  List<UnitDTO> findListUnitByCodeOrName(UnitDTO unitDTO);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();
}
