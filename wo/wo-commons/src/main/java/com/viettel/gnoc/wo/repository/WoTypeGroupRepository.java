package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTypeGroupRepository {

  List<WoTypeGroupDTO> getListWoTypeGroupDTO(WoTypeGroupDTO woTypeGroupDTO);

  ResultInSideDto insertWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO);

  ResultInSideDto deleteWoTypeGroup(Long woTypeGroupId);

  ResultInSideDto deleteWoTypeGroupByWoGroupId(Long woGroupId);

  WoTypeGroupDTO checkTypeGroupExitBy2Id(Long woGroupId, Long woTypeId);

  ResultInSideDto updateWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO);
}
