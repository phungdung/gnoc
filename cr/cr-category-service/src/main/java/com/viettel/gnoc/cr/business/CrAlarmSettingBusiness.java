package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface CrAlarmSettingBusiness {

  Datatable getCrAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();

  File exportData(CrAlarmSettingDTO crAlarmSettingDTO) throws Exception;

  List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto saveOrUpdateAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  ResultInSideDto updateVendorOrModuleAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  ResultInSideDto validateAlarmSetting(Long casId);

  ResultInSideDto updateAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  ResultInSideDto delete(Long casId);

  List<CrAlarmFaultGroupDTO> getListGroupFaultSrc(String faultSrc, String nationCode)
      throws Exception;

  Datatable getAlarmList(CrAlarmInsiteDTO crAlarmDTO) throws Exception;

  HashSet<String> getListFaultSrc(String nationCode) throws Exception;

  Datatable getModuleList(String serviceCode, String moduleCode, String unitCode,
      String nationCode, int page, int pageSize) throws Exception;

  CrAlarmSettingDTO findAlarmSettingByProcessId(CrAlarmSettingDTO crAlarmSettingDTO);

  Datatable getVendorList(String vendorCode, String vendorName, int page, int pageSize)
      throws Exception;

  CrAlarmSettingDTO findCrAlarmSettingById(CrAlarmInsiteDTO crAlarmDTO);

  Map<String, String> nationMap();
}
