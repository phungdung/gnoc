package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface CfgTroubleCallIpccRepository {

  BaseDto sqlSearch(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO);

  Datatable getListCfgTroubleCallIpccDTO(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO);

  CfgTroubleCallIpccDTO getDetailById(Long id);

  ResultInSideDto insertOrUpdateCfgTroubleCallIpcc(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO);

  ResultInSideDto deleteCfgTroubleCallIpcc(Long id);

}
