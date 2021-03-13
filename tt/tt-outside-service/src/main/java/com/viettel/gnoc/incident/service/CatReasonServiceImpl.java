package com.viettel.gnoc.incident.service;

import static com.viettel.gnoc.commons.utils.DateTimeUtils.validateInput;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.incident.business.CatReasonBusiness;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CatReasonServiceImpl implements CatReasonService {

  @Autowired
  protected CatReasonBusiness catReasonBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<CatReasonDTO> getReasonDTOForTreeByTroubleCode(Boolean isRoot, String troubleCode,
      String parentId) {
    log.debug("Request to getReasonDTOForTreeByTroubleCode : {}", parentId);
    I18n.setLocaleForService(wsContext);
    return catReasonBusiness.getReasonDTOForTreeByTroubleCode(isRoot, troubleCode, parentId);
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForVsmart(String troubleCode, String parentId, int level) {
    log.debug("Request to getReasonDTOForVsmart : {}", parentId);
    I18n.setLocaleForService(wsContext);
    return catReasonBusiness.getReasonDTOForVsmart(troubleCode, parentId, level);
  }

  @Override
  public ResultDTO getReasonDTO(String fromDate, String toDate) {
    log.info("Request to getReasonDTO : {}", fromDate, toDate);
    I18n.setLocaleForService(wsContext);
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      String validate = validateInput(fromDate, toDate);
      if (validate.equals(RESULT.SUCCESS)) {
        List<CatReasonDTO> lst = catReasonBusiness.getReasonDTO(fromDate, toDate);
        result.setLstResult(lst);
      }
      String endTime = DateTimeUtils.getSysDateTime();
      if (validate.equals(RESULT.SUCCESS)) {
        result.setKey(RESULT.SUCCESS);
      } else {
        result.setKey(RESULT.FAIL);
      }
      result.setMessage(validate);
      result.setRequestTime(startTime);
      result.setFinishTime(endTime);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return result;
  }

}
