package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import java.util.List;

public interface MrCfgProcedureTelRepository {

  MrCfgProcedureTelDTO findMrCfgProcedureTelById(Long procedureId);

  Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO, String type);

  List<MrCfgProcedureTelDTO> onSearchExport(MrCfgProcedureTelDTO mrCfgProcedureBtsDTO, String type);

  ResultInSideDto insertOrUpdate(MrCfgProcedureTelDTO mrCfgProcedureTelDTO);

  String insertOrUpdateList(List<MrCfgProcedureTelDTO> listDTO);

  ResultInSideDto delete(Long procedureId);

  MrCfgProcedureTelDTO getDetail(Long procedureId);

  List<MrDeviceDTO> getListNetworkType();

  List<MrDeviceDTO> getListDeviceType2();

  List<MrScheduleTelDTO> getScheduleInProcedure(MrScheduleTelDTO mrScheduleTelDTO);

  List<MrCfgProcedureTelDTO> onSearchEntity(MrCfgProcedureTelDTO mrCfgProcedureTelDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  //TrungDuong them check trùng
  List<MrCfgProcedureTelDTO> checkMrCfgProcedureTelHardExist2(MrCfgProcedureTelDTO cfgProcedureDTO);

  MrCfgProcedureTelDTO findMrCfgProcedureTelHard(String marketCode, String arrayCode,
      String networkType,
      String deviceType, String cycleType, Long cycle);
}
