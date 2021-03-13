package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTO;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTOSearch;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.model.CrRoleDeptEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrRolesDeptRepository {

  List<CrRolesDTO> getListCrRolesDTO(CrRolesDTO crRolesDTO);

  ResultInSideDto insertCrRoles(CrRolesDTO crRolesDTO);

  ResultInSideDto updateCrRoles(CrRolesDTO crRolesDTO);

  CrRolesDTO findCrRolesDTOById(Long id);

  ResultInSideDto deleteRoles(Long id);

  ResultInSideDto insertRoleDept(CrRoleDeptDTO crRoleDeptDTO);

  ResultInSideDto updateRoleDept(CrRoleDeptDTO crRoleDeptDTO);

  ResultInSideDto deleteRoleDept(Long id);

  CrRoleDeptEntity findCrRoleDeptEntityById(Long id);

  Datatable getListRoleDept(CrRoleDeptDTOSearch crRoleDeptDTOSearch);
}
