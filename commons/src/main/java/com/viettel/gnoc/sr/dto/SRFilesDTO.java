package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SRFilesDTO extends BaseDto implements Cloneable {

  //Fields
  private Long fileId;
  private String fileType;
  private String fileContent;
  @NotEmpty(message = "{validation.srFiles.null.fileName}")
  private String fileName;
  @NotEmpty(message = "{validation.srFiles.null.filePath}")
  private String filePath;
  @NotEmpty(message = "{validation.srFiles.null.fileGroup}")
  private String fileGroup;
  private String comments;
  private Long obejctId;
  private Long templateId;
  private Long requireCreateSR;
  private String typeWS;

  private String serviceArray;
  private String serviceCode;
  private String serviceName;
  private String serviceGroup;
  private String country;
  private String fileTypeName;
  private String serviceArrayName;
  private String serviceGroupName;

  private Long indexFile;
  @XmlTransient
  private MultipartFile file;
  private String pathTemplate;
  private boolean openConnect;
  private boolean serviceNims;
  private boolean serviceAom;
  private boolean autoCreatCR;
  private boolean dvTrungKe;
  private String ipMop;

  public SRFilesDTO(Long fileId, String fileType, String fileContent, String fileName,
      String filePath, String fileGroup, String comments, Long obejctId, Long templateId,
      Long requireCreateSR, String typeWS) {
    this.fileId = fileId;
    this.fileType = fileType;
    this.fileContent = fileContent;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileGroup = fileGroup;
    this.comments = comments;
    this.obejctId = obejctId;
    this.templateId = templateId;
    this.requireCreateSR = requireCreateSR;
    this.typeWS = typeWS;
  }

  public SRFilesDTO clone() throws CloneNotSupportedException {
    return (SRFilesDTO) super.clone();
  }

  public SRFilesEntity toEntity() {
    SRFilesEntity model = new SRFilesEntity(
        fileId, fileType, fileContent, fileName, filePath, fileGroup, comments, obejctId,
        templateId, requireCreateSR, typeWS
    );
    return model;
  }
}
