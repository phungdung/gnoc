package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CfgMapNetworkLevelIncBusinessImpl implements CfgMapNetworkLevelIncBusiness {

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Override
  public List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return ttServiceProxy.getListCfgMapNetLevelIncTypeDTO(cfgMapNetLevelIncTypeDTO);
  }

  @Override
  public Datatable getListCfgMapNetLevelIncTypeDatatable(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return ttServiceProxy.getListCfgMapNetLevelIncTypeDatatable(cfgMapNetLevelIncTypeDTO);
  }

  @Override
  public ResultInSideDto updateCfgMapNetLevelIncType(
      @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return ttServiceProxy.updateCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
  }

  @Override
  public ResultInSideDto deleteCfgMapNetLevelIncType(Long id) {
    return ttServiceProxy.deleteCfgMapNetLevelIncType(id);
  }

  @Override
  public CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id) {
    return ttServiceProxy.findCfgMapNetLevelIncTypeById(id);
  }

  @Override
  public ResultInSideDto insertCfgMapNetLevelIncType(
      @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return ttServiceProxy.insertCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
  }

  @Override
  public Map<?, ?> netWorkRiskList() {
    return ttServiceProxy.netWorkRiskList();
  }

  @Override
  public Map<?, ?> netWorkLevelList() {
    return ttServiceProxy.netWorkLevelList();
  }
}
