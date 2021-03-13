package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.ItAccountDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "IT_ACCOUNT")
public class ItAccountEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "it_account_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "INCIDENT_ID")
  private Long incidentId;

  @Column(name = "ACCOUNT")
  private String account;

  public ItAccountEntity(Long id, Long incidentId, String account) {
    this.id = id;
    this.incidentId = incidentId;
    this.account = account;
  }

  public ItAccountDTO toDTO() {
    ItAccountDTO dto = new ItAccountDTO(
        id,
        incidentId,
        account
    );
    return dto;
  }
}
