package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import java.util.List;

public interface MrImpactedNodesBusiness {

  ResultInSideDto deleteImpactNodeByMrId(String mrId);

  String insertOrUpdateListMrImpactedNodes(List<MrImpactedNodesDTO> lst);

  List<MrImpactedNodesDTO> getImpactedNodesByMrId(String mrId);
}
