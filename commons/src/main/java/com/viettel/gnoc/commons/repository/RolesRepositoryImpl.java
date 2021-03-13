package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.model.RolesEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RolesRepositoryImpl extends BaseRepository implements RolesRepository {

  @Override
  public List<RolesDTO> getListRolesByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new RolesEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<RolesDTO> getListRolesDTO(RolesDTO rolesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    List<RolesDTO> list = onSearchEntity(RolesEntity.class, rolesDTO, rowStart, maxRow, sortType,
        sortFieldList);
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.COMMON_TRANSLATE_BUSINESS.ROLES.toString());
    try {
      list = setLanguage(list, lstLanguage, "roleId", "roleName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }
}
