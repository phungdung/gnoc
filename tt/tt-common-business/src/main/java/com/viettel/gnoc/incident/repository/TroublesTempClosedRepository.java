package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author ITSOL
 */

@Repository
public interface TroublesTempClosedRepository {


  ResultInSideDto add(TroublesTempClosedDTO troublesTempClosedDTO);

  ResultInSideDto insertList(List<TroublesTempClosedDTO> troublesTempList);
}
