package com.viettel.gnoc.commons.interceptor;

import com.viettel.gnoc.commons.business.CfgWhiteListIpBusiness;
import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.security.PassTranformer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BasicAuthAuthorizationIntercepter extends SoapHeaderInterceptor {

  @Autowired
  CfgWhiteListIpBusiness userPassWordBusiness;


  @Override
  public void handleMessage(Message message) throws Fault {

    HttpServletRequest request = (HttpServletRequest) message
        .get(AbstractHTTPDestination.HTTP_REQUEST);
    String ipClient = request.getRemoteAddr();
    String userWS = "";
    String[] list = new String[0];

    // This is set by CXF
    AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
    // If the policy is not set, the user did not specify
    // credentials. A 401 is sent to the client to indicate
    // that authentication is required
    if (policy == null) {
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
      return;
    }
    CfgWhiteListIpDTO userPassWordDTO = userPassWordBusiness
        .checkIpSystemName(policy.getUserName());
    if (userPassWordDTO != null && StringUtils.isNotNullOrEmpty(userPassWordDTO.getIp())) {
      list = userPassWordDTO.getIp().split(",");
      List<String> lstIp = new ArrayList<>();
      List<String> lstSub = new ArrayList<>();
      if (list != null && list.length > 0) {
        for (String item : list) {
          if (item.contains("/")) {
            lstSub.add(item);
          }
          else {
            lstIp.add(item);
          }
        }
      }

      if (!lstIp.contains(ipClient) && !checkLstSub(ipClient, lstSub)) {
        sendErrorResponse(message, HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
      } else {
        // Verify the password
        try {
          PassTranformer.setInputKey(userPassWordDTO.getSystemName());
          userWS = PassTranformer.decrypt(policy.getPassword());
          PassTranformer.setInputKey(null);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (StringUtils.isStringNullOrEmpty(userWS) || !policy.getUserName().equals(userWS)) {
          log.warn("Invalid username or password for user: " + policy.getUserName());
          sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
      }
    } else {
      sendErrorResponse(message, HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
    }
  }


  private static boolean checkLstSub (String ip, List<String> lstSub) {
    try {
      for (String sub : lstSub) {
        if (checkIp(ip, sub)) {
          return true;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return false;
  }

  private static boolean checkIp(String ipCheck, String sub) {
    long ipLow = ipToLong(ipAdressGetLow(sub));
    long ipHight = ipToLong(ipAdressGetHigh(sub));
    long ip = ipToLong(ipCheck);

    if (ipLow <= ip && ip <= ipHight) {
      return true;
    } else {
      return false;
    }
  }

  private static String ipAdressGetHigh(String ipAddress) {
    SubnetUtils utils = new SubnetUtils(ipAddress);
    SubnetUtils.SubnetInfo info = utils.getInfo();
    return info.getHighAddress();
  }

  private static String ipAdressGetLow(String ipAddress) {
    SubnetUtils utils = new SubnetUtils(ipAddress);
    SubnetUtils.SubnetInfo info = utils.getInfo();
    return info.getLowAddress();
  }

  private static long ipToLong(String ipAddress) {

    String[] ipAddressInArray = ipAddress.split("\\.");

    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {

      int power = 3 - i;
      int ip = Integer.parseInt(ipAddressInArray[i]);
      result += ip * Math.pow(256, power);

    }

    return result;
  }

  private void sendErrorResponse(Message message, int responseCode) {
    Message outMessage = getOutMessage(message);
    outMessage.put(Message.RESPONSE_CODE, responseCode);
    // Set the response headers
    Map responseHeaders = (Map) message.get(Message.PROTOCOL_HEADERS);
    if (responseHeaders != null) {
      responseHeaders.put("WWW-Authenticate", Arrays.asList(new String[]{"Basic realm=realm"}));
      responseHeaders.put("Content-length", Arrays.asList(new String[]{"0"}));
    }
    message.getInterceptorChain().abort();
    try {
      getConduit(message).prepare(outMessage);
      close(outMessage);
    } catch (IOException e) {
      log.warn(e.getMessage(), e);
    }
  }

  private Message getOutMessage(Message inMessage) {
    Exchange exchange = inMessage.getExchange();
    Message outMessage = exchange.getOutMessage();
    if (outMessage == null) {
      Endpoint endpoint = exchange.get(Endpoint.class);
      outMessage = endpoint.getBinding().createMessage();
      exchange.setOutMessage(outMessage);
    }
    outMessage.putAll(inMessage);
    return outMessage;
  }

  private Conduit getConduit(Message inMessage) throws IOException {
    Exchange exchange = inMessage.getExchange();
    EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
    Conduit conduit = exchange.getDestination().getBackChannel(inMessage);
    exchange.setConduit(conduit);
    return conduit;
  }

  private void close(Message outMessage) throws IOException {
    OutputStream os = outMessage.getContent(OutputStream.class);
    os.flush();
    os.close();
  }


}
