package com.viettel.gnoc.passport.client;

import com.viettel.passport.PassportWSService;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import viettel.passport.client.AppToken;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;

@Getter
@Setter
@Slf4j
public class VSAValidate {

  private static final Logger log = LoggerFactory.getLogger(VSAValidate.class);
  private String casValidateUrl;
  private String user;
  private String password;
  private String domainCode;
  private UserToken userToken;
  private String entireResponse;
  private boolean successfulAuthentication;
  private static final int DEFAULT_TIME_OUT_VALUE = 5000;
  private int timeOutVal = 5000;

  public void validate() throws IOException, ParserConfigurationException {
    URL url = null;
    try {
      URL baseUrl = PassportWSService.class.getResource(".");
      url = new URL(baseUrl, getCasValidateUrl());
    } catch (MalformedURLException e) {
      e.getMessage();
    }

    PassportWSService pws = new PassportWSService(url,
        new QName("http://passport.viettel.com/", "passportWSService"));

    entireResponse = pws.getPassportWSPort().
        validate(this.user, this.password, this.domainCode);
    try {
      if ("no".equalsIgnoreCase(entireResponse)) {
        log.info("authenticate failure [username=" + this.user + "]");

        this.successfulAuthentication = false;

        return;
      }

      this.userToken = UserToken.parseXMLResponse(entireResponse);
      if ((this.userToken.getObjectTokens() != null)
          && (this.userToken.getObjectTokens().size() > 0)) {
        log.info("authenticate successful [username=" + this.user + "]");

        this.successfulAuthentication = true;
      } else {
        log.info("authenticate failure [username=" + this.user + "]");

        this.successfulAuthentication = false;
      }
    } catch (SAXException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public ArrayList<AppToken> getAllApp()
      throws IOException, ParserConfigurationException {
    URL url = null;
    try {
      URL baseUrl = PassportWSService.class.getResource(".");
      url = new URL(baseUrl, getCasValidateUrl());
    } catch (MalformedURLException e) {
      e.getMessage();
    }

    PassportWSService pws = new PassportWSService(url,
        new QName("http://passport.viettel.com/", "passportWSService"));

    String entireResponse = pws.getPassportWSPort().getAllowedApp(this.user);

    return AppToken.parseApp(entireResponse);
  }

  public ArrayList<ObjectToken> getAllMenu() throws IOException,
      ParserConfigurationException, SAXException {
    this.domainCode = this.domainCode.trim().toLowerCase();
    URL url = null;
    try {
      URL baseUrl = PassportWSService.class.getResource(".");
      url = new URL(baseUrl, getCasValidateUrl());
    } catch (MalformedURLException e) {
      e.getMessage();
    }

    PassportWSService pws = new PassportWSService(url,
        new QName("http://passport.viettel.com/", "passportWSService"));

    String entireResponse = pws.getPassportWSPort().getAppFunctions(this.domainCode);

    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

    Document doc = db.parse(new InputSource(new StringReader(entireResponse)));

    ArrayList arrlObjects = new ArrayList();
    NodeList nl = doc.getElementsByTagName("ObjectAll");
    if ((nl != null) && (nl.getLength() > 0)) {
      Element objectEle = (Element) nl.item(0);
      NodeList nlObjects = objectEle.getElementsByTagName("Row");
      if ((nlObjects != null) && (nlObjects.getLength() > 0)) {
        for (int i = 0; i < nlObjects.getLength(); i++) {
          Element el = (Element) nlObjects.item(i);
          ObjectToken mt = ObjectToken.getMenuToken(el);
          arrlObjects.add(mt);
        }
      }
    }

    return arrlObjects;
  }
}

