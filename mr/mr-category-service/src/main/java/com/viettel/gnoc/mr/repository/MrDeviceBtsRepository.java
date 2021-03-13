package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrDeviceBtsRepository {

  Datatable getListMrDeviceBtsPage(MrDeviceBtsDTO mrDeviceBtsDTO);

  List<MrDeviceBtsDTO> getListMrDeviceBtsDTO(MrDeviceBtsDTO mrDeviceBtsDTO);

  ResultInSideDto insertOrUpdate(MrDeviceBtsDTO mrDeviceBtsDTO);

  String UpdateListDeviceBts(List<MrDeviceBtsDTO> lstMrDeviceBts);

  ResultInSideDto delete(Long deviceId);

  MrDeviceBtsDTO getDetail(Long deviceId);

  List<MrDeviceBtsDTO> getListDeviceType();

  List<MrDeviceBtsDTO> getListfuelTypeByDeviceType(String deviceType, String marketCode);

  List<MrDeviceBtsDTO> getListProducerByDeviceType(String deviceType, String marketCode);

  List<MrDeviceBtsDTO> getListProvince(String marketCode);

  List<MrDeviceBtsDTO> getListMrDeviceBtsForCD(MrDeviceBtsDTO mrDeviceBtsDTO) throws Exception;

  List<MrDeviceBtsDTO> onSearchExport(MrDeviceBtsDTO mrDeviceBtsDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(String woCode);

  List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String chedklistId, String woId);

  String updateStatusTask(List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTO);

  String updateMaintenanceTimeMrDeviceBts(List<MrDeviceBtsDTO> lstDevices);

  List<MrDeviceBtsDTO> getListSupplierBtsByDeviceType(String deviceType, String marketCode);

  List<MrDeviceBtsDTO> getListMrDeviceBtsByListId(List<Long> lstIds);

}
