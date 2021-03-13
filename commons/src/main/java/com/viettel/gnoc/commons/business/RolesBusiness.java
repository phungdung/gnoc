package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

public interface RolesBusiness {

  List<RolesDTO> getListRolePmByUserOfKedb(String userLoginId);

  List<RolesDTO> getListRolesByCondition(List<ConditionBean> lstCondition, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<RolesDTO> getListRolesDTO(RolesDTO rolesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
