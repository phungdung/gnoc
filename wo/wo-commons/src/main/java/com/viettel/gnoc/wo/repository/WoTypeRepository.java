package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgFileCreateWoDTO;
import com.viettel.gnoc.wo.dto.WoTypeFilesGuideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import com.viettel.gnoc.wo.model.CfgFileCreateWoEntity;
import com.viettel.gnoc.wo.model.WoTypeFilesGuideEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTypeRepository {

  Datatable getListWoTypeByLocalePage(WoTypeInsideDTO woTypeInsideDTO);

  ResultInSideDto delete(Long woTypeId);

  ResultInSideDto insertListWoType(List<WoTypeInsideDTO> woTypeInsideDTOList);

  Datatable getListDataExport(WoTypeInsideDTO woTypeInsideDTO);

  ResultInSideDto add(WoTypeInsideDTO woTypeInsideDTO);

  WoTypeInsideDTO checkWoTypeExist(String woTypeCode);

  WoTypeInsideDTO findByWoTypeId(Long woTypeDTOId);

  List<WoTypeInsideDTO> getListWoTypeForWoCdGroup(List<Long> listWoTypeId);

  List<WoTypeInsideDTO> getListWoTypeByWoTypeName(String woTypeName);

  List<WoTypeTimeDTO> getListWoTypeTimeDTO(WoTypeTimeDTO woTypeTimeDTO);

  List<WoTypeInsideDTO> getListWoTypeIsEnable(WoTypeInsideDTO woTypeInsideDTO);

  Datatable getListWoTypeIsEnableDataTable(WoTypeInsideDTO woTypeInsideDTO);

  List<WoTypeInsideDTO> getListWoTypeByLocaleNotLike(WoTypeInsideDTO woTypeInsideDTO);

  List<WoTypeInsideDTO> getListWoTypeByWoGroup(Long cdGroupId, String system,
      String locale);

  List<WoTypeInsideDTO> getListWoTypeByLocale(
      WoTypeInsideDTO woTypeDTO, String locale);

  WoTypeInsideDTO getWoTypeByCode(String woTypeCode);

  List<LanguageExchangeDTO> getLanguageExchangeWoType(String system, String business);

  ResultInSideDto addWoTypeFilesGuide(WoTypeFilesGuideDTO woTypeFilesGuideDTO);

  ResultInSideDto deleteWoTypeFilesGuide(Long woTypeFilesGuideId);

  List<WoTypeFilesGuideEntity> getListFilesGuideByWoTypeId(Long woTypeId);

  ResultInSideDto addCfgFileCreateWo(CfgFileCreateWoDTO cfgFileCreateWoDTO);

  ResultInSideDto deleteCfgFileCreateWo(Long cfgFileCreateWoId);

  List<CfgFileCreateWoEntity> getListFileCreateByWoTypeId(Long woTypeId);

  List<WoTypeFilesGuideDTO> getListWoTypeFilesGuideDTO(WoTypeFilesGuideDTO typeFileCon);
}
