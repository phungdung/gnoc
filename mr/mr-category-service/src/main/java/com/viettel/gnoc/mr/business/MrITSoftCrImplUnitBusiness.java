package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface MrITSoftCrImplUnitBusiness {

  Datatable getListDataMrCfgCrUnitITSoft(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  ResultInSideDto insertMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  ResultInSideDto updateMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  MrITSoftCrImplUnitDTO findMrCfgCrUnitITById(Long cfgId);

  ResultInSideDto deleteMrCfgCrUnitIT(Long cfgId);

  File exportData(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) throws Exception;

  ResultInSideDto importDataMrCfgCrUnitIT(MultipartFile fileImport);

  File getTemplateImport() throws IOException;
}
