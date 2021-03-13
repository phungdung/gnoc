package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrUserCfgApprovedSmsBtsBusiness {

  Datatable getListMrUserCfgApprovedSmsBts(MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  MrUserCfgApprovedSmsBtsDTO getDetail(Long id);

  ResultInSideDto insertOrUpdate(MrUserCfgApprovedSmsBtsDTO smsBtsDTO);

  ResultInSideDto deleteMrUserCfgApprovedSmsBts(Long userCfgApprovedSmsId);

  UsersInsideDto getUserByUserId(Long userId);

  MrUserCfgApprovedSmsBtsDTO getApproveLevelByUserLogin();

  File exportData(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) throws Exception;

  ResultInSideDto importData(MultipartFile multipartFile) throws Exception;

  File getFileTemplate() throws Exception;

}
