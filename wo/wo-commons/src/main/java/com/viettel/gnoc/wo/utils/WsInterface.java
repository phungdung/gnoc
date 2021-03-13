package com.viettel.gnoc.wo.utils;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.bccs.cc.service.SpmServiceImpl;
import com.viettel.bccs.inventory.service.InventoryService;
import com.viettel.cc.webserivce.CcServiceForNTMS;
import com.viettel.eee2ws.webservice.CPWS;
import com.viettel.ipcc.services.CallService;
import com.viettel.ipcc.unitel.services.CallServicePortType;
import com.viettel.nims.webservice.cd.BccsWS;
import com.viettel.nims.webservice.ht.InfraWS;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.payment.services.PaymentServices;
import com.viettel.qlctkt_cd.webservice.QLTWS;
import com.viettel.qldtktts.service2.VSmartService;
import com.viettel.soc.spm.service.ServiceProblemService;
import com.viettel.spm.analys.webservice.AnalysProcessService;
import com.viettel.vmsa.WSForGNOC;
import com.viettel.webservice.cc_stl.ServiceManagement;
import com.viettel.webservice.codien.EEE2WS;
import com.viettel.webservice.imes.QltnConcentrateWS;
import com.viettel.webservice.qlctkt.bccs.BccsService;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WsInterface {

  @Value("${application.ws.recvTimeoutNTMS:null}")
  private String recvTimeoutNTMS;
  @Value("${application.ws.connectTimeoutNTMS:null}")
  private String connectTimeoutNTMS;

  @Autowired
  WSUtil wsUtil;

  public <T> T createPort(String wsName, String wsLink) {
    if ("IPCC".equalsIgnoreCase(wsName)) {
      return (T) createConnectIPCC(wsLink);
    } else if ("CC".equalsIgnoreCase(wsName)) {
      return (T) createConnectCC();
    } else if ("BCCS_CC".equalsIgnoreCase(wsName)) {
      return (T) createConnectCC2();
    } else if ("SPM".equalsIgnoreCase(wsName)) {
      return (T) createConnectSPM();
    } else if ("PAYMENT".equalsIgnoreCase(wsName)) {
      return (T) createConnectPayment();
    } else if ("KTTS2".equalsIgnoreCase(wsName)) {
      return (T) createConnectKTTS2();
    } else if ("IMInventory".equalsIgnoreCase(wsName)) {
      return (T) createConnectIMInventory();
    } else if ("NIMS_CD".equalsIgnoreCase(wsName)) {
      return (T) createConnectNIMSCD();
    } else if ("NIMS_CD_BCCS2".equalsIgnoreCase(wsName)) {
      return (T) createConnectNIMSCD_BCCS2();
    } else if ("NIMS_HT".equalsIgnoreCase(wsName)) {
      return (T) createConnectNIMSHT();
    } else if ("NOCPRO".equalsIgnoreCase(wsName)) {
      return (T) createConnectNocPro();
    } else if ("QLCTKT_CD".equalsIgnoreCase(wsName)) {
      return (T) createConnectQLCTKTCD();
    } else if ("WS_QLCTKT_BCCS".equalsIgnoreCase(wsName)) {
      return (T) createConnectQLCTKT_BCCS();
    } else if ("IPCC_STL".equalsIgnoreCase(wsName)) {
      return (T) createConnectIPCC_STL(wsLink);
    } else if ("CHI_PHI".equalsIgnoreCase(wsName)) {
      return (T) createConnectCHI_PHI();
    } else if ("CO_DIEN".equalsIgnoreCase(wsName)) {
      return (T) createConnectCO_DIEN();
    } else if ("SPM_ANALYS".equalsIgnoreCase(wsName)) {
      return (T) createConnectSPM_ANALYS();
    } else if ("BCCS_CC_STL".equalsIgnoreCase(wsName)) {
      return (T) createConnectCcStl();
    } else if ("NIMS_HT_GLOBAL".equalsIgnoreCase(wsName)) {
      return (T) createConnectNimsGlobal();
    } else if ("NIMS_CD_GLOBAL".equalsIgnoreCase(wsName)) {
      return (T) createConnectNIMSCD_GLOBAL();
    } else if ("WS_SAP".equalsIgnoreCase(wsName)) {
      return (T) createConnectVIPADD();
    } else if ("WS_IMES".equalsIgnoreCase(wsName)) {
      return (T) createConnectIMES();
    }else if ("GNOC1_WO".equalsIgnoreCase(wsName)) {
      return (T) createConnectGNOC1_WO();
    }
    else {
      return null;
    }
  }

  public <T> T createPort(String wsName, String wsLink, String nationCode) {
    if ("BCCS_CC_GLOBAL".equalsIgnoreCase(wsName)) {
      return (T) createConnectCC_Global(wsLink, nationCode);
    } else if ("IMInventory_Global".equalsIgnoreCase(wsName)) {
      return (T) createConnectIMInventoryGlobal(wsLink, nationCode);
    } else {
      return null;
    }

  }

  private com.viettel.webservice.nims_cd_global.BccsWS createConnectNIMSCD_GLOBAL() {
    try {
      com.viettel.webservice.nims_cd_global.BccsWS port = wsUtil.getNIMSCD_GlobalService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private com.viettel.webservice.nims_ht_global.InfraWS createConnectNimsGlobal() {
    try {
      com.viettel.webservice.nims_ht_global.InfraWS port = wsUtil.getNimsGlobalPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private ServiceManagement createConnectCcStl() {
    try {
      ServiceManagement port = wsUtil.getCcStlPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private AnalysProcessService createConnectSPM_ANALYS() {
    try {
      AnalysProcessService port = wsUtil.getSpmAnalysPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private EEE2WS createConnectCO_DIEN() {
    try {
      EEE2WS port = wsUtil.getCoDienPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private CPWS createConnectCHI_PHI() {
    try {
      CPWS port = wsUtil.getChiPhiWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private CallServicePortType createConnectIPCC_STL(String ipccWsLink) {
    try {
      CallServicePortType port = wsUtil.getIpccWS_STL(ipccWsLink);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private BccsService createConnectQLCTKT_BCCS() {
    try {
      BccsService port = wsUtil.getQLCTKT_BCCSService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private QLTWS createConnectQLCTKTCD() {
    try {
      QLTWS port = wsUtil.getQLCTKTCDService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private NocproWebservice createConnectNocPro() {
    try {
      NocproWebservice port = wsUtil.getNocProService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private InfraWS createConnectNIMSHT() {
    try {
      InfraWS port = wsUtil.getNIMSHTService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private com.viettel.nims.webservice.cd.bccs2.BccsWS createConnectNIMSCD_BCCS2() {
    try {
      com.viettel.nims.webservice.cd.bccs2.BccsWS port = wsUtil.getNIMSCD_BCCS2_Service();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private BccsWS createConnectNIMSCD() {
    try {
      BccsWS port = wsUtil.getNIMSCDService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private InventoryService createConnectIMInventory() {
    try {
      InventoryService port = wsUtil.getIMInventoryWs();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private VSmartService createConnectKTTS2() {
    try {
      VSmartService port = wsUtil.getKTTSWS2();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private PaymentServices createConnectPayment() {
    try {
      PaymentServices port = wsUtil.getPaymentWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private ServiceProblemService createConnectSPM() {
    try {
      ServiceProblemService port = wsUtil.getSPMWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private SpmServiceImpl createConnectCC2() {
    try {
      SpmServiceImpl port = wsUtil.getBccs2Service();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private CcServiceForNTMS createConnectCC() {
    try {
      CcServiceForNTMS port = wsUtil.getCCWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private CallService createConnectIPCC(String ipccWsLink) {
    try {
      CallService port = wsUtil.getIpccWS(ipccWsLink);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }


  private WSForGNOC createConnectVIPADD() {
    try {
      WSForGNOC port = wsUtil.getVIPADDWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private QltnConcentrateWS createConnectIMES() {
    try {
      QltnConcentrateWS port = wsUtil.getImesPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private com.viettel.gnoc1.service.WoServices createConnectGNOC1_WO() {
    try {
      com.viettel.gnoc1.service.WoServices port = wsUtil.getGnoc1WoPort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private com.viettel.webservice.bccs_cc2_global.SpmServiceImpl createConnectCC_Global(String link,
      String nationCode) {
    try {
      com.viettel.webservice.bccs_cc2_global.SpmServiceImpl port = wsUtil
          .getBccs2GlobalService(link, nationCode);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

  private com.viettel.webservice.inventory2.global.InventoryService createConnectIMInventoryGlobal(
      String wsLink, String nationCode) {
    try {
      com.viettel.webservice.inventory2.global.InventoryService port = wsUtil
          .getIMInventoryGlobalPort(wsLink, nationCode);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(recvTimeoutNTMS));
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(connectTimeoutNTMS));
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }

  }

}
