package com.viettel.gnoc.wo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoanCongInput {

  private String user;
  private String pass;
  private String woId;

  @Override
  public String toString() {
    return "{" + "user:" + user + ", pass:" + pass + ", woId:" + woId + '}';
  }
}
