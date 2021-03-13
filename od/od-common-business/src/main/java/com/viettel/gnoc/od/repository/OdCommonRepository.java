package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdCommonRepository {

  // OdTypeDTO getDetailUserByUsername(String username);

  @SuppressWarnings("unchecked")
  Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO);

  List<OdSearchInsideDTO> getListDataExport(OdSearchInsideDTO odSearchInsideDTO);

  OdTypeDTO checkOdTypeExist(String odTypeCode);

  UnitEntity getUnitByUnitCode(String unitCode);

  UnitEntity getUnitCodeMapNims(String unitNimsCode);

//  Map<String, String> getConfigProperty() throws Exception;

  String getSeqTableOD(String sequense);

}
