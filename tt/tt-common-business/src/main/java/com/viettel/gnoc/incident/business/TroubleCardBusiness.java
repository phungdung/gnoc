package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import com.viettel.gnoc.incident.dto.TroubleCardInsertDTO;
import java.util.List;

public interface TroubleCardBusiness {

  Datatable getListTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto updateTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto insertTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto insertOrUpdateTroubleCard(List<TroubleCardDTO> troubleCardDTOS);

  ResultInSideDto deleteTroubleCard(Long id);

  ResultInSideDto deleteListTroubleCard(List<TroubleCardDTO> troubleCardDTOS);

  List<TroubleCardDTO> getListTroubleCardDTOByTroubleId(Long TroubleId);

  ResultInSideDto insertListTroubleCard(TroubleCardInsertDTO troubleCardInsertDTOS);
}
