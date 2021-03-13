package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.List;

public interface SRServiceManageRepository {

  Datatable getListSearchSRServiceArray(SRConfigDTO srConfigDTO);

  ResultInSideDto insertOrUpdateService(SRConfigDTO srConfigDTO);

  SRConfigDTO getSRServiceArrayDetail(Long id);

  SRConfigDTO getSRServiceGroupDetail(Long id);

  ResultInSideDto deleteSRService(Long id);

  List<SRConfigDTO> getByConfigGroup(String configGroup);

  Datatable getListSearchSRServiceGroup(SRConfigDTO srConfigDTO);

  SRConfigDTO getSRServiceArrayByDTO(SRConfigDTO srConfigDTO);

  SRConfigDTO getSRServiceGroupByDTO(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> checkEnableGroup(SRConfigDTO configDTO);

  List<SRConfigDTO> checkEnableGroupNotConvert(SRConfigDTO configDTO);

  Datatable getListConfigServiceSystem(SRConfigDTO srConfigDTO);

  ResultInSideDto insertOrUpdateSRConfigService(SRConfigDTO srConfigDTO);

  boolean checkSrConfigExisted(SRConfigDTO srConfigDTO);

  boolean checkSrConfigExistedByCode(SRConfigDTO srConfigDTO);

  boolean checkSrConfigExistedByName(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListDataExportServiceArray(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListDataExportServiceGroup(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListSrConfig(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getSrConfigExistedByName(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListConfigDTO(
      SRConfigDTO srConfigDTO); // dung cho validate import group service

  List<SRConfigDTO> getListConfigDTOGroup(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListSRConfigImport(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListSrConfigGroup(SRConfigDTO srConfigDTO);
}
