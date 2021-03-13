package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemFilesDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface PtProblemsRepository {


  @SuppressWarnings("unchecked")
  List<ProblemsInsideDTO> getListProblemDTO(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemDTOForTT(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemsDTO(ProblemsInsideDTO problemsInsideDTO);


  Datatable getListProblemsSearch(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemsSearchExport(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemSearch(ProblemsInsideDTO problemsInsideDTO);

  int getListProblemSearchCount(ProblemsInsideDTO problemsInsideDTO);

  List<String> getSequenseProblems(String seqName, int... size);

  ResultInSideDto add(ProblemsInsideDTO problemsInsideDTO);

  String getSeqTableProblems(String seq);

  ResultInSideDto edit(ProblemsInsideDTO problemsInsideDTO);


  ResultInSideDto delete(Long id);

  ResultInSideDto deleteListProblems(List<ProblemsInsideDTO> problemsListDTO);

  ProblemsInsideDTO findProblemsById(Long id);

  List<ProblemsChartDTO> getProblemsChartData(String receiveUnitId);

  List<ProblemMonitorDTO> getProblemsMonitor(String priorityId, String unitId,
      String fromDate, String toDate, String findInSubUnit, String unitType);


  List<ProblemsInsideDTO> getListProblemsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ProblemsMobileDTO> getProblemsMobileUnit(String receiveUnitId);

  List<ProblemsMobileDTO> getProblemsMobileUnitAll(String receiveUnitId);

  ResultInSideDto insertOrUpdateListProblems(List<ProblemsInsideDTO> problemsInsideDTO);

  List<CatItemDTO> getTransitionStatus(ProblemsInsideDTO dto);

  Datatable getListProblemSearchDulidates(String fromDate, String toDate,
      UserTokenGNOCSimple userTokenGNOC,
      List<String> lstIn, List<ProblemsInsideDTO> lstNotIn, Integer startRow, Integer pageLength,
      String sortName, String sortType);

  List<UsersInsideDto> getListChatUsers(ProblemsInsideDTO problemsInsideDTO);

  List<InfraDeviceDTO> getInfraDeviceDTOSByListCode(List<String> nodeCodes);

  Long getMaxRowDuplicate(String tableCode);

  Datatable searchParentPTForCR(ProblemsInsideDTO dto);

  Datatable getListProblemFiles(GnocFileDto gnocFileDto);

  ResultInSideDto insertProblemFiles(ProblemFilesDTO problemFilesDTO);

  ResultInSideDto deleteProblemFiles(Long problemFileId);

  Datatable getDatatableProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO);

  List<TransitionStateConfigDTO> getSkipStatusPT(TransitionStateConfigDTO dto);

  List<UsersInsideDto> lstUserLDP(Long unitId);

  List<UsersInsideDto> lstUserLDP1(Long id);

  List<ProblemsDTO> onSynchProblem(String fromDate, String toDate, String insertSource,
      List<String> lstState);
}
