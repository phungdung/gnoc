package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgFollowWorkTimeRepository {

  Datatable getListCfgFollowWorkTimePage(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO);

  ResultInSideDto insertOrUpdate(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO);

  ResultInSideDto delete(Long configFollowWorkTimeId);

  CfgFollowWorkTimeDTO getDetail(Long configFollowWorkTimeId);

}
