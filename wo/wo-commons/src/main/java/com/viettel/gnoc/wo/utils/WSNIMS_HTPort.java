package com.viettel.gnoc.wo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.nims.webservice.ht.CableInfoForm;
import com.viettel.nims.webservice.ht.CheckPortSubDescriptionByWOForm;
import com.viettel.nims.webservice.ht.ResultCheckStatusCabinet;
import com.viettel.nims.webservice.ht.ResultCheckStatusStations;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import java.io.StringReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSNIMS_HTPort {

  //  InfraWS port = null;
  @Value("${application.ws.userNimsHT:null}")
  private String userNimsHt;
  @Value("${application.ws.passNimsHT:null}")
  private String passNimsHt;
  @Value("${application.ws.nims.ht.url:null}")
  public String SERVICE_URL_NIMS_HT;
  @Autowired
  WSNIMS_HTPortFactory wsnims_htPortFactory;

  @Autowired
  WSNIMS_HTPort wsnims_htPort;

  @Autowired
  WSNIMS_HT_GLOBAL_Port wsnims_ht_global_port;

  public final int CONNECT_TIMEOUT = 60000;//ms
  public final int REQUEST_TIMEOUT = 60000;//ms

  @PostConstruct
  public void init() {
    try {
      userNimsHt = PassProtector.decrypt(userNimsHt, WS_USERS.SALT);
      passNimsHt = PassProtector.decrypt(passNimsHt, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /*
  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (InfraWS) wsnims_htPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
    log.info("Start create connect SpmServiceImpl WS NIM HT");
  }*/

  public List<ResultGetDepartmentByLocationForm> getDepartmentByLocation(String locationCode) {
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        List<ResultGetDepartmentByLocationForm> res = port
            .getDepartmentByLocation(userNimsHt, passNimsHt, locationCode);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;*/
    return getDepartmentByLocationBasic(locationCode);
  }

  public List<ResultGetDepartmentByLocationForm> getDepartmentByLocation(
      String locationCode, String nationCode) {
    List<ResultGetDepartmentByLocationForm> res = null;
    // gọi link VNM
    if (StringUtils.isStringNullOrEmpty(nationCode) || "VNM".equals(nationCode)) {
      res = wsnims_htPort.getDepartmentByLocation(locationCode);
    } // goi link global co truyen nationCode
    else {
      wsnims_ht_global_port.setNationCode(nationCode);
      List<com.viettel.webservice.nims_ht_global.ResultGetDepartmentByLocationForm> gRes
          = wsnims_ht_global_port.getDepartmentByLocation(locationCode);
      if (gRes != null && gRes.size() > 0) {
        res = new ArrayList<>();
        for (com.viettel.webservice.nims_ht_global.ResultGetDepartmentByLocationForm i : gRes) {
          ResultGetDepartmentByLocationForm tmp = new ResultGetDepartmentByLocationForm();
          tmp = (ResultGetDepartmentByLocationForm) DataUtil.updateObjectData(i, tmp);
          res.add(tmp);
        }
      }
    }
    return res;
  }

  public List<CableInfoForm> getListCableInfo(Long searchType, String cableCode) throws Exception {
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultDesignForm res = port.getListCableInfo(userNimsHt, passNimsHt, searchType, cableCode);
        if (res != null) {
          return res.getLstCableInfo();
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }*/

    return getListCableInfoBasic(searchType, cableCode);

  }

  public CheckPortSubDescriptionByWOForm checkPortSubDescriptionByWO(String woCode)
      throws Exception {  // 1 ma tu   2 ma nha tram
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        CheckPortSubDescriptionByWOForm form = new CheckPortSubDescriptionByWOForm();
        form.setWoCode(woCode);
        CheckPortSubDescriptionByWOForm res = port
            .checkPortSubDescriptionByWO(userNimsHt, passNimsHt, form);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
    */

    return checkPortSubDescriptionByWOBasic(woCode);

  }

  public ResultCheckStatusStations checkStatusStations(String stationCode)
      throws Exception {  // 1 ma tu   2 ma nha tram
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        CheckStatusStationsInputForm form = new CheckStatusStationsInputForm();
        form.setStationCode(stationCode);
        ResultCheckStatusStations res = port.checkStatusStations(userNimsHt, passNimsHt, form);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  */
    return checkStatusStationsBasic(stationCode);
  }

  public ResultCheckStatusCabinet checkStatusCabinet(String stationCode)
      throws Exception {  // 1 ma tu   2 ma nha tram
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        CheckStatusCabinetInputForm form = new CheckStatusCabinetInputForm();
        form.setCabinetCode(stationCode);
        ResultCheckStatusCabinet res = port.checkStatusCabinet(userNimsHt, passNimsHt, form);
        if (res != null) {
          return res;
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  */

    return checkStatusCabinetBasic(stationCode);
  }

  public NimsStationForm getStationInfo(String stationCode) throws Exception {
   /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        RequestInputBO requestBO = new RequestInputBO();
        requestBO.setCode(Constants.AP_PARAM.GET_DATA_JSON_STATION);
        ParameterBO param = new ParameterBO();
        param.setName("stationCode");
        param.setType("STRING");
        param.setValue(stationCode);
        requestBO.getParams().add(param);
        JsonResponseBO res = port.getDataJson(userNimsHt, passNimsHt, requestBO);

        String response = res.getDataJson();

        Gson gson = new Gson();

        JSONObject json = new JSONObject(response);
        JSONArray info = json.getJSONArray("data");

        TypeToken<List<NimsStationForm>> token = new TypeToken<List<NimsStationForm>>() {
        };
        List<NimsStationForm> listData = gson.fromJson(info.toString(), token.getType());
        if (listData != null && listData.size() > 0) {
          return listData.get(0);
        }
      } else {
        throw new Exception("[NIMS] Error communicate WS NIMS");
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    return null;
//end comment call WS
*/
    log.info("getStationInfo: " + SERVICE_URL_NIMS_HT);
    return getStationInfoBasic(stationCode);
  }

  public List<ResultGetDepartmentByLocationForm> getSubscriptionInfo(String locationCode) {
    /*
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        List<ResultGetDepartmentByLocationForm> res = port
            .getDepartmentByLocation(userNimsHt, passNimsHt, locationCode);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_htPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
    */
    return getDepartmentByLocationBasic(locationCode);
  }

  public NimsStationForm getStationInfoBasic(String stationCode) throws Exception {
    try {
      String contenRequet =
          "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
              + " xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
              + "   <soapenv:Header/>\n"
              + "   <soapenv:Body>\n"
              + "      <web:getDataJson>\n"
              + "         <username>" + userNimsHt + "</username>\n"
              + "         <password>" + passNimsHt + "</password>\n"
              + "         <requestInputBO>\n"
              + "            <code>" + Constants.AP_PARAM.GET_DATA_JSON_STATION + "</code>\n"
              + "            <params>\n"
              + "               <name>stationCode</name>\n"
              + "               <value>" + stationCode + "</value>\n"
              + "            </params>\n"
              + "         </requestInputBO>\n"
              + "      </web:getDataJson>\n"
              + "   </soapenv:Body>\n"
              + "</soapenv:Envelope>";
      NimHtUnmarshallerDTO res = getDataSoapUiBase(contenRequet,
          JsonResponseBOUnmarshallerDTO.class);
      if (res != null && res.getJsonResponseBO() != null) {
        String response = res.getJsonResponseBO().getDataJson();
        Gson gson = new Gson();
        JSONObject json = new JSONObject(response);
        JSONArray info = json.getJSONArray("data");
        TypeToken<List<NimsStationForm>> token = new TypeToken<>() {
        };
        List<NimsStationForm> listData = gson.fromJson(info.toString(), token.getType());
        if (listData != null && listData.size() > 0) {
          return listData.get(0);
        }
      }
    } catch (SocketTimeoutException e) {
      log.error(e.getMessage(), e);
      throw new Exception("[NIMS] Timeout call WS NIMS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return null;

  }

  public List<ResultGetDepartmentByLocationForm> getDepartmentByLocationBasic(String locationCode) {
    String contentRequest =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:getDepartmentByLocation>\n"
            + "         <username>" + userNimsHt + "</username>\n"
            + "         <password>" + passNimsHt + "</password>\n"
            + "         <locationCode>" + locationCode + "</locationCode>\n"
            + "      </web:getDepartmentByLocation>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    try {
      NimHtUnmarshallerDTO dataNims = getDataSoapUiBase(contentRequest,
          ResultDepartmentFormUnmarshallerDTO.class);
      if (dataNims != null) {
        return dataNims.getResultGetDepartmentByLocationForm();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public List<CableInfoForm> getListCableInfoBasic(Long searchType, String cableCode)
      throws Exception {
    String contentRequest =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:getListCableInfo>\n"
            + "         <username>" + userNimsHt + "</username>\n"
            + "         <password>" + passNimsHt + "</password>\n"
            + "         <searchType>" + searchType + "</searchType>\n"
            + "         <cableCode>" + cableCode + "</cableCode>\n"
            + "      </web:getListCableInfo>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    NimHtUnmarshallerDTO dataNims = getDataSoapUiBase(contentRequest,
        ResultDesignFormUnmarshallerDTO.class);
    if (dataNims != null && dataNims.getResultDesignForm() != null) {
      return dataNims.getResultDesignForm().getLstCableInfo();
    }
    return null;
  }

  public CheckPortSubDescriptionByWOForm checkPortSubDescriptionByWOBasic(String woCode)
      throws Exception {  // 1 ma tu   2 ma nha tram

    String contentRequest =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:checkPortSubDescriptionByWO>\n"
            + "         <username>" + userNimsHt + "</username>\n"
            + "         <password>" + passNimsHt + "</password>\n"
            + "         <inputForm>\n"
            + "            <woCode>" + woCode + "</woCode>\n"
            + "         </inputForm>\n"
            + "      </web:checkPortSubDescriptionByWO>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    NimHtUnmarshallerDTO dataNims = getDataSoapUiBase(contentRequest,
        CheckPortSubDescriptionFormUnmarshallerDTO.class);
    if (dataNims != null) {
      return dataNims.getCheckPortSubDescriptionByWOForm();
    }
    return null;
  }

  public ResultCheckStatusStations checkStatusStationsBasic(String stationCode)
      throws Exception {  // 1 ma tu   2 ma nha tram
    String contentRequest =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:checkStatusStations>\n"
            + "         <username>" + userNimsHt + "</username>\n"
            + "         <password>" + passNimsHt + "</password>\n"
            + "         <inputForm>\n"
            + "            <stationCode>" + stationCode + "</stationCode>\n"
            + "         </inputForm>\n"
            + "      </web:checkStatusStations>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    NimHtUnmarshallerDTO dataNims = getDataSoapUiBase(contentRequest,
        ResultCheckStatusStationUnmarshallerDTO.class);
    if (dataNims != null) {
      return dataNims.getResultCheckStatusStations();
    }
    return null;

  }

  public ResultCheckStatusCabinet checkStatusCabinetBasic(String stationCode)
      throws Exception {  // 1 ma tu   2 ma nha tram
    String contentRequest =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.infra.nims.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:checkStatusCabinet>\n"
            + "         <username>" + userNimsHt + "</username>\n"
            + "         <password>" + passNimsHt + "</password>\n"
            + "         <inputForm>\n"
            + "            <cabinetCode>" + stationCode + "</cabinetCode>\n"
            + "         </inputForm>\n"
            + "      </web:checkStatusCabinet>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    NimHtUnmarshallerDTO dataNims = getDataSoapUiBase(contentRequest,
        ResultCheckStatusCabinetUnmarshallerDTO.class);
    if (dataNims != null) {
      return dataNims.getResultCheckStatusCabinet();
    }
    return null;
  }

  public NimHtUnmarshallerDTO getDataSoapUiBase(String contentRequest, Class<?> zClass)
      throws Exception {
    try {
      String str = sendRequest(contentRequest, SERVICE_URL_NIMS_HT, REQUEST_TIMEOUT,
          CONNECT_TIMEOUT);
      NimHtUnmarshallerDTO rq = convertNodesListFromXml(str, zClass);
      if (rq != null) {
        return rq;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return null;
  }

  public String sendRequest(String requestContent, String wsdlUrl, int recvTimeout,
      int connectTimeout) throws Exception {
    HttpClient httpclient = new HttpClient();
    httpclient.getParams().setParameter("http.socket.timeout", recvTimeout);
    httpclient.getParams().setParameter("http.connection.timeout", connectTimeout);
    //create an instance PostMethod
    PostMethod post = new PostMethod(wsdlUrl);
    try {
      RequestEntity entity = new StringRequestEntity(requestContent, "text/xml", "UTF-8");
      post.setRequestEntity(entity);
      httpclient.executeMethod(post);
      return post.getResponseBodyAsString();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new Exception("[NIMS] Error communicate WS NIMS");
    }
  }

  public NimHtUnmarshallerDTO convertNodesListFromXml(String xml, Class<?> zClass) {
    NimHtUnmarshallerDTO data = null;
    try {

      xml = xml.replace("&quot;", "\"").replace("&lt;", "<").
          replace("&gt;", ">");
      JAXBContext jaxbContext = JAXBContext.newInstance(zClass);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      XMLInputFactory factory = XMLInputFactory.newInstance();
      String regex = "<return>(.*)</return>";
      Pattern p = Pattern.compile(regex, Pattern.DOTALL);
      Matcher m = p.matcher(xml);
      StringReader stringReader;
      while (m.find()) {
        stringReader = new StringReader(
            "<nimHtUnmarshallerDTO>" + m.group() + "</nimHtUnmarshallerDTO>");
        XMLEventReader someSource = factory.createXMLEventReader(stringReader);
        JAXBElement<?> objectElement = unmarshaller
            .unmarshal(someSource, zClass);
        if (objectElement != null) {
          data = (NimHtUnmarshallerDTO) objectElement.getValue();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
//            log.error(e.getMessage(), e);
    }
    return data;
  }
}
