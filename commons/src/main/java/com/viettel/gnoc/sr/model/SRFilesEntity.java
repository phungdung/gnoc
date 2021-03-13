
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRFilesDTO;
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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_FILES")
public class SRFilesEntity {

//  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_FILES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", nullable = false)
  private Long fileId;

  @Column(name = "FILE_TYPE")
  private String fileType;

  @Column(name = "FILE_CONTENT")
  private String fileContent;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  @Column(name = "FILE_GROUP")
  private String fileGroup;

  @Column(name = "COMMENTS")
  private String comments;

  @Column(name = "OBEJCT_ID")
  private Long obejctId;

  @Column(name = "TEMPLATE_ID")
  private Long templateId;

  @Column(name = "REQUIRE_CREATE_SR")
  private Long requireCreateSR;

  @Column(name = "TYPE_WS")
  private String typeWS;

  public SRFilesDTO toDTO() {
    SRFilesDTO dto = new SRFilesDTO(
        fileId, fileType, fileContent, fileName, filePath, fileGroup, comments, obejctId,
        templateId, requireCreateSR, typeWS);
    return dto;
  }
}

