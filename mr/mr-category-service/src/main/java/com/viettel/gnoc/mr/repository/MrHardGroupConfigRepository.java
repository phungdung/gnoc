package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardGroupConfigDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHardGroupConfigRepository {

  Datatable getListMrHardGroupConfigDTO(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  ResultInSideDto add(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  ResultInSideDto edit(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  MrHardGroupConfigDTO getDetail(Long id);

  MrHardGroupConfigDTO findMrHardGroupConfigById(Long id);

  ResultInSideDto delete(Long id);

  List<MrHardGroupConfigDTO> getListDataExport(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  List<MrDeviceDTO> getListRegionByMarketCode(String marketCode);

  List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode);

  MrHardGroupConfigDTO ckeckMrHardGroupConfigExist(MrHardGroupConfigDTO mrHardGroupConfigDTO);

  List<MrDeviceDTO> getListRegion();
}
