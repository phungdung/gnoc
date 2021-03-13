package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgSupportCaseTestRepository {


  ResultInSideDto add(CfgSupportCaseTestDTO cfgSupportCaseTestDTO);

  ResultInSideDto edit(CfgSupportCaseTestDTO cfgSupportCaseTestDTO);

  ResultInSideDto delete(Long id);

  CfgSupportCaseTestDTO checkCfgSupportCaseTestExist(Long cfgSuppportCaseId, String testCaseName,
      Long fileRequired);

  List<CfgSupportCaseTestDTO> getListCfgSupportCaseTestId(Long cfgSuppportCaseId);
}
