package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.mr.business.MrServiceBusiness;
import com.viettel.gnoc.mr.business.WorkLogBusiness;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkLogServiceImpl implements WorkLogService {

  @Autowired
  WorkLogBusiness workLogBusiness;

  @Autowired
  MrServiceBusiness mrServiceBusiness;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Resource
  WebServiceContext wsContext;

  public final static String REGEX_NUMBER = "([0-9])+";

  @Override
  public ResultDTO insertWorkLog(WorkLogDTO workLogDTO) {
    I18n.setLocaleForService(wsContext);
    ResultDTO resultDTO = validateInserWorkLog(workLogDTO);
    if (resultDTO == null) {
      return workLogBusiness.createObject(workLogDTO);
    }
    return resultDTO;
  }

  @Override
  public List<WorkLogCategoryDTO> getListWorkLogCategoryDTO(WorkLogCategoryDTO workLogCategoryDTO) {
    I18n.setLocaleForService(wsContext);
    List<WorkLogCategoryDTO> lstResult = new ArrayList<>();
    WorkLogCategoryInsideDTO workLogInsiteDTO = workLogCategoryDTO.toInsideDTO();
    if (StringUtils.isNotNullOrEmpty(I18n.getLocale())) {
      workLogInsiteDTO.setProxyLocale(I18n.getLocale());
    }
    List<WorkLogCategoryInsideDTO> lst = mrCategoryProxy
        .getListWorkLogCategoryDTO(workLogInsiteDTO);
    if (lst != null && !lst.isEmpty()) {
      for (WorkLogCategoryInsideDTO workLogCategoryInsideDTO : lst) {
        lstResult.add(workLogCategoryInsideDTO.toOutSide());
      }
    }
    return lstResult;
  }

  @Override
  public List<WorkLogResultDTO> getListWorklogSearch(WorkLogDTO dto) {
    I18n.setLocaleForService(wsContext);
    return workLogBusiness.getListWorklogSearch(dto);
  }

  public ResultDTO validateInserWorkLog(WorkLogDTO workLogDTO) {
    Date checkDate = DateUtil
        .string2DateByPattern(workLogDTO.getCreatedDate(), "dd/MM/yyyy HH:mm:ss");
    if (checkDate == null) {
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
          I18n.getLanguage("mrService.createTime.invalid"));
    }
    if (!StringUtils.isStringNullOrEmpty(workLogDTO.getUserId()) && !workLogDTO.getUserId()
        .matches(REGEX_NUMBER)) {
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
          I18n.getLanguage("mrService.userId.invalid"));
    }
    if (!StringUtils.isStringNullOrEmpty(workLogDTO.getUserGroupAction()) && !workLogDTO
        .getUserGroupAction().matches(REGEX_NUMBER)) {
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
          I18n.getLanguage("mrService.userGroupCode.invalid"));
    }
    return null;
  }
}
