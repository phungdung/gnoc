/**
 * @(#)CatReasonForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CatReasonInSideDTOSearch extends CatReasonInSideDTO {

  //Fields
  private String parentName;

  //Constructor
  public CatReasonInSideDTOSearch() {
    super();
  }

}
