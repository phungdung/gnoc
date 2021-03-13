/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.utils;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.lang.reflect.Type;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;

import com.viettel.gnoc.commons.dto.RestfulDTO;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

//import org.springframework.web.client.RestClientException;

/**
 * @author thanhlv12
 */
public class CallRestful {

  protected static Logger log = Logger.getLogger(CallRestful.class);

  private static final int DEFAULT_TIME_OUT = 120000;
//    private static final String DEFAULT_METHOD = "POST";

  public static String post(RestfulDTO input) throws Exception {

    try {
      RestTemplate restTemplate = new RestTemplate();

      SimpleClientHttpRequestFactory rf
          = (SimpleClientHttpRequestFactory) restTemplate
          .getRequestFactory();
      rf.setReadTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);
      rf.setConnectTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);

      input = prepare(input);
      HttpEntity<String> req = new HttpEntity<String>(input.getBody(),
          input.getHeaders());
      ResponseEntity<String> response = restTemplate.
          postForEntity((input.getUrl()), req, String.class);
      if (HttpStatus.OK == response.getStatusCode()) { //check 200 OK
        return response.getBody();
      } else {
        return response.getBody();
      }
    } catch (HttpClientErrorException e) {
      return e.getResponseBodyAsString();
    } catch (HttpServerErrorException e) {
      return e.getResponseBodyAsString();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static ResponseEntity<String> postForGetResponse(RestfulDTO input) throws Exception {

    try {
      RestTemplate restTemplate = new RestTemplate();

      SimpleClientHttpRequestFactory rf
          = (SimpleClientHttpRequestFactory) restTemplate
          .getRequestFactory();
      rf.setReadTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);
      rf.setConnectTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);

      input = prepare(input);
      HttpEntity<String> req = new HttpEntity<String>(input.getBody(),
          input.getHeaders());
      ResponseEntity<String> response = restTemplate.
          postForEntity((input.getUrl()), req, String.class);
      if (HttpStatus.OK == response.getStatusCode()) { //check 200 OK
        return response;
      } else {
        return response;
      }
    } catch (HttpClientErrorException e) {
      log.error(e.getMessage(), e);
      return null;
    } catch (HttpServerErrorException e) {
      log.error(e.getMessage(), e);
      return null;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public static String get(RestfulDTO input) throws Exception {

    try {
      RestTemplate restTemplate = new RestTemplate();

      SimpleClientHttpRequestFactory rf
          = (SimpleClientHttpRequestFactory) restTemplate
          .getRequestFactory();
      rf.setReadTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);
      rf.setConnectTimeout(input.getTimeOut() != null
          ? input.getTimeOut().intValue() : DEFAULT_TIME_OUT);

      input = prepare(input);
      HttpEntity<String> req = new HttpEntity<String>(input.getBody(),
          input.getHeaders());
      ResponseEntity<String> response = restTemplate.
          exchange((input.getUrl()), HttpMethod.GET, req, String.class);
      if (HttpStatus.OK == response.getStatusCode()) { //check 200 OK
        return response.getBody();
      } else {
        return response.getBody();
      }
    } catch (HttpClientErrorException e) {
      return e.getResponseBodyAsString();
    } catch (HttpServerErrorException e) {
      return e.getResponseBodyAsString();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }


    /*
    day du lieu len res va lay ve dang JSON
     */
//    public static String callRest(RestfulDTO input) {
//
//        HttpURLConnection conn = null;
//        try {
//            StringBuilder strTmp = new StringBuilder();
//            conn = prepare(input);
//            int responseCode = 200;
//            BufferedReader br = null;
//
//            conn.getResponseMessage();
//            conn.getContent();
//
//            try {
//                br = new BufferedReader(new InputStreamReader(
//                        (conn.getInputStream())));
//                responseCode = conn.getResponseCode();
//            } catch (Exception e) {
//                responseCode = conn.getResponseCode();
//            }
//            String output;
//            int resPonseCode = conn.getResponseCode();
//            if (resPonseCode == 200 && br != null) {
//                while ((output = br.readLine()) != null) {
//                    strTmp.append(output);
//                }
//            } else {
//
//                throw new Exception(resPonseCode + "");
//            }
//            conn.disconnect();
//            return strTmp.toString();
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            try {
//                conn.disconnect();
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//        }
//        return null;
//    }
//    public static Object execute2(RestfulDTO input, Type type) {
//        try {
//            log.info("-------Begin execute REST------------");
//            return new Gson().fromJson(callRest2(input), type);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return null;
//    }

    /*
    mo ket noi
     */
//    public static HttpURLConnection getConnection(String url,
//            RestfulDTO o) {
//        try {
//            URL u = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
//
//            conn.setConnectTimeout(o.getTimeOut() != null
//                    ? o.getTimeOut() : DEFAULT_TIME_OUT);
//
//            conn.setRequestMethod(o.getMethod() != null
//                    ? o.getMethod() : DEFAULT_METHOD);
//            // set header
//            if (o.getmHeader() != null && !o.getmHeader().isEmpty()) {
//                for (Map.Entry<String, String> h : o.getmHeader().entrySet()) {
//                    conn.setRequestProperty(h.getKey(), h.getValue());
//                }
//            }
//
//            conn.setDoOutput(true);
//            if (!StringUtils.isNullOrEmpty(o.getBody())) {
//                try (OutputStream os = conn.getOutputStream()) {
//                    byte[] input = o.getBody().getBytes();
//                    os.write(input, 0, input.length);
//                }
//            }
//            return conn;
//        } catch (Exception e) {
//            log.info("-------FAIL: Open Connection to REST------------");
//            log.error(e.getMessage(), e);
//        }
//        return null;
//    }

  /*
  chuan bi du lieu
  param, header, resource, timeOut,
   */
  public static RestfulDTO prepare(RestfulDTO res) {
    String url = res.getUrl();
    // set them resource
    url += res.getResourcePath() != null ? res.getResourcePath() : "";

    // set them param
    if (res.getParams() != null && !res.getParams().isEmpty()) {
      url += "?";
      // noi param vao url
      for (Map.Entry<String, String> p : res.getParams().entrySet()) {
        url += p.getKey() + "="
            + (p.getValue() != null ? p.getValue() : "") + "&";
      }
    }
    res.setUrl(url);
    // lay ra connection
//        HttpURLConnection conn = getConnection(url, res);
    return res;
  }

}
