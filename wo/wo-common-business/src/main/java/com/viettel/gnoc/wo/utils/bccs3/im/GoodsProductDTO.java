/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils.bccs3.im;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author thanhlv12
 */
@Getter
@Setter
@NoArgsConstructor
public class GoodsProductDTO {

  public Long goodsProductId;
  public Long productOfferingId;
  public Long productOfferTypeId;
  public Long quantity;
  public String name;
  public String code;
  public String serial;
  public String serialRetrieve;
}
