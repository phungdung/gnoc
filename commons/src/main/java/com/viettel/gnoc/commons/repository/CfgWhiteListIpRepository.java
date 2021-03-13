package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import org.springframework.stereotype.Repository;

/**
 * @author ITSOL
 */
@Repository
public interface CfgWhiteListIpRepository {

  Datatable getListDataSearchWeb(CfgWhiteListIpDTO dto);

  ResultInSideDto add(CfgWhiteListIpDTO coCfgWhiteListIpDTO);

  CfgWhiteListIpDTO getDetailById(Long id);

  ResultInSideDto edit(CfgWhiteListIpDTO coCfgWhiteListIpDTO);

  ResultInSideDto delete(Long id);

  CfgWhiteListIpDTO checkIpSystemName(String userName);
}
