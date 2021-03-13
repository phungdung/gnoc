package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.proxy.KedbServiceProxy;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.mr.repository.MrApprovalDepartmentRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrApprovalDepartmentBusinessImpl implements MrApprovalDepartmentBusiness {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  KedbServiceProxy kedbServiceProxy;

  @Autowired
  MrApprovalDepartmentRepository mrApprovalDepartmentRepository;

  @Autowired
  UserBusiness userBusiness;

  @Override
  public ResultInSideDto insertMrApprovalDepartment(
      MrApprovalDepartmentDTO mrApprovalDepartmentDTO) {
    //time zone
    try {
      Double offset = getOffset();
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (mrApprovalDepartmentDTO.getApprovedDate() != null && !""
          .equals(mrApprovalDepartmentDTO.getApprovedDate())) {
        Date d = spd.parse(mrApprovalDepartmentDTO.getApprovedDate());
        mrApprovalDepartmentDTO
            .setApprovedDate(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrApprovalDepartmentDTO.getIncommingDate() != null && !""
          .equals(mrApprovalDepartmentDTO.getIncommingDate())) {
        Date d = spd.parse(mrApprovalDepartmentDTO.getIncommingDate());
        mrApprovalDepartmentDTO
            .setIncommingDate(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //time zone
    return mrApprovalDepartmentRepository.insertMrApprovalDepartment(mrApprovalDepartmentDTO);
  }

  @Override
  public List<MrApproveRolesDTO> getLstMrApproveUserByRole(MrApproveRolesDTO mrRole) {
    return mrApprovalDepartmentRepository.getLstUserByRole(mrRole);
  }

  public Double getOffset() {
    Double offSet;
    UserTokenGNOCSimple userTokenGNOCSimple = new UserTokenGNOCSimple();
    UserToken userToken = ticketProvider.getUserToken();
    userTokenGNOCSimple.setUserId(userToken.getUserID());
    userTokenGNOCSimple.setUserName(userToken.getUserName());
//    String offSetStr = kedbServiceProxy.getOffset(userTokenGNOCSimple);
    offSet = userBusiness.getOffsetFromUser(ticketProvider.getUserToken().getUserID());
//    try {
//      offSet = Double.valueOf(offSetStr);
//    } catch (Exception e) {
//      offSet = 0D;
//    }
    return offSet;
  }

  @Override
  public String updateMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO) {
    try {
      Double offset = getOffset();
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (mrApprovalDepartmentDTO.getApprovedDate() != null && !""
          .equals(mrApprovalDepartmentDTO.getApprovedDate())) {
        Date d = spd.parse(mrApprovalDepartmentDTO.getApprovedDate());
        mrApprovalDepartmentDTO
            .setApprovedDate(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrApprovalDepartmentDTO.getIncommingDate() != null && !""
          .equals(mrApprovalDepartmentDTO.getIncommingDate())) {
        Date d = spd.parse(mrApprovalDepartmentDTO.getIncommingDate());
        mrApprovalDepartmentDTO
            .setIncommingDate(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //time zone
    return mrApprovalDepartmentRepository.updateMrApprovalDepartment(mrApprovalDepartmentDTO);
  }

  @Override
  public List<MrApproveSearchDTO> getLstMrApproveSearch(MrApproveSearchDTO s) {
    return mrApprovalDepartmentRepository.getLstMrApproveSearch(s);
  }

  @Override
  public List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String userId) {
    return mrApprovalDepartmentRepository.getLstMrApproveDeptByUser(userId);
  }
}
