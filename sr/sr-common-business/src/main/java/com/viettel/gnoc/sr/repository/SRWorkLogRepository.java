package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import java.util.List;


public interface SRWorkLogRepository {

  List<SRWorkLogDTO> getListSRWorklogWithUnit(Long srId);

  List<SRWorklogTypeDTO> getBySRStatus(SRWorklogTypeDTO dto);

  ResultInSideDto insertSRWorklog(SRWorkLogDTO srWorklogDTO);

  List<SRWorklogTypeDTO> getListSRWorklogTypeDTO(SRWorklogTypeDTO dto, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<SRReasonRejectDTO> getReasonByStatus(Long wlTypeId);

  int checkInputWorklog(Long srId);
}
