package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrForOtherSystemRepository {

  List<CrCreatedFromOtherSysDTO> getListDataByObjectId(Long objectId);

  List<CrCreatedFromOtherSysDTO> getListData(Long crId, Long systemId, Long objectId);

  CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(Long crId);

  boolean checkWoCloseAutoSetting(Long unitId, Long woTypeId);

  CrOutputForQLTNDTO getCrForQLTN(String crNumber, String type);

  List<CrOutputForOCSDTO> getCrForOCS(String userName, String startTime);

  List<CrOutputForSOCDTO> getListDeviceAffectForSOC(String lastUpdateTime);

  List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(String crNumber,
      String attachTime, String fileType);

  List<CrFileAttachOutput> getCrFileDTAttach(String crNumber, String attachTime);

  SelectionResultDTO getCrlistFromTimeInterval(double minute);

  String createWorklogResolveCR(String userName, String wlgText, Long crId, Long userId);

  UsersDTO getUserInfo(String userName);

  CrInsiteDTO getCrById(Long crId);

  String actionResolveCr(CrInsiteDTO crDTO, String locale);

  String createMapFile(String username, String crId, String fileType, String fileName,
      String fileContent);

  ResultDTO insertFile(String userName, String crNumber, String fileType, String fileName,
      String fileContent);

  CrInsiteDTO getCrInfo(String crNumber);

  ResultDTO updateDtInfo(String userName, String crNumber, String dtCode,
      List<String> lstIpImpact, List<String> lstIpAffect, String mopFile, String mopFileContent,
      String mopRollbackFile, String mopRollbackFileContent, List<String> lstAffectService,
      String nationCode);

  String setCrInfor(CrDTO crDTO);

  ResultInSideDto saveObjectSession(CrHisDTO crHisDTO);

  ResultInSideDto saveObject(CrDTO crDTO);
}
