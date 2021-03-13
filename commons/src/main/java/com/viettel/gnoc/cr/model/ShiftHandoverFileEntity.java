package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftHandoverFileDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_HANDOVER_FILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftHandoverFileEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_HANDOVER_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;
  @Column(name = "FILE_ID")
  private Long fileId;
  @Column(name = "SHIFT_HANDOVER_ID")
  private Long shiftHandoverId;

  public ShiftHandoverFileDTO toDTO() {
    return new ShiftHandoverFileDTO(
        id,
        fileId,
        shiftHandoverId
    );
  }
}
