package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.maintenance.model.MrChecklistsBtsDetailEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrChecklistsBtsDetailDTO extends BaseDto {

  private Long checklistDetailId;
  private Long checklistId;
  @NotNull(message = "validation.mrChecklistsBtsDetailDTO.content.notNull")
  @Size(max = 1000, message = "validation.mrChecklistsBtsDetailDTO.content.tooLong")
  private String content;
  private Long photoReq;
  private Long minPhoto;
  private Long maxPhoto;
  @SizeByte(max = 4000, message = "validation.mrChecklistsBtsDetailDTO.captureGuide.tooLong")
  private String captureGuide;
  private Double scoreChecklist;
  private Long isImportant;

  public MrChecklistsBtsDetailEntity toEntity() {
    return new MrChecklistsBtsDetailEntity(checklistDetailId, checklistId, content, photoReq,
        minPhoto, maxPhoto, captureGuide, scoreChecklist, isImportant);
  }

}
