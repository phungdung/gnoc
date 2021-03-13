package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.CfgFileCreateWoEntity;
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
public class CfgFileCreateWoDTO extends BaseDto {

  //Fields
  private Long cfgFileCreateWoId;
  private Long woTypeId;
  private String fileName;
  private Long required;
  private String filePath;

  private Long indexFile;
  private MultipartFile file;

  public CfgFileCreateWoDTO(Long cfgFileCreateWoId, Long woTypeId, String fileName,
      Long required, String filePath) {
    this.cfgFileCreateWoId = cfgFileCreateWoId;
    this.woTypeId = woTypeId;
    this.fileName = fileName;
    this.required = required;
    this.filePath = filePath;
  }

  public CfgFileCreateWoEntity toEntity() {
    CfgFileCreateWoEntity model = new CfgFileCreateWoEntity(
        cfgFileCreateWoId
        , woTypeId
        , fileName
        , required
        , filePath
    );
    return model;
  }
}
