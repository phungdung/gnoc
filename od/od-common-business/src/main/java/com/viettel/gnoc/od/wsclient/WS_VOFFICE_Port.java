/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

import com.viettel.voffice.ws_autosign.service.KttsVofficeCommInpuParam;
import com.viettel.voffice.ws_autosign.service.Vo2AutoSignSystemImpl;
import com.viettel.voffice.ws_autosign.service.Vof2EntityUser;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */

@Service
@Slf4j
public class WS_VOFFICE_Port {

  @Autowired
  WS_VOFFICE_PortFactory wsVofficePortFactory;

  Vo2AutoSignSystemImpl port = null;

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SPM WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (Vo2AutoSignSystemImpl) wsVofficePortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SPM WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  /*
  thuc hien trinh ky
  */
  public Long vo2RegDigitalDocByEmail(KttsVofficeCommInpuParam param) throws Exception {
    Long res = null;

    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.vo2RegDigitalDocByEmail(param);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVofficePortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  /*
   Lay danh sach nhan vien
   */
  public List<Vof2EntityUser> getListVof2UserByMail(List<String> lstUser) throws Exception {
    List<Vof2EntityUser> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getListVof2UserByMail(lstUser);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsVofficePortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
