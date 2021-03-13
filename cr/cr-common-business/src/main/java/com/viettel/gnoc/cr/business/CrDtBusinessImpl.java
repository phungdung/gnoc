/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import com.viettel.gnoc.cr.repository.CrDtRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class CrDtBusinessImpl implements CrDtBusiness {

  @Value("${application.conf.user_service:null}")
  private String userConfig;
  @Value("${application.conf.pass_service:null}")
  private String passConfig;
  @Value("${application.conf.salt_service:null}")
  private String saltService;

  @Autowired
  private CrDtRepository crDtRepository;

  @Override
  public ResultDTO insertVMSADT(
      String userService, String passService, String systemCode, Long crId, Long validateKey,
      int createMopSuccess, String createMopDetail, List<VMSAMopDetailDTO> mopDTOList,
      String nationCode, String locale) {
    Locale locale1 = new Locale(locale);
    if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("en", "US");
    } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("vi", "VN");
    } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("lo", "LA");
    }

    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);

    if (nationCode == null || nationCode.trim().isEmpty()) {
      nationCode = "VNM";
    }
    nationCode = nationCode.trim().toUpperCase();

    try {
      String userConf = userConfig;
      String passConf = passConfig;
      String saltConf = saltService;
      try {
        String user = PassProtector.encrypt(userService, saltConf);
        String pass = PassProtector.encrypt(passService, saltConf);
        if (!userConf.equals(user) || !passConf.equals(pass)) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(
              I18n.getLanguageByLocale(locale1, "qltn.invalidUserNameOrPassWord"));
          return resultDTO;
        }

      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      resultDTO = crDtRepository
          .insertVMSADT(crId, validateKey, systemCode, createMopSuccess, createMopDetail,
              mopDTOList, nationCode, locale);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
    }

    return resultDTO;
  }

  @Override
  public List<ItemDataCR> getAllActiveAffectedService(String userService, String passService,
      String locale) {
    try {
      try {
        String user = PassProtector.encrypt(userService, saltService);
        String pass = PassProtector.encrypt(passService, saltService);
        if (!userConfig.equals(user) || !passConfig.equals(pass)) {
          return null;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      return crDtRepository.getAllActiveAffectedService(locale);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return new ArrayList<>();
  }

}
