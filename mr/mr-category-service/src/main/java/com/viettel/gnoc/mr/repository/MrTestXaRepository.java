package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.wo.dto.WoDTO;

import java.util.List;

public interface MrTestXaRepository {
  Datatable getListDatatableMrCdBatterryDTO(MrCdBatteryDTO mrCdBatteryDTO);

  MrCdBatteryDTO findMrCDBatteryById(Long dcPowerId);

  List<ItemDataCRInside> getListLocationCombobox(Long parentID);

  List<ItemDataCRInside> getListLocationComboboxByCode(String locationCode);

  List<ItemDataCRInside> getListCountry();

  MrCdBatteryDTO getDetailById(Long dcPowerId);

  MrCdBatteryDTO findById(Long dcPowerId);

  List<MrCdBatteryDTO> getListMrCdBatteryByListId(List<Long> lstIds);

  ResultInSideDto updateMrCdBatteryDTO(MrCdBatteryDTO mrCdBatteryDTO);

  List<MrCdBatteryDTO> getDataExport(MrCdBatteryDTO mrCdBatteryDTO);

  MrNodesDTO findMrNodeByID(Long mrNodeID);

  ResultInSideDto deleteMrNode(Long id);

  ResultInSideDto updateWo(WoDTO woDTO);

  List<MrCdBatteryDTO> getByIdImport(List<Long> dcPowerIds, String userName);

  List<MrCdBatteryDTO> getListWoFromMrCdBattery(String woCode);

  MrCdBatteryDTO findMrCDBatteryByProperty(MrCdBatteryDTO mrCdBatteryDTO);

}
