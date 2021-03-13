package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrScheduleTelBusiness {

  Datatable getListMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO);

  MrScheduleTelDTO getDetail(Long id, String type);

  File exportData(MrScheduleTelDTO mrScheduleTelDTO) throws Exception;

  ResultInSideDto updateMrScheduleTelDTO(MrScheduleTelDTO mrScheduleTelDTO);

  ResultInSideDto deleteMrScheduleTel(Long scheduleId);

  List<MrConfigDTO> getConfigByGroup(String configGroup);

  File getTemplate(String type) throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  Datatable onSearchScheduleTel(MrScheduleTelDTO mrScheduleTelDTO);

  File exportSoftData(MrScheduleTelDTO mrScheduleTelDTO) throws Exception;

  ResultInSideDto importSoftData(MultipartFile uploadFile) throws Exception;

  ResultInSideDto onUpdateSoft(MrScheduleTelDTO mrScheduleTelDTO);

  ResultDTO updateScheduleByCrIdCreatrHis(MrScheduleTelDTO mrScheduleTelDTO, String lateTimeUpdate);

  ResultInSideDto deleteMrScheduleTelSoft(List<MrScheduleTelDTO> mrScheduleTelDTOS);

  WoInsideDTO findWoById(Long woId, Long mrId);
}
