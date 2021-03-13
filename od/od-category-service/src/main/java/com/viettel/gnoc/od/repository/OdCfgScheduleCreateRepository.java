package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import com.viettel.gnoc.od.dto.OdFileDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OdCfgScheduleCreateRepository {

  Datatable getListOdCfgScheduleCreateDTOSearchWeb(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  OdCfgScheduleCreateDTO findOdCfgScheduleCreateById(Long id);

  ResultInSideDto insertOdCfgScheduleCreate(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  ResultInSideDto updateOdCfgScheduleCreate(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  ResultInSideDto insertOrUpdateListOdCfgScheduleCreate(
      List<OdCfgScheduleCreateDTO> listScheduleCreate);

  OdCfgScheduleCreateDTO checkOdCfgScheduleCreateExit(
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  ResultInSideDto deleteOdCfgScheduleCreate(Long id);

  String getSequenseOdCfgScheduleCreate(String sequense);

  Datatable getListDataExport(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  List<OdTypeDTO> getListOdType();

  List<OdTypeDTO> getListOdTypeByGroupId(Long odGroupId);

  ResultInSideDto insertOdFile(OdFileDTO odFileDTO);

  ResultInSideDto deleteListOdFile(List<Long> odFileIds);
}
