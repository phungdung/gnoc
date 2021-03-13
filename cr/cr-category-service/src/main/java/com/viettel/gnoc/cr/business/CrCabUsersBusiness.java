package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CrCabUsersBusiness {

  ResultInSideDto insertCrCabUsers(CrCabUsersDTO crCabUsersDTO);

  ResultInSideDto updateCrCabUsers(CrCabUsersDTO crCabUsersDTO);

  ResultInSideDto deleteCrCabUsers(Long id);

  ResultInSideDto deleteListCrCabUsers(List<CrCabUsersDTO> crCabUsersDTOS);

  Datatable getAllInfoCrCABUsers(CrCabUsersDTO crCabUsersDTO);

  List<UnitDTO> getAllUserInUnitCrCABUsers(Long deptId, Long userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode);

  List<ItemDataCRInside> getListImpactSegmentCBB();

  ResultInSideDto importData(MultipartFile uploadfile);

  CrCabUsersDTO findById(Long id);

  File exportData(CrCabUsersDTO crCabUsersDTO) throws Exception;

  File getTemplate() throws Exception;
}
