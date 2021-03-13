/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.cr.dto.CrDTO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrService")
public interface CrService {

  @WebMethod(operationName = "actionScheduleCr")
  public String actionScheduleCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionResolveCr")
  public String actionResolveCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionCab")
  public String actionCab(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionApproveCR")
  public String actionApproveCR(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionUpdateNotify")
  public String actionUpdateNotify(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "actionCode") Long actionCode);

  @WebMethod(operationName = "updateCrWithNoti")
  public ResultDTO updateCrWithNoti(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionEditCr")
  public String actionEditCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionReceiveCr")
  public String actionReceiveCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionVerify")
  public String actionVerify(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionAssignCab")
  public String actionAssignCab(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionAppraiseCr")
  public String actionAppraiseCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionCancelCr")
  public String actionCancelCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionCloseCr")
  public String actionCloseCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "findCrById")
  public CrDTO findCrById(@WebParam(name = "crDTOId") Long id,
      @WebParam(name = "UserTokenGNOCSimple") UserTokenGNOCSimple userTokenGNOC);

  @WebMethod(operationName = "insertCr")
  public ResultDTO insertCr(@WebParam(name = "crDTO") CrDTO crDTO);
}
