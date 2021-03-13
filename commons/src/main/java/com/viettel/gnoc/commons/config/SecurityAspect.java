package com.viettel.gnoc.commons.config;

import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import viettel.passport.client.RoleToken;
import viettel.passport.client.UserToken;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CommonBusiness commonBusiness;

  @Pointcut("@annotation(com.viettel.gnoc.commons.config.SecurityAnnotation)")
  private void securityAnnotation() {
  }

  @Around("com.viettel.gnoc.commons.config.SecurityAspect.securityAnnotation()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    HttpServletResponse res = getResponse();
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    SecurityAnnotation securityAnnotation = method.getAnnotation(SecurityAnnotation.class);
    UserToken userToken = ticketProvider.getUserToken();
    List<String> listAuthority = userToken.getRoleUserList().stream().map(RoleToken::getRoleCode)
        .collect(Collectors.toList());
    if (securityAnnotation.role().length == 1 && securityAnnotation.moduleRole().length == 0
        && securityAnnotation.permission().length > 0) {
      if (!listAuthority.contains(securityAnnotation.role()[0])) {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access is denied");
        return null;
      }
    } else {
      if (securityAnnotation.role().length >= 1
          && securityAnnotation.role().length == securityAnnotation.moduleRole().length
          && securityAnnotation.permission().length > 0) {
        String moduleName = (String) getParameterByName(pjp, "moduleName");
        if (StringUtils.isNotNullOrEmpty(moduleName)) {
          String role = null;
          for (int i = 0; i < securityAnnotation.moduleRole().length; i++) {
            if (moduleName.equals(securityAnnotation.moduleRole()[i])) {
              role = securityAnnotation.role()[i];
              break;
            }
          }
          if (StringUtils.isNotNullOrEmpty(role)) {
            if (!listAuthority.contains(role)) {
              res.sendError(HttpStatus.FORBIDDEN.value(), "Access is denied");
              return null;
            }
          }
        }
      }
    }
    return pjp.proceed();
  }

  @Pointcut("@annotation(com.viettel.gnoc.commons.config.CustomSecurityAnnotation)")
  private void customSecurityAnnotation() {
  }

  @Around("com.viettel.gnoc.commons.config.SecurityAspect.customSecurityAnnotation()")
  public Object customAround(ProceedingJoinPoint pjp) throws Throwable {
    HttpServletResponse res = getResponse();
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    CustomSecurityAnnotation customSecurityAnnotation = method
        .getAnnotation(CustomSecurityAnnotation.class);
    if (CONFIG_PROPERTY.SUB_ADMIN_EDIT.equals(customSecurityAnnotation.target())) {
      if (customSecurityAnnotation.permission().length == 1
          && customSecurityAnnotation.modulePermission().length == 0) {
        ResultInSideDto data = commonBusiness
            .checkRoleSubAdmin(customSecurityAnnotation.permission()[0]);
        if (!data.getCheck()) {
          res.sendError(HttpStatus.FORBIDDEN.value(), "Access is denied");
          return null;
        }
      } else {
        if (customSecurityAnnotation.permission().length >= 1
            && customSecurityAnnotation.permission().length == customSecurityAnnotation
            .modulePermission().length) {
          String moduleName = (String) getParameterByName(pjp, "moduleName");
          if (StringUtils.isNotNullOrEmpty(moduleName)) {
            String permission = null;
            for (int i = 0; i < customSecurityAnnotation.modulePermission().length; i++) {
              if (moduleName.equals(customSecurityAnnotation.modulePermission()[i])) {
                permission = customSecurityAnnotation.permission()[i];
                break;
              }
            }
            if (StringUtils.isNotNullOrEmpty(permission)) {
              ResultInSideDto data = commonBusiness.checkRoleSubAdmin(permission);
              if (!data.getCheck()) {
                res.sendError(HttpStatus.FORBIDDEN.value(), "Access is denied");
                return null;
              }
            }
          }
        }
      }
    }
    return pjp.proceed();
  }

  public Object getParameterByName(ProceedingJoinPoint proceedingJoinPoint, String parameterName) {
    MethodSignature methodSig = (MethodSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    String[] parametersName = methodSig.getParameterNames();
    int idx = Arrays.asList(parametersName).indexOf(parameterName);
    if (args.length > idx) {
      return args[idx];
    }
    return null;
  }

  private HttpServletRequest getRequest() {
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    return sra.getRequest();
  }

  private HttpServletResponse getResponse() {
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    return sra.getResponse();
  }
}
