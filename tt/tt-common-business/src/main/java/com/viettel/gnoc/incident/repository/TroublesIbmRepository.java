package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleFileIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.model.TroublesIbmEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TroublesIbmRepository {

  Datatable getListTroublesIbmDTO(TroublesIbmDTO troublesIbmDTO);

  ResultInSideDto insertTroublesIbm(TroublesIbmDTO troublesIbmDTO);

  ResultInSideDto insertTroubleFileIbm(TroubleFileIbmDTO troubleFileIbmDTO);

  TroublesIbmEntity findById(Long id);

  List<GnocFileDto> getGnocFileIBM(Long troubleIbmId);
}
