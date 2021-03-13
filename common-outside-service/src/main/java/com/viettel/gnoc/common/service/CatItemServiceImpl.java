package com.viettel.gnoc.common.service;

import static com.viettel.gnoc.commons.utils.DateTimeUtils.validateInput;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.ws.dto.CatItemDTO;
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
public class CatItemServiceImpl implements CatItemService {

  @Autowired
  CatItemBusiness catItemBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO getListCateItem(String category, String fromDate,
      String toDate) {
    log.info("Request to getListCateItem : {}", category, fromDate, toDate);
    I18n.setLocaleForService(wsContext);
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      String validate = validateInput(fromDate, toDate);
      if (validate.equals(RESULT.SUCCESS)) {
        List<com.viettel.gnoc.ws.dto.CatItemDTO> lst = catItemBusiness
            .getListCateItem(category, fromDate, toDate);
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

  /**
   * @author tripm
   */
  @Override
  public List<CatItemDTO> getListCatItemDTO(CatItemDTO catItemDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    log.info("Request to getListCatItemDTO : {}", catItemDTO, rowStart, maxRow, sortType,
        sortFieldList);
    I18n.setLocaleForService(wsContext);
    if (catItemDTO != null) {
      return catItemBusiness.search(catItemDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }
}
