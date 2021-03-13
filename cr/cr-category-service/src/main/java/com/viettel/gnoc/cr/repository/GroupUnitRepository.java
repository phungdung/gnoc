package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUnitRepository {

  Datatable getListGroupUnitDTO(GroupUnitDTO groupUnitDTO);

  ResultInSideDto insertGroupUnit(GroupUnitDTO groupUnitDTO);

  ResultInSideDto updateGroupUnit(GroupUnitDTO groupUnitDTO);

  GroupUnitDTO findGroupUnitById(Long id);

  ResultInSideDto deleteGroupUnit(Long id);

  ResultInSideDto deleteListGroupUnit(List<GroupUnitDTO> groupUnitDTOS);

  ResultInSideDto updateListGroupUnit(List<Long> ids);

  List<GroupUnitDTO> getAllListGroupUnitDTO(GroupUnitDTO groupUnitDTO);
}
