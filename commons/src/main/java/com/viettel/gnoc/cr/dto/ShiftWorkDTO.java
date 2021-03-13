package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftWorkEntity;
import java.util.Date;
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
public class ShiftWorkDTO extends BaseDto {

  private Long id;
  private String workName;
  private String description;
  private String process;
  private String reasonExist;
  private String contact;
  private String opinion;
  private Long country;
  private Long shiftHandoverId;
  private Date startTime;
  private String startTimeError;
  private Date deadLine;
  private String deadLineError;
  private String owner;
  private String handle;
  private String importantLevel;
  private String result;
  private String nextWork;
  private String workStatus;
  private Boolean isDeleteShiftWork;
  private String resultImport;

  public ShiftWorkEntity toEntity() {
    return new ShiftWorkEntity(
        id, workName, description, process, reasonExist, contact, opinion, country, shiftHandoverId,
        startTime, deadLine, owner, handle, importantLevel, result, nextWork, workStatus
    );
  }

  public ShiftWorkDTO(Long id, String workName, String description, String process,
      String reasonExist, String contact, String opinion, Long country,
      Long shiftHandoverId, Date startTime, Date deadLine, String owner, String handle,
      String importantLevel, String result, String nextWork, String workStatus) {
    this.id = id;
    this.workName = workName;
    this.description = description;
    this.process = process;
    this.reasonExist = reasonExist;
    this.contact = contact;
    this.opinion = opinion;
    this.country = country;
    this.shiftHandoverId = shiftHandoverId;
    this.startTime = startTime;
    this.deadLine = deadLine;
    this.owner = owner;
    this.handle = handle;
    this.importantLevel = importantLevel;
    this.result = result;
    this.nextWork = nextWork;
    this.workStatus = workStatus;
  }
}
