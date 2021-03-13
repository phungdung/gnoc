package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.repository.MrWoTempRepository;
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
public class MrWoTempBusinessImpl implements MrWoTempBusiness {

  @Autowired
  MrWoTempRepository mrWoTempRepository;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<MrWoTempDTO> getListMrWoTempDTO(MrWoTempDTO mrWoTempDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    if (mrWoTempDTO != null) {
      return mrWoTempRepository.search(mrWoTempDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateMrWoTemp(MrWoTempDTO mrWoTemp) {
    //time zone
    try {
      UserToken userToken = ticketProvider.getUserToken();
      Double offset = userBusiness.getOffsetFromUser(userToken.getUserID());
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (mrWoTemp.getCreateDate() != null && !"".equals(mrWoTemp.getCreateDate())) {
        Date d = spd.parse(mrWoTemp.getCreateDate());
        mrWoTemp
            .setCreateDate(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrWoTemp.getStartTime() != null && !"".equals(mrWoTemp.getStartTime())) {
        Date d = spd.parse(mrWoTemp.getStartTime());
        mrWoTemp.setStartTime(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrWoTemp.getEndTime() != null && !"".equals(mrWoTemp.getEndTime())) {
        Date d = spd.parse(mrWoTemp.getEndTime());
        mrWoTemp.setEndTime(spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
      if (mrWoTemp.getLastUpdateTime() != null && !"".equals(mrWoTemp.getLastUpdateTime())) {
        Date d = spd.parse(mrWoTemp.getLastUpdateTime());
        mrWoTemp.setLastUpdateTime(
            spd.format(new Date(d.getTime() - (long) (offset * 60 * 60 * 1000))));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //time zone
    return mrWoTempRepository.updateMrWoTemp(mrWoTemp);
  }
}
