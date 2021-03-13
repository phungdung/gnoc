/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.maintenance.dto.MrDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tiennv
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrCreatedFromOtherSysDTO {

  private String ccfosmId;
  private String crId;
  private String systemId;
  private String objectId;
  private String stepId;
  private String isActive;
  private String status;
  private String objectCode;
  private MrDTO mr;
  private CrDTO crDTO;

  public static class SYSTEM {

    public static final String MR = "1";
    public static final String PT = "2";
    public static final String TT = "3";
    public static final String WO = "4";
    public static final String SR = "5";
    public static final String RDM = "6";
    public static final String RR = "7";
    private static final Map<String, String> getText = new HashMap<>() {
      {
        put(MR, "MR");
        put(PT, "PT");
        put(TT, "TT");
        put(WO, "WO");
        put(SR, "SR");
        put(RDM, "RDM");
        put(RR, "RR");

      }
    };

    public static Map<String, String> getGetText() {
      return getText;
    }
  }

}
