package com.viettel.gnoc.od.business;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdHistoryDTO;
import com.viettel.gnoc.od.repository.OdHistoryRepository;
import com.viettel.gnoc.od.repository.OdRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NamTN
 */
@Service
@Transactional
@Slf4j
public class OdHistoryBusinessImpl implements OdHistoryBusiness {

  @Autowired
  protected OdRepository odRepository;

  @Autowired
  protected OdHistoryRepository odHistoryRepository;

  private final static String OD_HISTORY_SEQ = "OD_HISTORY_SEQ";

  @Override
  public ResultInSideDto add(OdDTO odDTO) {
    log.debug("Request to add : {}", odDTO);
    return odRepository.add(odDTO);
  }

  @Override
  public ResultInSideDto edit(OdDTO odDTO) {
    log.debug("Request to edit : {}", odDTO);
    return odRepository.edit(odDTO);
  }

  @Override
  public ResultInSideDto insertOdHistory(OdDTO odDTO, Long oldStatus, Long newStatus,
      String content,
      UsersEntity user) {
    OdHistoryDTO his = new OdHistoryDTO();
    his.setContent(content);
    his.setNewStatus(newStatus);
    his.setOdId(odDTO.getOdId());
    his.setOldStatus(oldStatus);
    his.setUnitId(user.getUnitId());
    his.setUpdateTime(new Date());
    his.setUserId(user.getUserId());
    his.setUserName(user.getUsername());
    return odHistoryRepository.insertOrUpdate(his);
  }

  @Override
  public ResultInSideDto delete(Long odHisId) {
    log.debug("Request to delete : {}", odHisId);
    return odHistoryRepository.delete(odHisId);
  }

  @Override
  public ResultInSideDto insertOrUpdate(OdHistoryDTO odDTO) {
    log.debug("Request to insertOrUpdate : {}", odDTO);
    return odHistoryRepository.insertOrUpdate(odDTO);
  }

  @Override
  public String getSeqOHistory() {
    log.debug("Request to getSeqOHistory : {}");
    return odHistoryRepository.getSeqOHistory(OD_HISTORY_SEQ);
  }

  @Override
  public List<OdHistoryDTO> getOdHistoryByOdId(Long odId) {
    log.debug("Request to getOdHistoryByOdId : {}");
    return odHistoryRepository.getOdHistoryByOdId(odId);
  }
}
