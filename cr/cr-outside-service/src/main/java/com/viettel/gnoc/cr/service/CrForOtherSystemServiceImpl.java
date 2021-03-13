package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.business.CrForOtherSystemBusiness;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrForOtherSystemServiceImpl implements CrForOtherSystemService {

  @Value("${application.conf.user_service: null}")
  private String userConf;

  @Value("${application.conf.pass_service: null}")
  private String passConf;

  @Value("${application.conf.salt_service: null}")
  private String saltConf;

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Override
  public List<CrCreatedFromOtherSysDTO> getListData(Long crId, Long systemId, Long objectId) {
    setLocale();
    return crForOtherSystemBusiness.getListData(crId, systemId, objectId);
  }

  @Override
  public List<CrCreatedFromOtherSysDTO> getListDataByObjectId(Long objectId) {
    setLocale();
    return crForOtherSystemBusiness.getListDataByObjectId(objectId);
  }

  @Override
  public CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(Long crId) {
    setLocale();
    return crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(crId);
  }

  @Override
  public boolean checkWoCloseAutoSetting(Long unitId, Long woTypeId) {
    setLocale();
    return crForOtherSystemBusiness.checkWoCloseAutoSetting(unitId, woTypeId);
  }

  @Override
  public SelectionResultDTO getlistCRFromTimeInterval(String userService, String passService,
      String minute) {
    setLocale();
    return crForOtherSystemBusiness.getCrlistFromTimeInterval(userService, passService, minute);
  }

  @Override
  public CrOutputForQLTNDTO getCrForQLTN(String userService, String passService, String crNumber) {
    setLocale();
    return crForOtherSystemBusiness.getCrForQLTN(userService, passService, crNumber);
  }

  @Override
  public List<CrOutputForOCSDTO> getCrForOCS(String userService, String passService,
      String userName, String startTime) {
    setLocale();
    return crForOtherSystemBusiness.getCrForOCS(userService, passService, userName, startTime);
  }

  @Override
  public List<CrOutputForSOCDTO> getListDeviceAffectForSOC(String lastUpdateTime) {
    setLocale();
    return crForOtherSystemBusiness.getListDeviceAffectForSOC(lastUpdateTime);
  }

  @Override
  public List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(String userService,
      String passService, String crNumber, String attachTime, String fileType) {
    setLocale();
    return crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
  }

  @Override
  public List<CrFileAttachOutput> getCrFileDTAttach(String crNumber, String attachTime) {
    setLocale();
    return crForOtherSystemBusiness.getCrFileDTAttach(crNumber, attachTime);
  }

  @Override
  public ResultDTO createCRTraceFileAttach(String userService, String passService, String username,
      String crId, String fileType, String fileName, String fileContent) {

    try {
      setLocale();
      ResultDTO resultDTO = crForOtherSystemBusiness
          .createCRTraceFileAttach(userService, passService, username, crId, fileType, fileName,
              fileContent);
      return resultDTO;
    } catch (Exception ex) {
      ResultDTO rs = new ResultDTO();
      rs.setKey("NOK");
      rs.setMessage(ex.getMessage());
      return rs;
    }
//    return resultDTO;
  }

  @Override
  public String actionResolveCrOcs(String userService, String passService, String userName,
      String crNumber, String returnCode) {
    setLocale();
    return crForOtherSystemBusiness
        .actionResolveCrOcs(userService, passService, userName, crNumber, returnCode,
            I18n.getLocale());
  }

  @Override
  public ResultDTO insertFile(String userService, String passService, String userName,
      String crNumber, String fileType, String fileName, String fileContent) {
    setLocale();
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
  }

  @Override
  public ResultDTO updateDtInfo(String userService, String passService, String userName,
      String crNumber, String dtCode, List<String> lstIpImpact, List<String> lstIpAffect,
      String mopFile, String mopFileContent, String mopRollbackFile, String mopRollbackFileContent,
      List<String> lstAffectService, String nationCode) {
    setLocale();
    return crForOtherSystemBusiness
        .updateDtInfo(userService, passService, userName, crNumber, dtCode, lstIpImpact,
            lstIpAffect, mopFile, mopFileContent, mopRollbackFile, mopRollbackFileContent,
            lstAffectService, nationCode);
  }

  @Override
  public ResultDTO createCRTrace(String userService, String passService, CrDTO crDTO) {
    setLocale();
    try {
      return crForOtherSystemBusiness
          .createCRTrace(userService, passService, crDTO).toResultDTO();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      ResultDTO rs = new ResultDTO();
      rs.setKey("NOK");
      rs.setMessage(ex.getMessage());
      return rs;
    }
  }

  @Override
  public CrOutputForQLTNDTO getCrByCode(String userService, String passService, String crNumber) {
    setLocale();
    return crForOtherSystemBusiness.getCrForByCode(userService, passService, crNumber);
  }

  private void setLocale() {
    I18n.setLocaleForService(wsContext);
  }

}
