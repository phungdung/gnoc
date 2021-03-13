package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import java.io.File;

public interface WoConfigPropertyBusiness {

  Datatable getListConfigPropertyDTO(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto addConfigProperty(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto updateConfigProperty(WoConfigPropertyDTO configPropertyDTO);

  ResultInSideDto delete(String key);

  File exportData(WoConfigPropertyDTO lstData) throws Exception;

  WoConfigPropertyDTO getDetail(String key);
}
