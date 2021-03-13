package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.viettel.gnoc.commons.dto.ContactDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CONTACT")
public class ContactEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.CONTACT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONTACT_ID", unique = true)
  private Long contactId;

  @Column(name = "SYSTEM")
  private String system;

  @Column(name = "ROLES")
  private String roles;

  @Column(name = "FULLNAME")
  private String fullName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "MOBILE")
  private String mobile;

  @Column(name = "ORDERS")
  private Long orders;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  public ContactDTO toDTO() {
    return new ContactDTO(contactId, system, roles, fullName, email, mobile, orders, isEnable);
  }
}

