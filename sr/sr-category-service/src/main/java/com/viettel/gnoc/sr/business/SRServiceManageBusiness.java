package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SearchConfigServiceDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SRServiceManageBusiness {

  Datatable getListSearchSRServiceArray(SRConfigDTO srConfigDTO);

  ResultInSideDto insertOrUpdateServiceArray(SRConfigDTO srConfigDTO);

  File exportDataServiceArray(SRConfigDTO srConfigDTO) throws Exception;

  File exportDataServiceGroup(SRConfigDTO srConfigDTO) throws Exception;

  File getTemplateServiceArray() throws Exception;

  File getTemplateServiceGroup() throws Exception;

  ResultInSideDto importDataServiceArray(MultipartFile uploadfile);

  ResultInSideDto importDataServiceGroup(MultipartFile uploadfile);

  SRConfigDTO getSRServiceArrayDetail(Long id);

  SRConfigDTO getSRServiceGroupDetail(Long id);

  ResultInSideDto deleteSRService(Long srConfigDTO);

  Datatable getListSearchSRServiceGroup(SRConfigDTO srConfigDTO);

  ResultInSideDto insertOrUpdateServiceGroup(SRConfigDTO srConfigDTO);

  SRConfigDTO getSRServiceArrayByDTO(SRConfigDTO srConfigDTO);

  boolean checkEnableGroup(SRConfigDTO configDTO);

  Datatable getListConfigServiceSystem(SRConfigDTO srConfigDTO);

  ResultInSideDto insertOrUpdateSRConfigService(SRConfigDTO srConfigDTO);

  ResultInSideDto deleteSRConfig(Long srConfigDTO);

  List<SRConfigDTO> getByConfigGroup(String configGroup);

  List<SRConfigDTO> getByConfigGroupCountry(String country, String configGroup);

  boolean checkSrConfigExisted(SRConfigDTO configDTO);

  SearchConfigServiceDTO getListSearch();

  List<SRConfigDTO> getListSrConfig(SRConfigDTO srConfigDTO);
}
