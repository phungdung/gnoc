package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCrRecreatedDTO;
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

/**
 * @author TrungDuong
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CR_RECREATED")
public class MrCrRecreatedEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CR_RECREATED_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RECREATED_ID")
  private Long recreatedId;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "CR_ID")
  private Long crId;

  public MrCrRecreatedDTO toDTO() {
    MrCrRecreatedDTO dto = new MrCrRecreatedDTO(
        recreatedId == null ? null : recreatedId.toString(),
        mrId == null ? null : mrId.toString(),
        crId == null ? null : crId.toString()
    );
    return dto;
  }
}
