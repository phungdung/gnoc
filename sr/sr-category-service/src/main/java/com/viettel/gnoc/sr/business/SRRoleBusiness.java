package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SRRoleBusiness {

  Datatable getListSRRolePage(SRRoleDTO srRoleDTO);

  ResultInSideDto insert(SRRoleDTO srRoleDTO);

  ResultInSideDto update(SRRoleDTO srRoleDTO);

  ResultInSideDto delete(Long roleId);

  SRRoleDTO getDetail(Long roleId);

  ResultInSideDto importData(MultipartFile uploadfile) throws Exception;

  File getTemplate() throws Exception;

  File exportData(SRRoleDTO srRoleDTO) throws Exception;

  List<SRRoleDTO> getListSRRoleByLocationCBB(SRRoleDTO srRoleDTO);

  List<SRRoleDTO> getListSRRoleDTO(SRRoleDTO srRoleDTO);
}
