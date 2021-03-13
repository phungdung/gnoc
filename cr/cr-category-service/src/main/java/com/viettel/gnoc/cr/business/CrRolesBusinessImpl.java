package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.repository.CrRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author DungPV
 */
@Service
@Slf4j
@Transactional
public class CrRolesBusinessImpl implements CrRolesBusiness {

  @Autowired
  protected CrRolesRepository crRolesRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListCrRoles(CrRolesDTO crRolesDTO) {
    log.info("Request to getListCrRoles : {}", crRolesDTO);
    return crRolesRepository.getListCrRoles(crRolesDTO);
  }

  @Override
  public CrRolesDTO getDetail(Long cmreId) {
    log.info("Request to getDetail : {}", cmreId);
    CrRolesDTO crRolesDTO = crRolesRepository.getDetail(cmreId);
    crRolesDTO.setListRoleName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", cmreId, null));
    return crRolesDTO;
  }

  @Override
  public ResultInSideDto deleteCrRoles(Long cmreId) {
    log.info("Request to deleteCrRoles : {}", cmreId);
    ResultInSideDto resultDto = crRolesRepository.deleteCrRoles(cmreId);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete CrManagerRoles", "Delete CrManagerRoles ID: " + cmreId, null, null));
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", cmreId);
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListCrRoles(CrRolesDTO crRolesDTO) {
    log.info("Request to deleteListCrRoles : {}", crRolesDTO);
    ResultInSideDto resultDto = new ResultInSideDto();
    if (!crRolesDTO.getListId().isEmpty() && crRolesDTO.getListId().size() > 0) {
      for (Long id : crRolesDTO.getListId()) {
        resultDto = crRolesRepository.deleteCrRoles(id);
      }
    } else {
      resultDto.setKey(Constants.RESULT.NODATA);
    }
    return resultDto;
  }

  @Override
  public ResultInSideDto addCrRoles(CrRolesDTO crRolesDTO) {
    log.info("Request to addCrRoles : {}", crRolesDTO);
    ResultInSideDto resultDto = crRolesRepository.addOrEdit(crRolesDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", resultDto.getId(),
            crRolesDTO.getListRoleName());
    if (resultDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add CrManagerRoles", "Add CrManagerRoles ID: " + resultDto.getId(), crRolesDTO, null));
    }
    return resultDto;
  }

  @Override
  public ResultInSideDto updateCrRoles(CrRolesDTO crRolesDTO) {
    log.info("Request to updateCrRoles : {}", crRolesDTO);
    ResultInSideDto resultDto = new ResultInSideDto();
    if (crRolesDTO.getCmreId() != null && crRolesDTO.getCmreId() > 0) {
      resultDto = crRolesRepository.addOrEdit(crRolesDTO);
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", resultDto.getId(),
              crRolesDTO.getListRoleName());
    } else {
      resultDto.setKey(Constants.RESULT.NODATA);
    }
    if (resultDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CrManagerRoles", "Update CrManagerRoles ID: " + resultDto.getId(), crRolesDTO,
          null));
    }
    return resultDto;
  }
}
