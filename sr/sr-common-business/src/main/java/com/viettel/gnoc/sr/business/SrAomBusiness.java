package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;

public interface SrAomBusiness {

  public ResultDTO getListSRForGatePro(String fromDate, String toDate);

  public ResultDTO updateSRForGatePro(String srCode, String status, String fileContent);
}
