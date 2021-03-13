package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.MessageForm;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsDBRepository {

  List<MessageForm> getListUsersByUserId(List<Long> listUserId,
      boolean getManager);

  void actionInsertIntoMess(MessageForm messageForm);

  ResultDTO actionSendSmsIncaseOfCreateCROrUpdateCR(ResultDTO actionResult,
      CrInsiteDTO crDTO);

  void actionSendSmsIncaseOfApproveCR(Long actionType, CrInsiteDTO crDTO,
      List<CrApprovalDepartmentInsiteDTO> listLowerLevelDept, boolean isLastDepartment);

  void actionSendSms(Long actionType, CrInsiteDTO crDTO, CrInsiteDTO crDTOtoSendSMS);

  String sendSMSToLstUserConfig(String crId, String contentType);

  String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode);

  CrInsiteDTO getCrById(Long crId);
}
