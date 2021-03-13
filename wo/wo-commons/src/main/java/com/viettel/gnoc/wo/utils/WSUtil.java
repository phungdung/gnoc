package com.viettel.gnoc.wo.utils;

import com.viettel.bccs.cc.service.SpmServiceImpl;
import com.viettel.bccs.cc.service.SpmServiceImplService;
import com.viettel.bccs.inventory.service.InventoryService;
import com.viettel.bccs.inventory.service.InventoryServiceImplService;
import com.viettel.cc.webserivce.CcServiceForNTMS;
import com.viettel.cc.webserivce.CcServiceForNTMS_Service;
import com.viettel.eee2ws.webservice.CPWS;
import com.viettel.eee2ws.webservice.CPWS_Service;
import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.ipcc.services.CallService;
import com.viettel.ipcc.services.CallService_Service;
import com.viettel.ipcc.unitel.services.CallServicePortType;
import com.viettel.nims.webservice.cd.BccsWS;
import com.viettel.nims.webservice.cd.BccsWS_Service;
import com.viettel.nims.webservice.ht.InfraWS;
import com.viettel.nims.webservice.ht.InfraWS_Service;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.nocproV4.NocproWebservice_Service;
import com.viettel.payment.services.PaymentServices;
import com.viettel.payment.services.PaymentServicesService;
import com.viettel.qlctkt_cd.webservice.QLTWS;
import com.viettel.qlctkt_cd.webservice.QLTWS_Service;
import com.viettel.qldtktts.service2.VSmartService;
import com.viettel.qldtktts.service2.VSmartService_Service;
import com.viettel.soc.spm.service.ServiceProblemService;
import com.viettel.soc.spm.service.ServiceProblemServiceImplService;
import com.viettel.spm.analys.webservice.AnalysProcessServiceImplService;
import com.viettel.webservice.cc_stl.BCCSGateway;
import com.viettel.webservice.cc_stl.ServiceManagement;
import com.viettel.webservice.codien.EEE2WS;
import com.viettel.webservice.codien.EEE2WS_Service;
import com.viettel.webservice.imes.QltnConcentrateWS;
import com.viettel.webservice.imes.QltnConcentrateWS_Service;
import com.viettel.webservice.qlctkt.bccs.BccsService;
import com.viettel.webservice.qlctkt.bccs.BccsService_Service;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WSUtil {

  @Value("${application.ws.service_url_ipcc:null}")
  public String SERVICE_URL_IPCC;
  @Value("${application.ws.service_url_cc:null}")
  public String SERVICE_URL_CC;
  @Value("${application.ws.bccs2_service_url:null}")
  public String SERVICE_URL_BCCS_2_0;
  @Value("${application.ws.bccs2_service_username:null}")
  public String bccs2_service_username;
  @Value("${application.ws.bccs2_service_pass:null}")
  public String bccs2_service_pass;
  @Value("${application.ws.service_url_spm:null}")
  public String SERVICE_URL_SPM;
  @Value("${application.ws.service_url_payment:null}")
  public String SERVICE_URL_PAYMENT;
  @Value("${application.ws.service_url_ktts2:null}")
  public String SERVICE_URL_KTTS2;
  @Value("${application.ws.bccs.im.inventory.url:null}")
  public String SERVICE_URL_IM_INVENTORY;
  @Value("${application.ws.bccs.im.inventory.targetNamespace:null}")
  public String inventoryTargetNamespace;
  @Value("${application.ws.bccs.im.inventory.name:null}")
  public String inventoryName;
  @Value("${application.ws.bccs.im.inventory.username:null}")
  public String inventoryUserName;
  @Value("${application.ws.bccs.im.inventory.password:null}")
  public String inventoryPassword;
  @Value("${application.ws.nims.bccs.url:null}")
  public String SERVICE_URL_NIMS_CD;
  @Value("${application.ws.nims.cd.bccs2.url:null}")
  public String SERVICE_URL_NIMS_CD_BCCS2;
  @Value("${application.ws.nims.ht.url:null}")
  public String SERVICE_URL_NIMS_HT;
  @Value("${application.ws.qlctkt.url:null}")
  public String SERVICE_URL_QLCTKT_CD;
  @Value("${application.ws.nocpro_service_url:null}")
  public String SERVICE_URL_NOCPRO;
  @Value("${application.ws.qlctkt.bccs.vtt.url:null}")
  public String SERVICE_URL_QLCTKT_BCCS;
  @Value("${application.ws.chi.phi.url:null}")
  public String SERVICE_URL_CHI_PHI;
  @Value("${application.ws.co.dien.url:null}")
  public String SERVICE_URL_CO_DIEN;
  @Value("${application.ws.spm.analys.url:null}")
  public String SERVICE_URL_SPM_ANALYS;
  @Value("${application.ws.cc.stl.url:null}")
  public String SERVICE_URL_CC_STL;
  @Value("${application.ws.nims.ht.global.url:null}")
  public String SERVICE_URL_NIMS_HT_GLOBAL;
  @Value("${application.ws.nims.cd.global.url:null}")
  public String SERVICE_URL_NIMS_CD_GLOBAL;
  @Value("${application.ws.service_url_vipadd:null}")
  public String SERVICE_URL_VIPA_DD;
  @Value("${application.ws.imes.url:null}")
  public String SERVICE_URL_IMES;
/// link WS GNOC 1 _ WO
  @Value("${application.ws.gnoc1_wo:null}")
  public String SERVICE_URL_GNOC1_WO;


  public final int CONNECT_TIMEOUT = 60000;//ms
  public final int REQUEST_TIMEOUT = 60000;//ms


  public com.viettel.gnoc1.service.WoServices getGnoc1WoPort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.gnoc1.service.WoServicesImplService service;
      baseUrl = com.viettel.gnoc1.service.WoServicesImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_GNOC1_WO);
      service = new com.viettel.gnoc1.service.WoServicesImplService(url,
          new QName("http://service.wfm.gnoc.viettel.com/", "WoServicesImplService"));
      return (com.viettel.gnoc1.service.WoServices) setTimeOut(service.getWoServicesImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }


  public com.viettel.webservice.nims_cd_global.BccsWS getNIMSCD_GlobalService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.webservice.nims_cd_global.BccsWS_Service service;
      baseUrl = com.viettel.webservice.nims_cd_global.BccsWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_CD_GLOBAL);
      service = new com.viettel.webservice.nims_cd_global.BccsWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "BccsWS"));
      return (com.viettel.webservice.nims_cd_global.BccsWS) setTimeOut(service.getBccsWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.webservice.nims_ht_global.InfraWS getNimsGlobalPort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.webservice.nims_ht_global.InfraWS_Service service;
      baseUrl = com.viettel.webservice.nims_ht_global.InfraWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_HT_GLOBAL);
      service = new com.viettel.webservice.nims_ht_global.InfraWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "InfraWS"));
      return (com.viettel.webservice.nims_ht_global.InfraWS) setTimeOut(service.getInfraWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public ServiceManagement getCcStlPort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      BCCSGateway service;
      baseUrl = BCCSGateway.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_CC_STL);
      service = new BCCSGateway(url,
          new QName("http://webservice.bccsgw.viettel.com/", "BCCSGateway"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(null, null, "CC_STL");
      service.setHandlerResolver(handlerResolver);
      return (ServiceManagement) setTimeOut(service.getBCCSGatewaySOAP());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.spm.analys.webservice.AnalysProcessService getSpmAnalysPort()
      throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.spm.analys.webservice.AnalysProcessServiceImplService service;
      baseUrl = AnalysProcessServiceImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_SPM_ANALYS);
      service = new AnalysProcessServiceImplService(url,
          new QName("http://service.spm.soc.viettel.com/", "AnalysProcessServiceImplService"));
      return (com.viettel.spm.analys.webservice.AnalysProcessService) setTimeOut(
          service.getAnalysProcessServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public EEE2WS getCoDienPort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      EEE2WS_Service service;
      baseUrl = EEE2WS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_CO_DIEN);
      service = new EEE2WS_Service(url,
          new QName("http://webservice.eee2ws.viettel.com/", "EEE2WS"));
      return (EEE2WS) setTimeOut(service.getEEE2WSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public CPWS getChiPhiWS() throws Exception {
    try {
      URL url;
      URL baseUrl;
      CPWS_Service service;
      baseUrl = CPWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_CHI_PHI);
      service = new CPWS_Service(url,
          new QName("http://webservice.cpws.viettel.com/", "CPWS"));
      return (CPWS) setTimeOut(service.getCPWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public CallServicePortType getIpccWS_STL(String link) throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.ipcc.unitel.services.CallService service;
      baseUrl = com.viettel.ipcc.unitel.services.CallService.class.getResource(".");
      url = new URL(baseUrl, link != null ? link : SERVICE_URL_IPCC);
      service = new com.viettel.ipcc.unitel.services.CallService(url,
          new QName("http://ipcc.itpro.vn", "CallService"));
      return (CallServicePortType) setTimeOut(service.getCallServiceHttpSoap11Endpoint());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public BccsService getQLCTKT_BCCSService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      BccsService_Service service;
      baseUrl = BccsService_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_QLCTKT_BCCS);
      service = new BccsService_Service(url,
          new QName("http://bccs.qlctkt.viettel.com/", "BccsService"));
      return (BccsService) setTimeOut(service.getBccsServicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public NocproWebservice getNocProService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      NocproWebservice_Service service;
      baseUrl = NocproWebservice_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NOCPRO);
      service = new NocproWebservice_Service(url,
          new QName("http://service.nocpro.nms.viettel.com/", "NocproWebservice"));
      return (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public QLTWS getQLCTKTCDService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      QLTWS_Service service;
      baseUrl = QLTWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_QLCTKT_CD);
      service = new QLTWS_Service(url,
          new QName("http://webservice.qlctkt.viettel.com/", "QLTWS"));
      return (QLTWS) setTimeOut(service.getQLTWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public InfraWS getNIMSHTService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      InfraWS_Service service;
      baseUrl = InfraWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_HT);
      log.info("getNIMSHTService() URL: " + SERVICE_URL_NIMS_HT);
      service = new InfraWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "InfraWS"));
      return (InfraWS) setTimeOut(service.getInfraWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.nims.webservice.cd.bccs2.BccsWS getNIMSCD_BCCS2_Service() throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.nims.webservice.cd.bccs2.BccsWS_Service service;
      baseUrl = com.viettel.nims.webservice.cd.bccs2.BccsWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_CD_BCCS2);
      service = new com.viettel.nims.webservice.cd.bccs2.BccsWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "BccsWS"));
      return (com.viettel.nims.webservice.cd.bccs2.BccsWS) setTimeOut(service.getBccsWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public BccsWS getNIMSCDService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      BccsWS_Service service;
      baseUrl = BccsWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_CD);
      service = new BccsWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "BccsWS"));
      return (BccsWS) setTimeOut(service.getBccsWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public InventoryService getIMInventoryWs() throws Exception {
    try {
      URL url;
      URL baseUrl;
      InventoryServiceImplService service;
      baseUrl = InventoryServiceImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_IM_INVENTORY);
      service = new InventoryServiceImplService(url,
          new QName(inventoryTargetNamespace, inventoryName));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(
          PassProtector.decrypt(inventoryUserName, WS_USERS.SALT),
          PassProtector.decrypt(inventoryPassword, WS_USERS.SALT),
          "BCCS_IM"
      );
      service.setHandlerResolver(handlerResolver);
      return (InventoryService) setTimeOut(service.getInventoryServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public CallService getIpccWS(String link) throws Exception {
    try {
      URL url;
      URL baseUrl;
      CallService_Service service;
      baseUrl = CallService_Service.class.getResource(".");
      url = new URL(baseUrl, link != null ? link : SERVICE_URL_IPCC);
      service = new CallService_Service(url,
          new QName("http://services.ipcc.viettel.com/", "CallService"));
      return (CallService) setTimeOut(service.getCallServicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public CcServiceForNTMS getCCWS() throws Exception {
    try {
      URL url;
      URL baseUrl;
      CcServiceForNTMS_Service service;
      baseUrl = CcServiceForNTMS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_CC);
      service = new CcServiceForNTMS_Service(url,
          new QName("http://webserivce.cc.viettel.com/", "CcServiceForNTMS"));
      return (CcServiceForNTMS) setTimeOut(service.getCcServiceForNTMSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public SpmServiceImpl getBccs2Service() throws Exception {
    try {
      URL url;
      URL baseUrl;
      SpmServiceImplService service;
      baseUrl = SpmServiceImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_BCCS_2_0);
      service = new SpmServiceImplService(url,
          new QName("http://service.cc.bccs.viettel.com/", "SpmServiceImplService"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(
          PassProtector.decrypt(bccs2_service_username, WS_USERS.SALT),
          PassProtector.decrypt(bccs2_service_pass, WS_USERS.SALT),
          "SPM"
      );
      service.setHandlerResolver(handlerResolver);

      return (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public ServiceProblemService getSPMWS() throws Exception {
    try {
      URL url;
      URL baseUrl;
      ServiceProblemServiceImplService service;
      baseUrl = ServiceProblemServiceImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_SPM);
      service = new ServiceProblemServiceImplService(url,
          new QName("http://service.spm.soc.viettel.com/", "ServiceProblemServiceImplService"));
      return (ServiceProblemService) setTimeOut(service.getServiceProblemServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public PaymentServices getPaymentWS() throws Exception {
    try {
      URL url;
      URL baseUrl;
      PaymentServicesService service;
      baseUrl = PaymentServicesService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_PAYMENT);
      service = new PaymentServicesService(url,
          new QName("http://services.payment.viettel.com/", "PaymentServicesService"));
      return (PaymentServices) setTimeOut(service.getPaymentServicesPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public VSmartService getKTTSWS2() throws Exception {
    try {
      URL url;
      URL baseUrl;
      VSmartService_Service service;
      baseUrl = VSmartService_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_KTTS2);
      service = new VSmartService_Service(url,
          new QName("http://webservice.qldtktts.viettel.com/", "VSmartService"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(null, null, "KTTS");
      service.setHandlerResolver(handlerResolver);
      return (VSmartService) setTimeOut(service.getVSmartServicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.vmsa.WSForGNOC getVIPADDWS() throws Exception {
    try {
      URL baseUrl = com.viettel.vmsa.WSForGNOCImplService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_VIPA_DD); //
      com.viettel.vmsa.WSForGNOCImplService service = new com.viettel.vmsa.WSForGNOCImplService(url,
          new QName("http://webservice.viettel.com/", "WSForGNOCImplService"));
      return (com.viettel.vmsa.WSForGNOC) setTimeOut(service.getWSForGNOCImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public QltnConcentrateWS getImesPort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      QltnConcentrateWS_Service service;
      baseUrl = QltnConcentrateWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_IMES);
      service = new QltnConcentrateWS_Service(url,
          new QName("http://webservice.viettel.com/", "QltnConcentrateWS"));

      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(null, null, "IMES");
      service.setHandlerResolver(handlerResolver);

      return (QltnConcentrateWS) setTimeOut(service.getQltnConcentrateWSPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.webservice.bccs_cc2_global.SpmServiceImpl getBccs2GlobalService(String link,
      String nation) throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.webservice.bccs_cc2_global.SpmServiceImplService service;
      baseUrl = com.viettel.webservice.bccs_cc2_global.SpmServiceImplService.class.getResource(".");
      url = new URL(baseUrl, link);
      service = new com.viettel.webservice.bccs_cc2_global.SpmServiceImplService(url,
          new QName("http://service.cc.bccs.viettel.com/", "SpmServiceImplService"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(
          PassProtector.decrypt(bccs2_service_username, WS_USERS.SALT),
          PassProtector.decrypt(bccs2_service_pass, WS_USERS.SALT),
          "BCCS_CC_" + nation
      );
      service.setHandlerResolver(handlerResolver);
      return (com.viettel.webservice.bccs_cc2_global.SpmServiceImpl) setTimeOut(
          service.getSpmServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.webservice.inventory2.global.InventoryService getIMInventoryGlobalPort(
      String wsLink, String nationCode) throws Exception {
    try {
      URL url;
      URL baseUrl;
      com.viettel.webservice.inventory2.global.InventoryServiceImplService service;
      baseUrl = com.viettel.webservice.inventory2.global.InventoryServiceImplService.class
          .getResource(".");
      url = new URL(baseUrl, wsLink);
      service = new com.viettel.webservice.inventory2.global.InventoryServiceImplService(url,
          new QName(inventoryTargetNamespace, inventoryName));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(
          PassProtector.decrypt(inventoryUserName, WS_USERS.SALT),
          PassProtector.decrypt(inventoryPassword, WS_USERS.SALT),
          "BCCS_IM" + nationCode
      );
      service.setHandlerResolver(handlerResolver);
      return (com.viettel.webservice.inventory2.global.InventoryService) setTimeOut(
          service.getInventoryServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

}
