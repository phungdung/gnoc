package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrDeviceBtsBusiness {

  Datatable getListMrDeviceBtsPage(MrDeviceBtsDTO mrDeviceBtsDTO);

  List<MrDeviceBtsDTO> getListMrDeviceBtsDTO(MrDeviceBtsDTO mrDeviceBtsDTO);

  ResultInSideDto update(MrDeviceBtsDTO mrDeviceBtsDTO);

  ResultInSideDto delete(Long deviceId);

  MrDeviceBtsDTO getDetail(Long deviceId);

  List<MrDeviceBtsDTO> getListDeviceType();

  List<MrDeviceBtsDTO> getListfuelTypeByDeviceType(String deviceType, String marketCode);

  List<MrDeviceBtsDTO> getListProducerByDeviceType(String deviceType, String marketCode);

  List<MrDeviceBtsDTO> getListProvince(String marketCode);

  List<MrDeviceBtsDTO> getListMrDeviceBtsForCD(MrDeviceBtsDTO mrDeviceBtsDTO) throws Exception;

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(String woCode);

  List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto);

  File exportSearchData(MrDeviceBtsDTO mrDeviceBtsDTO) throws Exception;

  ResultInSideDto importMrDeviceBts(MultipartFile fileImport) throws Exception;

  File getFileTemplate(MrDeviceBtsDTO mrDeviceBtsDTO);

  List<MrDeviceBtsDTO> getListSupplierBtsByDeviceType(String deviceType, String marketCode);
}
