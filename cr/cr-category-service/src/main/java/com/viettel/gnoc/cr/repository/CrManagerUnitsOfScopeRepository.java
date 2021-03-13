package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrManagerUnitsOfScopeRepository {

  BaseDto sqlSearch(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  Datatable getListCrManagerUnitsOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  CrManagerUnitsOfScopeDTO getDetail(Long cmnoseId);

  ResultInSideDto deleteCrManagerUnitsOfScope(Long id);

  Datatable getListDataExport(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  ResultInSideDto add(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  ResultInSideDto edit(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO);

  List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(Long impactSegmentId);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();
}
