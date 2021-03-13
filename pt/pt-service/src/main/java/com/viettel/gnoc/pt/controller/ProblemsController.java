package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.pt.business.ProblemsBusiness;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "Problems")
@Slf4j
public class ProblemsController {

  @Autowired
  ProblemsBusiness problemsBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @PostMapping("/getListProblemDTO")
  public ResponseEntity<List<ProblemsInsideDTO>> getListProblemDTO(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> data = problemsBusiness.getListProblemDTO(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemDTOForTT")
  public ResponseEntity<List<ProblemsInsideDTO>> getListProblemDTOForTT(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> data = problemsBusiness.getListProblemDTOForTT(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataProblemsSearch")
  public ResponseEntity<File> exportDataProblemsSearch(
      @RequestBody ProblemsInsideDTO problemsInsideDTO)
      throws Exception {
    File data = problemsBusiness.getListProblemsSearchExport(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemsDTO")
  public ResponseEntity<List<ProblemsInsideDTO>> getListProblemsDTO(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> data = problemsBusiness.getListProblemsDTO(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemsSearch")
  public ResponseEntity<Datatable> getListProblemsSearch(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable data = problemsBusiness.getListProblemsSearch(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemSearchCount")
  public ResponseEntity<Integer> getListProblemSearchCount(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    int data = problemsBusiness.getListProblemSearchCount(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long problemId) {
    ResultInSideDto result = problemsBusiness.delete(problemId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListProblems")
  public ResponseEntity<ResultInSideDto> deleteListProblems(
      @RequestBody List<ProblemsInsideDTO> problemsListDTO) {
    ResultInSideDto result = problemsBusiness.deleteListProblems(problemsListDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/findProblemsById")
  public ResponseEntity<ProblemsInsideDTO> findProblemsById(Long id) {
    ProblemsInsideDTO data = problemsBusiness.findProblemsById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getProblemsChartData")
  public ResponseEntity<List<ProblemsChartDTO>> getProblemsChartData(String receiveUnitId) {
    List<ProblemsChartDTO> data = problemsBusiness.getProblemsChartData(receiveUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getProblemsMonitor")
  public ResponseEntity<List<ProblemMonitorDTO>> getProblemsMonitor(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemMonitorDTO> data = problemsBusiness.getProblemsMonitor(Long.toString(
        problemsInsideDTO.getPriorityId()),
        problemsInsideDTO.getUnitId(), problemsInsideDTO.getFromDate(),
        problemsInsideDTO.getToDate(),
        problemsInsideDTO.getFindInSubUnit(),
        problemsInsideDTO.getUnitType());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getProblemsMobileUnitAll")
  public ResponseEntity<List<ProblemsMobileDTO>> getProblemsMobileUnitAll(String receiveUnitId) {
    List<ProblemsMobileDTO> data = problemsBusiness.getProblemsMobileUnitAll(receiveUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getProblemsMobileUnit")
  public ResponseEntity<List<ProblemsMobileDTO>> getProblemsMobileUnit(String receiveUnitId) {
    List<ProblemsMobileDTO> data = problemsBusiness.getProblemsMobileUnit(receiveUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getProblemsMobileUnitProxy/receiveUnitId{receiveUnitId}")
  public ResponseEntity<List<ProblemsMobileDTO>> getProblemsMobileUnitProxy(
      @PathVariable("receiveUnitId") String receiveUnitId) {
    List<ProblemsMobileDTO> data = problemsBusiness.getProblemsMobileUnit(receiveUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getProblemsMobileUnitAllProxy/receiveUnitId{receiveUnitId}")
  public ResponseEntity<List<ProblemsMobileDTO>> getProblemsMobileUnitAllProxy(
      @PathVariable("receiveUnitId") String receiveUnitId) {
    List<ProblemsMobileDTO> data = problemsBusiness.getProblemsMobileUnitAll(receiveUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemSearchDulidate")
  public ResponseEntity<Datatable> getListProblemSearchDulidate(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable data = problemsBusiness.getListProblemSearchDulidate(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemsByCondition")
  public ResponseEntity<List<ProblemsInsideDTO>> getListProblemsByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemsInsideDTO> data = problemsBusiness.getListProblemsByCondition(lstCondition,
        rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertProblems")
  public ResponseEntity<ResultInSideDto> insertProblems(@RequestPart List<MultipartFile> files,
      @RequestPart("formDataJson") ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto data = problemsBusiness.insertProblems(files, problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblems")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListProblems(
      @RequestBody List<ProblemsInsideDTO> problemsInsideDTOList) {
    ResultInSideDto data = problemsBusiness.insertOrUpdateListProblems(problemsInsideDTOList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateListProblems")
  public ResponseEntity<ResultInSideDto> updateListProblems(
      @RequestBody List<ProblemsInsideDTO> lstProblemsInsideDTOS,
      ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto data = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateProblems")
  public ResponseEntity<ResultInSideDto> updateProblems(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) throws Exception {
    ResultInSideDto data = problemsBusiness.updateProblems(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateProblemsNew")
  public ResponseEntity<ResultInSideDto> updateProblemsNew(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto data = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTransitionStatus")
  public ResponseEntity<Datatable> getTransitionStatus(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable data = problemsBusiness.getTransitionStatus(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListPtRelated")
  public ResponseEntity<List<ProblemsInsideDTO>> getListPtRelated(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> data = problemsBusiness.getListPtRelated(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListChatUsers")
  public ResponseEntity<List<UsersInsideDto>> getListChatUsers(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    List<UsersInsideDto> result = problemsBusiness.getListChatUsers(problemsInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/sendChatListUsers")
  public ResponseEntity<ResultInSideDto> sendChatListUsers(
      @RequestBody ProblemsInsideDTO problemsInsideDTO)
      throws Exception {
    return new ResponseEntity<>(problemsBusiness.sendChatListUsers(problemsInsideDTO),
        HttpStatus.OK);
  }

  @PostMapping("/loadUserSupportGroup")
  public ResponseEntity<Datatable> loadUserSupportGroup(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable datatable = problemsBusiness.loadUserSupportGroup(problemsInsideDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getListRolePmByUser")
  public ResponseEntity<List<RolesDTO>> getListRolePmByUser() {
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> rolesDTOS = problemsBusiness.getListRolePmByUser(userToken.getUserID());
    return new ResponseEntity<>(rolesDTOS, HttpStatus.OK);
  }

  @PostMapping("/searchParentPTForCR")
  public Datatable searchParentPTForCR(@RequestBody ProblemsInsideDTO dto) {
    Datatable data = problemsBusiness.searchParentPTForCR(dto);
    return data;
  }

  @PostMapping("/getListProblemFiles")
  public ResponseEntity<Datatable> getListProblemFiles(
      @RequestBody GnocFileDto gnocFileDto) {
    Datatable data = problemsBusiness.getListProblemFiles(gnocFileDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertProblemFiles")
  public ResponseEntity<ResultInSideDto> insertProblemFiles(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") ProblemsInsideDTO problemsInsideDTO) throws IOException {
    ResultInSideDto resultInSideDto = problemsBusiness.insertProblemFiles(files,
        problemsInsideDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemFiles")
  public ResponseEntity<ResultInSideDto> deleteProblemFiles(@RequestBody GnocFileDto gnocFileDto) {
    ResultInSideDto result = problemsBusiness.deleteProblemFiles(gnocFileDto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListProblemsChartUpgrade")
  public ResponseEntity<Map<String, Map<String, Long>>> getListProblemsChartUpgrade(@RequestBody ProblemsInsideDTO dto) {
    Map<String, Map<String, Long>> result = problemsBusiness.getListProblemsChartUpgrade(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/checkInfoUserPM")
  public ResponseEntity<ResultInSideDto> checkInfoUserPM() {
    ResultInSideDto resultInSideDto = problemsBusiness.checkInfoUserPM();
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getDatatableProblemsChartUpgrade")
  public ResponseEntity<Datatable> getDatatableProblemsChartUpgrade(@RequestBody ProblemsInsideDTO dto) {
    Datatable datatable = problemsBusiness.getDatatableProblemsChartUpgrade(dto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getTimeHandlingPt")
  public ResponseEntity<String> getDatatableProblemsChartUpgrade(@RequestBody ProblemConfigTimeDTO dto) {
    String countDayOff = problemsBusiness.countDayOff(dto);
    return new ResponseEntity<>(countDayOff, HttpStatus.OK);
  }

  //tab OD
  @GetMapping("/findListOdByPt")
  public ResponseEntity<List<OdSearchInsideDTO>> findListOdByPt(Long problemId) {
    List<OdSearchInsideDTO> data = problemsBusiness.findListOdByPt(problemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab SR
  @GetMapping("/findListSrByPt")
  public List<SrInsiteDTO> findListSrByPt(Long problemId) {
    List<SrInsiteDTO> data = problemsBusiness.findListSrByPt(problemId);
    return data;
  }

  // get unit id
  @GetMapping("/getUnitIdWithUser")
  public UsersEntity usersEntity() {
    UserToken userToken = ticketProvider.getUserToken();
    UsersEntity usersEntity = problemsBusiness.usersEntity(userToken.getUserID());
    return usersEntity;
  }
}
