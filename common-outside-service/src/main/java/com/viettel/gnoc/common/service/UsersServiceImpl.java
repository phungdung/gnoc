package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
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
public class UsersServiceImpl implements UsersService {

  @Autowired
  UserBusiness userBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO getUserDTO(AuthorityDTO requestDTO) {
    log.info("Request to getUserDTO : {}");
    I18n.setLocaleForService(wsContext);
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);
      if (validate.equals(RESULT.SUCCESS)) {
        List<UsersDTO> lst = userBusiness.getUserDTO();
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
