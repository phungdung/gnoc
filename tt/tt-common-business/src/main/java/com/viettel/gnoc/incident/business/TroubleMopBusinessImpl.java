package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import com.viettel.gnoc.incident.repository.TroubleMopRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class TroubleMopBusinessImpl implements TroubleMopBusiness {

  @Autowired
  TroubleMopRepository troubleMopRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto insertTroubleMop(TroubleMopInsiteDTO troubleMopDTO) {
    return troubleMopRepository.insertTroubleMop(troubleMopDTO);
  }

  @Override
  public Datatable getListTroubleMopDTO(TroubleMopInsiteDTO troubleMopDTO) {
    if (troubleMopDTO.getTroubleMopId() != null) {
      troubleMopDTO.setPage(1);
      troubleMopDTO.setPageSize(1);
    }
    return troubleMopRepository.getListTroubleMopDTO(troubleMopDTO);
  }

  @Override
  public TroubleMopInsiteDTO findById(Long id) {
    return troubleMopRepository.findById(id);
  }

  @Override
  public Datatable getListTroubleMopDtDTO(TroubleMopInsiteDTO troubleMopDTO) {
    TroubleMopDtInSideDTO troubleMopDtInSideDTO = new TroubleMopDtInSideDTO();
    troubleMopDtInSideDTO.setTroubleMopId(troubleMopDTO.getTroubleMopId());
    troubleMopDtInSideDTO.setPage(troubleMopDTO.getPage());
    troubleMopDtInSideDTO.setPageSize(troubleMopDTO.getPageSize());
    troubleMopDtInSideDTO.setSortType(troubleMopDTO.getSortType());
    troubleMopDtInSideDTO.setSortName(troubleMopDTO.getSortName());
    return troubleMopRepository.getListTroubleMopDtDTO(troubleMopDtInSideDTO);
  }

  @Override
  public ResultInSideDto updateTroubleMop(TroubleMopInsiteDTO troubleMopDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    troubleMopDTO.setStateMop(2l);
    troubleMopDTO.setIsRun(1l);
    UserToken userToken = ticketProvider.getUserToken();
    troubleMopDTO
        .setWorkLog((troubleMopDTO.getWorkLog() == null ? "" : troubleMopDTO.getWorkLog()) + "\n\r"
            + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + userToken.getUserName()
            + " run MOP");
    troubleMopDTO.setLastUpdateTime(null);
    resultInSideDto.setKey(troubleMopRepository.updateTroubleMop(troubleMopDTO));
    return resultInSideDto;
  }
}
