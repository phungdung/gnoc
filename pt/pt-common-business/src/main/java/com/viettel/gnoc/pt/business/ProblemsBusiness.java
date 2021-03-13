package com.viettel.gnoc.pt.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface ProblemsBusiness {

  List<ProblemsInsideDTO> getListProblemDTO(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemDTOForTT(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemsDTO(ProblemsInsideDTO problemsInsideDTO);

  Datatable getListProblemsSearch(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListProblemSearch(ProblemsInsideDTO problemsInsideDTO);

  File getListProblemsSearchExport(ProblemsInsideDTO problemsInsideDTO) throws Exception;

  int getListProblemSearchCount(ProblemsInsideDTO problemsInsideDTO);

  List<String> getSequenseProblems(String seqName, int... size);

  ResultInSideDto add(ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto edit(ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto delete(Long id);

  ResultInSideDto deleteListProblems(List<ProblemsInsideDTO> problemsListDTO);

  ProblemsInsideDTO findProblemsById(Long id);

  List<ProblemsChartDTO> getProblemsChartData(String receiveUnitId);

  List<ProblemMonitorDTO> getProblemsMonitor(String priorityId, String unitId,
      String fromDate, String toDate, String findInSubUnit, String unitType);

  List<ProblemsMobileDTO> getProblemsMobileUnitAll(String receiveUnitId);

  List<ProblemsInsideDTO> getListProblemsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ProblemsMobileDTO> getProblemsMobileUnit(String receiveUnitId);

  Datatable getListProblemSearchDulidate(ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto insertProblems(List<MultipartFile> files, ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto insertProblemsCommon(List<MultipartFile> files,
      ProblemsDTO problemsInsideDTO, boolean isInside);

  ResultInSideDto insertOrUpdateListProblems(List<ProblemsInsideDTO> problemsInsideDTOList);

  ResultInSideDto updateProblems(ProblemsInsideDTO problemsInsideDTO) throws Exception;

  ResultInSideDto updateListProblems(List<ProblemsInsideDTO> lstProblemsInsideDTOS,
      ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto updateProblemsNew(ProblemsInsideDTO problemsInsideDTO);

  Datatable getTransitionStatus(ProblemsInsideDTO dto);

  List<UsersInsideDto> getListChatUsers(ProblemsInsideDTO problemsInsideDTO);

  List<ProblemsInsideDTO> getListPtRelated(ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto sendChatListUsers(ProblemsInsideDTO problemsInsideDTO) throws Exception;

  Datatable loadUserSupportGroup(ProblemsInsideDTO problemsInsideDTO);

  List<RolesDTO> getListRolePmByUser(Long userLoginId);

  Datatable searchParentPTForCR(ProblemsInsideDTO dto);

  Datatable getListProblemFiles(GnocFileDto gnocFileDto);

  ResultInSideDto insertProblemFiles(List<MultipartFile> files, ProblemsInsideDTO problemsInsideDTO)
      throws IOException;

  ResultInSideDto deleteProblemFiles(GnocFileDto gnocFileDto);

  Datatable getDatatableProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO);

  Map<String, Map<String, Long>> getListProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto checkInfoUserPM();

  String countDayOff(ProblemConfigTimeDTO dto);

  //add tab OD
  List<OdSearchInsideDTO> findListOdByPt(Long problemId);

  //add tab Sr
  List<SrInsiteDTO> findListSrByPt(Long problemId);

  // getUnitIdWithUser
  UsersEntity usersEntity(Long userId);

  public List<ProblemsDTO> onSynchProblem( String fromDate, String toDate, String insertSource, List<String> lstState);
}
