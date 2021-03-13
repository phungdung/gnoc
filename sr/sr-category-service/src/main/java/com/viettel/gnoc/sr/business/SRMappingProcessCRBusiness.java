package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRSearchDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface SRMappingProcessCRBusiness {

  Datatable getListMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO);

  ResultInSideDto insertOrUpdate(SRMappingProcessCRDTO srMappingProcessCRDTO);

  SRMappingProcessCRDTO getSRMappingProcessCRDetail(Long id);

  ResultInSideDto deleteSRMappingProcessCR(Long id);

  SRSearchDTO getListSearchDTO();

  File exportSRMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadfile) throws Exception;

  List<SRMappingProcessCRDTO> getListWoById(Long crProcessId);

  List<SRMappingProcessCRDTO> getListSRMappingProcessCRDTO(SRMappingProcessCRDTO dto);

  SRMappingProcessCRDTO getStartTimeEndTimeFromCrImpact(SRCreateAutoCRDTO dto);

  List<WoCdGroupInsideDTO> getLstWoCdGroupCBB();

  List<ItemDataCRInside> getLstDutyTypeCBB();

  List<ItemDataCRInside> getDutyTypeByProcessId(Long processId);

  List<ItemDataCRInside> getLstAffectedServiceCBB();

  List<WoTypeInsideDTO> getListWoTypeDTOCBB(WoTypeInsideDTO woTypeInsideDTO);

  List<WoTypeInsideDTO> getListWoTestTypeCBB();

  Map<String, String> getListStatusCrCBB(Long crProcessId);

  List<WoInsideDTO> getLstPriority();

}
