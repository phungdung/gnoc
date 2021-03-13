/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import java.util.List;

/**
 * @author thanhlong
 */
public interface CrDtBusiness {

  public ResultDTO insertVMSADT(String userService, String passService, String systemCode,
      Long crId, Long validateKey,
      int createMopSuccess, String createMopDetail, List<VMSAMopDetailDTO> mopDTOList,
      String nationCode, String locale);

  public List<ItemDataCR> getAllActiveAffectedService(String userService, String passService,
      String locale);

}
