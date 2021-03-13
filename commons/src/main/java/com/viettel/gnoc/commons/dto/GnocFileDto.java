package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.GnocFileEntity;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GnocFileDto extends BaseDto {

  private Long id;
  private String businessCode;
  private Long businessId;
  private String path;
  private String fileName;
  private String fileType;
  private String content;
  private Long required;
  private String comments;
  private Long templateId;
  private String typeWs;
  private Long createUnitId;
  private String createUnitName;
  private Long createUserId;
  private String createUserName;
  private Date createTime;
  private Long mappingId;

  private Long indexFile;
  private String fileTypeName;
  private boolean openConnect;
  @XmlTransient
  private MultipartFile multipartFile;
  private String pathTemplate;
  private byte[] bytes;
  private String language;

  public GnocFileDto(Long id, String businessCode, Long businessId, String path,
      String fileName, String fileType, String content, Long required, String comments,
      Long templateId, String typeWs, Long createUnitId, String createUnitName, Long createUserId,
      String createUserName, Date createTime, Long mappingId) {
    this.id = id;
    this.businessCode = businessCode;
    this.businessId = businessId;
    this.path = path;
    this.fileName = fileName;
    this.fileType = fileType;
    this.content = content;
    this.required = required;
    this.comments = comments;
    this.templateId = templateId;
    this.typeWs = typeWs;
    this.createUnitId = createUnitId;
    this.createUnitName = createUnitName;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.createTime = createTime;
    this.mappingId = mappingId;
  }

  public GnocFileEntity toEntity() {
    return new GnocFileEntity(id, businessCode, businessId, path, fileName, fileType, content,
        required, comments, templateId, typeWs, createUnitId, createUnitName, createUserId,
        createUserName, createTime, mappingId);
  }
}
