package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.mr.repository.MrSchedulePeriodicRepository;
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
public class MrSchedulePeriodicBusinessImpl implements MrSchedulePeriodicBusiness {

  @Autowired
  MrSchedulePeriodicRepository mrSchedulePeriodicRepository;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<MrSchedulePeriodicDTO> getListMrSchedulePeriodicDTO(
      MrSchedulePeriodicDTO mrSchedulePeriodicDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    if (mrSchedulePeriodicDTO != null) {
      return mrSchedulePeriodicRepository
          .search(mrSchedulePeriodicDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public ResultInSideDto insertMrSchedulePeriodic(MrSchedulePeriodicDTO mrSchedulePeriodicDTO) {
    try {
      UserToken userToken = ticketProvider.getUserToken();
      Double offset = userBusiness.getOffsetFromUser(userToken.getUserID());
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            if (mrSchedulePeriodicDTO.getTimeInsert() != null && !"".equals(mrSchedulePeriodicDTO.getTimeInsert())) {
//                Date d = spd.parse(mrSchedulePeriodicDTO.getTimeInsert());
//                mrSchedulePeriodicDTO.setTimeInsert(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
//            }
      if (mrSchedulePeriodicDTO.getTimeSend() != null && !""
          .equals(mrSchedulePeriodicDTO.getTimeSend())) {
        Date d = spd.parse(mrSchedulePeriodicDTO.getTimeSend());
        mrSchedulePeriodicDTO
            .setTimeSend(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrSchedulePeriodicDTO.getTimeWoEnd() != null && !""
          .equals(mrSchedulePeriodicDTO.getTimeWoEnd())) {
        Date d = spd.parse(mrSchedulePeriodicDTO.getTimeWoEnd());
        mrSchedulePeriodicDTO
            .setTimeWoEnd(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrSchedulePeriodicDTO.getTimeWoStart() != null && !""
          .equals(mrSchedulePeriodicDTO.getTimeWoStart())) {
        Date d = spd.parse(mrSchedulePeriodicDTO.getTimeWoStart());
        mrSchedulePeriodicDTO
            .setTimeWoStart(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return mrSchedulePeriodicRepository.insertMrSchedulePeriodic(mrSchedulePeriodicDTO);
  }
}
