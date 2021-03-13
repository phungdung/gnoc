package com.viettel.gnoc.mr.repository;

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
public interface MrRepository {

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(String woId, String mrNodeId);

  String getConfigUCTTForCreateWo(String configGroup, String configCode);

  ResultInSideDto updateMrNodeChecklistForVS(MrNodeChecklistDTO dtoUpdate);

  List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId);

  String deleteFileFromMrNodeChecklistForVS(String nodeChecklistId);

  List<MrNodeChecklistDTO> findMrNodeChecklistById(String nodeChecklistId);

  ResultInSideDto insertFileMrNodeChecklistForVS(MrNodeChecklistFilesDTO filesDTO);

  String getIdSequenceMrNodeChecklist();

  ResultInSideDto insertMrNodeChecklistForVS(MrNodeChecklistDTO dtoInsert);

  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId);

  String updateWODischargeBattery(List<MrNodesDTO> listMrNodeDTO);

  Boolean checkWO(String wo_id);

  ResultDTO updateMrAndWoCDBattery(String woId, Date recentDischargeCd, String nodeName,
      String nodeIp);

  /**
   * @author Dunglv3
   */
  ResultDTO createMrDTO(MrDTO mrDTO, UsersDTO usersDTO, List<MrWoTempDTO> listMrWoTempDTO);

  boolean checkNodeStatusByWo(String woId);

  ResultInSideDto delete(Long mrId);

  boolean updateMrState(String mrId, String state, ResultDTO res);

  ResultInSideDto insertMrHis(MrHisDTO mrHisDTO);

  ResultDTO insertMrScheduleTelHis(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  List<MrScheduleTelDTO> getByMrId(String mrId);

  ResultInSideDto deleteMrScheduleTel(Long id);

  MrDeviceDTO findDetailById(Long deviceId);

  ResultInSideDto updateMrDeviceDTO(MrDeviceDTO dtoUpdate);

  //tiennv bo sung ham for vsmart
  List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId);
}
