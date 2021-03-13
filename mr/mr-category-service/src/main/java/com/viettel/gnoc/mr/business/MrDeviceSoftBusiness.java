package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrDeviceSoftBusiness {

  Datatable getListDataMrDeviceSoftSearchWeb(MrDeviceDTO mrDeviceDTO);

  MrDeviceDTO findMrDeviceSoftWeb(Long deviceId);

  ResultInSideDto updateMrDeviceSoft(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto deleteMrDeviceSoft(MrDeviceDTO mrDeviceDTO);

  List<MrConfigDTO> getListMaintainStatusCombobox();

  File exportDataMrDeviceSoft(MrDeviceDTO mrDeviceDTO) throws Exception;

  List<WoCdGroupInsideDTO> getListWoCdGroupCombobox();

  List<MrConfigDTO> getListConfirmSoftCombobox();

  List<MrCfgMarketDTO> getListMrCfgMarket();

  ResultInSideDto updateListMrCfgMarket(MrCfgMarketDTO mrCfgMarketDTO);

  ResultInSideDto approveMrDeviceSoft(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto importDataMrDeviceSoft(MultipartFile fileImport);

  File getTemplateImport() throws IOException;

  List<String> getListRegionCombobox(String marketCode);

  List<String> getListNetworkTypeCombobox(String arrayCode);

  List<String> getListDeviceTypeCombobox(MrDeviceDTO mrDeviceDTO);

  List<UnitDTO> getListUnitCombobox();
}
