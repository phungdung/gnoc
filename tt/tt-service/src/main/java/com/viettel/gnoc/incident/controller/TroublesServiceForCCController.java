package com.viettel.gnoc.incident.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.business.TroublesServiceForCCBusiness;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "TroublesServiceForCC")
@Slf4j
public class TroublesServiceForCCController {

  @Autowired
  TroublesServiceForCCBusiness troublesServiceForCCBusiness;

  @PostMapping("/onRollBackTroubleForCC")
  public ResponseEntity<List<ResultDTO>> onRollBackTroubleForCC(
      @RequestBody List<CommonDTO> lstComplaint) {
    List<ResultDTO> list = troublesServiceForCCBusiness.onRollBackTroubleForCC(lstComplaint);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/onSearchCountForCC")
  public ResponseEntity<Integer> onSearchCountForCC(@RequestBody TroublesDTO troublesDTO) {
    int result = troublesServiceForCCBusiness.onSearchCountForCC(troublesDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getTroubleInfoForCC")
  public ResponseEntity<ResultDTO> getTroubleInfoForCC(@RequestBody TroublesDTO troublesDTO) {
    ResultDTO resultInSideDto = troublesServiceForCCBusiness.getTroubleInfoForCC(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/onInsertTroubleForCC")
  public ResponseEntity<ResultDTO> onInsertTroubleForCC(@RequestBody TroublesDTO troublesDTO) {
    ResultDTO resultInSideDto = troublesServiceForCCBusiness.onInsertTroubleForCC(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/findTroubleById")
  public ResponseEntity<TroublesInSideDTO> findTroubleById(Long id) throws Exception {
    TroublesInSideDTO data = troublesServiceForCCBusiness.findTroublesById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getSequenseTroubles")
  public ResponseEntity<List<String>> getSequenseTroubles(String seqName, int... size) {
    List<String> result = troublesServiceForCCBusiness.getSequenseTroubles(seqName, size);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/reassignTicketForCC")
  public ResponseEntity<ResultDTO> reassignTicketForCC(@RequestBody TroublesDTO troublesDTO) {
    ResultDTO result = troublesServiceForCCBusiness.reassignTicketForCC(troublesDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListTroubleActionLog")
  public ResponseEntity<List<TroubleActionLogsDTO>> getListTroubleActionLog(String troubleCode) {
    List<TroubleActionLogsDTO> list = troublesServiceForCCBusiness
        .getListTroubleActionLog(troubleCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListWorkLog")
  public ResponseEntity<List<TroubleWorklogInsiteDTO>> getListWorkLog(String troubleCode) {
    List<TroubleWorklogInsiteDTO> list = troublesServiceForCCBusiness.getListWorkLog(troubleCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getConcaveByTicket")
  public ResponseEntity<String> getConcaveByTicket(String troubleCode) throws Exception {
    String data = troublesServiceForCCBusiness.getConcaveByTicket(troubleCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/sendTicketToTKTU")
  public ResponseEntity<ResultDTO> sendTicketToTKTU(@RequestBody TroublesInSideDTO tForm)
      throws Exception {
    ResultDTO result = troublesServiceForCCBusiness.sendTicketToTKTU(tForm);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getConcaveByCellAndLocation")
  public ResponseEntity<List<ConcaveDTO>> getConcaveByCellAndLocation(String strLstCell,
      String lng, String lat) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    List<String> lstCell = null;
    if (StringUtils.isNotNullOrEmpty(strLstCell)) {
      lstCell = mapper.readValue(strLstCell, ArrayList.class);
    }
    List<ConcaveDTO> list = troublesServiceForCCBusiness
        .getConcaveByCellAndLocation(lstCell, lng, lat);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListUnitByTrouble")
  public ResponseEntity<List<UnitDTO>> getListUnitByTrouble(String troubleCode) {
    List<UnitDTO> list = troublesServiceForCCBusiness.getListUnitByTrouble(troubleCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/onUpdateTroubleFromTKTU")
  public ResponseEntity<ResultDTO> onUpdateTroubleFromTKTU(
      @RequestBody TroublesInSideDTO troublesDTO)
      throws Exception {
    ResultDTO result = troublesServiceForCCBusiness.onUpdateTroubleFromTKTU(troublesDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onUpdateTroubleFromWo")
  public ResultDTO onUpdateTroubleFromWo(@RequestBody TroublesDTO troublesDTO) throws Exception {
    return troublesServiceForCCBusiness.onUpdateTroubleFromWo(troublesDTO);
  }

  @PostMapping("/getListWoLog")
  public ResponseEntity<List<WoHistoryDTO>> getListWoLog(String troubleCode) throws ParseException {
    List<WoHistoryDTO> list = troublesServiceForCCBusiness.getListWoLog(troubleCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }
}
