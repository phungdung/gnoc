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
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.repository.CfgChildArrayRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class CfgChildArrayBusinessImpl implements CfgChildArrayBusiness {

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CfgChildArrayRepository cfgChildArrayRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;


  @Override
  public Datatable getListCfgChildArray(CfgChildArrayDTO cfgChildArrayDTO) {
    log.info("Request to getListCfgChildArray : {}", cfgChildArrayDTO);
    return cfgChildArrayRepository.getListCfgChildArray(cfgChildArrayDTO);
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    log.info("Request to getListImpactSegmentCBB : {}");
    List<ImpactSegmentDTO> lstReturn = cfgChildArrayRepository.getListImpactSegmentCBB();
    try {
      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
          Constants.APPLIED_BUSSINESS.IMPACT_SEGMENT, locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, "impactSegmentId", "impactSegmentName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public ResultInSideDto delete(Long childrenId) {
    log.info("Request to delete : {}", childrenId);
    ResultInSideDto resultInSideDto = cfgChildArrayRepository.delete(childrenId);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete cfgChildArray", "Delete cfgChildArray ID: " + childrenId,
        null, null));
    languageExchangeRepository
        .deleteListLanguageExchange("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY", childrenId);
    return resultInSideDto;
  }

  @Override
  public CfgChildArrayDTO getDetail(Long childrenId) {
    log.info("Request to findById : {}", childrenId);
    CfgChildArrayDTO cfgChildArrayDTO = cfgChildArrayRepository.getDetail(childrenId);
    cfgChildArrayDTO.setListChildrenName(languageExchangeRepository
        .getListLanguageExchangeById("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY", childrenId,
            null));
    return cfgChildArrayDTO;
  }

  @Override
  public ResultInSideDto add(CfgChildArrayDTO cfgChildArrayDTO) {
    log.info("Request to add : {}", cfgChildArrayDTO);
    UserToken userToken = ticketProvider.getUserToken();
    cfgChildArrayDTO.setUpdatedUser(userToken.getUserName());
    cfgChildArrayDTO.setUpdatedTime(DateTimeUtils
        .convertDateToString(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    ResultInSideDto resultInSideDto = cfgChildArrayRepository.addOrUpdate(cfgChildArrayDTO);
    languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY",
            resultInSideDto.getId(),
            cfgChildArrayDTO.getListChildrenName());
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {

      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add cfgChildArray", "Add cfgChildArray ID: " + resultInSideDto.getId(),
          cfgChildArrayDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(CfgChildArrayDTO cfgChildArrayDTO) {
    log.info("Request to update : {}", cfgChildArrayDTO);
    UserToken userToken = ticketProvider.getUserToken();
    cfgChildArrayDTO.setUpdatedUser(userToken.getUserName());
    cfgChildArrayDTO.setUpdatedTime(DateTimeUtils
        .convertDateToString(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (cfgChildArrayDTO.getChildrenId() != null && cfgChildArrayDTO.getChildrenId() > 0) {
      resultInSideDto = cfgChildArrayRepository.addOrUpdate(cfgChildArrayDTO);
      languageExchangeRepository
          .saveListLanguageExchange("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY",
              resultInSideDto.getId(),
              cfgChildArrayDTO.getListChildrenName());
      if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
        commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update cfgChildArray", "Update cfgChildArray ID: " + resultInSideDto.getId(),
            cfgChildArrayDTO, null));
      }
    } else {
      resultInSideDto.setKey(RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto) {
    return cfgChildArrayRepository.getCbbChildArray(dto);
  }
}
