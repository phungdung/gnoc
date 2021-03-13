package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUnitDetailRepository {

  Datatable getListGroupUnitDetailDTO(GroupUnitDetailDTO dto);

  ResultInSideDto insertGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO);

  ResultInSideDto updateGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO);

  ResultInSideDto deleteGroupUnitDetail(Long id);

  ResultInSideDto deleteListGroupUnitDetail(List<GroupUnitDetailDTO> groupUnitDetailListDTO);

  GroupUnitDetailDTO findById(Long id);

  Datatable getListUnitOfGroup(GroupUnitDetailNameDTO groupUnitDetailNameDTO);

  Datatable getIDGroup(GroupUnitDetailDTO groupUnitDetailDTO);

  GroupUnitDetailDTO getDetail(Long id);

  List<GroupUnitDetailDTO> getAllListGroupUnitDetailDTO(GroupUnitDetailDTO dto);
}
