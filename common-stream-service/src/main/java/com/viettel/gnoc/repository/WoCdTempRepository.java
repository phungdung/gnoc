package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoCdTempRepository {

  Datatable getListWoCdTempDTO(WoCdTempInsideDTO woCdTempInsideDTO);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  List<WoCdGroupUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO);

  ResultInSideDto add(WoCdTempInsideDTO woCdTempInsideDTO);

  ResultInSideDto edit(WoCdTempInsideDTO woCdTempInsideDTO);

  WoCdTempInsideDTO getDetail(Long woCdTempId);

  ResultInSideDto delete(Long woCdTempId);

  List<WoCdTempInsideDTO> getListDataExport(WoCdTempInsideDTO woCdTempInsideDTO);

  String getSeqWoCdTemp(String sequence);

  List<WoCdGroupUnitDTO> getListUserByCdGroupIsEnable(Long cdGroupId);

  List<WoCdDTO> getListWoCdDTO(WoCdDTO woCdDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);
}
