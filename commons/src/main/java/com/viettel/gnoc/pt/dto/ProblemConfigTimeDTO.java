package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.pt.model.ProblemConfigTimeEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.problemConfigTime.null.unique}", clazz = ProblemConfigTimeEntity.class, uniqueFields = "reasonGroupId,solutionTypeId", idField = "id")
public class ProblemConfigTimeDTO extends BaseDto {
  private Long id;
  private Long reasonGroupId;
  private Long solutionTypeId;
  private Long timeProcess;
  private Date createDate;
  private Long typeId;
  private Long subCategoryId;
  private String reasonGroupName;
  private String solutionTypeName;
  private String typeIdStr;
  private String subCategoryIdStr;
  private String resultImport;

  public ProblemConfigTimeDTO(Long id, Long reasonGroupId, Long solutionTypeId
      , Long timeProcess, Date createDate, Long typeId, Long subCategoryId) {
    this.id = id;
    this.reasonGroupId = reasonGroupId;
    this.solutionTypeId = solutionTypeId;
    this.timeProcess = timeProcess;
    this.createDate = createDate;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
  }

  public ProblemConfigTimeEntity toEntity() {
    ProblemConfigTimeEntity model = new ProblemConfigTimeEntity(
        id,
        reasonGroupId,
        solutionTypeId,
        timeProcess,
        createDate, typeId, subCategoryId);
    return model;
  }
}
