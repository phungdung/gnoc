package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleBtsHisRepository {

  Datatable getListMrScheduleBtsHisDTO(MrScheduleBtsHisDTO mrScheduleBtsHisDTO);

  List<MrScheduleBtsHisDTO> getDataExport(MrScheduleBtsHisDTO mrScheduleBtsHisDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  ResultInSideDto editHisDetail(MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  String updateMrScheduleBts(String serial, String deviceType, String cycle);

  String updateMrScheduleBtsHis(String serial, String deviceType, String cycle, String woCode,
      String isComplete, String finishTime);

  ResultInSideDto insertHisDetail(MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  ResultInSideDto deleteMrScheduleBtsHisByWoCode(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  ResultInSideDto insertReassignWO(String woCodeOld, String woCodeNew);

  String deleteMrScheduleBtsByWoCode(String woCode);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoBtsBySerial(String serial);

  List<MrScheduleBtsHisDetailInsiteDTO> getDataExportDetail(
      MrScheduleBtsHisDTO mrScheduleBtsHisDTO);
}
