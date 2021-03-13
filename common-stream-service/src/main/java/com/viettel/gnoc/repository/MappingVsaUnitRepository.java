package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MappingVsaUnitRepository {

  Datatable getListMappingVsaUnitDTO(MappingVsaUnitDTO mappingVsaUnitDTO);

  ResultInSideDto updateMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO);

  ResultInSideDto deleteMappingVsaUnit(Long id);

  ResultInSideDto deleteListMappingVsaUnit(List<MappingVsaUnitDTO> mappingVsaUnitListDTO);

  MappingVsaUnitDTO findMappingVsaUnitById(Long id);

  ResultInSideDto insertMappingVsaUnit(
      MappingVsaUnitDTO mappingVsaUnitDTO);

  ResultInSideDto insertOrUpdateListMappingVsaUnit(
      List<MappingVsaUnitDTO> mappingVsaUnitDTO);

  List<MappingVsaUnitDTO> getListForName(MappingVsaUnitDTO mappingVsaUnitDTO);

  List<MappingVsaUnitDTO> checkExistVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO);

  List<MappingVsaUnitDTO> checkExistUnitAndVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO);

  List<UnitDTO> checkExistUnitNotActive(UnitDTO unitDTO);
}
