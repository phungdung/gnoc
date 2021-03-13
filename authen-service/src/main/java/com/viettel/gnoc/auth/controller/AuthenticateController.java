package com.viettel.gnoc.auth.controller;

import com.google.gson.Gson;
import com.viettel.gnoc.security.TokenProvider;
import com.viettel.gnoc.security.business.UserBusiness;
import com.viettel.gnoc.security.dto.HazelcastDto;
import com.viettel.gnoc.security.dto.HazelcastResponseDto;
import com.viettel.gnoc.security.dto.ResultDto;
import com.viettel.gnoc.security.dto.RolesDTO;
import com.viettel.gnoc.security.dto.UsersDto;
import com.viettel.gnoc.security.proxy.CommonProxy;
import com.viettel.gnoc.security.proxy.HazelcastProxy;
import com.viettel.gnoc.security.util.SecurityConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import smartofficeauth.SmartOfficeAuth;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.RoleToken;
import viettel.passport.client.UserToken;

/**
 * @author Quang.NgoVan
 * @since 20190321
 */

@RestController
@Slf4j
@RequestMapping("/oauthVsa")
public class AuthenticateController {

  private static final String SECURITY_AUTHENTICATION_JWT_SECRET = "SECURITY_AUTHENTICATION_JWT_SECRET";

  @Value("${spring.security.authentication.jwt.secret}")
  private String secretKey;

  @Value("${application.passport.redirectUrl}")
  private String redirectUrl;

  @Value("${application.passport.domainCode}")
  private String domainCode;

  @Value("${application.passport.service}")
  private String service;

  @Value("${application.passport.logoutUrl}")
  private String logoutUrl;

  @Value("${application.passport.logedInTimeOut:60}")
  private long logedInTimeOut;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  TokenProvider tokenProvider;

  @Autowired
  HazelcastProxy hazelcastProxy;

  @Autowired
  CommonProxy commonProxy;

  @RequestMapping(value = "/home", method = RequestMethod.GET)
  public RedirectView gotoHome(HttpServletRequest request, HttpServletResponse response) {
    String ticket = request.getParameter("ticket");
    try {
      UserToken vsaUserToken = (UserToken) request.getSession().getAttribute("vsaUserToken");
      if (vsaUserToken != null && ticket != null) {
        Gson gson = new Gson();
        UsersDto usersDto = userBusiness.getUserDTOByUserName(vsaUserToken.getUserName());
        if (usersDto != null) {
          vsaUserToken.setUserID(usersDto.getUserId());
          vsaUserToken.setDeptId(usersDto.getUnitId());
          vsaUserToken.setFullName(usersDto.getFullname());
          vsaUserToken.setTelephone(usersDto.getMobile());
        } else {
          log.warn("UserTick get Null: " + vsaUserToken.getUserName());
        }

        //add process Module Role
        processRoleModule(vsaUserToken);

        String userJsonObj = gson.toJson(vsaUserToken);
        /**
         * QuangNV test loi heap size
         */
      /*HazelcastDto hazelcastDto = new HazelcastDto(ticket, userJsonObj, logedInTimeOut,
          TimeUnit.MINUTES);
      hazelcastProxy.putDataToHazelCast(hazelcastDto);
      HazelcastDto hazelcastDtoSecret = new HazelcastDto(SECURITY_AUTHENTICATION_JWT_SECRET,
          secretKey, logedInTimeOut, TimeUnit.MINUTES);
      hazelcastProxy.putDataToHazelCast(hazelcastDtoSecret);*/
        HazelcastDto hazelcastDto = new HazelcastDto(ticket, userJsonObj, 2,
            TimeUnit.DAYS);
        hazelcastProxy.putDataToHazelCast(hazelcastDto);
        HazelcastDto hazelcastDtoSecret = new HazelcastDto(SECURITY_AUTHENTICATION_JWT_SECRET,
            secretKey, 2, TimeUnit.DAYS);
        hazelcastProxy.putDataToHazelCast(hazelcastDtoSecret);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    RedirectView redirectView = new RedirectView();
    if (ticket != null) {
      redirectView.setUrl(redirectUrl + "?ticket=" + ticket);
    } else {
      redirectView.setUrl(redirectUrl);
    }
    return redirectView;
  }

  @ApiOperation(value = "Validate the given token VSA", response = ResultDto.class)
  @RequestMapping(value = "/validateTokenVsa", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = ResultDto.class),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 403, message = "Forbidden"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Failure")})
  public ResponseEntity<ResultDto> validateTokenVsa(@RequestHeader HttpHeaders httpHeaders,
      HttpServletRequest request, HttpServletResponse response, Principal principal) {
    String ticket = request.getParameter("ticket");
    ResultDto resultDto = new ResultDto();
    if (ticket == null) {
      log.info("ticket is null");
      resultDto.setMessage("invalid_session_vsa");
      return new ResponseEntity<>(resultDto, HttpStatus.UNAUTHORIZED);
    }
    ResponseEntity<ResultDto> responseEntity;
    try {
      HazelcastDto hazelcastDto = new HazelcastDto(ticket, null, 0, null);
      HazelcastResponseDto hazelcastResponseDto = hazelcastProxy.getDataFromHazelCast(hazelcastDto);
      String userJsonObj = hazelcastResponseDto.getResponse();
      UserToken vsaUserToken = new Gson().fromJson(userJsonObj, UserToken.class);
      if (vsaUserToken != null) {
        UsersDto usersDto = null;
        try {
          System.out.println("=============debug login==================");
          usersDto = userBusiness.getUserDTOByUserName(vsaUserToken.getUserName());
          if (usersDto != null) {
            List<RolesDTO> gnocRoles = userBusiness.getRolesByUser(usersDto.getUserId());
            for (RolesDTO roleDto : gnocRoles) {
              RoleToken roleToken = new RoleToken();
              roleToken.setRoleCode(roleDto.getRoleCode());
              roleToken.setRoleName(roleDto.getRoleName());
              roleToken.setDescription(roleDto.getDescription());
              if (vsaUserToken.getRolesList() == null) {
                vsaUserToken.setRolesList(new ArrayList<>());
              }
              vsaUserToken.getRolesList().add(roleToken);
            }
            resultDto.setLanguageKey(usersDto.getLanguageKey());
          } else {
            resultDto.setMessage("user not active");
            //request.getSession().invalidate();
            responseEntity = new ResponseEntity<>(resultDto, HttpStatus.UNAUTHORIZED);
            return responseEntity;
          }
        } catch (Exception ex) {
          usersDto = null;
          log.trace("Authentication exception trace: {}", ex);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAutority = new SimpleGrantedAuthority(SecurityConstants.GNOC_ROLE);
        authorities.add(grantedAutority);
        // Menu List
        if (vsaUserToken.getParentMenu() != null) {
          for (ObjectToken parentItem : vsaUserToken.getParentMenu()) {
            if (parentItem.getChildObjects() != null) {
              GrantedAuthority parentMenu = new SimpleGrantedAuthority(parentItem.getObjectCode());
              authorities.add(parentMenu);
              parentItem.setObjectName(
                  String.format("%s.%s", "menu", parentItem.getObjectCode()));
              for (ObjectToken childItem : parentItem.getChildObjects()) {
                if (childItem.getChildObjects() != null) {
                  for (ObjectToken childItemOfChild : childItem.getChildObjects()) {
                    GrantedAuthority vsaChildMenuOfChild = new SimpleGrantedAuthority(
                        childItemOfChild.getObjectCode());
                    authorities.add(vsaChildMenuOfChild);
                    childItemOfChild.setObjectName(
                        String.format("%s.%s", "menu", childItemOfChild.getObjectCode()));
                  }
                }
                GrantedAuthority vsaChildMenu = new SimpleGrantedAuthority(
                    childItem.getObjectCode());
                authorities.add(vsaChildMenu);
                childItem.setObjectName(
                    String.format("%s.%s", "menu", childItem.getObjectCode()));
              }
            }
          }
        }
        // Role List
//      if (vsaUserToken.getRolesList() != null) {
//        for (RoleToken roleToken : vsaUserToken.getRolesList()) {
//          GrantedAuthority roleCode = new SimpleGrantedAuthority(roleToken.getRoleCode());
//          authorities.add(roleCode);
//        }
//      }
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(vsaUserToken.getUserName(), "", authorities);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authenticationToken, rememberMe, usersDto, ticket);
        response.addHeader("Authorization", "Bearer " + jwt);
        resultDto.setTimeout(logedInTimeOut);
        resultDto.setUserToken(vsaUserToken);
        resultDto.setSubAdminViews(commonProxy.getSubAdminViews());
        try {
          Map<String, String> configMap = userBusiness.getConfigProperty();
          Resource resource = new ClassPathResource("public_key.der");
          Path temp = Files.createTempFile("public_key-", ".der");
          Files.copy(resource.getInputStream(), temp, StandardCopyOption.REPLACE_EXISTING);
          File filePublicKey = temp.toFile();
          String pathFile = filePublicKey.getAbsolutePath();
          String token = SmartOfficeAuth
              .getToken(configMap.get("SmartOffice_APP_CODE"), configMap.get("SmartOffice_KEY"),
                  pathFile, vsaUserToken.getStaffCode());
          resultDto.setSmartOfficeToken(token);
          resultDto.setSmartOfficeLink(configMap.get("SmartOffice_Link"));
        } catch (Exception e) {
          log.info(e.getMessage(), e);
        }
        responseEntity = new ResponseEntity<>(resultDto, HttpStatus.OK);
      } else {
        resultDto.setMessage("invalid_session_vsa");
        //request.getSession().invalidate();
        responseEntity = new ResponseEntity<>(resultDto, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      responseEntity = new ResponseEntity<>(resultDto, HttpStatus.UNAUTHORIZED);
    }
    return responseEntity;
  }

  @RequestMapping(value = "/logoutVsa", method = RequestMethod.GET)
  public RedirectView logoutVsa(HttpServletRequest request, HttpServletResponse response)
      throws UnsupportedEncodingException {
    String ticket = request.getParameter("ticket");
    if (ticket != null) {
      HazelcastDto hazelcastDto = new HazelcastDto(ticket, null, 0, null);
      hazelcastProxy.removeDataFromHazelCast(hazelcastDto);
    }
    StringBuilder logoutUrlRedirect = new StringBuilder();
    logoutUrlRedirect.append(logoutUrl)
        .append("?service=" + URLEncoder.encode(service, "UTF-8"))
        .append("&appCode=").append(domainCode);
    request.getSession().invalidate();
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(logoutUrlRedirect.toString());
    return redirectView;
  }

  public void processRoleModule(UserToken vsaUserToken) {
    // Menu List
    List<RoleToken> lstRoleModule = new ArrayList<>();
    try {
      if (vsaUserToken.getParentMenu() != null) {
        RoleToken roleToken;
        for (ObjectToken parentItem : vsaUserToken.getParentMenu()) {
          if (parentItem.getChildObjects() != null) {
            roleToken = new RoleToken();
            roleToken.setRoleCode(parentItem.getObjectCode());
            lstRoleModule.add(roleToken);
            for (ObjectToken childItem : parentItem.getChildObjects()) {
              if (childItem.getChildObjects() != null) {
                for (ObjectToken childItemOfChild : childItem.getChildObjects()) {
                  roleToken = new RoleToken();
                  roleToken.setRoleCode(childItemOfChild.getObjectCode());
                  lstRoleModule.add(roleToken);
                }
              }
              roleToken = new RoleToken();
              roleToken.setRoleCode(childItem.getObjectCode());
              lstRoleModule.add(roleToken);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    vsaUserToken.setRoleUserList(lstRoleModule);
  }
}
