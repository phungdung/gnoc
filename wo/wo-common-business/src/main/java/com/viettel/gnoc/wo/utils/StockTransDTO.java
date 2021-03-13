/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author thanhlv12
 */
public class StockTransDTO {

  public String teamCode;
  public String account;
  public String troubleType;
  public List<StockTransDetail> stockTransDetails;
  public List<StockTransDetail> stockTotals;
  public String transactionId;
  public String createDateTime;

  public String getTeamCode() {
    return teamCode;
  }

  public void setTeamCode(String teamCode) {
    this.teamCode = teamCode;
  }

  public List<StockTransDetail> getStockTotals() {
    return stockTotals;
  }

  public void setStockTotals(List<StockTransDetail> stockTotals) {
    this.stockTotals = stockTotals;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getTroubleType() {
    return troubleType;
  }

  public void setTroubleType(String troubleType) {
    this.troubleType = troubleType;
  }

  public List<StockTransDetail> getStockTransDetails() {
    return stockTransDetails;
  }

  public void setStockTransDetails(List<StockTransDetail> stockTransDetails) {
    this.stockTransDetails = stockTransDetails;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(Date createDateTime) {
    SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    this.createDateTime = dfm.format(createDateTime);
  }

}
