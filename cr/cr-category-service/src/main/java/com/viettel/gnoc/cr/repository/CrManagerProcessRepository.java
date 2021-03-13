package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
import com.viettel.gnoc.cr.dto.CrProcessDeptGroupDTO;
import com.viettel.gnoc.cr.dto.CrProcessGroup;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessTemplateDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;

public interface CrManagerProcessRepository {

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

  ResultInSideDto saveCrProcessGroupDept(CrProcessDeptGroupDTO crProcessWoDTO);

  ResultInSideDto saveCrProcessTemplate(CrProcessTemplateDTO crProcessTemplateDTO);

  ResultInSideDto deleteListCrProcessWo(List<Long> lstCrProcessWoId);

  ResultInSideDto deleteCrProcessWo(Long lstCrProcessWoId);

  List<CrProcessWoDTO> getLstWoFromProcessId(Long crProcessId);

  List<CrOcsScheduleDTO> getListCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO);

  ResultInSideDto insertOrUpdateCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO);

  ResultInSideDto insertOrUpdateCrProcessDTO(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto deleteListCrOcsScheduleDTO(List<Long> crOcsScheduleDTOs);

  List<CrProcessInsideDTO> getListDataExport(CrProcessInsideDTO crManagerScopesOfRolesDTO);

  CrProcessInsideDTO checkCrProcessExist(CrProcessInsideDTO dto);

  CrProcessInsideDTO getCrProcessDetail(Long crProcessId);

  CrProcessInsideDTO getCrProcessLevelByCode(String crProcessCode);

  CrProcessInsideDTO getCrProcessById(Long crProcessId);

  CrProcessInsideDTO findCrProcess(CrProcessInsideDTO crProcessDTO);

  ResultInSideDto saveAllList(CrProcessInsideDTO crProcessDTO);

  CrProcessWoDTO getcCrProcessWo(Long woId);

  List<WoTypeInsideDTO> getListWoType();

  WoTypeInsideDTO getWoTypeInsideDTOByCode(String Code);

  Datatable getLstFileTemplate(CrProcessGroup crProcessGroup);

  ResultInSideDto deleteListCrProcess(List<Long> crProcessIds);

  ResultInSideDto deleteCrProcessDTO(Long crProcessId);

  List<Long> deleteChildByParent(Long crProcessId);

  CrProcessWoDTO getCrProcessWoDTO(Long crProcessWoId);

  CrProcessInsideDTO generateCrProcessCode(CrProcessInsideDTO crProcessDTO);

  List<TempImportDTO> getListTempImportDTO(String search);

  void saveDataImport(Long crProcessId, List<TempImportDTO> tempImport,
      List<GroupUnitDTO> groupUnit);

  List<GroupUnitDTO> getGroupUnitDTO(GroupUnitDTO groupUnitDTO);

  boolean checkIsParent(Long crProcessId);

  List<WoTypeInsideDTO> getListWoTypeWithEnable();

  List<CrProcessInsideDTO> getListCrProcessDTO(CrProcessInsideDTO crProcessDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<CrProcessInsideDTO> getAllCrProcess(Long parentId);
}
