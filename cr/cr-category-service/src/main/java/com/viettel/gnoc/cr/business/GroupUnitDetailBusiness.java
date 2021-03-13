package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
import java.io.File;
import java.util.List;

public interface GroupUnitDetailBusiness {

  Datatable getListGroupUnitDetailDTO(GroupUnitDetailDTO dto);

  ResultInSideDto insertGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO);

  ResultInSideDto updateGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO);

  ResultInSideDto deleteGroupUnitDetail(Long id);

  ResultInSideDto deleteListGroupUnitDetail(List<GroupUnitDetailDTO> groupUnitDetailListDTO);

  GroupUnitDetailDTO findById(Long id);

  Datatable getListUnitOfGroup(GroupUnitDetailNameDTO groupUnitDetailNameDTO);

  Datatable getIDGroup(GroupUnitDetailDTO groupUnitDetailDTO);

  File exportData(GroupUnitDetailNameDTO groupUnitDetailNameDTO) throws Exception;
}
