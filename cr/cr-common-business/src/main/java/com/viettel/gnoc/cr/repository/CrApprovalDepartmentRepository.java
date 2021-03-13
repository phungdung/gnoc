package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrApprovalDepartmentRepository {

  String saveListDTONoIdSession(List<CrApprovalDepartmentInsiteDTO> obj);

  void deleteAppDeptByCrId(String crId);

  List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByCrId(
      CrApprovalDepartmentInsiteDTO dto);

  List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByCreator(
      CrApprovalDepartmentInsiteDTO dto);

  List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByProcess(Long crProcessId);

  List<CrApprovalDepartmentInsiteDTO> search(CrApprovalDepartmentInsiteDTO tDTO, int start,
      int maxResult,
      String sortType, String sortField);

  void updateCurentApprovalDepartmentNoFlush(CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId);

  List<CrApprovalDepartmentDTO> onSearch(CrApprovalDepartmentDTO tDTO, int start, int maxResult,
      String sortType, String sortField);
}
