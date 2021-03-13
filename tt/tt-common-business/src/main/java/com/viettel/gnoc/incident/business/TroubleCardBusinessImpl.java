package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import com.viettel.gnoc.incident.dto.TroubleCardInsertDTO;
import com.viettel.gnoc.incident.repository.TroubleCardRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TroubleCardBusinessImpl implements TroubleCardBusiness {

  @Autowired
  TroubleCardRepository troubleCardRepository;

  @Autowired
  TroubleCardBusiness troubleCardBusiness;

  @Override
  public Datatable getListTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    return troubleCardRepository.getListTroubleCardDTO(troubleCardDTO);
  }

  @Override
  public ResultInSideDto updateTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    Date date = new Date();
    troubleCardDTO.setUpdateTime(date);
    return troubleCardRepository.updateTroubleCardDTO(troubleCardDTO);
  }

  @Override
  public ResultInSideDto insertTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    Date date = new Date();
    troubleCardDTO.setUpdateTime(date);
    return troubleCardRepository.insertTroubleCardDTO(troubleCardDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateTroubleCard(List<TroubleCardDTO> troubleCardDTOS) {
    try {
      for (TroubleCardDTO item : troubleCardDTOS) {
        if (item.getUpdateTime() != null && !"".equals(item.getUpdateTime())) {
          Date parsedDate = item.getUpdateTime();
          item.setUpdateTime(parsedDate);
        } else {
          Date date = new Date();
          item.setUpdateTime(date);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return troubleCardRepository.insertOrUpdateTroubleCard(troubleCardDTOS);
  }

  @Override
  public ResultInSideDto deleteTroubleCard(Long id) {
    return troubleCardRepository.deleteTroubleCard(id);
  }

  @Override
  public ResultInSideDto deleteListTroubleCard(List<TroubleCardDTO> troubleCardDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (TroubleCardDTO item : troubleCardDTOS) {
      resultInSideDto = deleteTroubleCard(item.getTroubleCardId());
    }
    return resultInSideDto;
  }

  @Override
  public List<TroubleCardDTO> getListTroubleCardDTOByTroubleId(Long TroubleId) {
    return troubleCardRepository.getListTroubleCardDTOByTroubleId(TroubleId);
  }

  @Override
  public ResultInSideDto insertListTroubleCard(TroubleCardInsertDTO troubleCardInsertDTOS) {
    ResultInSideDto result = new ResultInSideDto();
    Long troubleId = Long.parseLong(troubleCardInsertDTOS.getTroubleId());
    List<TroubleCardDTO> list = getListTroubleCardDTOByTroubleId(troubleId);
    deleteListTroubleCard(list);
    if (troubleCardInsertDTOS.getLst() != null && troubleCardInsertDTOS.getLst().size() > 0) {
      result = insertOrUpdateTroubleCard(troubleCardInsertDTOS.getLst());
    }
    return result;
  }
}
