package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.pt.model.ProblemFilesEntity;
import java.util.Date;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemFilesDTO extends BaseDto {

  private Long problemFileId;
  @Size(max = 255, message = "validation.problemFilesDTO.problemFileName.tooLong")
  private String problemFileName;
  private Long createUnitId;
  private String createUnitName;
  private Long createUserId;
  private String createUserName;
  private Date createTime;
  private Long problemId;

  public ProblemFilesEntity toEntity() {
    return new ProblemFilesEntity(problemFileId, problemFileName, createUnitId, createUnitName,
        createUserId, createUserName, createTime, problemId);
  }
}
