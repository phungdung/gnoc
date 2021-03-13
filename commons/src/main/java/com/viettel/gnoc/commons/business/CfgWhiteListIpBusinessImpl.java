package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CfgWhiteListIpRepository;
import com.viettel.gnoc.commons.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class CfgWhiteListIpBusinessImpl implements CfgWhiteListIpBusiness {

  @Autowired
  protected CfgWhiteListIpRepository cfgWhiteListIpRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Override
  public Datatable getListDataSearchWeb(CfgWhiteListIpDTO dto) {
    log.debug("Request to add");
    return cfgWhiteListIpRepository.getListDataSearchWeb(dto);
  }

  @Override
  public ResultInSideDto add(CfgWhiteListIpDTO coCfgWhiteListIpDTO) {
    log.debug("Request to add");
    ResultInSideDto resultInSideDto;
    resultInSideDto = cfgWhiteListIpRepository.add(coCfgWhiteListIpDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("UTILITY_CFG_WHITE_LIST_IP");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new CfgWhiteListIpDTO());
        //New Object History
        dataHistoryChange.setNewObject(coCfgWhiteListIpDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public CfgWhiteListIpDTO getDetailById(Long id) {
    log.debug("Request to get detail by id");
    return cfgWhiteListIpRepository.getDetailById(id);
  }

  @Override
  public ResultInSideDto edit(CfgWhiteListIpDTO coCfgWhiteListIpDTO) {
    log.debug("Request to edit");
    ResultInSideDto resultInSideDto;
    CfgWhiteListIpDTO oldHis = getDetailById(coCfgWhiteListIpDTO.getId());
    resultInSideDto = cfgWhiteListIpRepository.edit(coCfgWhiteListIpDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(coCfgWhiteListIpDTO.getId().toString());
        dataHistoryChange.setType("UTILITY_CFG_WHITE_LIST_IP");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(coCfgWhiteListIpDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete");
    ResultInSideDto resultInSideDto;
    CfgWhiteListIpDTO oldHis = getDetailById(id);
    resultInSideDto = cfgWhiteListIpRepository.delete(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        dataHistoryChange.setType("UTILITY_CFG_WHITE_LIST_IP");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new CfgWhiteListIpDTO());
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public CfgWhiteListIpDTO checkIpSystemName(String userName) {
    log.debug("Request to checkIpSystemName");
    return cfgWhiteListIpRepository.checkIpSystemName(userName);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CfgWhiteListIpDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end

}
