package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.wo.dto.WoTypeFilesGuideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface WoTypeBusiness {

  Datatable getListWoTypeByLocalePage(WoTypeInsideDTO woTypeInsideDTO);

  ResultInSideDto delete(Long woTypeId);

  Datatable getListDataExport(WoTypeInsideDTO woTypeInsideDTO);

  File getWoTypeTemplate() throws Exception;

  File exportData(WoTypeInsideDTO woTypeInsideDTO) throws Exception;

  ResultInSideDto importData(MultipartFile upLoadFile) throws Exception;

  WoTypeInsideDTO findByWoTypeId(Long woTypeId);

  ResultInSideDto insert(List<MultipartFile> filesGuideline, List<MultipartFile> filesAttached,
      WoTypeInsideDTO woTypeInsideDTO) throws IOException;

  ResultInSideDto update(List<MultipartFile> filesGuideline, List<MultipartFile> filesAttached,
      WoTypeInsideDTO woTypeInsideDTO) throws IOException;

  WoTypeInsideDTO findWoTypeById(Long woTypeDTOId);

  List<WoTypeInsideDTO> getListWoTypeByWoGroup(Long cdGroupId, String system,
      String locale);

  List<WoTypeInsideDTO> getListWoTypeByLocale(
      WoTypeInsideDTO woTypeDTO, String locale);

  List<WoTypeTimeDTO> getListWoTypeTimeDTO(WoTypeTimeDTO woTypeTimeDTO);

  List<WoTypeInsideDTO> getListWoTypeIsEnable(WoTypeInsideDTO woTypeInsideDTO);

  Datatable getListWoTypeIsEnableDataTable(WoTypeInsideDTO woTypeInsideDTO);

  List<WoTypeInsideDTO> getListWoTypeByLocaleNotLike(WoTypeInsideDTO woTypeInsideDTO);

  WoTypeInsideDTO getWoTypeByCode(String woTypeCode);

  List<LanguageExchangeDTO> getLanguageExchangeWoType(String system, String business);

  List<WoTypeFilesGuideDTO> getListWoTypeFilesGuideDTO(WoTypeFilesGuideDTO typeFileCon);

  UsersDTO getUnitNameByUserName(String username);
}
