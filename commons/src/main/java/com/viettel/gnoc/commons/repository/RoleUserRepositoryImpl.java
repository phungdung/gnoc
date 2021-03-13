package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.RoleUserEntity;
import com.viettel.gnoc.commons.model.RolesEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */

@Repository
@Slf4j
public class RoleUserRepositoryImpl extends BaseRepository implements RoleUserRepository {

  @Override
  public List<RoleUserDTO> getListRoleUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new RoleUserEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<RoleUserDTO> getListRolesDTO(RoleUserDTO roleUserDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(RoleUserEntity.class, roleUserDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto addRoleUser(RoleUserDTO roleUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      getEntityManager().persist(roleUserDTO.toEntity());
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public List<RoleUserEntity> listRoleUser(Long userId) {
    try {
      List<RoleUserEntity> lstRoleUser = findByMultilParam(RoleUserEntity.class, "userId", userId);
      if (lstRoleUser != null && !lstRoleUser.isEmpty()) {
        return lstRoleUser;
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteRoleUser(RoleUserEntity roleUserEntity) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      getEntityManager().remove(roleUserEntity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public ResultInSideDto insertOrUpdateRoleUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      RoleUserDTO roleImport = new RoleUserDTO();
      String[] lstRoleCode = null;
      if (usersInsideDto.getRoleCode() != null) {
        lstRoleCode = usersInsideDto.getRoleCode().trim().split(",");
      }
      if (usersInsideDto.getAction() == 0L) {
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        if (lstRoleCode == null) {
          return resultInSideDto;
        } else {
          for (int a = 0; a < lstRoleCode.length; a++) {
            RolesDTO rolesDTO = findRoleByCode(lstRoleCode[a]);
            if (rolesDTO != null) {
              roleImport.setIsActive(rolesDTO.getStatus());
              roleImport.setUserId(usersInsideDto.getUserId().toString());
              roleImport.setRoleId(rolesDTO.getRoleId());
              if (lstRoleCode[a].toUpperCase() == "ADMIN") {
                roleImport.setIsAdmin("1");
              } else {
                roleImport.setIsAdmin("0");
              }
              getEntityManager().persist(roleImport.toEntity());
            }
          }
        }
      } else if (usersInsideDto.getAction() == 1L) {
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        List<RoleUserEntity> rolesEntityList = getListRole(usersInsideDto.getUserId());
        if (rolesEntityList != null) {
          for (RoleUserEntity role : rolesEntityList) {
            getEntityManager().remove(role);
          }
        }
        if (lstRoleCode == null) {
          return resultInSideDto;
        } else {
          for (int i = 0; i < lstRoleCode.length; i++) {
            RolesDTO roleDTO = findRoleByCode(lstRoleCode[i]);
            if (roleDTO != null) {
              roleImport.setRoleId(roleDTO.getRoleId());
              roleImport.setIsActive(roleDTO.getStatus());
              roleImport.setUserId(usersInsideDto.getUserId().toString());
              if (lstRoleCode[i].toUpperCase() == "ADMIN") {
                roleImport.setIsAdmin("1");
              } else {
                roleImport.setIsAdmin("0");
              }
              getEntityManager().persist(roleImport.toEntity());
            }
          }
        }
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public RolesEntity findRoleById(Long roleId) {
    try {
      List<RolesEntity> datas = findByMultilParam(RolesEntity.class, "roleId", roleId);
      if (datas != null && !datas.isEmpty()) {
        return datas.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  private List<RoleUserEntity> getListRole(Long userId) {
    try {
      List<RoleUserEntity> datas = findByMultilParam(RoleUserEntity.class, "userId", userId);
      if (datas != null && !datas.isEmpty()) {
        return datas;
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public RolesDTO findRoleByCode(String roleCode) {
    try {
      String sql = "select * from common_gnoc.roles where ROLE_CODE = :roleCode";
      Map<String, Object> param = new HashMap<>();
      param.put("roleCode", roleCode);
      List<RolesDTO> lstRole = getNamedParameterJdbcTemplate()
          .query(sql, param, BeanPropertyRowMapper.newInstance(RolesDTO.class));
      if (lstRole != null && !lstRole.isEmpty()) {
        return lstRole.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public List<RolesDTO> getListRoleCodeByUserId(Long userId) {
    try {
      String sql = "Select r.ROLE_CODE roleCode From ROLE_USER ru join Roles r on ru.ROLE_ID = r.ROLE_ID where ru.USER_ID = :userId";
      Map<String, Object> param = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(userId)) {
        param.put("userId", userId);
      }
      List<RolesDTO> lsRolesDTOS = getNamedParameterJdbcTemplate()
          .query(sql, param, BeanPropertyRowMapper.newInstance(RolesDTO.class));
      if (lsRolesDTOS != null && !lsRolesDTOS.isEmpty()) {
        return lsRolesDTOS;
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }
}
