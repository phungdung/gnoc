package com.viettel.gnoc.hazelcast.controller;

import com.google.gson.Gson;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.viettel.gnoc.hazelcast.dto.HazelcastDto;
import com.viettel.gnoc.hazelcast.dto.HazelcastResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import viettel.passport.client.UserToken;

@Slf4j
@RestController
@RequestMapping("/hazelcast")
public class CommandController {

  @Autowired
  HazelcastInstance hazelcastInstance;

  @Value("${application.hazelcast.mapName}")
  private String mapName;

  @PostMapping("/putData")
  public HazelcastResponseDto put(@RequestBody HazelcastDto hazelcastDto) {
    IMap<String, String> map = hazelcastInstance.getMap(mapName);
    String value = map.put(hazelcastDto.getKey(), hazelcastDto.getValue(), hazelcastDto.getExpire(),
        hazelcastDto.getTimeUnit());
    try {
      if (value == null && hazelcastDto.getValue() != null && hazelcastDto.getValue().toLowerCase()
          .contains("username")) {
        UserToken vsaUserToken = new Gson().fromJson(hazelcastDto.getValue(), UserToken.class);
        if (vsaUserToken != null) {
          log.info("--- Member " + String.valueOf(vsaUserToken.getUserName()).toUpperCase()
              + ": has login Success !");
          log.info(
              "--- Total member: " + (map.values().size() == 0 ? 1 : (map.values().size() - 1)));
          vsaUserToken = null;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new HazelcastResponseDto(value);
  }

  @PostMapping("/getData")
  public HazelcastResponseDto get(@RequestBody HazelcastDto hazelcastDto) {
    IMap<String, String> map = hazelcastInstance.getMap(mapName);
    String value = map.get(hazelcastDto.getKey());
    if (value == null || "".equals(value)) {
      log.info(
          String.format("GET DATA KEY {%s}: %s", hazelcastDto.getKey(), hazelcastDto.getValue()));
    }
    if (map != null) {
      map = null;
    }
    return new HazelcastResponseDto(value);
  }

  @PostMapping("/removeData")
  public void remove(@RequestBody HazelcastDto hazelcastDto) {
    IMap<String, String> map = hazelcastInstance.getMap(mapName);
    try {
      String value = map.get(hazelcastDto.getKey());
      UserToken vsaUserToken = new Gson().fromJson(value, UserToken.class);
      if (vsaUserToken != null) {
        log.info("--- Member " + String.valueOf(vsaUserToken.getUserName()).toUpperCase()
            + " has logout Success !");
        log.info("--- Total member: " + (map.values().size() - 2));
        vsaUserToken = null;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    map.delete(hazelcastDto.getKey());
    if (map != null) {
      map = null;
    }
  }
}
