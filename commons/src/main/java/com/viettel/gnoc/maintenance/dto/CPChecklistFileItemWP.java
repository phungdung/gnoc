/**
 * @(#)AffectedServicesForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.maintenance.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author anhmv6
 * @version 1.0
 * @since 8/14/2015 9:50 AM
 */
@Getter
@Setter
public class CPChecklistFileItemWP {

  private String fileName;
  private String updatedUser;
  private byte[] fileContent;
  private String checklistId;
  private String photoReq;
  private String valueAI;
  private String photoRate;
}
