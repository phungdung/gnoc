package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWoHighTempRepository {

  Datatable onSearch(CfgWoHighTempDTO dto);

  ResultInSideDto insertOrUpdate(CfgWoHighTempDTO dto);

  CfgWoHighTempDTO findById(Long id);

  ResultInSideDto delete(Long id);

  List<CfgWoHighTempDTO> onSearchExport(CfgWoHighTempDTO dto);

  boolean checkExisted(Long reasonLv1, Long reasonLv2, Long actionId, String id);

  List<DataItemDTO> getListPriority();

}
