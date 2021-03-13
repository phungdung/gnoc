package com.viettel.gnoc.commons.dto;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hungtv77
 */
@Getter
@Setter
@NoArgsConstructor
public class DataHistoryChange {

  private Object oldObject;
  private Object newObject;
  private List<String> keys;
  private String type;
  private String userId;
  private String actionType;
  private String recordId;
}

