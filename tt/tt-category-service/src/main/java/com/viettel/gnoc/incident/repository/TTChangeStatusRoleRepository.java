package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TTChangeStatusRoleDTO;
import java.util.List;

public interface TTChangeStatusRoleRepository {

  List<TTChangeStatusRoleDTO> getListRoleByTTChangeStatusId(Long ttChangeStatusId);

  ResultInSideDto deleteListTTChangeStatusRole(Long ttChangeStatusId);

  ResultInSideDto insertTTChangeStatusRole(TTChangeStatusRoleDTO ttChangeStatusRoleDTO);
}
