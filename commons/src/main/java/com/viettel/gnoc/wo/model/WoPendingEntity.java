package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoPendingDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_PENDING")
public class WoPendingEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_PENDING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_PENDING_ID", unique = true, nullable = false)
  private Long woPendingId;

  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Column(name = "END_PENDING_TIME")
  private Date endPendingTime;

  @Column(name = "REASON_PENDING_ID")
  private Long reasonPendingId;

  @Column(name = "REASON_PENDING_NAME")
  private String reasonPendingName;

  @Column(name = "CUSTOMER")
  private String customer;

  @Column(name = "CUS_PHONE")
  private String cusPhone;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "OPEN_TIME")
  private Date openTime;

  @Column(name = "OPEN_USER")
  private String openUser;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PENDING_TYPE")
  private Long pendingType;

  public WoPendingDTO toDTO() {
    return new WoPendingDTO(woPendingId, insertTime, endPendingTime, reasonPendingId,
        reasonPendingName, customer, cusPhone, woId, openTime, openUser, description, pendingType);
  }
}
