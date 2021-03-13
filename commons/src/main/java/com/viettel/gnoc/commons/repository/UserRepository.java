package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@Repository
public interface UserRepository {

  UsersEntity getUserByUserId(Long userId);

  UsersEntity getUserByUserName(String userName);

  UsersEntity getUserByUserNameCheckDupblicate(String userName);

  UsersInsideDto getUserDTOByUserNameInnerJoint(String userName);

  List<UsersInsideDto> getListUsersByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType,
      String sortFieldList);

  Datatable getListUsersDTO(UsersInsideDto dto);

  List<UsersInsideDto> getListUsersDTOS(UsersInsideDto dto);

  Datatable getListUsersByList(UsersInsideDto dto);

  List<UsersInsideDto> getListUsersDTO(String input, String type);

  UsersInsideDto getUserByStaffCode(String staffCode);

  UsersInsideDto getUserDTOByUserName(String userName);

  List<UsersInsideDto> getListUsersByListUserId(List<Long> ids);

  Double getOffsetFromUser(Long userId);

  Double getOffsetFromUser(String userName);

  ResultInSideDto updateUserTimeZone(String userId, String timeZoneId);

  ResultInSideDto updateUserLanguage(String userId, String languageId);

  ResultInSideDto updateUserApprove(UsersInsideDto usersInsideDto);

  boolean isManagerOfUnits(Long userId);

  UsersEntity getUserByUserIdCheck(Long userId);

  Long getUserId(String username);

  UsersDTO getUnitNameByUserName(String username);

  String getUserName(Long userId);

  String checkAccountSubAdmin(String username);

  List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit);

  List<UsersInsideDto> getListUserDTOByuserName(String userName);

  Long getLstUserOfUnitByRole(Long unitId, String roleCode);

  UsersDTO getUserInfo(String userName, String staffCode);

  List<Users> getListUserOfUnit(Long unitId);

  boolean checkRoleOfUser(String roleCode, Long userId);

  List<UsersInsideDto> getUsersByRoleCode(String roleCode);

  boolean checkAccountHaveRole(UsersInsideDto dto);

  Users getUserModelInfo(String userName, String staffCode);

  String getUnitParentForApprove(String type, String unitId);

  List<ImpactSegmentDTO> getImpactSegment(String system, String active);

  ResultInSideDto deleteUser(Long userId);

  ResultInSideDto addUser(UsersInsideDto usersInsideDto);

  UsersInsideDto getUserDetaiById(Long id);

  ResultInSideDto updateUser(UsersInsideDto usersInsideDto);

  List<UsersInsideDto> listUserByDTO(UsersInsideDto usersInsideDto);

  ResultInSideDto insertOrUpdateUser(UsersInsideDto usersInsideDto);

  Boolean checkStaffCode(String staffCode);

  UnitEntity getUnitByCode(String unitCode);

  Datatable getListUserHistory(UserUpdateHisDTO userUpdateHisDTO);

  UserUpdateHisDTO findUserHistoryById(Long hisId);

  List<UsersInsideDto> getLstUsersByUserNameOrStaffCode(List<String> lstUserName,
      List<String> lstStaffCode);

  //trungduong them ham su dung o webservice
  List<UsersDTO> getUserDTO();

  UsersDTO getUserDTOByUsernameLower(String username);

  List<UsersInsideDto> getListUserByUnitId(Long unitId);

  UserTokenGNOC getUserInfor(String userName);

  List<UsersInsideDto> search(UsersInsideDto usersInsideDto, int rowStart, int maxRow,
      String sortType,
      String sortFieldList);
}
