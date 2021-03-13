package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CrImpactedNodesBusiness {

  ResultInSideDto actionImportAndGetNetworkNodeV2(MultipartFile fileImport,
      String nationCode, int type, List<CrImpactedNodesDTO> lstImpactedNodes,
      List<CrAffectedNodesDTO> lstAffectedNodes);

  boolean validateImportedFile(List<Object[]> lstHeader);

  List<InfraDeviceDTO> getListInfraDeviceIpByListIP(List<InfraDeviceDTO> listIp);

  List<InfraDeviceDTO> getListInfraDeviceIpV2(List<String> listIp, String nationCode);

  List<InfraDeviceDTO> getListDeviceByListDevice(List<InfraDeviceDTO> listDevice,
      String nationCode);

  File exportData(List<CrImpactedNodesDTO> lstExport) throws Exception;

  List<CrImpactedNodesDTO> getLisNodeOfCR(CrImpactedNodesDTO dto);

  List search(CrImpactedNodesDTO tForm, int start, int maxResult, String sortType,
      String sortField);

  List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(CrImpactedNodesDTO crImpactedNodesDTO);

  List<CrImpactedNodesDTO> getLisNodeOfCRForProxy(CrImpactedNodesDTO dto);

  List<CrImpactedNodesDTO> getLisNodeOfCRForOutSide(Long crId, String crCreatedDateStr,
      String earlierStartTimeStr, String nodeType, String saveType);

  List<CrImpactedNodesDTO> onSearch(CrImpactedNodesDTO tDTO, int start, int maxResult,
      String sortType, String sortField);

  List<CrImpactedNodesDTO> getImpactedNodes(Date startDate, Date earlierStartTime, String type,
      String deviceCode, String deviceName, String ip);
}
