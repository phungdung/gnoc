package com.viettel.gnoc.mr.repository;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import java.util.List;
import org.hibernate.Session;

/**
 * @author tripm
 * @version 2.0
 * @since 18/06/2020 14:05:00
 */
public interface MrNodesRepository {

  List<MrNodesDTO> getWoCrNodeList(String woId, String crId);

  ResultInSideDto updateWoStatus(String woId, String crId, String nodeCode, String status, String comment, boolean completeWo);

  ResultInSideDto delete(Long Id);

}
