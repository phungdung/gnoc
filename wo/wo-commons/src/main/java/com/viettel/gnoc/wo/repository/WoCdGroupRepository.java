package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoCdGroupRepository {

  Datatable getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO);

  List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO);

  WoCdGroupInsideDTO findWoCdGroupById(Long woCdGroupDTOId);

  ResultInSideDto insertWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  ResultInSideDto updateWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  ResultInSideDto deleteWoCdGroup(Long woGroupId);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  List<WoCdGroupInsideDTO> getListWoCdGroupExport(WoCdGroupInsideDTO woCdGroupInsideDTO);

  WoCdGroupInsideDTO checkWoCdGroupExit(WoCdGroupInsideDTO woCdGroupInsideDTO);

  ResultInSideDto updateStatusCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  List<WoCdGroupInsideDTO> getListCdGroup(String userName);

  List<WoCdGroupInsideDTO> getListGroupDispatch(Long woTypeId, Long groupTypeId,
      String locale);

  UnitDTO getUnitCodeMapNims(String unitNimsCode, String businessName);

  UnitDTO getUnitByUnitCode(String unitCode);

  WoCdGroupInsideDTO getCdByUnitCode(String unitCode, String woTypeId, String cdGroupType);

  List<WoCdGroupInsideDTO> getListCdGroupByUserDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      Long woTypeId,
      Long groupTypeId, Long userId, String locale);

  WoCdGroupInsideDTO getWoCdGroupWoByCdGroupCode(String woGroupCode);

  List<WoCdGroupInsideDTO> getListWoCdGroupActive(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<UsersInsideDto> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow);

  List<WoCdGroupInsideDTO> getListCdGroupByCodeNotLike(String cdGroupCode);

  List<WoCdGroupInsideDTO> getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);
}
