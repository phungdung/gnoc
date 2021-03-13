/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrFileObject {

  private String idTemplate;
  private String codeTemplate;
  private String linkTemplate;
  private String nameTemplate;
  private String fileType;
  private String crId;

}
