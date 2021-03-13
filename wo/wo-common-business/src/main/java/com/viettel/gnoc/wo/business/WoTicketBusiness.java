package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.AmiOneForm;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import java.util.List;

public interface WoTicketBusiness {

  List<CfgSupportForm> getListWoSupportInfo(String woCode);

  ResultDTO completeWorkHelp(String woCode, String userName, String worklog, String reasonCcId)
      throws Exception;

  List<AmiOneForm> getInfoAmiOne(List<String> lstAmiOneId);
}
