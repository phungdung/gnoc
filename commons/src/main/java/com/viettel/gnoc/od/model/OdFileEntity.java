package com.viettel.gnoc.od.model;

import com.viettel.gnoc.od.dto.OdFileDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(schema = "WFM", name = "OD_FILE")
public class OdFileEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "OD_FILE_ID", unique = true)
  private Long odFileId;

  @Column(name = "OD_ID")
  private Long odId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "PATH")
  private String path;

  public OdFileDTO toDTO() {
    return new OdFileDTO(odFileId, odId, fileName, path);
  }
}
