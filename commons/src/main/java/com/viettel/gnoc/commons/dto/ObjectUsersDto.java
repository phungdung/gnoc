package com.viettel.gnoc.commons.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class ObjectUsersDto extends BaseDto {

  private Long userId;
  private String username;
  private String password;
  private boolean enabled;
  private Long status;
  private String roleCode;
  private Timestamp createdTime;
  private String createdUser;
  private Timestamp updatedTime;
  private String updatedUser;
  private Long signInCount;
  private Timestamp currentSignInAt;
  private String currentSignInIp;
  private Timestamp lastSignInAt;
  private String lastSignInIp;

  private Long employeeId;
  private String employeeCode;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Timestamp dateOfBirth;
  private Long unitId;
  private Long avatarId;
}
