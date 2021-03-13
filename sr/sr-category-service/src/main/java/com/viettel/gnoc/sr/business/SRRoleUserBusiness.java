package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SRRoleUserBusiness {

  Datatable getListSRRoleUserPage(SRRoleUserInSideDTO srRoleUserDTO);

  List<SRRoleUserInSideDTO> getListSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto insert(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto update(SRRoleUserInSideDTO srRoleUserDTO);

  ResultInSideDto delete(Long roleUserId);

  SRRoleUserInSideDTO getDetail(Long roleUserId);

  File getTemplate() throws Exception;

  File exportData(SRRoleUserInSideDTO srRoleUserDTO) throws Exception;

  ResultInSideDto importData(MultipartFile uploadfile) throws Exception;

  List<SRRoleUserInSideDTO> getListUser(String unitId, String country, String username);
}
