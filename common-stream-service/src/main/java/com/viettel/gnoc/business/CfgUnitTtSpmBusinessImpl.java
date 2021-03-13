package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.repository.CfgUnitTtSpmRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgUnitTtSpmBusinessImpl implements CfgUnitTtSpmBusiness {

  @Autowired
  CfgUnitTtSpmRepository cfgUnitTtSpmRepository;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public String updateCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    CfgUnitTtSpmDTO oldHis = findCfgUnitTtSpmById(Long.parseLong(cfgUnitTtSpmDTO.getId()));
    String result = cfgUnitTtSpmRepository.updateCfgUnitTtSpm(cfgUnitTtSpmDTO);
    if (result.equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(cfgUnitTtSpmDTO.getId());
        dataHistoryChange.setType("TT_CONFIG_RECEIVE_UNIT");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(cfgUnitTtSpmDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return result;
  }

  @Override
  public String deleteCfgUnitTtSpm(Long id) {
    CfgUnitTtSpmDTO oldHis = findCfgUnitTtSpmById(id);
    String result = cfgUnitTtSpmRepository.deleteCfgUnitTtSpm(id);
    if (result.equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        dataHistoryChange.setType("TT_CONFIG_RECEIVE_UNIT");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new CfgUnitTtSpmDTO());
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return result;
  }

  @Override
  public String deleteListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmListDTO) {
    return cfgUnitTtSpmRepository.deleteListCfgUnitTtSpm(cfgUnitTtSpmListDTO);
  }

  @Override
  public List<CfgUnitTtSpmDTO> getListCfgUnitTtSpmDTO(CfgUnitTtSpmDTO cfgUnitTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return cfgUnitTtSpmRepository
        .getListCfgUnitTtSpmDTO(cfgUnitTtSpmDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto insertCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    ResultInSideDto resultInSideDto = cfgUnitTtSpmRepository.insertCfgUnitTtSpm(cfgUnitTtSpmDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("TT_CONFIG_RECEIVE_UNIT");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new CfgUnitTtSpmDTO());
        //New Object History
        dataHistoryChange.setNewObject(cfgUnitTtSpmDTO);
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
  public String insertOrUpdateListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmDTO) {
    return cfgUnitTtSpmRepository.insertOrUpdateListCfgUnitTtSpm(cfgUnitTtSpmDTO);
  }

  @Override
  public List<String> getSequenseCfgUnitTtSpm(String seqName, int... size) {
    return cfgUnitTtSpmRepository.getSequenseCfgUnitTtSpm(seqName, size);
  }

  @Override
  public CfgUnitTtSpmDTO findCfgUnitTtSpmById(Long id) {
    return cfgUnitTtSpmRepository.findCfgUnitTtSpmById(id);
  }

  @Override
  public Datatable getListUnitTtSpmSearch(CfgUnitTtSpmSearchDTO cfgUnitTtSpmSearchDTO) {
    return cfgUnitTtSpmRepository.getListUnitTtSpmSearch(cfgUnitTtSpmSearchDTO);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CfgUnitTtSpmDTO.class.getDeclaredFields();
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


