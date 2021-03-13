package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusRoleDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OdChangeStatusRoleRepository {

  ResultInSideDto addList(OdChangeStatusDTO odChangeStatusDTO);

  Long insertOrUpdate(OdChangeStatusRoleDTO odCfgBusinessDTO);

  List<OdChangeStatusRoleDTO> getListByOdChangeStatusId(Long odChangeStatusId);

  ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId);
}
