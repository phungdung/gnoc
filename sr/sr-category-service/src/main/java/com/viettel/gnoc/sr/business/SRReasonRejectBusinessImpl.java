package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.repository.SRReasonRejectRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Slf4j
@Transactional
public class SRReasonRejectBusinessImpl implements SRReasonRejectBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected SRReasonRejectRepository srServiceManageRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListSRReasonRejectDTO(SRReasonRejectDTO reasonRejectDTO) {
    return srServiceManageRepository.getListSRReasonRejectDTO(reasonRejectDTO);
  }

  @Override
  public ResultInSideDto deleteSRReasonReject(Long id) {
    return srServiceManageRepository.deleteSRReasonReject(id);
  }

  @Override
  public ResultInSideDto insertOrUpdateSRReason(SRReasonRejectDTO reasonRejectDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    reasonRejectDTO.setCreatedTime(new Date());
    reasonRejectDTO.setCreatedUser(userToken.getUserName());
    return srServiceManageRepository.insertOrUpdateSRReason(reasonRejectDTO);
  }

  @Override
  public SRReasonRejectDTO getSRReasonRejectById(Long id) {
    return srServiceManageRepository.getSRReasonRejectById(id);
  }
}
