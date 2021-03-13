package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CfgTroubleCallIpccRepository;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgTroubleCallIpccBusinessImpl implements CfgTroubleCallIpccBusiness {

  @Autowired
  private CfgTroubleCallIpccRepository cfgTroubleCallIpccRepository;
  @Autowired
  private LogChangeConfigRepository logChangeConfigRepository;
  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListCfgTroubleCallIpccDTO(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    log.info("Request to getListCfgTroubleCallIpccDTO : {}", cfgTroubleCallIpccDTO);
    return cfgTroubleCallIpccRepository.getListCfgTroubleCallIpccDTO(cfgTroubleCallIpccDTO);
  }

  @Override
  public CfgTroubleCallIpccDTO getDetailById(Long id) {
    log.info("Request to getDetailById : {}", id);
    return cfgTroubleCallIpccRepository.getDetailById(id);
  }

  @Override
  public ResultInSideDto insertOrUpdateCfgTroubleCallIpcc(
      CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    log.info("Request to insertOrUpdateCfgTroubleCallIpcc : {}", cfgTroubleCallIpccDTO);
    ResultInSideDto resultInSideDto = cfgTroubleCallIpccRepository
        .insertOrUpdateCfgTroubleCallIpcc(cfgTroubleCallIpccDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      if (cfgTroubleCallIpccDTO.getId() != null && cfgTroubleCallIpccDTO.getId() > 0) {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update cfgTroubleCallIpcc",
            "Update cfgTroubleCallIpcc ID: " + resultInSideDto.getId(), cfgTroubleCallIpccDTO,
            null));
      } else {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Add cfgTroubleCallIpcc",
            "Add cfgTroubleCallIpcc ID: " + resultInSideDto.getId(), cfgTroubleCallIpccDTO,
            null));
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCfgTroubleCallIpcc(Long id) {
    log.info("Request to deleteCfgTroubleCallIpcc : {}", id);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (id != null && id > 0) {
      resultInSideDto = cfgTroubleCallIpccRepository.deleteCfgTroubleCallIpcc(id);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        UserToken userToken = ticketProvider.getUserToken();
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Delete CfgTroubleCallIpcc",
            "Delete CfgTroubleCallIpcc ID: " + id, null, null));
      }
    }
    return resultInSideDto;
  }
}
