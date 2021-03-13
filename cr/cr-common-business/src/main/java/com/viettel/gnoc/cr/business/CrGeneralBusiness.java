package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import java.util.List;

public interface CrGeneralBusiness {

  List<ItemDataCRInside> getListSubcategoryCBB();

  List<ItemDataCRInside> getListImpactSegmentCBB();

  List<ItemDataCRInside> getListImpactAffectCBB();

  List<ItemDataCRInside> getListAffectedServiceCBB(Long form);

  List<ItemDataCRInside> getListDutyTypeCBB(CrImpactFrameInsiteDTO form);

  List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(CrInsiteDTO crDTO);

  List<ItemDataCRInside> getListActionCodeByCode(String code, String locale);

  List<UsersInsideDto> actionGetListUser(String deptId, String userId, String userName,
      String fullName,
      String staffCode, String deptName, String deptCode, String isAppraise);

  List<ItemDataCRInside> getListReturnCodeByActionCode(Long actionCode, String locale);

  List<ItemDataCRDTO> getCreatedBySys(String crId);

  List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto);

  List<UserCabCrForm> getListUserCab(String impactSegmentId, String executeUnitId);

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  ResultInSideDto insertCrCreatedFromOtherSystem(CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO);

  // anhlp add
  ObjResponse doLogin(String versionApp, String locale, String userName, String password);

  // anhlp add
  void saveLogAction(String userName, String desc);

  // anhlp add
  ObjResponse getUserInfoForMobile(String userName, String locale);

  // anhlp add
  void insertSession(String userId, String sessionId);

  // anhlp add
  ObjResponse doLoginV2(String versionApp, String locale, String userName, String password);

  // anhlp add
  void insertSessionV2(String userId, String unitId, String sessionId);

  List<ItemDataCR> getListScopeOfUserForAllRole(CrDTO crDTO, String locale);

  List<ItemDataCR> getListImpactSegmentCBBForServiceV2(String locale);

  List<ItemDataCR> getListSubcategoryCBBForServiceV2(String locale);

  List<UsersDTO> actionGetListUserForService(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise);

  List<ItemDataCR> getListReturnCodeByActionCodeForService(String actionCode, String locale);

  List<ItemDataCR> getListAffectedServiceCBB(Object form, String locale);

  List<ItemDataCR> getListImpactAffectCBB(Object form, String locale);

  List<ItemDataCR> getListDutyTypeCBB(CrImpactFrameDTO form, String locale);

  List<ItemDataCR> getListDeviceTypeCBB(Object form, String locale);

  List<ItemDataCR> getListDeviceTypeByImpactSegmentCBB(CrDTO form, String locale);

  List<ItemDataCR> getListActionCodeByCodeForService(String code, String locale);

  ResultInSideDto loadSearchTypeByRole();

  List<ItemDataCR> getCreatedBySysMobile(String crId);
}

