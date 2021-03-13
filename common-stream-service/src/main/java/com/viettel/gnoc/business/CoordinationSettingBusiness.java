package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;

public interface CoordinationSettingBusiness {

  ResultInSideDto deleteCoordinationSetting(Long id);

  Datatable getListCoordinationSettingPage(CoordinationSettingDTO coordinationSettingDTO);

  ResultInSideDto insert(CoordinationSettingDTO coordinationSettingDTO);

  ResultInSideDto update(CoordinationSettingDTO coordinationSettingDTO);

  CoordinationSettingDTO getDetail(Long id);
}
