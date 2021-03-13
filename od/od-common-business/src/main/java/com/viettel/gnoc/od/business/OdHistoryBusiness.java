package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdHistoryDTO;
import java.util.List;

/**
 * @author NamTN
 */
public interface OdHistoryBusiness {

  ResultInSideDto add(OdDTO odDTO);

  ResultInSideDto edit(OdDTO odDTO);

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto insertOdHistory(OdDTO odDTO, Long oldStatus, Long newStatus, String content,
      UsersEntity user);


  ResultInSideDto insertOrUpdate(OdHistoryDTO odDTO);

  String getSeqOHistory();

  List<OdHistoryDTO> getOdHistoryByOdId(Long odId);
}
