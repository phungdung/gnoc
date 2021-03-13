package com.viettel.gnoc.ws.provider;

import com.google.common.base.Strings;

/**
 * Created by vtsoft on 4/3/2015.
 */
public class WsEndpoint {

  private String name;
  private String address;
  private String targetNameSpace;
  private String userName;
  private String password;
  private int timeout = 0;

  public WsEndpoint() {

  }

  public WsEndpoint(String name, String address, String targetNameSpace) {
    setAddress(address);
    setTargetNameSpace(targetNameSpace);
    this.name = name;
    this.address = address;
    this.targetNameSpace = targetNameSpace;
  }

  public WsEndpoint(String name, String address, String targetNameSpace, String userName,
      String password) {
    setAddress(address);
    setTargetNameSpace(targetNameSpace);
    this.name = name;
    this.userName = userName;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public String getAddressConnectBean() {
    if (!Strings.isNullOrEmpty(address)) {
      if (!address.endsWith("/")) {
        return (address + "/");
      }
    }
    return address;
  }

  public void setAddress(String address) {
        /*
        if (!Strings.isNullOrEmpty(address)) {
            if (!address.endsWith("/")) address += "/";
        }*/

    this.address = address;
  }

  public String getTargetNameSpace() {
    return targetNameSpace;
  }

  public void setTargetNameSpace(String targetNameSpace) {
//        if (!Strings.isNullOrEmpty(targetNameSpace)) {
//            if (!targetNameSpace.endsWith("/")) targetNameSpace += "/";
//        }
    this.targetNameSpace = targetNameSpace;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public String toString() {
    return "WsEndpoint{" +
        "name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", targetNameSpace='" + targetNameSpace + '\'' +
        ", timeout='" + timeout + '\'' +
        '}';
  }
}
