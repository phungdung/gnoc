package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.business.CrAutoServiceForSRBusiness;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrAutoServiceImplForSR implements CrAutoServiceForSR {

  @Value("${application.ws.user_service:null}")
  private String userConf;

  @Value("${application.ws.pass_service:null}")
  private String passConf;

  @Value("${application.ws.salt_service:null}")
  private String saltConf;
  @Autowired
  CrAutoServiceForSRBusiness crAutoServiceForSRBusiness;
  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO insertAutoCrForSR(CrDTO crDTO, List<CrFilesAttachDTO> lstFile, String system,
      String nationCode, List<WoDTO> lstWo, List<String> lstMop, List<String> lstNodeIp,
      String userService,
      String passService) {
    setLocale();
    ResultDTO rs = new ResultDTO();
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userConf.equals(user) && passConf.equals(pass)) {
        rs = crAutoServiceForSRBusiness
            .insertAutoCrForSR(crDTO, lstFile, system, nationCode, lstWo, lstMop, lstNodeIp);
      } else {
        rs.setMessage(I18n.getChangeManagement("crmobile.alert.wrongaccount"));
        rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public String getCrNumber(String crProcessId, String userService, String passService) {
    setLocale();
    try {
      String user = PassProtector.encrypt(userService, saltConf); //cr_service
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userConf.equals(user) && passConf.equals(pass)) {
        return crAutoServiceForSRBusiness.getCrNumber(crProcessId);
      } else {
        return I18n.getChangeManagement("crmobile.alert.wrongaccount");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private void setLocale() {
    I18n.setLocaleForService(wsContext);
  }

}
