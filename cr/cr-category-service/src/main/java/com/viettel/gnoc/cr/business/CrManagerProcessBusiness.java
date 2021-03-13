package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
import com.viettel.gnoc.cr.dto.CrProcessGroup;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author KienPV
 */
public interface CrManagerProcessBusiness {

  Datatable getListSearchCrProcess(CrProcessInsideDTO crProcessDTO);

  List<CrProcessInsideDTO> actionGetListProcessType(CrProcessInsideDTO crProcessDTO);

  List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO);

  List<CrProcessInsideDTO> getRootCrProcess();

  List<CrProcessGroup> getLstFileFromProcessId(CrProcessGroup crProcessGroup);

  List<CrProcessGroup> getLstUnitFromProcessId(Long crProcessId);

  List<CrProcessInsideDTO> getLstAllChildrenByProcessId(Long crProcessId);

  CrProcessInsideDTO getParentByProcessId(Long crProcessId);

  ResultInSideDto deleteGroupUnitOrFileByProcessId(Long crProcessId);

  ResultInSideDto deleteAllChildByParent(Long crProcessId);

  ResultInSideDto deleteFileAndDataWhenChangeProcess(String crId);

  ResultInSideDto saveCrProcessWo(CrProcessWoDTO crProcessWoDTO);

  ResultInSideDto deleteListCrProcessWo(List<Long> lstCrProcessWoId);

  List<CrProcessWoDTO> getLstWoFromProcessId(Long crProcessId);

  List<CrOcsScheduleDTO> getListCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO);

  ResultInSideDto insertOrUpdateCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO);

  ResultInSideDto insertOrUpdateCrProcessDTO(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto deleteListCrOcsScheduleDTO(List<Long> crOcsScheduleDTOs);

  File exportData(CrProcessInsideDTO crProcessDTO) throws Exception;

  ResultInSideDto importData(MultipartFile uploadfile);

  CrProcessInsideDTO getCrProcessDetail(Long crProcessId);

  CrProcessInsideDTO getCrProcessById(Long crProcessId);

  List<CrProcessInsideDTO> getListCrProcessDTO(CrProcessInsideDTO crProcessDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList);

  File getTemplate() throws Exception;

  CrProcessInsideDTO findCrProcess(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto saveAllList(CrProcessInsideDTO crProcessDTO);

  CrProcessWoDTO getcCrProcessWo(Long woId);

  List<WoTypeInsideDTO> getListWoType();

  Datatable getLstFileTemplate(CrProcessGroup crProcessGroup);

  ResultInSideDto deleteListCrProcess(List<Long> crProcessIds);

  ResultInSideDto deleteCrProcess(Long crProcessId);

  CrProcessWoDTO getCrProcessWoDTO(Long crProcessWoId);

  CrProcessInsideDTO generateCrProcessCode(CrProcessInsideDTO crProcessDTO);

  File getTemplateSepical() throws Exception;

  List<CrProcessInsideDTO> getAllCrProcess(Long parentId);
}
