package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.repository.RoleUserRepository;
import com.viettel.gnoc.commons.repository.RolesRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RolesBusinessImpl implements RolesBusiness {

  @Autowired
  RoleUserRepository roleUserRepository;

  @Autowired
  RolesRepository rolesRepository;

  @Override
  public List<RolesDTO> getListRolePmByUserOfKedb(String userLoginId) {
    List<ConditionBean> lstCondition = new ArrayList<>();
    ConditionBean conditionUser = new ConditionBean("userId", userLoginId,
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(conditionUser);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<String> lstRoleId = new ArrayList<>();
    try {
      List<RoleUserDTO> lstRoleUser = roleUserRepository.getListRoleUserByCondition(lstCondition,
          0, 0, "asc", "roleId");
      if (lstRoleUser != null && lstRoleUser.size() >= 1) {
        for (RoleUserDTO dto : lstRoleUser) {
          lstRoleId.add(dto.getRoleId());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    List<RolesDTO> lstRole = new ArrayList<>();
    lstCondition = new ArrayList<>();
    try {
      ConditionBean condition = new ConditionBean("roleCode", "_KEDB",
          Constants.NAME_LIKE, Constants.STRING);
      lstCondition.add(condition);
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<RolesDTO> lstRoleAdmin = rolesRepository.getListRolesByCondition(lstCondition,
          0, 0, "asc", "roleCode");
      if (lstRoleAdmin != null && !lstRoleAdmin.isEmpty()) {
        lstRole.addAll(lstRoleAdmin);
      }
      for (int i = lstRole.size() - 1; i >= 0; i--) {
        if (!lstRoleId.contains(lstRole.get(i).getRoleId())) {
          lstRole.remove(i);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstRole;
  }

  @Override
  public List<RolesDTO> getListRolesByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return rolesRepository
        .getListRolesByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<RolesDTO> getListRolesDTO(RolesDTO rolesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return rolesRepository.getListRolesDTO(rolesDTO, rowStart, maxRow, sortType, sortFieldList);
  }


}
