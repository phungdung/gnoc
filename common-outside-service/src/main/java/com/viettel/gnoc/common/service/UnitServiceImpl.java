package com.viettel.gnoc.common.service;

import static com.viettel.gnoc.commons.utils.DateTimeUtils.validateInput;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TrungDuong
 */
@Service
@Slf4j
public class UnitServiceImpl implements UnitService {

  @Autowired
  UnitBusiness unitBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO getUnitDTO(AuthorityDTO requestDTO,String fromDate, String toDate) {
    log.info("Request to getUnitDTO : {}", fromDate, toDate);
    I18n.setLocaleForService(wsContext);
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);
      if (validate.equals(RESULT.SUCCESS)) {
        validate = validateInput(fromDate, toDate);
        if (validate.equals(RESULT.SUCCESS)) {
          List<com.viettel.gnoc.ws.dto.UnitDTO> ret = unitBusiness.getUnitDTO(fromDate, toDate);
          result.setLstResult(ret);
        }
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
