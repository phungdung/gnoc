package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import java.io.File;
import java.util.List;

/**
 * @author TungPV
 */
public interface OdCommonBusiness {

  Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO);

  Integer getCountListDataSearchForOther(OdSearchInsideDTO odDTO);

  List<OdSearchInsideDTO> getListDataSearchForOther(OdSearchInsideDTO odDTOSearch);

  List<OdSearchInsideDTO> getListDataSearchVsmart(OdSearchInsideDTO odDTOSearch);

  ResultDTO insertOdFromVsmart(List<ObjKeyValueVsmartDTO> o, String userName,
      String odTypeCode, String woId, String insertSource, String createUnitCode, String crateUnitId) throws Exception;

  ResultDTO insertOdFromOtherSystem(OdSearchInsideDTO o) throws Exception;

  File exportData(OdSearchInsideDTO odSearchInsideDTO) throws Exception;

}
