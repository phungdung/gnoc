package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleMopRepository {

  ResultInSideDto insertTroubleMop(TroubleMopInsiteDTO troubleMopDTO);

  Datatable getListTroubleMopDTO(TroubleMopInsiteDTO troubleMopDTO);

  TroubleMopInsiteDTO findById(Long id);

  Datatable getListTroubleMopDtDTO(TroubleMopDtInSideDTO troubleMopDTO);

  String updateTroubleMop(TroubleMopInsiteDTO troubleMopDTO);

  List<TroubleMopInsiteDTO> getListTroubleMopByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);
}
