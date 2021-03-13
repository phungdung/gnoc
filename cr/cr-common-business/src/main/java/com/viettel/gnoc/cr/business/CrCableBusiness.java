package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import java.util.List;

public interface CrCableBusiness {

  Datatable GNOC_getInfraCableLane(CrCableDTO crCableDTO);

  Datatable getAllCableInLane(CrCableDTO crCableDTO);

  List<InfraSleevesBO> getSleevesInCable(String sleeveCode, Long purpose, String cableCode);

  Datatable getLinkInfo(CrCableDTO crCableDTO);

  List<CrCableDTO> getListCrCableByCondition(CrInsiteDTO crInsiteDTO);

  List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
