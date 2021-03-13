package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.io.File;
import java.util.List;

public interface MrScheduleBtsHisBusiness {

  Datatable getListMrScheduleBtsHisDTO(MrScheduleBtsHisDTO mrScheduleBtsHisDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO);

  File exportData(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) throws Exception;

  ResultInSideDto reCreateWo(MrScheduleBtsHisDTO mrScheduleBtsHisDTO);

  ResultInSideDto reCreateWoConfirm(MrScheduleBtsHisDTO mrScheduleBtsHisDTO);

}
