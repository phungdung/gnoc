package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.List;

public interface MrBtsHisCheckListBusiness {

  ResultInSideDto checkIsApprovalReturnLstMrScheBtsHisDetail(String typeView,
      MrScheduleBtsHisDetailInsiteDTO form);

  ResultInSideDto checkIsHaveRole(String typeView, String woCode);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNew(
      MrScheduleBtsHisDetailInsiteDTO dto);

  List<MrScheduleBtsHisDetailInsiteDTO> getCheckListByWoId(String woCode);

  List<MrScheduleBtsHisDetailInsiteDTO> checkShowRedAndGetFiles(String woCode);

  ResultInSideDto actionSaveList(MrScheduleBtsHisDetailInsiteDTO dtoUpdate);
}
