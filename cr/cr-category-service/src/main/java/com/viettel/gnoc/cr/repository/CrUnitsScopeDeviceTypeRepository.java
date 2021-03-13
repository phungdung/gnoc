package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrUnitsScopeDeviceTypeDTO;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrUnitsScopeDeviceTypeRepository {

  ResultInSideDto addCrUnitsScopeDeviceType(CrUnitsScopeDeviceTypeDTO crUnitsScopeDeviceTypeDTO);

  ResultInSideDto delete(Long crUnitsScopeDeviceTypeId);

  ResultInSideDto deleteListUnitsScopeDeviceType(List<Long> lstUnitsScopeDeviceTypeId);
}
