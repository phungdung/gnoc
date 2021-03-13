package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface MrTestXaBusiness {
  Datatable getListDatatableMrCdBatterryDTO(MrCdBatteryDTO mrCdBatteryDTO);

  MrCdBatteryDTO findMrCDBatteryById(Long dcPowerId);

  List<ItemDataCRInside> getListLocationCombobox(Long parentId);

  List<ItemDataCRInside> getListCountry();

  MrCdBatteryDTO getDetailById(Long dcPowerId);

  ResultInSideDto updateMrCdBatteryDTO(MrCdBatteryDTO mrCdBatteryDTO);

  List<ItemDataCRInside> getListLocationComboboxByCode(String locationCode);

  File exportData(MrCdBatteryDTO mrCdBatteryDTO) throws Exception;

  File getTemplate();

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  MrCdBatteryDTO findMrCDBatteryByProperty(MrCdBatteryDTO mrCdBatteryDTO);

}
