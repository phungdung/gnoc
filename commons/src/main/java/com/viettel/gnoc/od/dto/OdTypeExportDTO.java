/**
 * @(#)OdTypeForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author namtn
 */
@Getter
@Setter
public class OdTypeExportDTO {

  //Fields
  private Long odTypeId;
  private Long idOdDetail;
  private String odTypeCode;
  private String odTypeName;
  private String status;
  private String statusName;
  private String odGroupTypeId;
  private String odGroupTypeName;
  private String processTime;
  private String processTimeNoti;
  private String priorityName;
  private String priorityId;
  private String resultImport;
  private String action;
  private String validate;
}
