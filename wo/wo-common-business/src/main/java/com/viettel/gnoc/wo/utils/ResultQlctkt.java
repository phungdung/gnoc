package com.viettel.gnoc.wo.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "ResultQlctkt")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultQlctkt", propOrder = {
    "identityCard",
    "resultCode"
})

@Getter
@Setter
public class ResultQlctkt {

  String identityCard;
  String resultCode;

  @Override
  public String toString() {
    return "resultCode = " + resultCode + " identityCard" + identityCard;
  }

}
