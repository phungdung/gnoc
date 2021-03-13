package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoInsiteDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CfgCreateWoRepository;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class CfgCreateWoBusinessImpl implements CfgCreateWoBusiness {

  @Autowired
  protected CfgCreateWoRepository cfgCreateWoRepository;
  @Autowired
  private CatItemRepository catItemRepository;
  @Autowired
  private LogChangeConfigRepository logChangeConfigRepository;
  @Autowired
  TicketProvider ticketProvider;

  public Datatable getListCfgCreateWoDTOPage(CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    log.info("Request to getListCfgCreateWoDTOPage : {}", cfgCreateWoDTO);
    return cfgCreateWoRepository.getListCfgCreateWoDTOPage(cfgCreateWoDTO);
  }

  @Override
  public CfgCreateWoInsiteDTO getDetailById(Long id) {
    log.info("Request to getDetailById : {}", id);
    return cfgCreateWoRepository.getDetailById(id);
  }

  @Override
  public List<CatItemDTO> getCmbArrayIncident() {
    log.info("Request to getCmbArrayIncident : {}");
    List<CatItemDTO> lstResult = new ArrayList<>();
    List<String> lstCategory = new ArrayList<>();
    lstCategory.add(Constants.PROBLEM.PT_TYPE);
    List<CatItemDTO> lst = catItemRepository.getListCatItemDTO(lstCategory, null);
    if (lst != null && !lst.isEmpty()) {
      for (CatItemDTO itemDTO : lst) {
        switch (itemDTO.getCategoryCode()) {
          case Constants.PROBLEM.PT_TYPE:
            lstResult.add(itemDTO);
            break;
          default:
            break;
        }
      }
    }
    return lstResult;
  }

  @Override
  public List<CatItemDTO> getCmbAlarmGroup(Long itemId) {
    log.info("Request to getCmbAlarmGroup : {}", itemId);
    if (itemId != null && itemId > 0) {
      return catItemRepository
          .getListItemByCategoryAndParent(Constants.PROBLEM.ALARM_GROUP, itemId.toString());
    }
    return null;
  }

  @Override
  public List<CfgCreateWoDTO> getListCfgCreateWoDTO(CfgCreateWoDTO cfgCreateWoDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.info("Request to getListCfgCreateWoDTO : {}", cfgCreateWoDTO, rowStart, maxRow, sortType,
        sortFieldList);
    return cfgCreateWoRepository
        .getListCfgCreateWoDTO(cfgCreateWoDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto insertOrUpdateCfgCreateWo(CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    log.info("Request to insertOrUpdateCfgCreateWo : {}", cfgCreateWoDTO);
    cfgCreateWoDTO.setLastUpdateTime(new Date(System.currentTimeMillis()));
    ResultInSideDto resultInSideDto = cfgCreateWoRepository.addOrEditCfgCreate(cfgCreateWoDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      if (cfgCreateWoDTO.getId() != null && cfgCreateWoDTO.getId() > 0) {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update CfgCreateWo",
            "Update CfgCreateWo ID: " + resultInSideDto.getId(), cfgCreateWoDTO,
            null));
      } else {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Add CfgCreateWo",
            "Add CfgCreateWo ID: " + resultInSideDto.getId(), cfgCreateWoDTO,
            null));
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCfgCreateWo(Long id) {
    log.info("Request to deleteCfgCreateWo : {}", id);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (id != null && id > 0) {
      resultInSideDto = cfgCreateWoRepository.deleteCfgCreateWo(id);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        UserToken userToken = ticketProvider.getUserToken();
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Delete CfgCreateWo",
            "Delete CfgCreateWo ID: " + id, null,
            null));

      }
    }
    return resultInSideDto;
  }
}
