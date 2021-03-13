package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import java.util.Date;
import java.util.List;

public interface MrDeviceBtsRepository {

  ResultInSideDto insertMrScheduleBtsHisFile(MrScheduleBtsHisFileDTO mrScheduleBtsHisFileDTO);

  List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String chedklistId, String woId);

  String updateStatusTask(List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO);

  List<GnocFileDto> getListGnocFileByCheckListIdAndWoId(
      MrScheduleBtsHisFileDTO mrScheduleBtsHisFileDTO);

  String updateStatusAfterMaintenance(String woCode, Date lastMaintenanceTime, String status);

  String updateAIByWoCode(String woCode, String checkListId, Long valueAI, Long photoRate);

  List<MrDeviceBtsDTO> getMrBTSDeviceInfor(String woCode);
}
