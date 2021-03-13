package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SRCatalogBusiness {

  Datatable getListSRCatalogPage(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListServiceChild(SRCatalogDTO srCatalogDTO);

  List<SRCatalogDTO> getListSRCatalog(SRCatalogDTO srCatalogDTO);

  ResultInSideDto delete(Long serviceId);

  SRCatalogDTO getDetail(Long serviceId);

  ResultInSideDto insert(List<MultipartFile> srFilesList,
      SRCatalogDTO srCatalogDTO) throws IOException;

  ResultInSideDto update(List<MultipartFile> srFilesList,
      SRCatalogDTO srCatalogDTO) throws Exception;

  File getCatalogTemplate() throws Exception;

  File getChildTemplate() throws Exception;

  ResultInSideDto importDataCatalogChild(MultipartFile uploadFile) throws Exception;

  ResultInSideDto importDataCatalog(MultipartFile uploadFile) throws Exception;

  List<SRCatalogDTO> getListServiceNameToMapping();

  List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO srCatalogDTO);

  File exportData(SRCatalogDTO srCatalogDTO) throws Exception;

  List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO);

  ResultInSideDto importDataUnit(MultipartFile uploadFile, SRRoleUserInSideDTO srRoleUserDTO)
      throws Exception;

  File getUnitTemplate() throws Exception;

  ResultInSideDto getListSRRoleUserByUnitId(SRRoleUserInSideDTO srRoleUserDTO);
}
