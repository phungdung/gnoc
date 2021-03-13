package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImpactSegmentRepository {

  Datatable getListUserImpactSegmentOfCr(UserImpactSegmentDTO dto);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();

  List<RolesDTO> getListRolesCBB();
}
