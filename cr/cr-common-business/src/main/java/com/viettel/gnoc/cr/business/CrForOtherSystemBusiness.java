package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import java.util.List;

public interface CrForOtherSystemBusiness {

  List<CrCreatedFromOtherSysDTO> getListDataByObjectId(Long objectId);

  List<CrCreatedFromOtherSysDTO> getListData(Long crId, Long systemId, Long objectId);

  CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(Long crId);

  boolean checkWoCloseAutoSetting(Long unitId, Long woTypeId);

  CrOutputForQLTNDTO getCrForQLTN(String userService, String passService, String crNumber);

  List<CrOutputForOCSDTO> getCrForOCS(String userService, String passService, String userName,
      String startTime);

  List<CrOutputForSOCDTO> getListDeviceAffectForSOC(String lastUpdateTime);

  List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(String userService,
      String passService, String crNumber,
      String attachTime, String fileType);

  List<CrFileAttachOutput> getCrFileDTAttach(String crNumber, String attachTime);

  SelectionResultDTO getCrlistFromTimeInterval(String userService, String passService,
      String minute);

  String actionResolveCrOcs(String userService, String passService, String userName,
      String crNumber, String returnCode, String locale);

  ResultDTO insertFile(String userName, String crNumber, String fileType, String fileName,
      String fileContent);

  ResultDTO createCRTraceFileAttach(String userService, String passService, String username,
      String crId, String fileType, String fileName, String fileContent);

  ResultDTO updateDtInfo(String userService, String passService, String userName, String crNumber,
      String dtCode,
      List<String> lstIpImpact, List<String> lstIpAffect, String mopFile, String mopFileContent,
      String mopRollbackFile, String mopRollbackFileContent, List<String> lstAffectService,
      String nationCode);

  ResultInSideDto createCRTrace(String userService, String passService, CrDTO crDTO);

  CrOutputForQLTNDTO getCrForByCode(String userService, String passService, String crNumber);

  ResultInSideDto actionVerifyMrIT(CrInsiteDTO crDTO, String locale);
}
