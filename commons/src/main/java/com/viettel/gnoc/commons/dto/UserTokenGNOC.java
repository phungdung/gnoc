/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.gnoc.commons.dto;

import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import viettel.passport.client.UserToken;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenGNOC {

  private UserToken vsaUserToken;
  private Long userId;
  private String userName = null;
  private String fullName = null;
  private Long unitId;
  private List roleIDs;
  private Long groupLevel;
  private Long roleLevel;
  private boolean belongToManyGroup = false;
  private String unitCode;
  private String unitName;
  private String mobile;
  private Locale locale;
  private Long userTimeZone;
  private int offset;
}
