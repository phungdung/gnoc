package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrImpactedNodesRepository {

  String saveListDTONoIdSession(List<CrImpactedNodesDTO> obj, Date crCreateTime);

  void deleteImpactNodeByCrId(String crId, Date crCreateDate);

  List<InfraDeviceDTO> getListInfraDeviceIpByListIP(List<InfraDeviceDTO> listIp);

  List<InfraDeviceDTO> getListInfraDeviceIpV2(List<String> listIp, String nationCode);

  List<InfraDeviceDTO> getListDeviceByListDevice(List listDevice, String nationCode);

  List<CrImpactedNodesDTO> getImpactedNodesByCrIdV2(Long crId, Date startDate,
      Date earlierStartTime, String type);

  List<CrImpactedNodesDTO> getImpactedNodesByCrId(Long crId, Date startDate,
      Date earlierStartTime, String type, String deviceCode, String deviceName, String ip);

  List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(CrImpactedNodesDTO crImpactedNodesDTO);

  List<CrImpactedNodesDTO> onSearch(CrImpactedNodesDTO tDTO, int start, int maxResult,
      String sortType, String sortField);

}
