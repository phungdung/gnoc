package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftCrDTO;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "SHIFT_CR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftCrEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_CR_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "CR_NUMBER")
  private String crNumber;
  @Column(name = "CR_NAME")
  private String crName;
  @Column(name = "PURPOSE")
  private String purpose;
  @Column(name = "UNIT_NAME")
  private String unitName;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "USER_CHECK_NAME")
  private String userCheckName;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "SHIFT_HANDOVER_ID", nullable = false)
  private Long shiftHandoverId;
  @Column(name = "COUNTRY")
  private Long country;

  public ShiftCrDTO toDTO() {
    return new ShiftCrDTO(
        id,
        crNumber,
        crName,
        purpose,
        unitName,
        userName,
        userCheckName,
        result,
        note,
        shiftHandoverId,
        country
    );
  }
}
