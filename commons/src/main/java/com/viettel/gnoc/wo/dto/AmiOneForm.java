package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmiOneForm {

  private String description;
  private String reasonName;
  private String reasonType;
  private List<String> listFileName;
  private List<byte[]> fileArr;
  private String userExecute;
  private String completedTime;
  private String gnocCode;

}
