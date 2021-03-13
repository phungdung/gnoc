package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.TemplateRelationsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TemplateRelationsDTO extends BaseDto {

  private Long trsId;
  private String trsName;
  private String hostTemplateId;
  private String clientTemplateId;
  private String relationType;
  private Long hostColId;
  private Long tempColId;

  public TemplateRelationsDTO(Long trsId, String trsName, String hostTemplateId,
      String clientTemplateId, String relationType) {
    this.trsId = trsId;
    this.trsName = trsName;
    this.hostTemplateId = hostTemplateId;
    this.clientTemplateId = clientTemplateId;
    this.relationType = relationType;
  }

  public TemplateRelationsEntity toEntity() {
    return new TemplateRelationsEntity(
        trsId,
        trsName,
        hostTemplateId,
        clientTemplateId,
        relationType
    );
  }
}
