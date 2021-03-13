package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import com.viettel.gnoc.mr.repository.MrITSoftCatMarketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MrITSoftCatMarketBusinessImpl implements MrITSoftCatMarketBusiness {

  @Autowired
  MrITSoftCatMarketRepository mrITSoftCatMarketRepository;

  @Override
  public Datatable getListMrCatMarketSearch(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    return mrITSoftCatMarketRepository.getListMrCatMarketSearch(mrITSoftCatMarketDTO);
  }

  @Override
  public ResultInSideDto insert(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    log.debug("Request to add: {}", mrITSoftCatMarketDTO);
    ResultInSideDto result = new ResultInSideDto();
    if (!StringUtils.isStringNullOrEmpty(mrITSoftCatMarketRepository.checkExist(
        mrITSoftCatMarketDTO.getMarketCode()))) {
      result.setKey(RESULT.DUPLICATE);
      return result;
    } else {
      return mrITSoftCatMarketRepository.insert(mrITSoftCatMarketDTO);
    }
  }

  @Override
  public ResultInSideDto update(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    log.debug("Request to update: {}", mrITSoftCatMarketDTO);
    return mrITSoftCatMarketRepository.update(mrITSoftCatMarketDTO);
  }

  @Override
  public ResultInSideDto delete(String marketCode) {
    log.debug("Request to update: {}", marketCode);
    return mrITSoftCatMarketRepository.delete(marketCode);
  }

}
