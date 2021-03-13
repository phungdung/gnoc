package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardGroupConfigDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrHardGroupConfigBusiness {

  Datatable getListMrHardGroupConfigDTO(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  ResultInSideDto insert(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  ResultInSideDto update(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  MrHardGroupConfigDTO getDetail(Long id);

  ResultInSideDto delete(Long id);

  List<MrDeviceDTO> getListRegionByMarketCode(String marketCode);

  List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode);

  File exportData(MrHardGroupConfigDTO mrHardGroupConfigDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
}
