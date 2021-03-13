package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import java.util.List;

public interface CrApprovalDepartmentBusiness {

  Datatable searchSQL(CrApprovalDepartmentInsiteDTO dto);

  List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByProcess(Long crProcessId);

  List<CrApprovalDepartmentInsiteDTO> search(CrApprovalDepartmentInsiteDTO tDTO, int start,
      int maxResult,
      String sortType, String sortField);

  List<CrApprovalDepartmentDTO> onSearch(CrApprovalDepartmentDTO tDTO, int start, int maxResult,
      String sortType, String sortField);
}
