package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TTCfgBusinessDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusRoleDTO;
import com.viettel.gnoc.incident.repository.TTCfgBusinessRepository;
import com.viettel.gnoc.incident.repository.TTChangeStatusRepository;
import com.viettel.gnoc.incident.repository.TTChangeStatusRoleRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Service
@Transactional
public class TTChangeStatusBusinessImpl implements TTChangeStatusBusiness {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  private TTChangeStatusRepository ttChangeStatusRepository;

  @Autowired
  private TTCfgBusinessRepository ttCfgBusinessRepository;

  @Autowired
  private TTChangeStatusRoleRepository ttChangeStatusRoleRepository;

  @Autowired
  private CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  private TicketProvider ticketProvider;

  @Override
  public Datatable getListTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO) {
    log.debug("Request to getListTTChangeStatus : {}", ttChangeStatusDTO);
    return ttChangeStatusRepository.getListTTChangeStatus(ttChangeStatusDTO);
  }

  @Override
  public TTChangeStatusDTO getDetailCfg(TTChangeStatusDTO ttChangeStatusDTO) {
    log.debug("Request to getDetailCfg : {}", ttChangeStatusDTO);
    TTChangeStatusDTO resultDTO = ttChangeStatusRepository
        .findTTChangeStatusDTO(ttChangeStatusDTO);
    if (resultDTO != null) {
      ttChangeStatusDTO.setId(resultDTO.getId());
      List<TTCfgBusinessDTO> ttCfgBusinessDTO = ttCfgBusinessRepository
          .getListTTCfgBusiness(ttChangeStatusDTO, true);
      resultDTO.setTtCfgBusinessDTO(ttCfgBusinessDTO);
      if (resultDTO.getId() > 0) {
        List<TTChangeStatusRoleDTO> ttChangeStatusRoleDTOS = ttChangeStatusRoleRepository
            .getListRoleByTTChangeStatusId(resultDTO.getId());
        resultDTO.setTtChangeStatusRoleDTO(ttChangeStatusRoleDTOS);
      }
    }
    return resultDTO;
  }

  @Override
  public ResultInSideDto deleteTTChangeStatus(Long ttChangeStatusId) {
    log.debug("Request to deleteTTChangeStatus : {}", ttChangeStatusId);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TTChangeStatusDTO oldHis = findTTChangeStatusById(ttChangeStatusId);
    ttCfgBusinessRepository.deleteListTTCfgBusiness(ttChangeStatusId);
    ttChangeStatusRoleRepository.deleteListTTChangeStatusRole(ttChangeStatusId);
    resultInSideDto = ttChangeStatusRepository.deleteTTChangeStatus(ttChangeStatusId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new TTChangeStatusDTO());
        dataHistoryChange.setType("TT_CONFIG_BUSINESS");
        dataHistoryChange.setActionType("delete");
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
  public ResultInSideDto insertOrUpdateTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO) {
    log.debug("Request to insertOrUpdateTTChangeStatus : {}", ttChangeStatusDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TTChangeStatusDTO oldHis = null;
    Long ttChangStatusId = ttChangeStatusDTO.getId();
    if (ttChangeStatusDTO.getId() != null) {
      oldHis = findTTChangeStatusById(ttChangeStatusDTO.getId());
    }
    if (StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getTtTypeId()) || StringUtils
        .isStringNullOrEmpty(ttChangeStatusDTO.getAlarmGroup())) {
      ttChangeStatusDTO.setTtTypeId(null);
      ttChangeStatusDTO.setAlarmGroup(null);
      ttChangeStatusDTO.setIsDefault(1L);
    }
    resultInSideDto = ttChangeStatusRepository
        .insertOrUpdateTTChangeStatus(ttChangeStatusDTO);
    if (ttChangStatusId == null) {
      ttChangStatusId = resultInSideDto.getId();
    } else {
      ttCfgBusinessRepository.deleteListTTCfgBusiness(ttChangStatusId);
      ttChangeStatusRoleRepository.deleteListTTChangeStatusRole(ttChangStatusId);
    }
    List<TTCfgBusinessDTO> lstCfgBusiness = ttChangeStatusDTO.getTtCfgBusinessDTO();
    if (lstCfgBusiness != null && lstCfgBusiness.size() > 0) {
      for (TTCfgBusinessDTO i : lstCfgBusiness) {
        i.setTtChangeStatusId(ttChangStatusId);
        resultInSideDto = ttCfgBusinessRepository.insertTTCfgBusiness(i);
      }
    }
    List<TTChangeStatusRoleDTO> lstRole = ttChangeStatusDTO.getTtChangeStatusRoleDTO();
    if (lstRole != null && lstRole.size() > 0) {
      for (TTChangeStatusRoleDTO i : lstRole) {
        i.setTtChangeStatusId(ttChangStatusId);
        resultInSideDto = ttChangeStatusRoleRepository.insertTTChangeStatusRole(i);
      }
    }
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setType("TT_CFG_BUSINESS");
        if (oldHis != null) {
          //Old Object History
          dataHistoryChange.setOldObject(oldHis);
          dataHistoryChange.setActionType("update");
        } else {
          //Old Object History
          dataHistoryChange.setOldObject(new TTChangeStatusDTO());
          dataHistoryChange.setActionType("add");
        }
        //New Object History
        dataHistoryChange.setNewObject(ttChangeStatusDTO);
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
  public TTChangeStatusDTO findTTChangeStatusById(Long id) {
    log.debug("Request to findTTChangeStatusById: {}", id);
    TTChangeStatusDTO ttChangeStatusDTO = ttChangeStatusRepository
        .findTTChangeStatusById(id);
    if (ttChangeStatusDTO != null) {
      ttChangeStatusDTO
          .setTtCfgBusinessDTO(
              ttCfgBusinessRepository.getListTTCfgBusiness(ttChangeStatusDTO, false));
      ttChangeStatusDTO
          .setTtChangeStatusRoleDTO(ttChangeStatusRoleRepository.getListRoleByTTChangeStatusId(id));
    }
    return ttChangeStatusDTO;
  }

  private List<String> getAllKeysDTO() {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = TTChangeStatusDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays
            .asList("typeName", "oldStatusName", "newStatusName", "isDefaultName", "ttPriorityName",
                "alarmGroupName", "ttCfgBusinessDTO", "ttChangeStatusRoleDTO");
        for (String rmKey : rmKeys) {
          keys.remove(rmKey);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
}
