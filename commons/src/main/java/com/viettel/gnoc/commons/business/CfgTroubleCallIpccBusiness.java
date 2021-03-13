package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface CfgTroubleCallIpccBusiness {

  Datatable getListCfgTroubleCallIpccDTO(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO);

  CfgTroubleCallIpccDTO getDetailById(Long id);

  ResultInSideDto insertOrUpdateCfgTroubleCallIpcc(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO);

  ResultInSideDto deleteCfgTroubleCallIpcc(Long id);
}
