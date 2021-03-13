package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdExportDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleCDRepository {

  Datatable onSearch(MrScheduleCdDTO mrScheduleCdDTO);

  List<MrScheduleCdExportDTO> onSearchExport(MrScheduleCdDTO dto);

  MrScheduleCdDTO findById(Long id);

  ResultInSideDto addOrUpdate(MrScheduleCdDTO dto);

  String insertOrUpdateListCd(List<MrScheduleCdDTO> lstData);

  ResultInSideDto deleteById(Long id);
}
