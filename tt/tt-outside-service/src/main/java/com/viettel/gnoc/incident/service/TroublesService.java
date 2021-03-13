package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "TroublesService")
public interface TroublesService {

  @WebMethod(operationName = "insertTroublesNOC")
  public ResultDTO insertTroublesNOC(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "listAccount") List<String> listAccount
  );

  @WebMethod(operationName = "updateTroublesNOC")
  public ResultDTO updateTroublesNOC(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "listTrouble") List<TroublesDTO> listTrouble);

  @WebMethod(operationName = "onSynchTrouble")
  public ResultDTO onSynchTrouble(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate,
      @WebParam(name = "insertSource") String insertSource,
      @WebParam(name = "subCategoryCode") String subCategoryCode,
      @WebParam(name = "tableCurrent") String tableCurrent
  );

  @WebMethod(operationName = "onSearchForSPM")
  public List<TroublesDTO> onSearchForSPM(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "account") String account, @WebParam(name = "spmCode") String spmCode,
      @WebParam(name = "typeSearch") Long typeSearch);

  @WebMethod(operationName = "onUpdateTroubleSPM")
  public ResultDTO onUpdateTroubleSPM(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO
  );

  @WebMethod(operationName = "onRollBackTroubleSPM")
  public List<ResultDTO> onRollBackTroubleSPM(
      @WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "lstSpmCode") List<String> spmCode
  );

  @WebMethod(operationName = "onInsertTroubleMobile")
  public ResultDTO onInsertTroubleMobile(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "listAccount") List<String> listAccount,
      @WebParam(name = "arrFileName") String[] arrFileName,
      @WebParam(name = "arrFileData") byte[][] arrFileData);

  @WebMethod(operationName = "onInsertTroubleFileWS")
  public ResultDTO onInsertTroubleFileWS(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "listAccount") List<String> listAccount,
      @WebParam(name = "arrFileName") String[] arrFileName,
      @WebParam(name = "arrFileData") byte[][] arrFileData);

  @WebMethod(operationName = "onSearchCountForVsmart")
  public int onSearchCountForVsmart(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "onSearchForVsmart")
  public List<TroublesDTO> onSearchForVsmart(
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "startRow") Integer startRow,
      @WebParam(name = "pageLength") Integer pageLength);

  @WebMethod
  public List<TroublesDTO> countTroubleByStation(
      @WebParam(name = "stationCode") String stationCode,
      @WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime,
      @WebParam(name = "type") int type
  );

  @WebMethod
  public ResultDTO onUpdateTroubleMobile(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "updateReasonTroubleFromNOC")
  public ResultDTO updateReasonTroubleFromNOC(
      @WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO
  );

  @WebMethod
  public List<String> getListActionInfo(
      @WebParam(name = "actionInfoDTO") ActionInfoDTO actionInfoDTO);

  @WebMethod
  public ResultDTO getTroubleInfoForVSMART(
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod
  public ResultDTO updateTroubleFromVSMART(
      @WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "insertTroubleFromOtherSystem")
  public ResultDTO insertTroubleFromOtherSystem(
      @WebParam(name = "troublesDTO") TroublesInSideDTO troublesDTO,
      @WebParam(name = "listAccount") List<String> listAccount) throws Exception;

  @WebMethod
  public List<TroublesDTO> countTroubleByCable(
      @WebParam(name = "lineCutCode") String lineCutCode,
      @WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime,
      @WebParam(name = "type") int type
  );
}
