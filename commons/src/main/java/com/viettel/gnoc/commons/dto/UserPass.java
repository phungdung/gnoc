/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

/**
 * @author thanhlv12
 */
public class UserPass {

  private String password;
  private String username;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "{\"password\":" + password + ",\"username\":" + username + "}";
  }

  public UserPass(String password, String username) {
    this.password = password;
    this.username = username;
  }
}
