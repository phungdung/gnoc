package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrAlarmRepository {

  BaseDto sqlSearch(CrAlarmSettingDTO crAlarmSettingDTO);

  boolean insertListAlarmSetting(List<CrAlarmSettingDTO> dtoList);

  boolean saveOrUpdateList(List<CrAlarmInsiteDTO> dtoList, Long crId, Date crCreateTime);

  boolean saveOrUpdateVendorDetail(List<CrVendorDetailDTO> dtoList, Long crId, Date crCreateTime);

  boolean saveOrUpdateModuleDetail(List<CrModuleDetailDTO> dtoList, Long crId, Date crCreateTime);

  List<CrAlarmInsiteDTO> getListAlarmByCr(CrInsiteDTO crInsiteDTO);

  List<CrAlarmSettingDTO> getAlarmSettingByVendor(CrAlarmSettingDTO crAlarmSettingDTO);

  List<CrModuleDetailDTO> getListModuleByCr(CrInsiteDTO crInsiteDTO);

  List<CrVendorDetailDTO> getListVendorByCr(CrInsiteDTO crInsiteDTO);

  Datatable getAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO);

  List<CrAlarmSettingDTO> getListAlarm(List<Long> lstCasId);

  List<CrAlarmSettingDTO> getAlarmSetting(String vendorCode, String moduleCode, String nationCode,
      Long crProcessId, String createdUser, Long array, Long deviceType, String alarmName,
      Long approvalStatus, Long faultId);

  List<CrAlarmSettingDTO> getAlarmSettingByModule(CrAlarmSettingDTO crAlarmSettingDTO);

  List<CrAlarmDTO> getListObjectByCr(CrDTO crDTO);

  List<CrAlarmInsiteDTO> getListAlarmByProcess(CrInsiteDTO crInsiteDTO);
}
