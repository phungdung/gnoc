package com.viettel.gnoc.security;

import com.google.gson.Gson;
import com.viettel.gnoc.security.dto.HazelcastDto;
import com.viettel.gnoc.security.dto.HazelcastResponseDto;
import com.viettel.gnoc.security.dto.UsersDto;
import com.viettel.gnoc.security.proxy.HazelcastProxy;
import io.jsonwebtoken.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import viettel.passport.client.RoleToken;
import viettel.passport.client.UserToken;

@Component
public class TokenProvider {

  @Autowired
  HazelcastProxy hazelcastProxy;

  private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

  private static final String AUTHORITIES_KEY = "auth";
  private static final String PASS_KEY = "pass";
  private static final String UNIT_ID_KEY = "unitId";
  private static final String USER_ID_KEY = "userId";
  private static final String FULL_NAME = "fullName";
  private static final String TICKET = "ticket";

  @Value("${spring.security.authentication.jwt.secret}")
  private String secretKey;

  @Value("${spring.security.authentication.jwt.token-validity-in-seconds}")
  private long tokenValidityInMilliseconds;

  @Value("${spring.security.authentication.jwt.token-validity-in-seconds-for-remember-me}")
  private long tokenValidityInMillisecondsForRememberMe;

  @Value("${application.passport.logedInTimeOut:60}")
  private long logedInTimeOut;

  public TokenProvider() {
  }

  @PostConstruct
  public void init() {
  }

  public String createToken(Authentication authentication, Boolean rememberMe, UsersDto userDto,
      String ticket) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity;
    if (rememberMe) {
      validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    } else {
      validity = new Date(now + this.tokenValidityInMilliseconds);
    }

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .claim(PASS_KEY, authentication.getCredentials().toString())
        .claim(UNIT_ID_KEY, userDto.getUnitId() == null ? 0 : String.valueOf(userDto.getUnitId()))
        .claim(USER_ID_KEY, userDto.getUserId() == null ? 0 : String.valueOf(userDto.getUserId()))
        .claim(FULL_NAME, String.valueOf(userDto.getFullname()))
        .claim(TICKET, ticket)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    String pass = claims.get(PASS_KEY).toString();
    User principal = new User(claims.getSubject(), pass, authorities);
    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
        principal, token, authorities);
    UsersDto usersDto = new UsersDto();
    usersDto.setUsername(userToken.getName());
    try {
      usersDto.setUnitId(Long.parseLong((String) (claims.get(UNIT_ID_KEY))));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      usersDto.setUnitId(null);
    }
    try {
      usersDto.setUserId(Long.parseLong((String) (claims.get(USER_ID_KEY))));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      usersDto.setUserId(null);
    }
    try {
      usersDto.setFullname((String) (claims.get(FULL_NAME)));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      usersDto.setFullname(null);
    }
    userToken.setDetails(usersDto);
    return userToken;
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
      Claims claims = Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(authToken)
          .getBody();
      if (claims != null && claims.get(TICKET) != null) {
        String ticket = (String) claims.get(TICKET);
        HazelcastDto hazelcastDto = new HazelcastDto(ticket, null, 0, null);
        HazelcastResponseDto hazelcastResponseDto = hazelcastProxy
            .getDataFromHazelCast(hazelcastDto);
        if (hazelcastResponseDto != null) {
          String userJsonObj = hazelcastResponseDto.getResponse();
          if (userJsonObj == null || "".equals(userJsonObj)) {
            return false;
          }
          UserToken vsaUserToken = new Gson().fromJson(userJsonObj, UserToken.class);
          vsaUserToken.setRoleUserList(new ArrayList<>());
          List<String> listAuthority = Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(","));
          for (String authority : listAuthority) {
            RoleToken roleToken = new RoleToken();
            roleToken.setRoleCode(authority);
            vsaUserToken.getRoleUserList().add(roleToken);
          }
          /**
           * QuangNV comment de check loi heap size
           */
          /*String userJsonObjNew = new Gson().toJson(vsaUserToken);
          HazelcastDto hazelcastDtoPut = new HazelcastDto(ticket, userJsonObjNew, logedInTimeOut, TimeUnit.MINUTES);
          hazelcastProxy.putDataToHazelCast(hazelcastDtoPut);*/
        } else {
          return false;
        }
      } else {
        return false;
      }
      return true;
    } catch (SignatureException e) {
      log.info("Invalid JWT signature.");
      log.trace("Invalid JWT signature trace: {}", e);
    } catch (MalformedJwtException e) {
      log.info("Invalid JWT token.");
      log.trace("Invalid JWT token trace: {}", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
      log.trace("Expired JWT token trace: {}", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
      log.trace("Unsupported JWT token trace: {}", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
      log.trace("JWT token compact of handler are invalid trace: {}", e);
    }
    return false;
  }
}
