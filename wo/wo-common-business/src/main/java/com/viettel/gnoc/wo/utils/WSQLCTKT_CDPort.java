package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.wo.dto.HeaderForm;
import com.viettel.qlctkt_cd.webservice.GetListStaffResult;
import com.viettel.qlctkt_cd.webservice.MessageForm;
import com.viettel.qlctkt_cd.webservice.QLTWS;
import com.viettel.qlctkt_cd.webservice.ScTroubleForm;
import com.viettel.qlctkt_cd.webservice.Staff;
import com.viettel.qlctkt_cd.webservice.SysUsersBO;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Getter
@Setter
public class WSQLCTKT_CDPort {

  List<HeaderForm> lstHeader;
  String nationCoce;
  QLTWS port = null;

  @Value("${application.ws.user_qlctkt_cd:null}")
  private String userCd;
  @Value("${application.ws.pass_qlctkt_cd:null}")
  private String passCd;

  @Autowired
  WSQLCTKT_CDPortFactory wsqlctkt_cdPortFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SPM WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (QLTWS) wsqlctkt_cdPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SPM WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public List<Staff> getListPartnerStaff(String stationCode) throws Exception {
    List<Staff> res = null;
    try {
      String user = PassProtector.decrypt(userCd, WS_USERS.SALT);
      String pass = PassProtector.decrypt(passCd, WS_USERS.SALT);
      if (port == null) {
        createConnect();
      }
      if (port != null) {
//        if (this.lstHeader != null) {
//          SetHeaderWs.setHeaderHandler(port, lstHeader);
//        }
        GetListStaffResult listStaff = port.getListPartnerStaff(stationCode, user, pass);
        if (listStaff != null) {
          res = listStaff.getListStaff();
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsqlctkt_cdPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<SysUsersBO> getListUserByLocation(String locationCode) throws Exception {
    List<SysUsersBO> res = null;
    try {
      String user = PassProtector.decrypt(userCd, WS_USERS.SALT);
      String pass = PassProtector.decrypt(passCd, WS_USERS.SALT);
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getListUserByLocation(locationCode, user, pass);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsqlctkt_cdPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<Staff> getListStaff(String stationCode, String staffMajorCode, String usedMajorCode)
      throws Exception {
    List<Staff> res = null;
    try {
      String user = PassProtector.decrypt(userCd, WS_USERS.SALT);
      String pass = PassProtector.decrypt(passCd, WS_USERS.SALT);
      if (port == null) {
        createConnect();
      }
      if (port != null) {
//        if (this.lstHeader != null) {
//          SetHeaderWs.setHeaderHandler(port, lstHeader);
//        }
        GetListStaffResult listStaff = port.getListStaff(null, null, stationCode, user, pass);
        if (listStaff != null) {
          // neu co yeu cau loai nhan vien
          if ("1".equals(usedMajorCode) && StringUtils.isNotNullOrEmpty(staffMajorCode)
              && listStaff.getListStaff() != null) {
            String[] code = staffMajorCode.split(",");
            if (code != null && code.length > 0) {
              res = new ArrayList<Staff>();
              for (int i = 0; i < code.length; i++) {
                for (Staff s : listStaff.getListStaff()) {
                  if (code[i].equals(s.getStaffMajorCode())) {
                    res.add(s);
                  }
                }
              }

            }
          } else {
            res = listStaff.getListStaff();
          }

        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsqlctkt_cdPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public MessageForm getUserAssignByAccount(String account) throws Exception {
    MessageForm res = null;
    try {
      String user = PassProtector.decrypt(userCd, WS_USERS.SALT);
      String pass = PassProtector.decrypt(passCd, WS_USERS.SALT);
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ScTroubleForm troubleF = new ScTroubleForm();
        troubleF.setAccount(account);
        res = port.getUserAssignByAccount(troubleF, user, pass);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsqlctkt_cdPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
