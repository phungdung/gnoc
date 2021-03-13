package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;


public interface WoFileTempBussiness {

  Datatable getListWoFileTemp(WoFileTempDto dto);

  WoFileTempDto getDetail(Long woFileTempId);

  List<WoTypeInsideDTO> getListWoTypeCBB();

  ResultInSideDto insertWoFileTemp(WoFileTempDto woFileTempDto);

  ResultInSideDto updateWoFileTemp(WoFileTempDto dto);

  ResultInSideDto deleteWoFileTempById(Long id);
}
