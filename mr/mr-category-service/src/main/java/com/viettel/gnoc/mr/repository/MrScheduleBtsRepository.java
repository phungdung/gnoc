package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleBtsRepository {

  Datatable onSearch(MrScheduleBtsDTO mrScheduleCdDTO);

  List<MrScheduleBtsDTO> onSearchExport(MrScheduleBtsDTO mrScheduleCdDTO);

  String updateMrScheduleBts(List<MrScheduleBtsDTO> lstMrSchedule);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNew(
      MrScheduleBtsHisDetailInsiteDTO dto);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNOK(
      MrScheduleBtsHisDetailInsiteDTO dto);

}

