package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.kedb.model.KedbEntity;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KedbDTO extends BaseDto {

  private Long kedbId;
  private String kedbCode;
  @NotNull(message = "validation.kedbDTO.kedbName.NotNull")
  @SizeByte(max = 1000, message = "validation.kedbDTO.kedbName.tooLong")
  private String kedbName;
  @NotNull(message = "validation.kedbDTO.description.NotNull")
  private String description;
  private String influenceScope;
  private Long createUserId;
  private String createUserName;
  private Long receiveUserId;
  private String ptTtRelated;
  @NotNull(message = "validation.kedbDTO.typeId.NotNull")
  private Long typeId;
  @NotNull(message = "validation.kedbDTO.subCategoryId.NotNull")
  private Long subCategoryId;
  @NotNull(message = "validation.kedbDTO.vendor.NotNull")
  @SizeByte(max = 1000, message = "validation.kedbDTO.vendor.tooLong")
  private String vendor;
  @NotNull(message = "validation.kedbDTO.softwareVersion.NotNull")
  @SizeByte(max = 200, message = "validation.kedbDTO.softwareVersion.tooLong")
  private String softwareVersion;
  private String ttWa;
  @NotNull(message = "validation.kedbDTO.rca.NotNull")
  private String rca;
  private String ptWa;
  @NotNull(message = "validation.kedbDTO.solution.NotNull")
  private String solution;
  private String worklog;
  private Date createdTime;
  @NotNull(message = "validation.kedbDTO.kedbState.NotNull")
  private Long kedbState;
  private String notes;
  @SizeByte(max = 1000, message = "validation.kedbDTO.receiveUserName.tooLong")
  private String receiveUserName;
  @NotNull(message = "validation.kedbDTO.hardwareVersion.NotNull")
  @SizeByte(max = 200, message = "validation.kedbDTO.hardwareVersion.tooLong")
  private String hardwareVersion;
  private String typeIdStr;
  @SizeByte(max = 250, message = "validation.kedbDTO.completer.tooLong")
  private String completer;
  private Date completedTime;
  private Long numLike;
  private Long numView;
  private String comments;
  private String usersLike;
  private Long unitCheckId;
  private String unitCheckName;
  @NotNull(message = "validation.kedbDTO.parentTypeId.NotNull")
  private Long parentTypeId;

  private String typeName;
  private String subCategoryName;
  private String vendorName;
  private String parentTypeName;
  private String kedbStateName;
  private Long kedbStateBeforeUpdate;
  private Long createUnitId;
  private String createUnitName;
  private String unitUpdateName;
  private String userUpdateName;
  private String fromDate;
  private String toDate;
  private String contentFile;
  private Double averageRating;
  private Long yourRating;
  private List<GnocFileDto> gnocFileDtos;
  private ProblemsInsideDTO problemsInsideDTO;
  private KedbRatingInsideDTO kedbRatingInsideDTO;
  private List<Long> listKedbState;
  private Double offset;
  private List<Long> idDeleteList;

  public KedbDTO(Long kedbId, String kedbCode, String kedbName, String description,
      String influenceScope, Long createUserId, String createUserName, Long receiveUserId,
      String ptTtRelated, Long typeId, Long subCategoryId, String vendor,
      String softwareVersion, String ttWa, String rca, String ptWa, String solution,
      String worklog, Date createdTime, Long kedbState, String notes,
      String receiveUserName, String hardwareVersion, String typeIdStr, String completer,
      Date completedTime, Long numLike, Long numView, String comments, String usersLike,
      Long unitCheckId, String unitCheckName, Long parentTypeId) {
    this.kedbId = kedbId;
    this.kedbCode = kedbCode;
    this.kedbName = kedbName;
    this.description = description;
    this.influenceScope = influenceScope;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.receiveUserId = receiveUserId;
    this.ptTtRelated = ptTtRelated;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.vendor = vendor;
    this.softwareVersion = softwareVersion;
    this.ttWa = ttWa;
    this.rca = rca;
    this.ptWa = ptWa;
    this.solution = solution;
    this.worklog = worklog;
    this.createdTime = createdTime;
    this.kedbState = kedbState;
    this.notes = notes;
    this.receiveUserName = receiveUserName;
    this.hardwareVersion = hardwareVersion;
    this.typeIdStr = typeIdStr;
    this.completer = completer;
    this.completedTime = completedTime;
    this.numLike = numLike;
    this.numView = numView;
    this.comments = comments;
    this.usersLike = usersLike;
    this.unitCheckId = unitCheckId;
    this.unitCheckName = unitCheckName;
    this.parentTypeId = parentTypeId;
  }

  public KedbEntity toEntity() {
    return new KedbEntity(kedbId, kedbCode, kedbName, description, influenceScope, createUserId,
        createUserName, receiveUserId, ptTtRelated, typeId, subCategoryId, vendor, softwareVersion,
        ttWa, rca, ptWa, solution, worklog, createdTime, kedbState, notes, receiveUserName,
        hardwareVersion, typeIdStr, completer, completedTime, numLike, numView, comments, usersLike,
        unitCheckId, unitCheckName, parentTypeId);
  }
}
