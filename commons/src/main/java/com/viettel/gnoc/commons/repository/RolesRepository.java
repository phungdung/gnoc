package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository {

  List<RolesDTO> getListRolesByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<RolesDTO> getListRolesDTO(RolesDTO rolesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
