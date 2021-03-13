package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrMaterialDisplacementRepository {

  List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO(
      MrMaterialDisplacementDTO mrMaterialDisplacementDTO);

  BaseDto sqlSearch(MrMaterialDTO mrMaterialDTO);

  Datatable getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO);

  MrMaterialDTO getDetail(Long id);

  List<MrMaterialDTO> getDataExport(MrMaterialDTO mrMaterialDTO);

  ResultInSideDto insertOrUpdateMrMaterial(MrMaterialDTO mrMaterialDTO);

  ResultInSideDto deleteMrMaterial(Long id);

  List<MrMaterialDTO> checkListDuplicate(MrMaterialDTO mrMaterialDTO);
}
