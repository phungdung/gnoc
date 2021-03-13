package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrAlarmSettingRepository {

  BaseDto sqlSearch(CrAlarmSettingDTO crAlarmSettingDTO);

  Datatable getCrAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  ResultInSideDto saveOrUpdateAlarmSetting(CrAlarmSettingDTO crAffectedLevelDTO);

  ResultInSideDto deleteAlarmSetting(Long casId);

  List<CrAlarmSettingDTO> getCrAlarmSettingExport(CrAlarmSettingDTO crAlarmSettingDTO);

  CrAlarmSettingDTO findCrAlarmSettingById(Long casId);

  List<CrAlarmSettingDTO> findCrAlarmSettingByProcessId(Long processId);

  List<ItemDataCRInside> listCrProcessCBB(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto checkDuplicate(Long faultId, Long processId);
}
