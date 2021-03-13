package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgSupportCaseRepository {


  ResultInSideDto add(CfgSupportCaseDTO cfgSupportCaseDTO);

  ResultInSideDto edit(CfgSupportCaseDTO cfgSupportCaseDTO);

  CfgSupportCaseDTO getDetail(Long id);

  ResultInSideDto deleteCaseAndCaseTest(Long id);

  Datatable getListCfgSupportCaseDTONew(CfgSupportCaseDTO cfgSupportCaseDTO);

  List<CfgSupportCaseDTO> getListCfgSupportCaseExport(CfgSupportCaseDTO cfgSupportCaseDTO);
}
