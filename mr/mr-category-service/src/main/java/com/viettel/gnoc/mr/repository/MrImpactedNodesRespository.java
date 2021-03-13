package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrImpactedNodesRespository {

  String insertList(List<MrImpactedNodesDTO> mrImpactedNodesDTO);

  ResultInSideDto deleteImpactNodeByMrId(String mrId);

  List<MrImpactedNodesDTO> getImpactedNodesByMrId(String mrId);
}
