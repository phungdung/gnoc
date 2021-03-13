package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.io.File;
import java.util.List;

public interface MaintenanceMngtBusiness {

  File exportSearchData(MrSearchDTO mrSearchDTO) throws Exception;

  Datatable getListMrDTOSearchDatatable(MrSearchDTO mrSearchDTO);

  ResultInSideDto onInsert(MrInsideDTO mrInsideDTO) throws Exception;

  ResultInSideDto onEdit(MrInsideDTO mrDTO);

  List<MrApproveSearchDTO> initTabApprove(MrDTO mrDTO);

  List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition);

  List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO);

  MrInsideDTO findById(Long id);

  boolean isCheckEdit(MrInsideDTO mrInsideDTO, String userId);

  ResultInSideDto insertWorkLogProxy(WorkLogInsiteDTO workLogInsiteDTO);

  ResultInSideDto insertWorkLog(WorkLogInsiteDTO workLogInsiteDTO);

  List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  //them ham call proxy
  ResultDTO updateMrStatus(String crId, String woId);

  ResultDTO reCreatedOrCloseCr(String crId, String status);

  MrClientDetail getMrChartInfoForNOC(MrForNocSearchDTO mrSearchDTO);

  List<MrDTO> getListMrForMobile(MrMobileDTO dto);

  List<MrNodesDTO> getWoCrNodeList(String woId, String crId);

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId);

  ResultInSideDto updateMrNodeChecklistForPopUp(List<MrNodeChecklistDTO> lstMrNodeChecklistDTO);

  ResultInSideDto updateWoCrNodeStatus(List<MrNodesDTO> lstNodes);

  List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId);
}
