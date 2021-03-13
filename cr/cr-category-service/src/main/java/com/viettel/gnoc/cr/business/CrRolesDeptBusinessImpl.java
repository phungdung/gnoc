package com.viettel.gnoc.cr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTO;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTOSearch;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.model.CrRoleDeptEntity;
import com.viettel.gnoc.cr.repository.CrRolesDeptRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class CrRolesDeptBusinessImpl implements CrRolesDeptBusiness {

  @Autowired
  CrRolesDeptRepository crRolesRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;


  @Override
  public List<CrRolesDTO> getListCrRolesDTO(CrRolesDTO crRolesDTO) {
    List<CrRolesDTO> lstReturn = crRolesRepository.getListCrRolesDTO(crRolesDTO);
    try {
      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
          Constants.APPLIED_BUSSINESS.CR_MANAGER_ROLE, locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, "cmreId", "cmreName");
    } catch (Exception e) {
      log.error("Exception:", e);
    }
    return lstReturn;
  }

  @Override
  public ResultInSideDto insertCrRoles(CrRolesDTO crRolesDTO) {
    return crRolesRepository.insertCrRoles(crRolesDTO);
  }

  @Override
  public ResultInSideDto updateCrRoles(CrRolesDTO crRolesDTO) {
    return crRolesRepository.updateCrRoles(crRolesDTO);
  }

  @Override
  public CrRolesDTO findCrRolesDTOById(Long id) {
    return crRolesRepository.findCrRolesDTOById(id);
  }


  @Override
  public ResultInSideDto insertRoleDept(CrRoleDeptDTO crRoleDeptDTO) {
    ResultInSideDto resultInSideDto = crRolesRepository.insertRoleDept(crRoleDeptDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add crRoleDept", "Add crRoleDept ID: " + resultInSideDto.getId(),
          crRoleDeptDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateRoleDept(CrRoleDeptDTO crRoleDeptDTO) {
    ResultInSideDto resultInSideDto = crRolesRepository.updateRoleDept(crRoleDeptDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update crRoleDept", "Update crRoleDept ID: " + resultInSideDto.getId(),
          crRoleDeptDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListRoleDept(CrRoleDeptDTOSearch crRoleDeptDTOSearch) {
    return crRolesRepository.getListRoleDept(crRoleDeptDTOSearch);
  }

  @Override
  public CrRoleDeptEntity findCrRoleDeptEntityById(Long id) {
    return crRolesRepository.findCrRoleDeptEntityById(id);
  }

  @Override
  public ResultInSideDto deleteRoleDept(Long id) {
    ResultInSideDto resultInSideDto = crRolesRepository.deleteRoleDept(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete crRoleDept", "Delete crRoleDept ID: " + id,
          null, null));
    }
    return resultInSideDto;
  }


}
