package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import com.viettel.gnoc.od.repository.OdRelationRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class OdRelationBusinessImpl implements OdRelationBusiness {

  @Autowired
  protected OdRelationRepository odRelationRepository;

  @Override
  public ResultInSideDto insertLstRelation(OdDTO odDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage(RESULT.SUCCESS);
      if (odDTO.getLstOdRelation() != null && odDTO.getLstOdRelation().size() > 0) {
        for (OdRelationDTO o : odDTO.getLstOdRelation()) {
          o.setOdId(odDTO.getOdId());
          odRelationRepository.insertOrUpdate(o);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(OdRelationDTO odDTO) {
    log.debug("Request to search insertOrUpdate : {}");
    return odRelationRepository.insertOrUpdate(odDTO);
  }

  @Override
  public List<OdRelationDTO> getRelationsByOdId(Long odId) {
    log.debug("Request to search getRelationsByOdId : {}");
    return odRelationRepository.getRelationsByOdId(odId);
  }


}
