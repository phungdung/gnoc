package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import java.util.Date;
import java.util.List;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
public interface MrBusiness {

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(String woId, String mrNodeId);

  ResultDTO updateMrNodeChecklistForPopUp_VS(List<MrNodeChecklistDTO> lstMrNodeChecklistDTO)
      throws Exception;

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId);

  List<MrNodesDTO> getWoCrNodeList_VS(String woId, String crId);

  ResultDTO updateWoCrNodeStatus(List<MrNodesDTO> lstNodes);

  String updateWODischargeBattery(List<MrNodesDTO> listMrNodeDTO);

  String closeWO(List<MrNodesDTO> listMrNodeDTO);

  ResultDTO updateMrAndWoCDBattery(String woId, Date recentDischargeCd, String nodeName,
      String nodeIp);

  /**
   * @author Dunglv3
   */
  ResultDTO createMrDTO(MrDTO mrDTO, UsersDTO usersDTO, List<MrWoTempDTO> lstMrWoTempDTO);

  boolean checkNodeStatusByWo(String woId);

  ResultInSideDto delete(Long mrId);

  boolean updateMrState(String mrId, String state, ResultDTO res);

  ResultInSideDto insertMrHis(MrHisDTO mrHisDTO);

  ResultDTO insertMrScheduleTelHis(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  List<MrScheduleTelDTO> getByMrId(String mrId);

  ResultInSideDto deleteMrScheduleTel(Long id);

  MrDeviceDTO findDetailById(Long deviceId);

  ResultInSideDto updateMrDeviceDTO(MrDeviceDTO dtoUpdate);

  List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId);

  List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId);
}
