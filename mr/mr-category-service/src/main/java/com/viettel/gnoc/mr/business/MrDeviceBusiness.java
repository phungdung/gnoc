package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface MrDeviceBusiness {

  List<MrDeviceDTO> getListDeviceTypeByNetworkType(String arrayCode, String networkType);

  List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode);

  Datatable getListMrDeviceSoftDTO(MrDeviceDTO mrDeviceDTO);

  List<MrDeviceDTO> getListRegionByMarketCode(String marketCode);

  ResultInSideDto updateMrDeviceHard(MrDeviceDTO mrDeviceDTO, boolean checkIsCommittee, boolean isImport);

  MrDeviceDTO getDetail(Long deviceId);

  ResultInSideDto deleteMrDeviceHard(MrDeviceDTO mrDeviceDTO);

  File exportData(MrDeviceDTO mrDeviceDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  List<MrDeviceDTO> onSearchEntity(MrDeviceDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  ResultInSideDto insertOrUpdateListDevice(List<MrDeviceDTO> lstMrDeviceDto);

  void updateStatusAndLastDate(MrScheduleTelDTO objScheduleTel, String status, String lastDate);

  List<MrDeviceDTO> getDeviceStationCodeCbb();

  ResultInSideDto approveMrDeviceHard(MrDeviceDTO mrDeviceDTO);

}
