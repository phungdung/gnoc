package com.viettel.gnoc.incident.utils;

import com.viettel.nims.infra.webservice.InfraSleevesBO;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.VCntCableLinkBO;
import com.viettel.nims.infra.webservice.VInfraCableWsBO;
import java.net.MalformedURLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSNIMSInfraPort {

  InfraWS port = null;

  @Autowired
  WSNIMSInfraPortFactory wsnimsInfraPortFactory;


  private void createConnect() throws MalformedURLException, Exception {
    //System.out.println("Start create connect NIMS_Infra WS ");
//        long startTime = Calendar.getInstance().getTimeInMillis();
    port = (InfraWS) wsnimsInfraPortFactory.getWsFactory().borrowObject();
    //System.out.println("End create connect NIMS_Infra WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  public List<VInfraCableWsBO> getCableInLanes(String s) throws Exception {
    List<VInfraCableWsBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getCableInLanes(s);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<VCntCableLinkBO> getLinkInCables(List<String> s) throws Exception {
    List<VCntCableLinkBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getLinkInCables(s);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<InfraSleevesBO> getSleevesInCable(String sleeveCode, Long purpose, String cableCode)
      throws Exception {
    List<InfraSleevesBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getSleevesInCable(sleeveCode, purpose, cableCode);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<InfraSleevesBO> getInfraSleevesBO(Long sleeveId, String sleeveCode, Long purpose)
      throws Exception {
    List<InfraSleevesBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getInfraSleevesBO(sleeveId, sleeveCode, purpose);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}

