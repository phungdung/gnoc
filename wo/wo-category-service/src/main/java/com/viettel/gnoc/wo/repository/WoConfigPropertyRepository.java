package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import java.util.List;

public interface WoConfigPropertyRepository {

  BaseDto sqlSearch(WoConfigPropertyDTO configPropertyDTO, String export);

  Datatable getListConfigPropertyDTO(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto addConfigProperty(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto updateConfigProperty(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto delete(String key);

  WoConfigPropertyDTO getDetail(String key);

  List<WoConfigPropertyDTO> getListDataExport(WoConfigPropertyDTO configPropertyDTO);
}
