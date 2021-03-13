package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.business.TroubleMopDtBusiness;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TroubleMopDtServiceImpl implements TroubleMopDtService {

  @Autowired
  protected TroubleMopDtBusiness troubleMopDtBusiness;

  @Override
  public ResultDTO insertTroubleMopDt(TroubleMopDtDTO troubleMopDtDTO) {
    log.debug("Request to insertTroubleMopDt : {}", troubleMopDtDTO);
    return troubleMopDtBusiness.insertTroubleMopDt(troubleMopDtDTO);
  }

  @Override
  public ResultDTO updateDt(AuthorityDTO requestDTO, TroubleMopDtDTO troubleMopDtDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      return troubleMopDtBusiness.updateDt(requestDTO, troubleMopDtDTO);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
    }
    return resultDTO;
  }
}
