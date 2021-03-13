package com.viettel.gnoc.ws.provider;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

/**
 * Created by LamNV5 on 4/27/2015.
 */
@Getter
@Setter
public class CxfWsClientFactory implements CallbackHandler {

  private Map<String, WsEndpoint> wsEndpointMap;
  private List<String> keys;

  public CxfWsClientFactory() {

  }

  /**
   * Callback handle for password verify
   */
  public void handle(Callback[] callbacks) {
    WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

    for (WsEndpoint wsEndpoint : wsEndpointMap.values()) {
      if (pc.getIdentifier().equals(wsEndpoint.getUserName())) {
        pc.setPassword(wsEndpoint.getPassword());
      }
    }
  }

  /**
   * Tao ra cxf-ws-client trong truong hop ServiceInterace va ServiceImpl khac package
   */
  public <T> T createWsClient(Class<T> interfaceClass) throws Exception {
//        disableSslVerification();
    WsEndpoint wsEndpoint = getWsEndpoint(interfaceClass);
    if (wsEndpoint == null) {
      throw new WsEndpointNotFound(interfaceClass.getName());
    }

    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    factory.setServiceClass(interfaceClass);
    factory.setAddress(wsEndpoint.getAddressConnectBean()
        + interfaceClass.getSimpleName()
    );

    //Neu co cau hinh username => dang ky
    if (StringUtils.isNotBlank(wsEndpoint.getUserName())) {
      Map<String, Object> outProps = new HashMap<String, Object>();
      outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
      // Specify our username
      outProps.put(WSHandlerConstants.USER, wsEndpoint.getUserName());
      // Password type : plain text
      outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
//            outProps.put("password", wsEndpoint.getPassword());
      // for hashed password use:
      //properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
      // Callback used to retrieve password for given user.
      outProps.put(WSHandlerConstants.PW_CALLBACK_REF,
          this);

      WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
      factory.getOutInterceptors().add(wssOut);
    }

    //Logging in
//        factory.getInInterceptors().add(loggingInInterceptor);
    //Logging out
//        factory.getOutInterceptors().add(loggingOutInterceptor);
    //Inject generic web info
//        factory.getOutInterceptors().add(injectGenericWebInfoInterceptor);
    //Gzip uncompress interceptor
//        factory.getInInterceptors().add(new org.apache.cxf.transport.common.gzip.GZIPInInterceptor());
    T serviceClient = (T) factory.create();

    //Compress data START, 30/05/2015
    Client proxy = ClientProxy.getClient(serviceClient);
    HTTPConduit http = (HTTPConduit) proxy.getConduit();

    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

    httpClientPolicy.setAllowChunking(false);
    httpClientPolicy.setAccept("text/xml");
    httpClientPolicy.setAcceptEncoding("gzip, deflate"); //gzip,deflate,sdch
    httpClientPolicy.setCacheControl("No-Cache");
    httpClientPolicy.setContentType("text/xml");
//        httpClientPolicy.setConnectionTimeout(wsEndpoint.getTimeout());
    httpClientPolicy.setReceiveTimeout(wsEndpoint.getTimeout());

    http.setClient(httpClientPolicy);
    CxfWsClientInInterceptor cxfWsClientInInterceptor = new CxfWsClientInInterceptor();
    CxfWsClientOutInterceptor cxfWsClientOutInterceptor = new CxfWsClientOutInterceptor();
    proxy.getInInterceptors().add(cxfWsClientInInterceptor);
    proxy.getOutInterceptors().add(cxfWsClientOutInterceptor);
    //Compress data END

    return serviceClient;
  }

  /**
   * @return com.viettel.sale => ep1 com.viettel.sale.connect => ep2 com.viettel.sale.connect.InputSubImpl
   * => ep3
   * <p/>
   * input: com.viettel.sale.trans.SaleTransImpl output: ep1
   * <p/>
   * input: com.viettel.sale.connect.InputCustImpl output: ep2
   * <p/>
   * input: com.viettel.sale.connect.InputSubImpl output: ep3
   */
  private WsEndpoint getWsEndpoint(Class callClass) {
    String classPath = callClass.getName();
    for (String key : keys) {
      if (classPath.startsWith(key)) {
        return wsEndpointMap.get(key);
      }
    }

    return null;
  }

  public void setWsEndpointMap(Map<String, WsEndpoint> wsEndpointMap) {
    this.wsEndpointMap = wsEndpointMap;
    keys = Lists.newArrayList(wsEndpointMap.keySet());
    Collections.sort(keys, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o2.compareTo(o1);
      }
    });
  }


}
