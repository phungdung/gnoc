package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_WORK_OTHER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftWorkOtherEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_WORK_OTHER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "WORK_NAME")
  private String workName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "PURPOSE")
  private String purpose;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "SOLUTION")
  private String solution;
  @Column(name = "TIME")
  private String time;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "SHIFT_HANDOVER_ID", nullable = false)
  private Long shiftHandoverId;
  @Column(name = "COUNTRY")
  private Long country;

  public ShiftWorkOtherDTO toDTO() {
    return new ShiftWorkOtherDTO(
        id,
        workName,
        description,
        purpose,
        reason,
        solution,
        time,
        result,
        note,
        shiftHandoverId,
        country
    );
  }

}
