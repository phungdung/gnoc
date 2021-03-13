package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import java.util.List;

public interface MrApprovalDepartmentBusiness {

  ResultInSideDto insertMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO);

  List<MrApproveRolesDTO> getLstMrApproveUserByRole(MrApproveRolesDTO mrRole);

  String updateMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO);

  List<MrApproveSearchDTO> getLstMrApproveSearch(MrApproveSearchDTO s);

  List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String userId);
}
