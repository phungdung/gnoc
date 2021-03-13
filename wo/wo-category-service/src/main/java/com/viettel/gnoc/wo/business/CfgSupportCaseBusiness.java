package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import java.io.File;
import java.util.List;

public interface CfgSupportCaseBusiness {


  ResultInSideDto deleteCaseAndCaseTest(Long id);

  ResultInSideDto add(CfgSupportCaseDTO cfgSupportCaseDTO);

  ResultInSideDto edit(CfgSupportCaseDTO cfgSupportCaseDTO);

  CfgSupportCaseDTO getDetail(Long id);

  Datatable getListCfgSupportCaseDTONew(CfgSupportCaseDTO cfgSupportCaseDTO);

  File exportData(CfgSupportCaseDTO cfgSupportCaseDTO) throws Exception;

  List<CfgSupportCaseTestDTO> getListCfgSupportCaseTestId(Long cfgSuppportCaseId);
}
