/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.dto;

/**
 * @author ITSOL
 */
public class FileForm {

  private String fileName;
  private byte[] fileArr;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public byte[] getFileArr() {
    return fileArr;
  }

  public void setFileArr(byte[] fileArr) {
    this.fileArr = fileArr;
  }
}
