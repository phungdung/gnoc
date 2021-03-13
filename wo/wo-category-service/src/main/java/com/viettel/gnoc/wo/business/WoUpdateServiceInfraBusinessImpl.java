package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoUpdateServiceInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoUpdateServiceInfraBusinessImpl implements WoUpdateServiceInfraBusiness {

  @Autowired
  protected WoUpdateServiceInfraRepository woUpdateServiceInfraRepository;

  @Autowired
  protected WoDetailRepository woDetailRepository;

  @Override
  public Datatable getListWoUpdateServiceInfraPage(
      WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    log.debug("Request to getListWoUpdateServiceInfraPage : {}", woUpdateServiceInfraDTO);
    return woUpdateServiceInfraRepository.getListWoUpdateServiceInfraPage(woUpdateServiceInfraDTO);
  }

  @Override
  public ResultInSideDto update(WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    log.debug("Request to update : {}", woUpdateServiceInfraDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoDetailDTO woDetailDTO = woDetailRepository.findById(woUpdateServiceInfraDTO.getWoId());
    if (woDetailDTO != null) {
      woDetailDTO.setServiceId(woUpdateServiceInfraDTO.getServiceId());
      woDetailDTO.setInfraType(woUpdateServiceInfraDTO.getInfraType());
      resultInSideDto = woDetailRepository.update(woDetailDTO);
      return resultInSideDto;
    } else {
      resultInSideDto.setKey(RESULT.FAIL);
      return resultInSideDto;
    }
  }

}
