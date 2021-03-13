package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleFileDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_FILE")
public class TroubleFileEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "FILE_ID", nullable = false)
  private Long fileId;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  public TroubleFileEntity(Long id, Long fileId, Long troubleId) {
    this.id = id;
    this.fileId = fileId;
    this.troubleId = troubleId;
  }

  public TroubleFileDTO toDTO() {
    TroubleFileDTO dto = new TroubleFileDTO(
        id,
        fileId,
        troubleId
    );
    return dto;
  }
}
