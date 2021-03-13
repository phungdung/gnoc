package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.dto.JsonResponseBO;
import com.viettel.gnoc.commons.dto.RequestInputBO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultTroubleDTO;
import com.viettel.gnoc.incident.dto.ItemDataTT;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "TroublesServiceForVSmart")
@XmlSeeAlso({TTChangeStatusDTO.class})
public interface TroublesServiceForVSmart {

  @WebMethod(operationName = "getDataConfig")
  public ResultTroubleDTO getDataConfig(
      @WebParam(name = "troubleId") String troubleId,
      @WebParam(name = "statusOld") String statusOld,
      @WebParam(name = "statusNew") String statusNew,
      @WebParam(name = "userLogin") String userLogin);

  @WebMethod(operationName = "updateTTFromVSMART")
  public ResultDTO updateTTFromVSMART(
      @WebParam(name = "ttChangeStatusDTO") TTChangeStatusDTO ttChangeStatusDTO);

  @WebMethod(operationName = "getListCombo")
  public List<ItemDataTT> getListCombo(
      @WebParam(name = "keyCode") String keyCode,
      @WebParam(name = "id") String id,
      @WebParam(name = "parentId") String parentId,
      @WebParam(name = "typeId") String typeId);

  @WebMethod(operationName = "getDataJSon")
  public JsonResponseBO getDataJSon(
      @WebParam(name = "requestInputBO") RequestInputBO requestInputBO);

  @WebMethod(operationName = "getDataDetailColumnJSon")
  public List<ItemDataTT> getDataDetailColumnJSon(
      @WebParam(name = "requestInputBO") RequestInputBO requestInputBO);


}
