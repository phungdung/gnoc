package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.InfraSleevesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmissionInfoRepository {

  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  List<CatReasonInSideDTO> getListReasonSearch(CatReasonInSideDTO reasonDto);

  Datatable onSearchTroubleByLineCutCode(TroublesInSideDTO troublesDTO);

  Datatable onSearchInfraCableLaneDTO(InfraCableLaneDTO infraCableLaneDTO);

  String updateTroubles1(TroublesEntity troublesEntity);

  Datatable getInfraSleevesBO(InfraSleevesDTO infraSleevesDTO);
}
