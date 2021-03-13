package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
import java.util.List;

public interface CfgFollowWorkTimeBusiness {

  Datatable getListCfgFollowWorkTimePage(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO);

  ResultInSideDto insert(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO);

  ResultInSideDto update(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO);

  ResultInSideDto delete(Long configFollowWorkTimeId);

  CfgFollowWorkTimeDTO getDetail(Long configFollowWorkTimeId);

  List<CatItemDTO> getListItemByCategory(String systemCode);

  List<CatItemDTO> getListItemByCat(String systemCode);

}
