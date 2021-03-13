package com.viettel.gnoc.kedb.servlet;

import com.viettel.gnoc.commons.utils.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author TienNV
 */
@Slf4j
@WebServlet(urlPatterns = "/ViewVideo/*")
public class ViewVideoServlet extends HttpServlet {

  @Value("${application.path.list_pt}")
  private String pathListPT;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    OutputStream out = null;
    InputStream inputStream = null;
    try {

      String subPath = (String) request.getParameter("createTime");
      String fileName = URLDecoder
          .decode(((String[]) request.getParameterMap().get("kedbFileName"))[0], "UTF-8");
      if (!StringUtils.isStringNullOrEmpty(subPath) && !StringUtils.isStringNullOrEmpty(fileName)) {
        String pathDownload = "";
        String[] lstPath = pathListPT.split("#####");
        for (String path : lstPath) {
          pathDownload =
              path + (subPath != null && !"".equals(subPath) ? subPath + "/" : "") + fileName;
          File fileDownload = new File(pathDownload);
          if (!fileDownload.exists()) {
            continue;
          }
          break;
        }
        File file = new File(pathDownload);
        inputStream = new FileInputStream(pathDownload);

        out = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
        response.setHeader("Content-Disposition", "inline; filename=" + fileName + ";");
        response.setContentLength((int) file.length());
        response.setContentType("application/octet-stream");

        out.close();
      }
    } catch (Exception e) {

      try {
        throw new ServletException("Exception in Excel Sample Servlet", e);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    try {
      doGet(request, response);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

}
