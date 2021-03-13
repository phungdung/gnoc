package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SREvaluateDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_EVALUATE")
public class SREvaluateEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_EVALUATE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "EVALUATE_ID")
  private Long evaluateId;
  @Column(name = "EVALUATE")
  private String evaluate;
  @Column(name = "EVALUATE_REASON")
  private String evaluateReason;
  @Column(name = "SR_ID")
  private Long srId;
  @Column(name = "CREATE_DATE")
  private Date createdTime;
  @Column(name = "CREATE_USER")
  private String createdUser;

  public SREvaluateDTO toDTO() {
    SREvaluateDTO dto = new SREvaluateDTO(
        evaluateId == null ? null : evaluateId.toString(),
        evaluate,
        evaluateReason,
        srId == null ? null : srId.toString(),
        createdTime,
        createdUser
    );
    return dto;
  }
}
