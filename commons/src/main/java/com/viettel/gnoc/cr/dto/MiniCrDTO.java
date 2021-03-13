package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MiniCrDTO {

  private Long crId;
  private String crNumber;
  private String title;
  private Date createdDate;
  private Date earliestStartTime;
  private Date lastestStartTime;
  private String activeUser;
  private String description;
}
