package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRoleDataRepository {

  CfgRoleDataDTO getConfigByDto(CfgRoleDataDTO cfgRoleDataDTO);

  Datatable onSearchCfgRoleData(CfgRoleDataDTO dto);

  ResultInSideDto insertCfgRoleData(CfgRoleDataDTO dto);

  ResultInSideDto updateCfgRoleData(CfgRoleDataDTO dto);

  CfgRoleDataDTO findCfgRoleDataById(Long id);

  String deleteCfgRoleData(Long id);

  List<CfgRoleDataDTO> onSearchExport(CfgRoleDataDTO dto);

  CfgRoleDataDTO checkCreateExit(CfgRoleDataDTO dto);

  ResultInSideDto checkExisted(String userName, String system, String id);
}
