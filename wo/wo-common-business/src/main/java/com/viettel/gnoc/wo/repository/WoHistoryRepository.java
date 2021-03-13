package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import java.text.ParseException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoHistoryRepository {

  ResultInSideDto insertWoHistory(WoHistoryInsideDTO woHistoryInsideDTO);

  ResultInSideDto updateWoHistory(WoHistoryInsideDTO woHistoryInsideDTO);

  Datatable getListWoHistoryByWoId(WoHistoryInsideDTO woHistoryInsideDTO);

  List<WoHistoryInsideDTO> getListWoHistoryDTO(WoHistoryInsideDTO woHistoryInsideDTO);

  List<WoHistoryInsideDTO> getListDataByWoId(Long woId, Double offsetFromUser);

  List<WoHistoryDTO> getListWoHistoryBySystem(String username, String woId, String system,
      String systemId//
      , String startDate, String endDate) throws ParseException;
}
