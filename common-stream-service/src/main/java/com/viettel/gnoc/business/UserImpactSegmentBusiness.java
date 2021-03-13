package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;

public interface UserImpactSegmentBusiness {

  Datatable getListUserImpactSegmentOfCr(UserImpactSegmentDTO dto);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();

  List<RolesDTO> getListRolesCBB();
}
