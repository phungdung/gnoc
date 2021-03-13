package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import java.util.List;


public interface SRMopRepository {

  List<SRMopDTO> getListSRMopDTO(SRMopDTO dto);

  List<SRMopDTO> getListSRMopNotSR(SRMopDTO dto);

  ResultInSideDto insertListSRMop(List<SRMopDTO> lsSrMop);

  ResultInSideDto updateListSRMop(List<SRMopDTO> lsSrMop);

  ResultInSideDto deleteListSRMop(List<SRMopDTO> lsSrMop);
}
