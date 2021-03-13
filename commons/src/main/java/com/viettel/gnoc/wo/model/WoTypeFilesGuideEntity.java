package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTypeFilesGuideDTO;
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
@Table(schema = "WFM", name = "WO_TYPE_FILES_GUIDE")
public class WoTypeFilesGuideEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TYPE_FILES_GUIDE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_TYPE_FILES_GUIDE_ID", unique = true, nullable = false)
  private Long woTypeFilesGuideId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  public WoTypeFilesGuideDTO toDTO() {
    return new WoTypeFilesGuideDTO(woTypeFilesGuideId, woTypeId, fileName, filePath);
  }

}
