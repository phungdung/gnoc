package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsUserDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgBusinessCallSmsRepository {

  Datatable getListCfgBusinessCallSmsDTO(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  ResultInSideDto add(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  ResultInSideDto edit(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  CfgBusinessCallSmsDTO getDetail(Long id);

  ResultInSideDto delete(Long id);

  List<CfgBusinessCallSmsDTO> getListDataExport(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  List<WoCdGroupInsideDTO> getListWoCdGroupCBB();

  ResultInSideDto addUser(CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO);

  ResultInSideDto editUser(CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO);

  ResultInSideDto deleteUser(Long id);

  CfgBusinessCallSmsUserDTO ckeckUserExist(Long cfgBusinessId, Long userId);

  CfgBusinessCallSmsDTO ckeckCfgBusinessCallSmsExist(Long cfgTypeId, Long cdId, Long cfgLevel);

  List<CfgBusinessCallSmsUserDTO> getListUserBycfgBusinessId(Long cfgBusinessId);
}
