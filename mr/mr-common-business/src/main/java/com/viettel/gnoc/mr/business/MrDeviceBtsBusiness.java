package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.maintenance.dto.CPChecklistFileItemWP;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.dto.Result;
import java.util.List;

public interface MrDeviceBtsBusiness {

  Result updateWOChecklistFiles(String woCode, List<CPChecklistFileItemWP> fileItems);

  List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String chedklistId, String woId);

  List<MrScheduleBtsHisDetailDTO> getListWoBts(String woCode);

  List<String> updateStatusTask(List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO);

  String updateStatusAfterMaintenance(String woCode, String lastMaintenanceTime, String status);

  List<MrDeviceBtsDTO> getMrBTSDeviceInfor(String woCode);
}
