package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface WoCdGroupBusiness {

  Datatable getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO);

  List<WoCdGroupInsideDTO> getListWoCdGroupDTOByWoTypeAndGroupType(Long woTypeId,
      Long groupTypeId,
      String locale);

  WoCdGroupInsideDTO getCdByStationCode(String stationCode, String woTypeId,
      String cdGroupType, String businessName);

  List<WoCdGroupInsideDTO> getListCdGroupByUserDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      Long woTypeId,
      Long groupTypeId, Long userId, String locale);

  WoCdGroupInsideDTO getCdByStationCodeNation(String stationCode, String woTypeId,
      String cdGroupType, String businessName, String nationCode);

  WoCdGroupInsideDTO getWoCdGroupWoByCdGroupCode(String woGroupCode);

  List<WoCdGroupInsideDTO> getListWoCdGroupActive(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<UsersInsideDto> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow);

  WoCdGroupInsideDTO findWoCdGroupById(Long woCdGroupDTOId);

  ResultInSideDto insertWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  ResultInSideDto updateWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  ResultInSideDto deleteWoCdGroup(Long woGroupId);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  List<ReceiveUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO);

  ResultInSideDto updateWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO);

  List<UsersInsideDto> getListWoCdDTO(WoCdDTO woCdDTO);

  ResultInSideDto updateWoCd(WoCdDTO woCdDTO);

  List<WoTypeInsideDTO> getListWoTypeGroupDTO(WoTypeGroupDTO woTypeGroupDTO);

  List<WoTypeInsideDTO> getListWoTypeAll();

  ResultInSideDto updateWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO);

  File exportData(WoCdGroupInsideDTO woCdGroupInsideDTO) throws Exception;

  File getTemplateImport() throws Exception;

  ResultInSideDto importDataWoCdGroup(MultipartFile fileImport);

  File exportDataWoCd(WoCdDTO woCdDTO) throws Exception;

  File getTemplateAssignUser() throws Exception;

  File getTemplateAssignTypeGroup() throws Exception;

  ResultInSideDto importDataAssignUser(MultipartFile fileImport);

  ResultInSideDto importDataAssignTypeGroup(MultipartFile fileImport);

  ResultInSideDto updateStatusCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO);

  List<WoCdGroupInsideDTO> getListCdGroup(String userName);

  List<WoCdGroupInsideDTO> getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO);
}
