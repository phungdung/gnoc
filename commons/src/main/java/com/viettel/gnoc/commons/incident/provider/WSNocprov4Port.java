/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.dto.CrAlarmDeviceType;
import com.viettel.gnoc.commons.dto.CrAlarmDraffDTO;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrAlarmVendor;
import com.viettel.gnoc.commons.dto.WSNocprov4DTO;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.nocproV4.AuthorityBO;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.nocproV4.NocproWebservice_Service;
import com.viettel.nocproV4.ParameterBO;
import com.viettel.nocproV4.RequestInputBO;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

@Slf4j
@Service
public class WSNocprov4Port {

  @Autowired
  protected TtServiceProxy ttServiceProxy;

  @Value("${application.ws.nocprov4_url:null}")
  public String NOCPROV4_URL;
  @Value("${application.ws.nocprov4_user:null}")
  public String userWS;
  @Value("${application.ws.nocprov4_pass:null}")
  public String passWS;
  @Value("${application.ws.nocprov4_salt:null}")
  public String salt;
  @Value("${application.ws.nocpro_url:null}")
  public String NOCPRO_URL;//danh cho chuyen trang thai CR

  public static final int CONNECT_TIMEOUT = 180000;//ms
  public static final int REQUEST_TIMEOUT = 180000;//ms

  @PostConstruct
  public void init() {
    try {
      passWS = PassProtector.decrypt(passWS, salt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  NocproWebservice port = null;
  NocproWebservice portVTP;
  NocproWebservice portNAT;
  NocproWebservice portCMR;
  NocproWebservice portOther;
  NocproWebservice portTZN;
  NocproWebservice portSTL;
  NocproWebservice portMVT;
  NocproWebservice portVTL;
  NocproWebservice portBRD;
  NocproWebservice portVTC;
  NocproWebservice portMYT;


  private void createConnect(String nationCode) throws Exception {
    List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
    lstCondition.add(new ConditionBean("insertSource", "CR_" + nationCode, Constants.NAME_EQUAL,
        Constants.STRING));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CfgServerNocDTO> lst = ttServiceProxy
        .getListCfgServerNocByCondition(new WSNocprov4DTO(lstCondition, 0, 100, "", ""));
    URL baseUrl = NocproWebservice_Service.class.getResource(".");
    URL url = new URL(baseUrl, lst.get(0).getLink());
    NocproWebservice_Service service = new NocproWebservice_Service(url,
        new QName("http://service.nocpro.nms.viettel.com/", "NocproWebservice"));
    if ("NOC_NAT".equalsIgnoreCase(nationCode)) {
      portNAT = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_VTP".equalsIgnoreCase(nationCode)) {
      portVTP = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_CMR".equalsIgnoreCase(nationCode)) {
      portCMR = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_TZN".equalsIgnoreCase(nationCode)) {
      portTZN = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_STL".equalsIgnoreCase(nationCode)) {
      portSTL = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_MVT".equalsIgnoreCase(nationCode)) {
      portMVT = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_VTL".equalsIgnoreCase(nationCode)) {
      portVTL = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_BRD".equalsIgnoreCase(nationCode)) {
      portBRD = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_VTC".equalsIgnoreCase(nationCode)) {
      portVTC = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC_MYT".equalsIgnoreCase(nationCode)) {
      portMYT = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else if ("NOC".equalsIgnoreCase(nationCode)) {
      port = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } else {
      portOther = (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    }
  }

  public List<CrAlarmFaultGroupDTO> getFautGroupInfo(String nationCode) throws Exception {
    List<CrAlarmFaultGroupDTO> res = new ArrayList<>();
    try {

      checkNationCode(nationCode);

      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setUserName(userWS);
      authorityBO.setPassword(passWS);
      authorityBO.setRequestId((1));

      RequestInputBO requestInputBO = new RequestInputBO();
      requestInputBO.setCode("WS_FAULT_SRC_FOR_GNOC");
      ParameterBO parameterBO = new ParameterBO();
      parameterBO.setName("faultSrc");
      parameterBO.setType("STRING");
      parameterBO.setValue("N/A");
      requestInputBO.getParams().add(parameterBO);

      JsonResponseBO response = responseJsonResponseBO(authorityBO, requestInputBO, nationCode);

      if (response.getStatus() != 0) {
        log.info("Can not get Data from NocsPro");
        return new ArrayList<>();
      }
      String dataJson = response.getDataJson();
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      JsonObject json = parser.parse(dataJson).getAsJsonObject();
      JsonArray info = json.getAsJsonArray("data");

      TypeToken<List<CrAlarmFaultGroupDTO>> token = new TypeToken<>() {
      };
      res = gson.fromJson(info.toString(), token.getType());
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return res;
  }

  List<CrAlarmVendor> getAllVendorInfo(String nationCode) throws Exception {
    List<CrAlarmVendor> res = new ArrayList<>();
    try {
      checkNationCode(nationCode);

      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setUserName(userWS);
      authorityBO.setPassword(passWS);
      authorityBO.setRequestId((1));

      RequestInputBO requestInputBO = new RequestInputBO();
      requestInputBO.setCode("WS_VENDOR_FOR_GNOC");
      requestInputBO.getParams().add(parameterBO("", "", "", "", ""));

      JsonResponseBO response = responseJsonResponseBO(authorityBO, requestInputBO, nationCode);

      if (response.getStatus() != 0) {
        log.info("Can not get Data from NocsPro");
        return new ArrayList<>();
      }
      String dataJson = response.getDataJson();

      Gson gson = new Gson();

      JsonParser parser = new JsonParser();
      JsonObject json = parser.parse(dataJson).getAsJsonObject();
      JsonArray info = json.getAsJsonArray("data");

      TypeToken<List<CrAlarmVendor>> token = new TypeToken<List<CrAlarmVendor>>() {
      };
      res = gson.fromJson(info.toString(), token.getType());

    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      throw e;
    }
    return res;
  }

  public List<CrAlarmDeviceType> getAllDeviceType(String nationCode) throws Exception {
    List<CrAlarmDeviceType> res = new ArrayList<>();
    try {
      checkNationCode(nationCode);
      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setUserName(userWS);
      authorityBO.setPassword(passWS);
      authorityBO.setRequestId((1));

      RequestInputBO requestInputBO = new RequestInputBO();
      requestInputBO.setCode("WS_DEVICE_TYPE_FOR_GNOC");
      requestInputBO.getParams().add(parameterBO("", "", "", "", ""));

      JsonResponseBO response = responseJsonResponseBO(authorityBO, requestInputBO, nationCode);

      if (response.getStatus() != 0) {
        log.info("Can not get Data from NocsPro");
        return new ArrayList<>();
      }
      String dataJson = response.getDataJson();

      Gson gson = new Gson();

      JsonParser parser = new JsonParser();
      JsonObject json = parser.parse(dataJson).getAsJsonObject();
      JsonArray info = json.getAsJsonArray("data");

      TypeToken<List<CrAlarmDeviceType>> token = new TypeToken<List<CrAlarmDeviceType>>() {
      };
      res = gson.fromJson(info.toString(), token.getType());
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      throw e;
    }
    return res;
  }

  public Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

  public List<CrAlarmInsiteDTO> getAlarmList(CrAlarmInsiteDTO crAlarmDTO) throws Exception {
    List<CrAlarmInsiteDTO> res = new ArrayList<>();
    try {
      checkNationCode(crAlarmDTO.getNationCode());

      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setUserName(userWS);
      authorityBO.setPassword(passWS);
      authorityBO.setRequestId((1));

      RequestInputBO requestInputBO = new RequestInputBO();
      requestInputBO.setCode("WS_ALARM_CATEGORY_FOR_GNOC");
      requestInputBO.getParams()
          .add(parameterBO("", "faultSrc", "", "STRING", crAlarmDTO.getFaultSrc().trim()));

      if (crAlarmDTO.getFaultGroupId() != null) {
        requestInputBO.getParams().add(
            parameterBO("", "faultGroupId", "", "", String.valueOf(crAlarmDTO.getFaultGroupId())));
      } else {
        requestInputBO.getParams().add(parameterBO("", "faultGroupId", "", "", ""));
      }

      if (crAlarmDTO.getVendorId() != null) {
        requestInputBO.getParams()
            .add(parameterBO("", "vendorId", "", "", String.valueOf(crAlarmDTO.getVendorId())));
      } else {
        requestInputBO.getParams().add(parameterBO("", "vendorId", "", "", ""));
      }

      if (crAlarmDTO.getDeviceTypeId() != null) {
        requestInputBO.getParams().add(
            parameterBO("", "deviceTypeId", "", "", String.valueOf(crAlarmDTO.getDeviceTypeId())));
      } else {
        requestInputBO.getParams().add(parameterBO("", "deviceTypeId", "", "", ""));
      }

      if (crAlarmDTO.getFaultName() != null && !crAlarmDTO.getFaultName().isEmpty()) {
        requestInputBO.getParams()
            .add(parameterBO("", "faultName", "", "", crAlarmDTO.getFaultName().trim()));
      } else {
        requestInputBO.getParams().add(parameterBO("", "faultName", "", "", ""));
      }

      if (StringUtils.isNotNullOrEmpty(crAlarmDTO.getProcessId())) {//day them dau viec
        requestInputBO.getParams()
            .add(parameterBO("", "processId", ",", "", crAlarmDTO.getProcessId().trim()));
      } else {
        requestInputBO.getParams().add(parameterBO("", "processId", "", "", ""));
      }

      if (crAlarmDTO.getNumberOccurences() != null) {
        requestInputBO.getParams()
            .add(parameterBO("", "minOccurences", "", "", crAlarmDTO.getNumberOccurences().toString()));
      } else {
        requestInputBO.getParams().add(parameterBO("", "minOccurences", "", "", ""));
      }

      JsonResponseBO response = responseJsonResponseBO(authorityBO, requestInputBO,
          crAlarmDTO.getNationCode());

      if (response.getStatus() != 0) {
        log.info("Can not get Data from NocsPro");
      }
      String dataJson = response.getDataJson();
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      JsonObject json = parser.parse(dataJson).getAsJsonObject();
      JsonArray info = json.getAsJsonArray("data");
      TypeToken<List<CrAlarmDraffDTO>> token = new TypeToken<List<CrAlarmDraffDTO>>() {
      };
      List<CrAlarmDraffDTO> draffList = gson.fromJson(info.toString(), token.getType());

      if (draffList != null) {
        for (CrAlarmDraffDTO draffDTO : draffList) {
          CrAlarmInsiteDTO alarmDTO = new CrAlarmInsiteDTO();
          alarmDTO.setDeviceTypeCode(draffDTO.getDevice_type_code());
          alarmDTO.setDeviceTypeId(draffDTO.getDevice_type_id());
          alarmDTO.setDeviceTypeCode(draffDTO.getDevice_type_code());
          alarmDTO.setFaultGroupId(draffDTO.getFault_group_id());
          alarmDTO.setFaultGroupName(draffDTO.getFault_group_name());
          alarmDTO.setFaultId(draffDTO.getFault_id());
          alarmDTO.setFaultLevelCode(draffDTO.getFault_level_code());
          alarmDTO.setFaultLevelId(draffDTO.getFault_level_id());
          alarmDTO.setFaultName(draffDTO.getFault_name());
          alarmDTO.setFaultSrc(draffDTO.getFault_src());
          alarmDTO.setVendorCode(draffDTO.getVendor_code());
          alarmDTO.setVendorName(draffDTO.getVendor_name());
          alarmDTO.setModuleCode(draffDTO.getModule_code());
          alarmDTO.setModuleName(draffDTO.getModule_name());
          alarmDTO.setKeyword(draffDTO.getKeyword());
          alarmDTO.setNumberOccurences(draffDTO.getNumber_occurences());
          res.add(alarmDTO);
        }
        if (res != null && res.size() < 1000) {
          res.sort(Comparator.comparing(CrAlarmInsiteDTO::getNumberOccurences));
        }
      }
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      throw e;
    }
    return res;
  }

  private ParameterBO parameterBO(String format, String name, String separator, String type,
      String value) {
    ParameterBO dto = new ParameterBO();
    dto.setFormat(format);
    dto.setName(name);
    dto.setSeparator(separator);
    dto.setType(type);
    dto.setValue(value);
    return dto;
  }

  private void checkNationCode(String nationCode) throws Exception {
    if ("NOC".equalsIgnoreCase(nationCode) && port == null) {
      createConnect(nationCode);
    } else if ("NOC_NAT".equalsIgnoreCase(nationCode) && portNAT == null) {
      createConnect(nationCode);
    } else if ("NOC_VTP".equalsIgnoreCase(nationCode) && portVTP == null) {
      createConnect(nationCode);
    } else if ("NOC_CMR".equalsIgnoreCase(nationCode) && portCMR == null) {
      createConnect(nationCode);
    } else if ("NOC_TZN".equalsIgnoreCase(nationCode) && portTZN == null) {
      createConnect(nationCode);
    } else if ("NOC_STL".equalsIgnoreCase(nationCode) && portSTL == null) {
      createConnect(nationCode);
    } else if ("NOC_MVT".equalsIgnoreCase(nationCode) && portMVT == null) {
      createConnect(nationCode);
    } else if ("NOC_VTL".equalsIgnoreCase(nationCode) && portVTL == null) {
      createConnect(nationCode);
    } else if ("NOC_BRD".equalsIgnoreCase(nationCode) && portBRD == null) {
      createConnect(nationCode);
    } else if ("NOC_VTC".equalsIgnoreCase(nationCode) && portVTC == null) {
      createConnect(nationCode);
    } else if ("NOC_MYT".equalsIgnoreCase(nationCode) && portMYT == null) {
      createConnect(nationCode);
    } else if (portOther == null) {
      createConnect(nationCode);
    }
  }

  private JsonResponseBO responseJsonResponseBO(AuthorityBO authorityBO,
      RequestInputBO requestInputBO, String nationCode) {
    JsonResponseBO response = new JsonResponseBO();
    if ("NOC".equalsIgnoreCase(nationCode) && port != null) {
      response = port.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_NAT".equalsIgnoreCase(nationCode) && portNAT != null) {
      response = portNAT.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_VTP".equalsIgnoreCase(nationCode) && portVTP != null) {
      response = portVTP.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_CMR".equalsIgnoreCase(nationCode) && portCMR != null) {
      response = portCMR.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_TZN".equalsIgnoreCase(nationCode) && portTZN != null) {
      response = portTZN.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_STL".equalsIgnoreCase(nationCode) && portSTL != null) {
      response = portSTL.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_MVT".equalsIgnoreCase(nationCode) && portMVT != null) {
      response = portMVT.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_VTL".equalsIgnoreCase(nationCode) && portVTL != null) {
      response = portVTL.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_BRD".equalsIgnoreCase(nationCode) && portBRD != null) {
      response = portBRD.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_VTC".equalsIgnoreCase(nationCode) && portVTC != null) {
      response = portVTC.getDataJson(authorityBO, requestInputBO);
    } else if ("NOC_MYT".equalsIgnoreCase(nationCode) && portMYT != null) {
      response = portMYT.getDataJson(authorityBO, requestInputBO);
    } else if (portOther != null) {
      response = portOther.getDataJson(authorityBO, requestInputBO);
    }
    return response;
  }

  public String onExecuteMapQuery(String crNumber, UserToken userTokenGNOC, String nationCode)
      throws Exception {
    String res = "";
    try {
      StringUtils.printLogData("CALL WS: NOC-PRO - Method onExecuteMapQuery :", crNumber, String.class);
      checkNationCode(nationCode);

      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setUserName(userWS);
      authorityBO.setPassword(passWS);
      authorityBO.setRequestId(1);

      RequestInputBO requestInputBO = new RequestInputBO();
      requestInputBO.setCode("WS_UPDATE_USER_CR");

      requestInputBO.getParams()
          .add(parameterBO("", "p_userName", ",", "STRING", userTokenGNOC.getUserName()));
      requestInputBO.getParams()
          .add(parameterBO("", "p_phoneNumber", ",", "STRING", userTokenGNOC.getTelephone()));
      requestInputBO.getParams().add(parameterBO("", "p_crNumber", ",", "STRING", crNumber));

      JsonResponseBO response = new JsonResponseBO();
      if ("NOC".equalsIgnoreCase(nationCode) && port != null) {
        response = port.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_NAT".equalsIgnoreCase(nationCode) && portNAT != null) {
        response = portNAT.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_VTP".equalsIgnoreCase(nationCode) && portVTP != null) {
        response = portVTP.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_CMR".equalsIgnoreCase(nationCode) && portCMR != null) {
        response = portCMR.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_TZN".equalsIgnoreCase(nationCode) && portTZN != null) {
        response = portTZN.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_STL".equalsIgnoreCase(nationCode) && portSTL != null) {
        response = portSTL.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_MVT".equalsIgnoreCase(nationCode) && portMVT != null) {
        response = portMVT.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_VTL".equalsIgnoreCase(nationCode) && portVTL != null) {
        response = portVTL.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_BRD".equalsIgnoreCase(nationCode) && portBRD != null) {
        response = portBRD.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_VTC".equalsIgnoreCase(nationCode) && portVTC != null) {
        response = portVTC.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if ("NOC_MYT".equalsIgnoreCase(nationCode) && portMYT != null) {
        response = portMYT.onExecuteMapQuery(authorityBO, requestInputBO);
      } else if (portOther != null) {
        response = portOther.onExecuteMapQuery(authorityBO, requestInputBO);
      }
      if (response.getStatus() != 0) {
        res = Constants.CR_RETURN_MESSAGE.ERROR + response.getDetailError();
      } else {
        res = Constants.CR_RETURN_MESSAGE.SUCCESS;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return res;
  }

}
