package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class TroublesWorklogBusinessImpl implements TroublesWorklogBusiness {

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  TroubleWorklogRepository troubleWorklogRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto insertTroubleWorklog(TroubleWorklogEntity entity) {
    log.debug("Request to insertTroubleWorklog : {}", entity);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    entity.setCreateUserId(userToken.getUserID());
    entity.setCreateUserName(userToken.getUserName());
    entity.setCreateUnitName(unitToken.getUnitName());
    entity.setCreateUnitId(userToken.getDeptId());
    return troubleWorklogRepository.insertTroubleWorklog(entity);
  }

  @Override
  public Datatable getListTroubleWorklogByTroubleId(TroubleWorklogInsiteDTO troubleWorklogDTO) {
    log.debug("Request to getListTroubleWorklogByTroubleId : {}", troubleWorklogDTO);
    return troubleWorklogRepository.getListTroubleWorklogByTroubleId(troubleWorklogDTO);
  }
}
