package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.mr.repository.MrNodesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tripm
 * @version 2.0
 * @since 18/06/2020 16:40:00
 */
@Service
@Transactional
@Slf4j
public class MrNodesBusinessImpl implements MrNodesBusiness {

  @Autowired
  MrNodesRepository mrNodesRepository;

  @Override
  public void updateStatus(MrNodesDTO nodes, boolean completeWo) {
    try {
      mrNodesRepository
          .updateWoStatus(nodes.getWoId(), nodes.getCrId(), nodes.getNodeCode(), nodes.getStatus(),
              nodes.getComments(), completeWo);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public ResultInSideDto delete(Long Id) {
    return null;
  }

  @Override
  public ResultInSideDto updateWoStatus(String woId, String crId, String nodeCode, String status,
      String comment, boolean completeWo) {
    return mrNodesRepository
        .updateWoStatus(woId, crId, nodeCode, status,
            comment, completeWo);
  }
}
