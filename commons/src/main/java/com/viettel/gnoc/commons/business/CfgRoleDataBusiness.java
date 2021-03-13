package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CfgRoleDataBusiness {

  Datatable onSearchCfgRoleData(CfgRoleDataDTO dto);

  ResultInSideDto insertCfgRoleData(CfgRoleDataDTO dto);

  ResultInSideDto updateCfgRoleData(CfgRoleDataDTO dto);

  CfgRoleDataDTO findCfgRoleDataById(Long id);

  String deleteCfgRoleData(Long id);

  UsersEntity getUserByUserName(String username);

  File exportData(CfgRoleDataDTO CfgRoleDataDTO) throws Exception;

  ResultInSideDto importData(MultipartFile fileImport);

  File getTemplate() throws IOException;
}
