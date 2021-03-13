package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import java.util.List;


public interface SRCreatedFromOtherSysRepository {

  List<SRCreatedFromOtherSysDTO> getListSRCreatedFromOtherSysDTO(SRCreatedFromOtherSysDTO dto);

  ResultInSideDto insertSRCreateFromOtherSys(SRCreatedFromOtherSysDTO dto);
}
