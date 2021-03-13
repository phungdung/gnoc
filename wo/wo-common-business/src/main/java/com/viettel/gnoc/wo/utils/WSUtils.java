package com.viettel.gnoc.wo.utils;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WSUtils {

  public static String sendRequest(String requestContent, String wsdlUrl, int recvTimeout,
      int connectTimeout) throws Exception {
    HttpClient httpclient = new HttpClient();
    httpclient.getParams().setParameter("http.socket.timeout", recvTimeout);
    httpclient.getParams().setParameter("http.connection.timeout", connectTimeout);
    //create an instance PostMethod
    PostMethod post = new PostMethod(wsdlUrl);
    String result = "";
    try {
      RequestEntity entity = new StringRequestEntity(requestContent, "text/xml", "UTF-8");
      post.setRequestEntity(entity);
      httpclient.executeMethod(post);
      result = post.getResponseBodyAsString();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return result;
  }

  public static ResultQlctkt convertNodesFromXml(String xml) throws Exception {
    ResultQlctkt bo = null;
    try {
      xml = xml.replace("&quot;", "\"").replace("&lt;", "<").
          replace("&gt;", ">").replace("\"", "\\\"");
      String regex = "<return>(.*)</return>";
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(xml);
      while (m.find()) {
        xml = m.group(1);
      }
      regex = "<return>(.*)</return>";
      p = Pattern.compile(regex);
      m = p.matcher(xml);
      while (m.find()) {
        xml = m.group(1);
      }
      xml = "<ResultQlctkt>" + xml + "</ResultQlctkt>";
      System.out.println(xml);
      JAXBContext jaxbContext = JAXBContext.newInstance(ResultQlctkt.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      bo = (ResultQlctkt) unmarshaller.unmarshal(new StringReader(xml));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return bo;
  }

}
