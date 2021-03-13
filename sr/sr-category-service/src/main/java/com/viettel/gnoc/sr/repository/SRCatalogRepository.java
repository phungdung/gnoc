package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import java.util.List;

public interface SRCatalogRepository {

  Datatable getListSRCatalogPage(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListServiceChild(SRCatalogDTO srCatalogDTO);

  ResultInSideDto delete(Long serviceId);

  ResultInSideDto add(SRCatalogDTO srCatalogDTO);

  SRCatalogDTO getDetail(Long serviceId);

  List<SRCatalogDTO> getListSRCatalogNotUsing(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListRoleCodeByServiceCode(String serviceCode);

  List<SRCatalogDTO> getListServiceNameToMapping();

  List<SRCatalogDTO> getListCountryService();

  List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListDataExport(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListSRCatalog(SRCatalogDTO srCatalogDTO);

  SRCatalogDTO checkSRCatalogExist(String serviceCode, String executionUnit);

  List<SRFilesEntity> getListSRFileByObejctId(Long obejctId);

  ResultInSideDto addSRFile(SRFilesDTO srFilesDTO);

  ResultInSideDto deleteSRFile(Long fileId);

  List<SRFilesDTO> findSrFilesByServiceIds(List<Long> serviceId);

  List<SRCatalogEntity> findSRCatalogByServiceCode(String serviceCode);

  SRFilesDTO findSRFile(Long id);
}
