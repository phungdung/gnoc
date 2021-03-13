package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardUnitConfigDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHardUnitConfigRepository {

  Datatable getListMrHardGroupConfigDTO(MrHardUnitConfigDTO mrHardUnitConfigDTO);

  ResultInSideDto add(MrHardUnitConfigDTO mrHardUnitConfigDTO);

  ResultInSideDto edit(MrHardUnitConfigDTO mrHardUnitConfigDTO);

  MrHardUnitConfigDTO getDetail(Long id);

  MrHardUnitConfigDTO findMrHardGroupConfigById(Long id);

  ResultInSideDto delete(Long id);

  List<MrHardUnitConfigDTO> getListDataExport(MrHardUnitConfigDTO mrHardUnitConfigDTO);

  List<MrDeviceDTO> getListRegionByMarketCode(String marketCode);

  List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode);

  MrHardUnitConfigDTO ckeckMrHardGroupConfigExist(MrHardUnitConfigDTO mrHardUnitConfigDTO);
}
