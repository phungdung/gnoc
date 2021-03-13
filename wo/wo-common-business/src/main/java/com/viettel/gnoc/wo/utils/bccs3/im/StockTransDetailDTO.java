/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils.bccs3.im;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockTransDetailDTO {
    protected Long prodOfferId;
    protected Long quantity;
    protected String actionCode;
    protected Long amount;
    protected String prodOfferCode;
    protected String name;
    protected String code;
    protected Long type;
    protected String serial;

    
}
