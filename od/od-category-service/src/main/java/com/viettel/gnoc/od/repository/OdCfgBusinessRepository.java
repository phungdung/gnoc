package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface OdCfgBusinessRepository {

  ResultInSideDto add(OdChangeStatusDTO odChangeStatusDTO);

  Long insertOrUpdate(OdCfgBusinessDTO odCfgBusinessDTO);

  List<OdCfgBusinessDTO> getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO);

  String delete(Long id);

  int deleteList(List<Long> ids);

  ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId);

  List<String> getListSequense(String seq, int size);

  String deleteLocaleList(List<Long> ids);

  String checkConstraint(List<Long> lstCondition);

  OdCfgBusinessDTO findOdCfgBusinessById(Long odCfgBusinessId);
}
