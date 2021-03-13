package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpInsideDTO;
import com.viettel.gnoc.wo.model.CfgWoTickHelpEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWoTickHelpRepository {

  ResultInSideDto add(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO);

  List<CfgWoTickHelpEntity> search(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO);

  List<CfgWoTickHelpInsideDTO> searchEntity(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO);

  List<CfgWoTickHelpEntity> searchTwoParam(CfgWoTickHelpInsideDTO toInsideDto);
}
