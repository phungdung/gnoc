package com.viettel.gnoc.wo.utils;

import com.viettel.security.PassTranformer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WS_HOAN_CONG_Port {

  @Value("${application.ws.hoan.cong.url:null}")
  private String urlHC;
  @Value("${application.ws.hoan.cong.user:null}")
  private String user;
  @Value("${application.ws.hoan.cong.pass:null}")
  private String pass;

  @PostConstruct
  public void init() {
    try {
      user = PassTranformer.decrypt(user);
      pass = PassTranformer.decrypt(pass);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public String updateWoToHoanCong(String woId) {
    try {
      URL url = new URL(urlHC);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      HoanCongInput o = new HoanCongInput(user, pass, woId);

      OutputStream os = conn.getOutputStream();
      os.write(o.toString().getBytes());
      os.flush();

      BufferedReader br = new BufferedReader(new InputStreamReader(
          (conn.getInputStream())));

      String output;
      String res = null;

      while ((output = br.readLine()) != null) {
        res = output;
      }
      conn.disconnect();
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

}
