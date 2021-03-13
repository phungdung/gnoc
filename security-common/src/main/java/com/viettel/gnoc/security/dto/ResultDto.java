package com.viettel.gnoc.security.dto;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.sql.Timestamp;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class ResultDto {
  Long id;
  String key;
  String message;
  Timestamp systemDate;
  Object object;
  File file;
  Long timeout;
  String authToken;
  String smartOfficeToken;
  String smartOfficeLink;
  String languageKey;
  UserToken userToken;
  Map<String, String> subAdminViews;

  public ResultDto(Long id, String key, String message) {
    this.id = id;
    this.key = key;
    this.message = message;
  }
}
