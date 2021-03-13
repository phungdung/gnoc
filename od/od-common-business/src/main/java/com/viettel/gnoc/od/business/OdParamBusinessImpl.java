package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdParamDTO;
import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import com.viettel.gnoc.od.repository.OdParamRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TungPV
 */
@Service
@Slf4j
public class OdParamBusinessImpl implements OdParamBusiness {

  @Autowired
  protected OdParamRepository odParamRepository;

  @Override
  public List<OdParamInsideDTO> findAll() {
    log.debug("Request to search OdParamInsideDTO : {}");
    return odParamRepository.findAll();
  }

  @Override
  public List<OdParamDTO> getListOdParamByOdId(Long id) {
    log.debug("Request to search getListOdParamByOdId :" + id);
    if (id != null) {
      return odParamRepository.getListOdParamByOdId(id);
    }
    return null;
  }

  @Override
  public ResultInSideDto add(OdParamInsideDTO odParamInsideDTO) {
    log.debug("Request to add : {}");
    return odParamRepository.add(odParamInsideDTO);
  }
}
