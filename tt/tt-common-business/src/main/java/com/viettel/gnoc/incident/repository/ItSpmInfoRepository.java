package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.model.ItSpmInfoEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ItSpmInfoRepository {

  ResultInSideDto insertItSpmInfo(ItSpmInfoEntity entity);

  ResultInSideDto delete(Long id);

  ResultInSideDto deleteItSpmInfoByIncidentId(Long incidentId);
}
