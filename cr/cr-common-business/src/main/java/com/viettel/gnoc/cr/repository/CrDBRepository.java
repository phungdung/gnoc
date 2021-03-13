package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrDBRepository {

  Datatable getListCrForRelateOrPreApprove(CrInsiteDTO crDTO);

  List<CrInsiteDTO> processListToGenGr(List<CrInsiteDTO> lst, CrInsiteDTO crDTO,
      boolean isManager,
      Long userId,
      Long userDep, String locale);

  String getResponeTimeCR(CrInsiteDTO crNew);

  void actionUpdateRelatedCR(CrInsiteDTO crDTO);

  void updateCrStatusInCaseOfCloseCR(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void actionDeleteCRFormOtherSystem(Long crId) throws Exception;

  String createCrMappingFromOtherSystem(CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO)
      throws Exception;

  void resetApprovalDeptByCr(Long crId);

  CrApprovalDepartmentInsiteDTO getCurrentCrApprovalDepartmentDTO(Long crId, Long deptId);

  boolean isLastDepartment(Long crId);

  void updateCurentApprovalDepartment(CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId);

  List<CrApprovalDepartmentInsiteDTO> getNextApprovalDepartment(
      Long crId,
      Long cadtLevel);

  void updateNextApprovalDepartment(List<CrApprovalDepartmentInsiteDTO> list);

  void insertIntoHistoryOfCr(
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId,
      Long status, String locale) throws Exception;

  void updateCrStatusInCaseOfApprove(Long actionType,
      Long crId,
      CrInsiteDTO crDTO, String locale) throws Exception;

  CrInsiteDTO getActiveWOController(CrInsiteDTO crDTO);

  List<CrApprovalDepartmentInsiteDTO> getLowerLevelUnAppovedRecords(Long crId, Long currentLevel);

  List<CrInsiteDTO> processListToGenGr(List<CrInsiteDTO> lst, CrInsiteDTO crDTO, String locale);

  void updateLowerLevelUnApprovedRecord(CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO, Long crId, Long actionType, Long returnCode, Long userId);

  void actionCloseCrIncaseOfRejectPrimaryCR(CrInsiteDTO crDTO, Long actionType, String locale)
      throws Exception;

  void updateCrStatusInCaseOfAppraisal(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void assignAppraiseToEmployee(Long actionType, Long crId, CrInsiteDTO crDTO, boolean hasSaveHis,
      String locale) throws Exception;

  void updateCrTypeInCaseOfVerify(Long crId, CrInsiteDTO crDTO);

  void updateCrStatusInCaseOfVerify(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void updateCrStatusInCaseOfSchedule(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void updateCrStatusInCaseOfReceiving(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO);

  List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crInsiteDTO);

  boolean validateCloseWoWhenResolveCR(CrInsiteDTO crDTO);

  void updateCrStatusInCaseOfResolve(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void updateCrStatusInCaseOfAssignCabCR(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception;

  void updateCrStatusInCaseOfCabCR(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  void updateCrInCaseOfQLTDChangeCR(Long actionType, Long crId, CrInsiteDTO crDTO, String locale)
      throws Exception;

  List<CrInsiteDTO> actionGetListDuplicateCRImpactedNode(CrInsiteDTO crDTO) throws Exception;

  Datatable loadCRRelated(CrInsiteDTO crInsiteDTO);

  List<CrDTO> processListToGenGrOutSide(List<CrDTO> lst, CrDTO crDTO,
      boolean isManager,
      Long userId, Long userDep, String locale);

  Datatable getDataTableSecondaryCr(CrInsiteDTO crInsiteDTO);

  Datatable getDataTablePreApprovedCr(CrInsiteDTO crInsiteDTO);

  void updateCrStatusInCaseOfScheduleNoSession(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception;

  void updateCrStatusInCaseOfReceivingNoSession(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception;

  void updateCrStatusInCaseOfApproveNoSession(Long actionType,
      Long crId,
      CrInsiteDTO crDTO, String locale) throws Exception;

  List<CrDTO> processListToGenGrService(List<CrDTO> lst, CrInsiteDTO crDTO,
      String locale);

  String generateTimeLeftService(CrDTO crDTO,
      String locale);

  List<CrDTO> getListPreApprovedCrOutSide(CrDTO crDTO);

  List<CrDTO> getListSecondaryCrOutSide(CrDTO crDTO);

  void updateCrStatusInCaseOfVerifyMrIT(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception;

  void updateCrTimeInCaseResolve(CrInsiteDTO crDTO, boolean isRollback);
}
