package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import com.viettel.gnoc.mr.repository.MrImpactedNodesRespository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrImpactedNodesBusinessImpl implements MrImpactedNodesBusiness {

  @Autowired
  MrImpactedNodesRespository mrImpactedNodesRespository;

  @Override
  public ResultInSideDto deleteImpactNodeByMrId(String mrId) {
    return mrImpactedNodesRespository.deleteImpactNodeByMrId(mrId);
  }

  @Override
  public String insertOrUpdateListMrImpactedNodes(List<MrImpactedNodesDTO> mrImpactedNodesDTO) {
    return mrImpactedNodesRespository.insertList(mrImpactedNodesDTO);
  }

  @Override
  public List<MrImpactedNodesDTO> getImpactedNodesByMrId(String mrId) {
    return mrImpactedNodesRespository.getImpactedNodesByMrId(mrId);
  }
}
