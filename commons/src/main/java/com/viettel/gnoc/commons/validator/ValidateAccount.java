package com.viettel.gnoc.commons.validator;

import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.Passwords;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
public class ValidateAccount {

  public String checkAuthenticate(AuthorityDTO requestDTO) {
    String result = RESULT.FAIL;
    try {
      if (StringUtils.isStringNullOrEmpty(requestDTO.getUsername())) {
        result = "username.isNull";
      } else if (StringUtils.isStringNullOrEmpty(requestDTO.getPassword())) {
        result = "password.isNull";
      } else if (authen(requestDTO.getUsername(), requestDTO.getPassword())) {
        result = RESULT.SUCCESS;
      } else {
        result = "username.password.invalid";
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  private boolean authen(String userService, String passService) {
    try {
      String fileUser = "user.dat";
      String filePass = "pass.dat";
      String fileSalt = "salt.dat";
      Resource resourceUser = new ClassPathResource(fileUser);
      InputStream fileUserInput = resourceUser.getInputStream();
      Resource resourcePass = new ClassPathResource(filePass);
      InputStream filePassInput = resourcePass.getInputStream();
      Resource resourceSalt = new ClassPathResource(fileSalt);
      InputStream fileSaltInput = resourceSalt.getInputStream();

      byte[] userConf = IOUtils.toByteArray(fileUserInput);
      byte[] passConf = IOUtils.toByteArray(filePassInput);
      byte[] saltConf = IOUtils.toByteArray(fileSaltInput);

      return Passwords.isExpectedPassword(userService.toCharArray(), saltConf, userConf)
          && Passwords.isExpectedPassword(passService.toCharArray(), saltConf, passConf);

    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
    return false;
  }

}
