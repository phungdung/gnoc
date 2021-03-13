package com.viettel.gnoc.gateway_service.security;

import com.viettel.gnoc.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
  @Autowired
  TokenProvider tokenProvider;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();
    String userName;
    try{
      userName = tokenProvider.getAuthentication(authToken).getName();
    }
    catch (Exception ex){
      log.info("AuthenticationManager get userName fron token error", ex);
      userName = null;
    }
    if(userName != null && tokenProvider.validateToken(authToken)) {
      Authentication authenticationTemp = tokenProvider.getAuthentication(authToken);
      if(authenticationTemp != null) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null,
            authenticationTemp.getAuthorities());
        return Mono.just(auth);
      }
      return Mono.empty();
    }else{
      return Mono.empty();
    }
  }
}
