/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjResponse {

  private long totalRow;
  private List<CrDTO> lstCrDTO;
  private String responseStatus;
  private String message;
  private String sessionId;
  private String fullName;
  private String timeZoneOffset;
  private String timeZoneName;
  private String userId;
  private String unitId;
  private String unitName;
  private String userName;

}
