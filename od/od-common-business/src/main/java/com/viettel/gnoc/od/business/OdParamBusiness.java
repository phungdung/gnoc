package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdParamDTO;
import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import java.util.List;

/**
 * @author TungPV
 */
public interface OdParamBusiness {

  List<OdParamInsideDTO> findAll();

  List<OdParamDTO> getListOdParamByOdId(Long id);

  ResultInSideDto add(OdParamInsideDTO odParamInsideDTO);
}
