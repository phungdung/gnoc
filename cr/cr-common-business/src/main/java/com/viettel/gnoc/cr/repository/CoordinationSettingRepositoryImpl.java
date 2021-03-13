package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.cr.model.CoordinationSettingEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CoordinationSettingRepositoryImpl extends BaseRepository implements
    CoordinationSettingRepository {

  @Override
  public CoordinationSettingDTO getCoordinationSettingInfor(Long unitId, Long groupId) {
    CoordinationSettingDTO coordinationSettingDTO = new CoordinationSettingDTO();
    if (unitId != null) {
      coordinationSettingDTO.setUnitId(unitId);
    }
    if (groupId != null) {
      coordinationSettingDTO.setGroupId(groupId);
    }
    try {
      List<CoordinationSettingDTO> listObject = onSearchEntity(CoordinationSettingEntity.class,
          coordinationSettingDTO, 0, 2, "", "");
      if (listObject != null && listObject.size() > 0) {
        return listObject.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
