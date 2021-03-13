package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrNodesRepository {

  ResultDTO updateWoStatus(String woId, String crId, String nodeCode, String status, String comment,
      boolean completeWo);

  List<MrNodesDTO> getListNodeNOK(String crId);

  void insertMrNodeByDTO(List<MrNodesDTO> node);

  ResultInSideDto deleteMrNodeByWoIdAndNodeName(String woId,String nodeName);

  ResultInSideDto updateMrNodeByWoIdAndNodeName(String woId,String nodeName);

  List<MrNodesDTO> getWoCrNodeList(String woId, String crId);

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId);

  ResultInSideDto updateMrNodeChecklistForPopUp(List<MrNodeChecklistDTO> lstMrNodeChecklistDTO);

  List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId);
}
