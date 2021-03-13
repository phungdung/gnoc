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
public class StockTotalDTO {

  public Long prodOfferId;
  public Long quantity;
  public List<ProdOfferSerial> prodOfferSerials;

}
