package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.WoFileTempDto;
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
@Table(schema = "COMMON_GNOC", name = "WO_FILE_TEMP")
public class WoFileTempEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "wo_file_temp_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_FILE_TEMP_ID", unique = true)
  private Long woFileTempId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "LABEL")
  private String label;

  @Column(name = "NUMBER_FILE")
  private Long numberFile;

  public WoFileTempDto toDTO() {
    return new WoFileTempDto(woFileTempId, woTypeId, label, numberFile);
  }
}
