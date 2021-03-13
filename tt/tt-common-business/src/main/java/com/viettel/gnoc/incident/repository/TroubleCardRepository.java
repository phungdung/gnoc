package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleCardRepository {

  Datatable getListTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto updateTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto insertTroubleCardDTO(TroubleCardDTO troubleCardDTO);

  ResultInSideDto insertOrUpdateTroubleCard(List<TroubleCardDTO> troubleCardDTOS);

  ResultInSideDto deleteTroubleCard(Long id);

  ResultInSideDto deleteListTroubleCard(List<TroubleCardDTO> troubleCardDTOS);

  List<TroubleCardDTO> getListTroubleCardDTOByTroubleId(Long TroubleId);
}
