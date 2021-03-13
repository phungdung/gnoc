package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdParamDTO;
import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdParamRepository {

  @SuppressWarnings("unchecked")
  List<OdParamInsideDTO> findAll();

  List<OdParamDTO> getListOdParamByOdId(Long id);

  ResultInSideDto add(OdParamInsideDTO odParamInsideDTO);
}
