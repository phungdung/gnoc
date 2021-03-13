package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;

/**
 * @author tripm
 * @version 2.0
 * @since 18/06/2020 16:40:00
 */
public interface MrNodesBusiness {

  void updateStatus(MrNodesDTO nodes, boolean completeWo);

  ResultInSideDto delete(Long Id);

  ResultInSideDto updateWoStatus(String woId, String crId, String nodeCode, String status, String comment, boolean completeWo);

}
