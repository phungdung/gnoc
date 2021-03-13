/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author quangdx
 */
public class JsonUtils {

  public HttpClient getHttpClient(Integer readTimeout, Integer connectTimeout) {
    RequestConfig config = RequestConfig.custom()
        .setSocketTimeout(readTimeout)
        .setConnectTimeout(connectTimeout)
        .setConnectionRequestTimeout(connectTimeout)
        .build();

    HttpClientBuilder clientBuilder = HttpClientBuilder.create()
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        .setDefaultRequestConfig(config);

    HttpClient httpClient = clientBuilder.build();

    return httpClient;
  }

  public void close(HttpClient httpClient) throws IOException {
    ((CloseableHttpClient) httpClient).close();
  }

  public RequestBuilder configureRequest(String method, String urlStr, String postData) {
    RequestBuilder builder;
    if ("GET".equals(method)) {
      builder = RequestBuilder.get().setUri(urlStr);
    } else if ("POST".equals(method)) {
      builder = RequestBuilder.post().setUri(urlStr);
      if (postData.length() > 0) {
        try {
          String contentType = "application/json";
          StringEntity entity = new StringEntity(postData);
          entity.setContentType(contentType);
          builder.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(
              "Cannot set body for REST request [" + builder.getMethod() + "] " + builder.getUri(),
              e);
        }
      }
    } else {
      throw new IllegalArgumentException("Method " + method + " is not supported");
    }
    return builder;
  }

  public HttpResponse doRequest(HttpClient httpclient, RequestBuilder requestBuilder) {
    HttpUriRequest request = requestBuilder.build();
    try {
      return httpclient.execute(request);
    } catch (Exception e) {
      throw new RuntimeException(
          "Could not execute request [" + request.getMethod() + "] " + request.getURI(), e);
    }
  }
}
