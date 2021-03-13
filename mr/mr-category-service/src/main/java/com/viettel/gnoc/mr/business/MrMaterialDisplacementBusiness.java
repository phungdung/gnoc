package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface MrMaterialDisplacementBusiness {

  List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO(
      MrMaterialDisplacementDTO mrMaterialDisplacementDTO);

  MrMaterialDTO getDetail(Long id);

  File exportData(MrMaterialDTO mrMaterialDTO) throws Exception;

  Datatable getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO);

  ResultInSideDto insertMrMaterial(MrMaterialDTO mrMaterialDTO);

  ResultInSideDto updateMrMaterial(MrMaterialDTO mrMaterialDTO);

  ResultInSideDto deleteMrMaterial(Long id);

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile upLoadFile);

  Map<String, String> getDeviceTypeCBB();

}
