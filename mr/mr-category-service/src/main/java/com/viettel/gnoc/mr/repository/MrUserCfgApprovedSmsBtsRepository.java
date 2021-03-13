package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrUserCfgApprovedSmsBtsRepository {

  Datatable getListMrUserCfgApprovedSmsBts(MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  List<MrUserCfgApprovedSmsBtsDTO> onSearchExport(MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  MrUserCfgApprovedSmsBtsDTO getDetail(Long id);

  MrUserCfgApprovedSmsBtsDTO getApproveLevelByUserLogin(String userNameLogin);

  ResultInSideDto insertOrUpdate(MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  ResultInSideDto deleteMrUserCfgApprovedSmsBts(Long userCfgApprovedSmsId);

  List<CatLocationDTO> getLstProvinceNamebyCode(String provinceCode);

  List<MrUserCfgApprovedSmsBtsDTO> checkExisted(
      MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  List<MrUserCfgApprovedSmsBtsDTO> getLstCountryAreaProvince();

  List<MrUserCfgApprovedSmsBtsDTO> getListUserInSystem();

  List<ItemDataCRInside> getLstCountryMap();
}
