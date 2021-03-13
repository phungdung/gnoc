package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinationSettinggRepository {

  Datatable getListCoordinationSettingPage(CoordinationSettingDTO coordinationSettingDTO);

  ResultInSideDto insertOrUpdate(CoordinationSettingDTO coordinationSettingDTO);

  CoordinationSettingDTO getDetail(Long id);

  ResultInSideDto deleteCoordinationSetting(Long id);

}
