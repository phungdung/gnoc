package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
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
public class MrServiceBusinessImpl implements MrServiceBusiness {

  @Autowired
  MrServiceRepository mrServiceRepository;

  @Autowired
  MrApprovalDepartmentBusinessImpl mrApprovalDepartmentBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Override
  public Datatable getListMrCrWoNew(MrSearchDTO mrSearchDTO) {
    return mrServiceRepository.getListMrCrWoNew(mrSearchDTO);
  }

  @Override
  public List<MrDTO> getWorklogFromWo(MrSearchDTO dto) {
    return mrServiceRepository.getWorklogFromWo(dto);
  }

  @Override
  public List<String> getIdSequence() {
    return mrServiceRepository.getIdSequence();
  }

  @Override
  public ResultInSideDto insertMr(MrInsideDTO mrDTO) {
    //time zone
    try {
      UserToken userToken = TicketProvider.getUserToken();
      Double offset = userBusiness.getOffsetFromUser(userToken.getUserID());
      if (mrDTO.getEarliestTime() != null && !"".equals(mrDTO.getEarliestTime())) {
        Date d = mrDTO.getEarliestTime();
        mrDTO.setEarliestTime(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
      if (mrDTO.getLastestTime() != null && !"".equals(mrDTO.getLastestTime())) {
        Date d = mrDTO.getLastestTime();
        mrDTO.setLastestTime(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
      if (mrDTO.getNextWoCreate() != null && !"".equals(mrDTO.getNextWoCreate())) {
        Date d = mrDTO.getNextWoCreate();
        mrDTO.setNextWoCreate(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //time zone
    return mrServiceRepository.insertMr(mrDTO);
  }

  @Override
  public ResultInSideDto updateMr(MrInsideDTO mrDTO) {
    try {
      Double offset = mrApprovalDepartmentBusiness.getOffset();
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (mrDTO.getEarliestTime() != null && !"".equals(mrDTO.getEarliestTime())) {
        Date d = mrDTO.getEarliestTime();
        mrDTO.setEarliestTime(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
      if (mrDTO.getLastestTime() != null && !"".equals(mrDTO.getLastestTime())) {
        Date d = mrDTO.getLastestTime();
        mrDTO.setLastestTime(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
      if (mrDTO.getCreatedTime() != null && !"".equals(mrDTO.getCreatedTime())) {
        Date d = mrDTO.getCreatedTime();
        mrDTO.setCreatedTime(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
      if (mrDTO.getNextWoCreate() != null && !"".equals(mrDTO.getNextWoCreate())) {
        Date d = mrDTO.getNextWoCreate();
        mrDTO.setNextWoCreate(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000)));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //time zone
    return mrServiceRepository.updateMr(mrDTO);
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    return mrServiceRepository.getListWorklogSearch(workLogInsiteDTO);
  }
}
