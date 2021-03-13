/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils.bccs3.im;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author thanhlv12
 */
@Getter
@Setter
@NoArgsConstructor
public class RestoreAPDeploymentDTO {

  public String teamCode;
  public String staffIdNo;
  public String account;
  public String troubleType;
  public String transactionId;
  public Boolean isRevoke;
  public List<StockTotalDTO> stockTotals;
  public List<StockTotalDTO> oldStockTotals;
  public List<GoodsProductDTO> lstOldGoodsProduct;
  public List<GoodsProductDTO> lstNewGoodsProduct;

}
