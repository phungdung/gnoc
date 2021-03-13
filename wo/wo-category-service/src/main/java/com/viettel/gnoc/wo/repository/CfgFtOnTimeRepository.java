package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgFtOnTimeRepository {

  List<UsersInsideDto> getListUserByCdGroup(String cdGroupId);

  Datatable onSearch(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  List<CfgFtOnTimeDTO> getListCfgFtOnTimeDTO(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  ResultInSideDto insertOrUpdate(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  CfgFtOnTimeDTO findById(Long id);

  boolean isDupplicate(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  ResultInSideDto delete(Long id);

}
