package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrCfgCrUnitTelBusiness {

  Datatable getListDataMrCfgCrUnitTelSearchWeb(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  ResultInSideDto insertMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  ResultInSideDto updateMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  MrCfgCrUnitTelDTO findMrCfgCrUnitTelById(Long cfgId);

  ResultInSideDto deleteMrCfgCrUnitTel(Long cfgId);

  File exportDataMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) throws Exception;

  File getTemplateImport() throws IOException;

  List<String> getListRegionCombobox(String marketCode);

  List<String> getListNetworkTypeCombobox(String arrayCode);

  List<String> getListDeviceTypeCombobox(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto importDataMrCfgCrUnitTel(MultipartFile fileImport);

  List<UnitDTO> getListUnitCombobox();
}
