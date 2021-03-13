package com.viettel.gnoc.commons.incident.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.iim.webservice.AuthorityBO;
import com.viettel.gnoc.iim.webservice.IimServices;
import com.viettel.gnoc.iim.webservice.JsonResponseBO;
import com.viettel.gnoc.iim.webservice.ParameterBO;
import com.viettel.gnoc.iim.webservice.RequestInputBO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WSIIMPort {

  @Autowired
  protected WSIIMPortFactory wsiimPortFactory;

  @Autowired
  protected WSNocproV4PortFactory wsNocproV4PortFactory;

  IimServices port = null;
  @Value("${application.ws.user_ws:null}")
  public String userWS;
  @Value("${application.ws.pass_ws:null}")
  public String passWS;

  private void createConnect() throws Exception {
    port = (IimServices) wsiimPortFactory.getWsFactory().borrowObject();

    if (port == null) {
      log.info("Can not create NocproV4port");
    }
  }

  boolean validateString(String txt) {
    return !(txt == null || txt.trim().isEmpty());
  }

  public List<CrModuleDraftDTO> getIIMModules(
      String serviceCode, String serviceName,
      String moduleCode, String moduleName,
      String unitCode, String nationCode) throws Exception {
    List<CrModuleDraftDTO> res = new ArrayList<>();
    try {
      if (port == null) {
        createConnect();
      }

      if (port != null) {
        AuthorityBO authorityBO = new AuthorityBO();
        authorityBO.setUserName(userWS);
        authorityBO.setPassword(passWS);
        authorityBO.setRequestId(1L);

        RequestInputBO requestInputBO = new RequestInputBO();
        requestInputBO.setCode("GET_FILTER_MODULE_GNOC");

        ParameterBO bo = new ParameterBO();
        bo.setName("serviceCode");
        if (validateString(serviceCode) && !"N/A".equalsIgnoreCase(serviceCode)) {
          bo.setValue("%" + serviceCode.toUpperCase().trim() + "%");
        } else {
          bo.setValue("N/A");
        }

        requestInputBO.getParams().add(bo);

        bo = new ParameterBO();
        bo.setName("serviceName");
        if (validateString(serviceName) && !"N/A".equalsIgnoreCase(serviceName)) {
          bo.setValue("%" + serviceName.toUpperCase().trim() + "%");
        } else {
          bo.setValue("N/A");
        }

        requestInputBO.getParams().add(bo);

        bo = new ParameterBO();
        bo.setName("moduleCode");
        if (validateString(moduleCode) && !"N/A".equalsIgnoreCase(moduleCode)) {
          bo.setValue("%" + moduleCode.toUpperCase().trim() + "%");
        } else {
          bo.setValue("N/A");
        }
        requestInputBO.getParams().add(bo);

        bo = new ParameterBO();
        bo.setName("moduleName");
        if (validateString(moduleName) && !"N/A".equalsIgnoreCase(moduleName)) {
          bo.setValue("%" + moduleName.toUpperCase().trim() + "%");
        } else {
          bo.setValue("N/A");
        }
        requestInputBO.getParams().add(bo);
        bo = new ParameterBO();
        bo.setName("unitCode");
        if (validateString(unitCode) && !"N/A".equalsIgnoreCase(unitCode)) {
          bo.setValue(unitCode.toUpperCase().trim());
        } else {
          bo.setValue("N/A");
        }
        requestInputBO.getParams().add(bo);

        JsonResponseBO response = port.getDataJson(authorityBO, requestInputBO, null);
        if (response.getStatus() != 0) {
          log.info("Can not get Data from IIM");
          log.info(response.getDetailError());
          return new ArrayList();
        }
        String dataJson = response.getDataJson();

        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(dataJson).getAsJsonObject();
        JsonArray info = json.getAsJsonArray("data");

        TypeToken<List<CrModuleDraftDTO>> token = new TypeToken<List<CrModuleDraftDTO>>() {
        };
        res = gson.fromJson(info.toString(), token.getType());
        if (res != null) {
          for (CrModuleDraftDTO dto : res) {
            dto.setNationCode(nationCode);
          }
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsNocproV4PortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }
}
