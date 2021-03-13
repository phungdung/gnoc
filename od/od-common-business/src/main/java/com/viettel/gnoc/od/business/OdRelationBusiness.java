package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import java.util.List;

/**
 * @author TungPV
 */
public interface OdRelationBusiness {


  ResultInSideDto insertLstRelation(OdDTO odDTO);

  ResultInSideDto insertOrUpdate(OdRelationDTO odDTO);

  List<OdRelationDTO> getRelationsByOdId(Long odId);
}
