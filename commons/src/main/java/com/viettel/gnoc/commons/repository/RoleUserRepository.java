package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.RoleUserEntity;
import com.viettel.gnoc.commons.model.RolesEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface RoleUserRepository {

  List<RoleUserDTO> getListRoleUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<RoleUserDTO> getListRolesDTO(RoleUserDTO roleUserDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  ResultInSideDto addRoleUser(RoleUserDTO roleUserDTO);

  List<RoleUserEntity> listRoleUser(Long userId);

  ResultInSideDto deleteRoleUser(RoleUserEntity roleUserEntity);

  ResultInSideDto insertOrUpdateRoleUser(UsersInsideDto usersInsideDto);

  RolesEntity findRoleById(Long roleId);

  RolesDTO findRoleByCode(String roleCode);

  List<RolesDTO> getListRoleCodeByUserId(Long userId);
}
