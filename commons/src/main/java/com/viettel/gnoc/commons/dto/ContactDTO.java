package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.ContactEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO extends BaseDto {

  //Fields
  private Long contactId;
  private String system;
  private String roles;
  private String fullName;
  private String email;
  private String mobile;
  private Long orders;
  private Long isEnable;

  public ContactEntity toEntity() {
    return new ContactEntity(contactId, system, roles, fullName, email, mobile, orders, isEnable);
  }
}
