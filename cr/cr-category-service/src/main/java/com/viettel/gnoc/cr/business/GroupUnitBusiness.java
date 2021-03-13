package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import java.io.File;
import java.util.List;

public interface GroupUnitBusiness {

  Datatable getListGroupUnitDTO(GroupUnitDTO groupUnitDTO);

  ResultInSideDto insertGroupUnit(GroupUnitDTO groupUnitDTO);

  ResultInSideDto updateGroupUnit(GroupUnitDTO groupUnitDTO);

  GroupUnitDTO findGroupUnitById(Long id);

  ResultInSideDto deleteGroupUnit(Long id);

  ResultInSideDto deleteListGroupUnit(List<GroupUnitDTO> groupUnitDTOS);

  ResultInSideDto updateListGroupUnit(List<GroupUnitDTO> groupUnitDTOS);

  File exportDataListGroupUnit(GroupUnitDTO groupUnitDTO) throws Exception;
}
