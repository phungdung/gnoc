package com.viettel.gnoc.cr.repository;

import com.viettel.aam.AppGroup;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrGeneralRepository {

  List<ItemDataCRInside> getListActionCodeById(String id, String locale);

  CrInsiteDTO getCRByIDIn30Day(String crId);

  List<ItemDataCRInside> getListSubcategoryCBB(String locale);

  List<ItemDataCRInside> getListImpactSegmentCBB(String locale);

  List<ItemDataCRInside> getListImpactAffectCBB(String locale);

  List<ItemDataCRInside> getListAffectedServiceCBB(Long form, String locale);

  List<ItemDataCRInside> getListDutyTypeCBB(CrImpactFrameInsiteDTO form, String locale);

  List<ItemDataCRInside> getListDeviceType(CrInsiteDTO crDTO, String locale);

  List<ItemDataCRInside> getListScopeOfUserForAllRole(CrInsiteDTO crDTO, String locale);

  List getNetworkNodeFromQLTN(List<AppGroup> lstSelected);

  List<ItemDataCRInside> getListActionCodeByCode(String code, String locale);

  List<UsersInsideDto> actionGetListUser(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise);

  List<ItemDataCRInside> getListReturnCodeByActionCode(Long actionCode, String locale);

  List<UserCabCrForm> getListUserCab(String impactSegmentId, String executeUnitId);

  List<ItemDataCRDTO> getCreatedBySys(String crId);

  List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto);

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  ImpactSegmentEntity findImpactSegmentById(Long id);

  String insertCrCreatedFromOtherSystem(CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO);

  UsersDTO getUserInfoForMobile(String username);

  public void insertSession(String userId, String sessionId);

  public void saveLogAction(String userName, String description);

  public void insertSessionV2(String userId, String unitId, String sessionId);

  List<ItemDataCR> getListScopeOfUserNewForServiceV2(Long deptId, String locale);

  List<ItemDataCR> getListScopeOfUserOfCabOrZ78ForServiceV2(CrDTO crDTO, String locale);

  List<ItemDataCR> getListImpactSegmentCBBForServiceV2(String locale);

  List<ItemDataCR> getListSubcategoryCBBForServiceV2(String locale);

  List<UsersDTO> actionGetListUserForService(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise);

  List<ItemDataCR> getListReturnCodeByActionCodeForService(Long actionCode, String locale);

  List<ItemDataCR> getListAffectedServiceCBBForService(Object form, String locale);

  List<ItemDataCR> getListImpactAffectCBBForService(Object form, String locale);

  List<ItemDataCR> getListDutyTypeCBB(CrImpactFrameDTO form, String locale);

  List<ItemDataCR> getListDeviceTypeCBB(Object form, String locale);

  List<ItemDataCR> getListDeviceType(CrDTO crDTO, String locale);

  List<ItemDataCR> getListActionCodeByCodeForService(String code, String locale);

  List<ItemDataCRInside> getListScopeOfUserNew(Long deptId, String locale);

  List<CrCabUsersDTO> getListUserCab(String impactSegmentId);

  List<ItemDataCR> getCreatedBySysMobile(String crId);
}
