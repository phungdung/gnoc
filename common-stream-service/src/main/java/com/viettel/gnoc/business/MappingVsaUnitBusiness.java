package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MappingVsaUnitBusiness {

  Datatable getListMappingVsaUnitDTO(MappingVsaUnitDTO mappingVsaUnitDTO);

  ResultInSideDto updateMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO);

  ResultInSideDto deleteMappingVsaUnit(Long id);

  ResultInSideDto deleteListMappingVsaUnit(List<MappingVsaUnitDTO> mappingVsaUnitListDTO);

  MappingVsaUnitDTO findMappingVsaUnitById(Long id);

  ResultInSideDto insertMappingVsaUnit(
      MappingVsaUnitDTO mappingVsaUnitDTO);
  ResultInSideDto insertOrUpdateListMappingVsaUnit(
      List<MappingVsaUnitDTO> mappingVsaUnitDTO);
  File getVsaTemplate() throws Exception;
  File getVsaTemplateReference() throws Exception;
  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
  ResultInSideDto insertMappingVsaUnitImport(MappingVsaUnitDTO mappingVsaUnitDTO);
}
