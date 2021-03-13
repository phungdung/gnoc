package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.GnocFileDto;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "GNOC_FILE")
public class GnocFileEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.GNOC_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "BUSINESS_CODE")
  private String businessCode;

  @Column(name = "BUSINESS_ID")
  private Long businessId;

  @Column(name = "PATH")
  private String path;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_TYPE")
  private String fileType;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "REQUIRED")
  private Long required;

  @Column(name = "COMMENTS")
  private String comments;

  @Column(name = "TEMPLATE_ID")
  private Long templateId;

  @Column(name = "TYPE_WS")
  private String typeWs;

  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;

  @Column(name = "CREATE_UNIT_NAME")
  private String createUnitName;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Column(name = "MAPPING_ID")
  private Long mappingId;

  public GnocFileDto toDTO() {
    return new GnocFileDto(id, businessCode, businessId, path, fileName, fileType, content,
        required, comments, templateId, typeWs, createUnitId, createUnitName, createUserId,
        createUserName, createTime, mappingId);
  }
}
