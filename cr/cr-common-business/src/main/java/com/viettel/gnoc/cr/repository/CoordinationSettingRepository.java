package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinationSettingRepository {

  CoordinationSettingDTO getCoordinationSettingInfor(Long unitId, Long groupId);
}
