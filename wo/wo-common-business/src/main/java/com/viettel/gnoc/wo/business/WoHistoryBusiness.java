package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import java.text.ParseException;
import java.util.List;

public interface WoHistoryBusiness {

  List<WoHistoryDTO> getListWoHistoryBySystem(String username, String woId, String system,
      String systemId, String startDate, String endDate) throws ParseException;

  ResultInSideDto insertWoHistory(WoHistoryInsideDTO woHistoryInsideDTO);

}
