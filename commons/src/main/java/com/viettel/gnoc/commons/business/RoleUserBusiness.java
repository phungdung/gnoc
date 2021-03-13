package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

/**
 * @author TienNV
 */
public interface RoleUserBusiness {

  List<RoleUserDTO> getListRoleUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<RoleUserDTO> getListRolesDTO(RoleUserDTO roleUserDTO, int rowStart, int maxRow,
      String sortType,
      String sortFieldList);
}
