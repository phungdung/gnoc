package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRStatusDTO;
import com.viettel.gnoc.sr.repository.SRStatusRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class SRStatusBusinessImpl implements SRStatusBusiness {

  @Autowired
  protected SRStatusRepository srStatusRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListSRStatusPage(SRStatusDTO srStatusDTO) {
    log.debug("Request to getListSRStatusPage : {}", srStatusDTO);
    return srStatusRepository.getListSRStatusPage(srStatusDTO);
  }

  @Override
  public ResultInSideDto insert(SRStatusDTO srStatusDTO) {
    log.debug("Request to insert : {}", srStatusDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srStatusDTO.setCreatedUser(userToken.getUserName());
    srStatusDTO.setCreatedTime(new Date(System.currentTimeMillis()));
    return srStatusRepository.add(srStatusDTO);
  }

  @Override
  public ResultInSideDto update(SRStatusDTO srStatusDTO) {
    log.debug("Request to update : {}", srStatusDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srStatusDTO.setUpdatedUser(userToken.getUserName());
    srStatusDTO.setUpdatedTime(new Date(System.currentTimeMillis()));
    return srStatusRepository.edit(srStatusDTO);
  }

  @Override
  public SRStatusDTO getDetail(Long configId) {
    log.debug("Request to update : {}", configId);
    return srStatusRepository.getDetail(configId);
  }
}




