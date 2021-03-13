package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
public interface UserBusiness {

  UsersEntity getUserByUserId(Long userId);

  UsersEntity getUserByUserName(String userName);

//  ResultInSideDto updateUserApprove(Boolean approve, Long userId, String content);

  ResultInSideDto approveUser(UsersInsideDto usersInsideDto);


  ResultInSideDto refuseUser(UsersInsideDto usersInsideDto);

  List<UsersInsideDto> getListUsersByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType,
      String sortFieldList);

  Datatable getListUsersDTO(UsersInsideDto dto);

  Datatable getListUsersApproveDTO(UsersInsideDto dto);

  File getApproveTemplate() throws Exception;

  ResultInSideDto importApproveUsers(MultipartFile multipartFile) throws Exception;

  File exportApproveUsersData(UsersInsideDto usersInsideDto) throws Exception;

  UsersInsideDto getUserDetailById(Long id);

  List<UsersInsideDto> getListUsersDTOS(UsersInsideDto dto);

  Datatable getListUsersByList(UsersInsideDto dto);

  UsersInsideDto getUserDTOByUserName(String userName);

  UsersInsideDto getUserDTOByUserNameInnerJoint(String userName);

  List<UsersInsideDto> getListUsersByListUserId(List<Long> ids);

  Double getOffsetFromUser(Long userId);

  ResultInSideDto updateUserTimeZone(String userId, String timeZoneId);

  ResultInSideDto updateUserLanguage(String userId, String languageId);

  List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit);

  UsersDTO getUserInfo(String userName, String staffCode);

  UsersInsideDto getUserInfor();

  ResultInSideDto updateUserInfor(UsersInsideDto usersInsideDto);

  Users getUserModelInfo(String userName, String staffCode);

  List<ImpactSegmentDTO> getImpactSegment(String system, String active);

  ResultInSideDto deleteUser(Long userId);

  ResultInSideDto addUser(UsersInsideDto usersInsideDto);

  UsersInsideDto getUserDetaiById(Long id);

  UsersInsideDto checkRole();

  ResultInSideDto updateUser(UsersInsideDto usersInsideDto);

  UsersDTO getUnitNameByUserName(String username);

  File exportData(UsersInsideDto usersInsideDto) throws Exception;

  File getTemplate() throws IOException;

  ResultInSideDto importData(MultipartFile fileImport);

  Datatable getListUserHistory(UserUpdateHisDTO userUpdateHisDTO);

  UserUpdateHisDTO findUserHistoryById(Long hisId);

  //trungduong them ham su dung o webservice
  List<UsersDTO> getUserDTO();

  boolean checkRoleOfUser(String roleCode, Long userId);

  List<UsersInsideDto> getListUserByUnitId(Long unitId);

  UserTokenGNOC getUserInfor(String userName);

  List<UsersDTO> search(UsersDTO usersDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);
}
