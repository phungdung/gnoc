package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;

public interface CrAutoServiceForSRBusiness {

  ResultDTO insertAutoCrForSR(CrDTO crDTO, List<CrFilesAttachDTO> lstFile, String system,
      String nationCode, List<WoDTO> lstWo, List<String> lstMop, List<String> lstNodeIp);

  String getCrNumber(String crProcessId) throws Exception;

  GnocFileDto getFilePathSrCr(GnocFileDto gnocFileDto);

//  ResultDTO insertCrFileForMR(CrDTO crDTO, String system, List<String> lstMop);
}
