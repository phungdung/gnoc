package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceInfo;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrDetailInfoDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrForNocProDTO;
import com.viettel.gnoc.cr.dto.MiniCrDTO;
import com.viettel.gnoc.cr.dto.MiniImpactedNode;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.Date;
import java.util.List;

public interface CrForNocProRepository {

  List<Long> getNodeIpId(String deviceCode, String deviceName, String ip, String nationCode);

  List<MiniCrDTO> getCrByStateAndActiveTime(Long state, Date activeTime);

  List<CrForNocProDTO> getCrFromImpactNode(List<Long> crIds, List<List<Long>> listIpIds,
      Date maxTime, Date minTime);

  List<CrForNocProDTO> getCrFromAffectNode(List<Long> crIds, List<List<Long>> listIpIds,
      Date maxTime, Date minTime);

  List<CrForNocProDTO> getCrByImpactSegment(Long state, String impactSegmentCode, Date activeTime);

  List<CrDetailInfoDTO> getListCrDetailInfoDTO(List<Long> stateList, Date earliestTime,
      Date latestTime, String nocType);

  List<MiniImpactedNode> getImpactedNodeByCrIdsV2(List<List<Long>> crIds, Date startTime,
      Date endTime);

  List<MiniImpactedNode> getAffectedNodeByCrIdsV2(List<List<Long>> crIds, Date startTime,
      Date endTime);

  List<CrAlarmDTO> getListAlarm(List<List<Long>> crIds, Date startTime, Date endTime);

  List<CrAffectedServiceInfo> getListAffectService(List<List<Long>> crIds, Date startTime,
      Date endTime);

  UsersDTO getUserByUserName(String username);

  WorkLogDTO getWorkLog(String crId);

  List<CrFilesAttachDTO> getFileAttachCrIdsV2(List<List<Long>> crIds, Date startTime, Date endTime);
}
