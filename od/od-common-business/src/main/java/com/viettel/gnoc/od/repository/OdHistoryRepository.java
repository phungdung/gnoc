package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdHistoryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdHistoryRepository {

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto insertOrUpdate(OdHistoryDTO odDTO);

  String getSeqOdType();

  List<OdHistoryDTO> getOdHistoryByOdId(Long odId);

  String getSeqOHistory(String sequense);
}
