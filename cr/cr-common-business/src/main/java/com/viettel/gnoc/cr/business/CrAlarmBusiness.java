package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface CrAlarmBusiness {

  List<CrAlarmInsiteDTO> getListAlarmByCr(CrInsiteDTO crInsiteDTO);

  HashSet<String> getListFaultSrc(String nationCode) throws Exception;

  List<CrAlarmFaultGroupDTO> getListGroupFaultSrc(String faultSrc, String nationCode)
      throws Exception;

  Datatable getAlarmList(CrAlarmInsiteDTO crAlarmDTO) throws Exception;

  List<CrAlarmSettingDTO> getAlarmSettingByVendor(CrAlarmSettingDTO crAlarmSettingDTO);

  File exportData(List<Long> lstCasId);

  Datatable getVendorList(CrAlarmSettingDTO alarmSettingDTO);

  Map<String, String> nationMap();

  Datatable setupModuleData(CrModuleDraftDTO crModuleDraftDTO);

  List<CrModuleDetailDTO> getListModuleByCr(CrInsiteDTO crInsiteDTO);

  List<CrVendorDetailDTO> getListVendorByCr(CrInsiteDTO crInsiteDTO);

  Datatable getAlarmSetting(CrAlarmSettingDTO alarmSettingDTO);

  List<CrAlarmSettingDTO> getAlarmSettingByModule(CrAlarmSettingDTO crAlarmSettingDTO);

  List<CrAlarmSettingDTO> getListAlarmSettingByModule(List<CrAlarmSettingDTO> crAlarmSettingDTOS);

  List<CrAlarmDTO> getListAlarmByCr(CrDTO crDto);

  File exportDataNew(List<CrAlarmSettingDTO> lstCrAlarms);
}
