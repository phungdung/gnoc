package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.List;

public interface SRMappingProcessCRRepository {

  Datatable getListMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO);

  List<SRMappingProcessCRDTO> getListAllMappingProcessCR(
      SRMappingProcessCRDTO srMappingProcessCRDTO);

  ResultInSideDto insertOrUpdate(SRMappingProcessCRDTO srMappingProcessCRDTO);

  SRMappingProcessCRDTO getSRMappingProcessCRDetail(SRMappingProcessCRDTO srMappingProcessCRDTO);

  ResultInSideDto deleteSRMappingProcessCR(Long id);

  List<SRMappingProcessCRDTO> getListAllProcess();

  List<SRMappingProcessCRDTO> getListParentChilLevel2();

  List<SRMappingProcessCRDTO> getListAllWo();

  List<SRMappingProcessCRDTO> checkDeleteSRMappingProcess(
      SRMappingProcessCRDTO srMappingProcessCRDTO);

  List<SRMappingProcessCRDTO> exportSRMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO);

  List<SRMappingProcessCRDTO> getCrProcessIdOrWoId(String crProcessCode);

  List<SRMappingProcessCRDTO> getListWo(Long crProcessId);

  List<SRMappingProcessCRDTO> getListParentChil();

  List<SRMappingProcessCRDTO> getListSRMappingProcessCRDTO(SRMappingProcessCRDTO dto);

  SRMappingProcessCRDTO getStartTimeEndTimeFromCrImpact(SRCreateAutoCRDTO dto);

  List<WoInsideDTO> getLstPriority();

  List<ItemDataCRInside> getDutyTypeByProcessId(Long processId);
}
