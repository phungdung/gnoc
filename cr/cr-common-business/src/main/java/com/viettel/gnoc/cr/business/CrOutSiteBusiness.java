package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import java.util.List;

public interface CrOutSiteBusiness {

  ResultDTO insertAutoCr(CrDTO crDTO, List<CrFilesAttachDTO> lstFile,
      String system, String nationCode,
      String lstFtId,
      String userService, String passService);

  String actionCloseAutoCr(CrDTO cr, String userService, String passService);

  String actionResolveAutoCr(CrDTO cr, String userService, String passService);

  String getCrNumber(String crProcessId, String userService, String passService);

}
