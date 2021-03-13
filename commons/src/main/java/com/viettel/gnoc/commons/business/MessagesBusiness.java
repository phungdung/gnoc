package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.List;

/**
 * @author TungPV
 */
public interface MessagesBusiness {

  ResultInSideDto insertOrUpdateCommon(MessagesDTO odDTO);

  ResultInSideDto insertOrUpdateWfm(MessagesDTO odDTO);

  ResultInSideDto insertSMSMessageForPm(String content, String pmGroup,
      ProblemsInsideDTO problemsInsideDTO);

  ResultInSideDto insertSMSMessageKEDBInst(String content, List<UserSmsDTO> lstUserSms,
      String smsAdd);

  ResultInSideDto insertMessageForUser(String content, String alias, List<UsersInsideDto> lst);

  void insertSMSMessageForUser(String content, UsersInsideDto usersInsideDTO,
      ProblemsInsideDTO problemsInsideDTO);

  void insertSMSMessageForUnitAllUser(String content, String unitId, TroublesInSideDTO troublesDTO,
      ProblemsInsideDTO problemsInsideDTO);

  void insertMessageForUserCR(String content, UsersEntity usersEntity);

  String insertOrUpdateListMessages(List<MessagesDTO> lstMsg);

  String insertOrUpdateListMessagesWS(List<MessagesDTO> lstMsg);

  void insertSMSMessageForUserForPT(String content, UsersInsideDto usersInsideDTO,
      ProblemsInsideDTO problemsInsideDTO);
}
