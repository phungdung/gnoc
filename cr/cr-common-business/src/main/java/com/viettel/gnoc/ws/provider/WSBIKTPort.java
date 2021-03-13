/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.thoughtworks.xstream.XStream;
import com.viettel.bikt.webservices.KpignocForm;
import com.viettel.bikt.webservices.ServiceGNOC;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSBIKTPort {

  ServiceGNOC port = null;

  @Value("${application.conf.userBIKT:null}")
  String userName;

  @Value("${application.conf.passBIKT:null}")
  String password;

  @Autowired
  WSBIKTPortFactory wsbiktPortFactory;

  private void createConnect() throws MalformedURLException, Exception {
    port = (ServiceGNOC) wsbiktPortFactory.getWSFactory().borrowObject();
  }

  public List<KpignocForm> getKpiCoDien(String updateTime, String kvCo, String provinceCode) {
    List<KpignocForm> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        log.info(port.toString());
        res = port.getKpiCoDien(updateTime, kvCo, provinceCode,
            userName, password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsbiktPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<KpignocForm> getKvProvince() {
    List<KpignocForm> res = new ArrayList<>();
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        log.info(port.toString());
        res = port.getKvProvince(userName, password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsbiktPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    try {
      XStream xstream = new XStream();
      xstream.alias("kpignocForm", KpignocForm.class);
      xstream.alias("return", List.class);
//    xstream.addImplicitCollection(KpignocForm.class, "list");
      String xml = xstream.toXML(res);
      log.info(getTemplate("getKvProvince", xml));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      StringBuilder valueResponse = new StringBuilder("");
      if (res != null) {
        res.forEach(item -> {
          if (item != null) {
            valueResponse.append(toSoapUI(item, "kpignocForm"));
          }
        });
      }
      log.info(getTemplate("getKvProvince", valueResponse.toString()));
    }
    return res;
  }

  public String getProvinceByLocationCode(String locationCode, List<KpignocForm> lstProvince) {
    String rs = "";
    String pre = locationCode.substring(0, 4);
    for (KpignocForm form : lstProvince) {
      if (pre.equals(form.getProvinceSymbol())) {
        rs = form.getProvinceCode();
        break;
      }
    }
    log.info(getTemplate("getProvinceCode", "<return>" + rs + "</return>"));
    return rs;
  }

  public String getKvCodeByProvinceCode(String provinceCode, List<KpignocForm> lstProvince) {
    String rs = "";
    for (KpignocForm form : lstProvince) {
      if (provinceCode.equals(form.getProvinceCode())) {
        rs = form.getKvCode();
        break;
      }
    }
    log.info(getTemplate("getKvCode", "<return>" + rs + "</return>"));
    return rs;
  }

  public List<KpignocForm> getKpiNIMS(String updateTime, String kvCo, String provinceCode) {
    List<KpignocForm> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        log.info(port.toString());
        res = port.getKpiNIMS(updateTime, kvCo, provinceCode,
            userName, password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsbiktPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<KpignocForm> getKpiVMTS(String updateTime, String kvCo, String provinceCode) {
    List<KpignocForm> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        log.info(port.toString());
        res = port.getKpiVMTS(updateTime, kvCo, provinceCode,
            userName, password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsbiktPortFactory.getWSFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public String getTemplate(String methodName, String value) {
    StringBuilder log = new StringBuilder();
    if (StringUtils.isStringNullOrEmpty(value)) {
      value = "";
    }
    log.append("\n<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
        + "\t<soap:Body>\n"
        + "\t\t<ns2:" + methodName
        + "Response xmlns:ns2=\"http://webservices.bikt.viettel.com/\">\n"
        + "\t\t\t");
    log.append(value);
    log.append("\n");
    log.append("\t\t</ns2:" + methodName + "Response>\n"
        + "\t</soap:Body>\n"
        + "</soap:Envelope>\n");
    return log.toString();
  }


  public String toSoapUI(KpignocForm data, String objectName) {
    String strTemp = "<" + objectName + ">";
    try {
      Field fields[] = data.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        try {
          String returnType = fields[i].getType().getSimpleName().toUpperCase();
          String fieldName = fields[i].getName();
          Method method = data.getClass()
              .getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
          Object value = method.invoke(data);
          if (value != null && !"".equals(value.toString())) {
            if (Constants.STRING.indexOf(returnType) >= 0
                || "INT, LONG, DOUBLE, FLOAT, DATE".indexOf(returnType) >= 0) {
              if ("STRING, INT, LONG, DOUBLE, FLOAT, DATE".indexOf(returnType) >= 0) {
                String stringValue = String.valueOf(value);
                strTemp += "<" + fieldName + ">" + stringValue + "</" + fieldName + ">";
              }
            } else {
              String stringValue = String.valueOf(value);
              strTemp += "<" + fieldName + ">" + stringValue + "</" + fieldName + ">";
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    strTemp += "</" + objectName + ">";
    return strTemp;
  }
}
