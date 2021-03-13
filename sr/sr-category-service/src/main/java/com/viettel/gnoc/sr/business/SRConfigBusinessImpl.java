package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class SRConfigBusinessImpl implements SRConfigBusiness {

  @Autowired
  protected SRConfigRepository srConfigRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListSRConfigPage(SRConfigDTO srConfigDTO) {
    log.debug("Request to getListSRConfigPage : {}", srConfigDTO);
    return srConfigRepository.getListSRConfigPage(srConfigDTO);
  }

  @Override
  public List<SRConfigDTO> getListConfigGroup(String parentCode) {
    log.debug("Request to getListConfigGroup : {}", parentCode);
    return srConfigRepository.getListConfigGroup(parentCode);
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(SRConfigDTO srConfigDTO) {
    log.debug("Request to getByConfigGroup : {}", srConfigDTO);
    return srConfigRepository.getByConfigGroup(srConfigDTO);
  }

  @Override
  public ResultInSideDto insert(SRConfigDTO srConfigDTO) {
    log.debug("Request to insert : {}", srConfigDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srConfigDTO.setCreatedUser(userToken.getUserName());
    srConfigDTO.setUpdatedUser(userToken.getUserName());
    srConfigDTO.setCreatedTime(DateUtil
        .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    srConfigDTO.setUpdatedTime(DateUtil
        .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    return srConfigRepository.add(srConfigDTO);
  }

  @Override
  public SRConfigDTO getDetail(Long configId) {
    log.debug("Request to getDetail : {}", configId);
    return srConfigRepository.getDetail(configId);
  }

  @Override
  public ResultInSideDto update(SRConfigDTO srConfigDTO) {
    log.debug("Request to update : {}", srConfigDTO);
    SRConfigDTO detail = getDetail(srConfigDTO.getConfigId());
    UserToken userToken = ticketProvider.getUserToken();
    srConfigDTO.setUpdatedUser(userToken.getUserName());
    srConfigDTO.setCreatedTime(detail.getCreatedTime());
    srConfigDTO.setCreatedUser(detail.getCreatedUser());
    srConfigDTO.setUpdatedTime(DateUtil
        .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    return srConfigRepository.edit(srConfigDTO);
  }

  @Override
  public ResultInSideDto delete(Long configId) {
    log.debug("Request to delete : {}", configId);
    return srConfigRepository.delete(configId);
  }

}




