package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrApprovalDepartmentRepository {

  ResultInSideDto insertMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO);

  List<MrApproveRolesDTO> getLstUserByRole(MrApproveRolesDTO mrRole);

  String updateMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO);

  List<MrApproveSearchDTO> getLstMrApproveSearch(MrApproveSearchDTO s);

  List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String userId);
}
