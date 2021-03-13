package com.viettel.gnoc.auth.config;

/**
 * Created by TungPV
 */

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;
import viettel.passport.util.Connector;
import viettel.passport.util.VsaFilter;

@Slf4j
public class CustomVsaFilter extends VsaFilter {

  private static HashSet<String> allMenuURL = new HashSet();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = null;
    HttpServletResponse res = null;
    if (request instanceof HttpServletRequest) {
      req = (HttpServletRequest) request;
    }

    if (response instanceof HttpServletResponse) {
      res = (HttpServletResponse) response;
    }
    if (req != null && res != null) {
      Connector cnn = new Connector(req, res);
      if (this.alowURL(req.getRequestURI(), Connector.getAllowedUrls())) {
        chain.doFilter(req, res);
      } else if (!cnn.isAuthenticate() && req.getRequestURI().contains("/oauthVsa/home")) {
        if (cnn.hadTicket()) {
          if (!cnn.getAuthenticate()) {
            res.sendRedirect(Connector.getErrorUrl());
          } else {
            chain.doFilter(request, response);
          }
        } else {
          res.sendRedirect(
              cnn.getPassportLoginURL() + "?appCode=" + cnn.getDomainCode() + "&service="
                  + URLEncoder.encode(cnn.getServiceURL(), "UTF-8"));
        }
      } else if (allMenuURL.contains(req.getServletPath())) {
        if (this.getVsaAllowedServletPath(req).contains(req.getServletPath())) {
          chain.doFilter(request, response);
        } else {
          res.sendRedirect(Connector.getErrorUrl());
        }
      } else {
        chain.doFilter(request, response);
      }
    }
  }

  private Boolean alowURL(String url, String[] listAlowUrl) {
    String[] arr$ = listAlowUrl;
    int len$ = listAlowUrl.length;

    for (int i$ = 0; i$ < len$; ++i$) {
      String str = arr$[i$];
      if (url.equalsIgnoreCase(str)) {
        return true;
      }
    }

    return false;
  }

  private HashSet<String> getVsaAllowedServletPath(HttpServletRequest request) {
    UserToken vsaUserToken = (UserToken) request.getSession().getAttribute("vsaUserToken");
    HashSet<String> vsaAllowedURL = new HashSet();
    Iterator var4 = vsaUserToken.getObjectTokens().iterator();

    while (var4.hasNext()) {
      ObjectToken ot = (ObjectToken) var4.next();
      String servletPath = ot.getObjectUrl();
      if (!"#".equals(servletPath)) {
        if (servletPath.startsWith("/")) {
          vsaAllowedURL.add(servletPath.split("\\?")[0]);
        } else {
          vsaAllowedURL.add("/" + servletPath.split("\\?")[0]);
        }
      }
    }

    return vsaAllowedURL;
  }
}

