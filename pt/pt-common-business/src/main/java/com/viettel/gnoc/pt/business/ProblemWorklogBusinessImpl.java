package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.repository.ProblemWorklogRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class ProblemWorklogBusinessImpl implements ProblemWorklogBusiness {

  @Autowired
  ProblemWorklogRepository problemWorklogRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitBusiness unitBusiness;

  @Override
  public ResultInSideDto onInsert(ProblemWorklogDTO problemWorklogDTO) throws Exception {
    log.debug("Request to add : {}", problemWorklogDTO);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitBusiness.findUnitById(userToken.getDeptId());
    problemWorklogDTO.setCreateUserId(userToken.getUserID());
    problemWorklogDTO.setCreateUnitId(userToken.getDeptId());
    problemWorklogDTO.setCreateUserName(userToken.getUserName());
    problemWorklogDTO.setCreateUnitName(unitToken.getUnitName());
    return problemWorklogRepository.onInsert(problemWorklogDTO);
  }

  @Override
  public List getListProblemWorklogByCondition(
      List<ConditionBean> lstConditionBean, int start, int maxResult, String sortType,
      String sortField) {
    log.debug("Request to getListProblemWorklogByCondition : {}");
    return problemWorklogRepository
        .getListProblemWorklogByCondition(lstConditionBean, start, maxResult, sortType, sortField);
  }

  @Override
  public List<String> getSequenseProblemWorklog(String seqName, int... size) {
    return problemWorklogRepository.getSequenceProblemWorklog(seqName, size);
  }

  @Override
  public ProblemWorklogDTO findProblemWorklogById(Long id) throws Exception {
    return problemWorklogRepository.findProblemWorklogById(id);
  }

  @Override
  public String deleteProblemWorklog(Long problemWorklogId) {
    return problemWorklogRepository.deleteProblemWorklog(problemWorklogId);
  }

  @Override
  public String deleteListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS) {
    return problemWorklogRepository.deleteListProblemWorklog(problemWorklogDTOS);
  }

  @Override
  public String updateProblemWorklog(ProblemWorklogDTO problemWorklogDTO) throws Exception {
    return problemWorklogRepository.updateProblemWorklog(problemWorklogDTO);
  }

  @Override
  public String insertOrUpdateListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS)
      throws Exception {
    return problemWorklogRepository.insertOrUpdateListProblemWorklog(problemWorklogDTOS);
  }

  @Override
  public Datatable getListProblemWorklogDTO(ProblemWorklogDTO problemWorklogDTO) {
    log.debug("Request to getListProblemFilesDTO: {}");
    Datatable datatable = problemWorklogRepository.getListProblemWorklogDTO(problemWorklogDTO);
    return datatable;
  }
}
