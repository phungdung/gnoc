package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.repository.RoleUserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class RoleUserBusinessImpl implements RoleUserBusiness {

  @Autowired
  protected RoleUserRepository roleUserRepository;

  @Override
  public List<RoleUserDTO> getListRoleUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return roleUserRepository
        .getListRoleUserByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<RoleUserDTO> getListRolesDTO(RoleUserDTO roleUserDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return roleUserRepository
        .getListRolesDTO(roleUserDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}
