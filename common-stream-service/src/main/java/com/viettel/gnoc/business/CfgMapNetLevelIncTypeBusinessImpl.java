package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.repository.CfgMapNetLevelIncTypeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgMapNetLevelIncTypeBusinessImpl implements CfgMapNetLevelIncTypeBusiness {

  @Autowired
  public CfgMapNetLevelIncTypeRepository cfgMapNetLevelIncTypeRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return cfgMapNetLevelIncTypeRepository.getListCfgMapNetLevelIncTypeDTO(cfgMapNetLevelIncTypeDTO,
        0, 20000, "asc", "id");
  }

  HashMap<Long, String> mapItemArrayIncident = new HashMap<Long, String>();
  HashMap<Long, String> mapItemNetWorkLevel = new HashMap<Long, String>();


  public void setMapNetwork() {
    mapItemArrayIncident.clear();
    mapItemNetWorkLevel.clear();

    // Mang
    List<String> lstCategory = new ArrayList<>();
    lstCategory.add(Constants.PROBLEM.PT_TYPE);
    List<CatItemDTO> lstArrayIncident = catItemBusiness
        .getListCatItemDTOByListCategoryCode(lstCategory);
    lstCategory.add(Constants.TROUBLE.NETWORK_LEVEL);
    List<CatItemDTO> lstNetworkLevel = catItemBusiness
        .getListCatItemDTOByListCategoryCode(lstCategory);

    if (lstArrayIncident != null && !lstArrayIncident.isEmpty()) {
      for (CatItemDTO itemDTO : lstArrayIncident) {
        mapItemArrayIncident.put(itemDTO.getItemId(), itemDTO.getItemName());
      }
    }

    if (lstNetworkLevel != null && !lstNetworkLevel.isEmpty()) {
      for (CatItemDTO itemDTO : lstNetworkLevel) {
        mapItemNetWorkLevel.put(itemDTO.getItemId(), itemDTO.getItemName());
      }
    }
  }

  @Override
  public Datatable getListCfgMapNetLevelIncTypeDatatable(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    Datatable datatable = cfgMapNetLevelIncTypeRepository
        .getListCfgMapNetLevelIncTypeDatatable(cfgMapNetLevelIncTypeDTO);
    List<CfgMapNetLevelIncTypeDTO> list = (List<CfgMapNetLevelIncTypeDTO>) datatable.getData();
    setMapNetwork();

    if (list != null && list.size() > 0) {
      for (CfgMapNetLevelIncTypeDTO item : list) {
        setParams(item);
      }
    }

    return datatable;
  }

  protected void setParams(CfgMapNetLevelIncTypeDTO item) {
    try {
      Long id = item.getTroubleTypeId();
      item.setTroubleTypeName(mapItemArrayIncident.get(id));
      String[] str = item.getNetworkLevelId().split(",");
      String net = "";
      for (int i = 0; i < str.length; i++) {
        for (Map.Entry<Long, String> entry : mapItemNetWorkLevel.entrySet()) {
          Long key = entry.getKey();
          String value = entry.getValue();

          // do what you have to do here
          // In your case, another loop.
          if (str[i].trim().equalsIgnoreCase(value)) {
            net += key + ",";
          }
        }
      }
      if (!StringUtils.isStringNullOrEmpty(net)) {
        net = net.substring(0, net.length() - 1);
      }
//      String temp = item.getNetworkLevelId();
//      if (!StringUtils.isStringNullOrEmpty(temp)) {
//        temp = temp.substring(0, temp.length() - 1);
//      }
      item.setNetworkLevelName(item.getNetworkLevelId());
      item.setNetworkLevelId(net);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public ResultInSideDto updateCfgMapNetLevelIncType(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    ResultInSideDto resultInSideDto = cfgMapNetLevelIncTypeRepository
        .updateCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();

      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CfgMapNetLevelIncType",
          "Update CfgMapNetLevelIncType" + " " + resultInSideDto.getId(),
          cfgMapNetLevelIncTypeDTO, null));
    }

    return resultInSideDto;

  }

  @Override
  public ResultInSideDto deleteCfgMapNetLevelIncType(Long id) {
    ResultInSideDto resultInSideDto = cfgMapNetLevelIncTypeRepository
        .deleteCfgMapNetLevelIncType(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();

      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CfgMapNetLevelIncType", "Delete CfgMapNetLevelIncType" + " " + id,
          null, null));
    }

    return resultInSideDto;
  }

  @Override
  public CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id) {
    setMapNetwork();
    CfgMapNetLevelIncTypeDTO dto = cfgMapNetLevelIncTypeRepository
        .findCfgMapNetLevelIncTypeById(id);
    if (dto != null) {
      setParams(dto);
    }

    return dto;
  }

  @Override
  public ResultInSideDto insertCfgMapNetLevelIncType(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    ResultInSideDto resultInSideDto = cfgMapNetLevelIncTypeRepository
        .insertCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();

      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert CfgMapNetLevelIncType",
          "Insert CfgMapNetLevelIncType" + " " + resultInSideDto.getId(),
          cfgMapNetLevelIncTypeDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public Map<?, ?> netWorkRiskList() {
    setMapNetwork();

    return mapItemArrayIncident;
  }

  @Override
  public Map<?, ?> netWorkLevelList() {
    setMapNetwork();

    return mapItemNetWorkLevel;
  }
}
