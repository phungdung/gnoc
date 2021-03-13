package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

/**
 * @author NamTN
 */
public interface CfgWhiteListIpBusiness {

  Datatable getListDataSearchWeb(CfgWhiteListIpDTO dto);

  ResultInSideDto add(CfgWhiteListIpDTO coCfgWhiteListIpDTO);

  CfgWhiteListIpDTO getDetailById(Long id);

  ResultInSideDto edit(CfgWhiteListIpDTO coCfgWhiteListIpDTO);

  ResultInSideDto delete(Long id);

  CfgWhiteListIpDTO checkIpSystemName(String userName);
}
