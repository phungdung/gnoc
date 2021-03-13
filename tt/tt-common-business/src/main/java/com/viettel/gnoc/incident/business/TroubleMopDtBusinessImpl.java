package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.EmailMessagesRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleMopDtRepository;
import com.viettel.gnoc.incident.repository.TroubleMopRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.pt.dto.EmailMessagesDTO;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TroubleMopDtBusinessImpl implements TroubleMopDtBusiness {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  TroubleMopDtRepository troubleMopDtRepository;

  @Autowired
  TroubleMopRepository troubleMopRepository;

  @Autowired
  TroublesRepository troublesRepository;

  @Autowired
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Autowired
  EmailMessagesRepository emailMessagesRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Override
  public ResultDTO insertTroubleMopDt(TroubleMopDtDTO troubleMopDtDTO) {
    log.debug("Request to insertTroubleMopDt: {}", troubleMopDtDTO);
    return troubleMopDtRepository.insertTroubleMopDt(troubleMopDtDTO);
  }

  @Override
  public ResultDTO updateDt(AuthorityDTO requestDTO, TroubleMopDtDTO troubleMopDtDTO)
      throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    String startTime = DateTimeUtils.getSysDateTime();
    ValidateAccount account = new ValidateAccount();
    String validateAcc = account.checkAuthenticate(requestDTO);
    if (validateAcc.equals(RESULT.SUCCESS)) {
      troubleMopDtDTO.setCreateTime(startTime);
      if (StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getDtId())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Dt Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getDtName())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Dt Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getState())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("State " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getNodes())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Nodes " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getResultDetail())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Result Detail " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      //insert file
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      UsersEntity usersEntity = userRepository.getUserByUserName(requestDTO.getUsername());
      UnitDTO unitToken = null;
      if (usersEntity != null) {
        unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
      }
      GnocFileDto gnocFileDto = new GnocFileDto();
      if (troubleMopDtDTO.getFileDocumentByteArray() != null
          && troubleMopDtDTO.getFileDocumentByteArray().length > 0) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, troubleMopDtDTO.getDtName(),
                troubleMopDtDTO.getFileDocumentByteArray(),
                null);
        String fullPathOld = FileUtils
            .saveUploadFile(troubleMopDtDTO.getDtName(), troubleMopDtDTO.getFileDocumentByteArray(),
                uploadFolder, null);
        troubleMopDtDTO.setPath(fullPathOld);
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(troubleMopDtDTO.getDtName());
        gnocFileDto.setCreateUnitId(usersEntity == null ? null : usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken == null ? null : unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity == null ? null : usersEntity.getUserId());
        gnocFileDto.setCreateUserName(requestDTO.getUsername());
        gnocFileDto.setCreateTime(new Date());
      }
      //update
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition
          .add(new ConditionBean("dtId", troubleMopDtDTO.getDtId(), "NAME_EQUAL", "NUMBER"));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<TroubleMopDtInSideDTO> lstTroubleMopDt = getListTroubleMopDtByCondition(lstCondition, 0,
          Integer.MAX_VALUE, "", "");
      if (lstTroubleMopDt == null || lstTroubleMopDt.isEmpty()) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Dt doesn't exist!");
        return resultDTO;
      }
      TroubleMopDtInSideDTO mopDtDTO = lstTroubleMopDt.get(0);
      mopDtDTO.setState(StringUtils.isStringNullOrEmpty(troubleMopDtDTO.getState()) ? null
          : Long.valueOf((troubleMopDtDTO.getState())));
      mopDtDTO.setNodes(troubleMopDtDTO.getNodes());
      mopDtDTO.setPath(troubleMopDtDTO.getPath());
      mopDtDTO.setResultDetail(troubleMopDtDTO.getResultDetail());
      gnocFileDto.setMappingId(mopDtDTO.getTroubleDtId());
      gnocFileDtos.add(gnocFileDto);
      troubleMopDtRepository.update(mopDtDTO);

      lstCondition = new ArrayList<>();
      lstCondition.add(new ConditionBean("troubleMopId", String.valueOf(mopDtDTO.getTroubleMopId()),
          "NAME_EQUAL", "NUMBER"));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<TroubleMopInsiteDTO> lstTroubleMop = troubleMopRepository
          .getListTroubleMopByCondition(lstCondition, 0, Integer.MAX_VALUE, "", "");
      TroubleMopInsiteDTO troubleMopDTO = lstTroubleMop.get(0);
      troubleMopDTO.setWorkLog(troubleMopDTO.getWorkLog() + "\n\r"
          + "Run DT " + troubleMopDtDTO.getDtName()
      );
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLE_MOP_DT,
              troubleMopDTO.getTroubleMopId(), gnocFileDtos);
      troubleMopRepository.updateTroubleMop(troubleMopDTO);

      if ("VMSA".equals(troubleMopDTO.getSystem()) && "0"
          .equals(troubleMopDtDTO.getState())) { //chay mop fail
        //upadte TT
        TroublesDTO dTO = new TroublesDTO();
        dTO.setTroubleId(String.valueOf(troubleMopDTO.getTroubleId()));
        TroublesInSideDTO troublesDTO = troublesRepository
            .getTroubleDTO(String.valueOf(troubleMopDTO.getTroubleId()), null, null, null, null,
                null, null);
        troublesDTO.setState(3L);
        troublesDTO.setLastUpdateTime(DateTimeUtils.convertStringToDateTime(startTime));
        troublesDTO.setAutoClose(0L);
        troublesRepository.updateTroubles(troublesDTO.toEntity());
        //ghi logs
        TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(
            null,
            I18n.getLanguage("common.btn.update") + " " + troublesDTO.getInsertSource() + " : "
                + I18n.getLanguage("update.tt.when.mop.fail"),
            DateTimeUtils.convertStringToDate(startTime), troublesDTO.getCreateUnitId(),
            troublesDTO.getCreateUserId(),
            I18n.getLanguage("incident.update"), troublesDTO.getTroubleId(),
            "VMSA", "VMSA", troublesDTO.getState(), troublesDTO.getTroubleCode(),
            I18n.getLanguage("incident.assign"), null, null);
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());
        // gui mail
        String content = I18n.getLanguage("content.email.when.mop.fail")
            .replaceAll("#mopCode#", troubleMopDtDTO.getDtName())
            .replaceAll("#troubleCode#", troublesDTO.getTroubleCode());
        String subject = I18n.getLanguage("subject.email.when.mop.fail")
            .replaceAll("#mopCode#", troubleMopDtDTO.getDtName())
            .replaceAll("#troubleCode#", troublesDTO.getTroubleCode());
        UnitDTO unit = new UnitDTO();
        unit.setUnitId(troublesDTO.getReceiveUnitId());
        List<UnitEntity> lstUnit = troublesRepository.getUnitByUnitDTO(unit);
        if (lstUnit != null && !lstUnit.isEmpty() && !StringUtils
            .isStringNullOrEmpty(lstUnit.get(0).getEmail()) && lstUnit.get(0).getEmail()
            .contains("@")) {
          EmailMessagesDTO emailMessagesDTO = new EmailMessagesDTO(null, content, null,
              lstUnit.get(0).getEmail(), startTime, null, "0", null, null, subject);
          emailMessagesRepository.insertOrUpdate(emailMessagesDTO);
        }

      }
      resultDTO.setKey(RESULT.SUCCESS);
    } else {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(validateAcc);
    }
    return resultDTO;
  }

  @Override
  public List<TroubleMopDtInSideDTO> getListTroubleMopDtByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return troubleMopDtRepository
        .getListTroubleMopDtByCondition(lstCondition, rowStart, maxRow, sortType, sortName);
  }
}
