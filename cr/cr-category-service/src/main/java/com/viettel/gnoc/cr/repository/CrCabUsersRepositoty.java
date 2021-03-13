package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrCabUsersRepositoty {

  ResultInSideDto insertCrCabUsers(CrCabUsersDTO crCabUsersDTO);

  ResultInSideDto updateCrCabUsers(CrCabUsersDTO crCabUsersDTO);

  ResultInSideDto deleteCrCabUsers(Long id);

  ResultInSideDto deleteListCrCabUsers(List<CrCabUsersDTO> crCabUsersDTOS);

  Datatable getAllInfoCrCABUsers(CrCabUsersDTO crCabUsersDTO);

  List<UnitDTO> getAllUserInUnitCrCABUsers(Long deptId,
      Long userId,
      String userName,
      String fullName,
      String staffCode,
      String deptName,
      String deptCode);

  List<ItemDataCRInside> getListImpactSegmentCBB();

  List<UnitDTO> getListUnitCrCABUsers(UnitDTO unitDTO);

  List<CrCabUsersDTO> getListUserFullName();

  List<CrCabUsersDTO> getListSegmentName();

  List<CrCabUsersDTO> getListUnitName();

  List<DataItemDTO> getLisFullNameMapUnitCab();

  CrCabUsersDTO findById(Long id);

  CrCabUsersDTO checkCrCabUsersExist(CrCabUsersDTO crCabUsersDTO);


}
