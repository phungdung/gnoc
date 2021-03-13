package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import java.text.ParseException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoHistoryBusinessImpl implements WoHistoryBusiness {

  @Autowired
  WoHistoryRepository woHistoryRepository;

  @Autowired
  WoRepository woRepository;

  @Override
  public List<WoHistoryDTO> getListWoHistoryBySystem(String username, String woId, String system,
      String systemId, String startDate, String endDate) throws ParseException {
    return woHistoryRepository
        .getListWoHistoryBySystem(username, woId, system, systemId, startDate, endDate);
  }

  @Override
  public ResultInSideDto insertWoHistory(WoHistoryInsideDTO woHistoryInsideDTO) {
    return woHistoryRepository.insertWoHistory(woHistoryInsideDTO);
  }


}
