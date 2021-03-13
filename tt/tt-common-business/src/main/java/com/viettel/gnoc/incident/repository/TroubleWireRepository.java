package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleWireDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleWireRepository {

  ResultInSideDto insertTroubleWire(TroubleWireDTO troubleWireDTO);
}
