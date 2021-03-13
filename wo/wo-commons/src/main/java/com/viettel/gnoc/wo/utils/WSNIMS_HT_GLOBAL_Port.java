package com.viettel.gnoc.wo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.wo.dto.HeaderForm;
import com.viettel.webservice.nims_ht_global.InfraWS;
import com.viettel.webservice.nims_ht_global.JsonResponseBO;
import com.viettel.webservice.nims_ht_global.ParameterBO;
import com.viettel.webservice.nims_ht_global.RequestInputBO;
import com.viettel.webservice.nims_ht_global.ResultGetDepartmentByLocationForm;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSNIMS_HT_GLOBAL_Port {

  InfraWS port = null;
  @Value("${application.ws.userNimsHTGlobal:null}")
  private String userNimsHtGlobal;
  @Value("${application.ws.passNimsHTGlobal:null}")
  private String passNimsHtGlobal;

  List<HeaderForm> lstHeader;
  String nationCoce;

  public void setNationCode(String nationCode) {
    lstHeader = new ArrayList<>();
    lstHeader.add(new HeaderForm("nationCode", nationCode));
    this.nationCoce = nationCode;
  }

  public void clearSetNationCode() {
    this.nationCoce = null;
    this.lstHeader = null;
  }

  @Autowired
  WSNIMS_HT_GOLOBAL_PortFactory wsnimsHtGolobalPortFactory;

  @PostConstruct
  public void init() {
    try {
      userNimsHtGlobal = PassProtector.decrypt(userNimsHtGlobal, WS_USERS.SALT);
      passNimsHtGlobal = PassProtector.decrypt(passNimsHtGlobal, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect NIMS_HT_GLOBAL WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (com.viettel.webservice.nims_ht_global.InfraWS) wsnimsHtGolobalPortFactory.getWsFactory()
        .borrowObject();
    System.out.println(
        "End create connect NIMS_HT_GLOBAL WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));

  }

  public List<ResultGetDepartmentByLocationForm> getDepartmentByLocation(String locationCode) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        SetHeaderWs.setHeaderHandler(port, lstHeader);
        List<ResultGetDepartmentByLocationForm> res = port
            .getDepartmentByLocation(userNimsHtGlobal, passNimsHtGlobal, locationCode);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnimsHtGolobalPortFactory.getWsFactory().returnObject(port);
//          WSNIMS_HTPortFactory.getInstance().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }

  public NimsStationForm getStationInfo(String stationCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        if (this.lstHeader != null) {
          SetHeaderWs.setHeaderHandler(port, lstHeader);
        }

        RequestInputBO requestBO = new RequestInputBO();
        requestBO.setCode(Constants.AP_PARAM.GET_DATA_JSON_STATION);

        ParameterBO param = new ParameterBO();
        param.setName("stationCode");
        param.setType("STRING");
        param.setValue(stationCode);
        requestBO.getParams().add(param);

        JsonResponseBO res = port.getDataJson(userNimsHtGlobal, passNimsHtGlobal, requestBO);

        String response = res.getDataJson();

        Gson gson = new Gson();

        JSONObject json = new JSONObject(response);
        JSONArray info = json.getJSONArray("data");

        TypeToken<List<NimsStationForm>> token = new TypeToken<>() {
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
          wsnimsHtGolobalPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }
}
