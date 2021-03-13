package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleTelRepository {

  Datatable getListMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO, String type);

  MrScheduleTelDTO getDetail(Long id, String type);

  List<MrScheduleTelDTO> getDataExport(MrScheduleTelDTO mrScheduleTelDTO, String type);

  ResultInSideDto updateMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO);

  ResultInSideDto deleteMrScheduleTel(Long id);

  MrInsideDTO findMrById(Long mrId);

  List<MrConfigDTO> getConfigByGroup(String configGroup);

  ResultInSideDto deleteListSchedule(List<MrScheduleTelDTO> lstScheduleDTO);

  List<MrScheduleTelDTO> getListMrScheduleTelDTO(MrScheduleTelDTO mrScheduleTelDTO);

  List<MrScheduleTelDTO> getListMrSheduleTelByIdForImport(List<String> idSearchs);

  MrScheduleTelDTO findById(Long id);

  List<MrScheduleTelHisDTO> getListScheduleMoveToHis(MrDeviceDTO mrDeviceDTO);

  List<CrHisDTO> checkExistCrId(String crId, String crIdOld);

  List<MrScheduleTelDTO> getByMrId(String mrId);
}

