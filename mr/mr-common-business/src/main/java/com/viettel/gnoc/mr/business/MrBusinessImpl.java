package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.repository.MrNodesRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Service
@Transactional
@Slf4j
public class MrBusinessImpl implements MrBusiness {

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

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrNodesRepository mrNodesRepository;

  @Autowired
  MrNodesBusiness mrNodesBusiness;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(String woId, String mrNodeId) {
    List<MrNodeChecklistDTO> lstResult = mrRepository
        .getListMrNodeChecklistForPopUp_VS(woId, mrNodeId);
    for (MrNodeChecklistDTO dto : lstResult) {
      if (StringUtils.isStringNullOrEmpty(dto.getIsRequiredChecklist())) {
        dto.setIsRequiredChecklist("0");
      }
    }
    return lstResult;
  }

  @Override
  public ResultDTO updateMrNodeChecklistForPopUp_VS(
      List<MrNodeChecklistDTO> lstMrNodeChecklistDTO) throws Exception {
    ResultDTO res = new ResultDTO();
    String updateSuccess = "";
    String insertSuccess = "";
    String insertFileSuccess = "";
    Date date = new Date();
    try {

      for (MrNodeChecklistDTO obj : lstMrNodeChecklistDTO) {
        //TH update:
        if (!StringUtils.isStringNullOrEmpty(obj.getNodeChecklistId())) {
          //update NodeChecklist truoc
          if (StringUtils.isStringNullOrEmpty(obj.getMrNodeId())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.mrNodeId.notnull"));
            return res;
          }
          if (StringUtils.isStringNullOrEmpty(obj.getChecklistId())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.checklistId.notnull"));
            return res;
          }
          if (!"OK".equals(obj.getStatus()) && !"NOK".equals(obj.getStatus()) || StringUtils
              .isStringNullOrEmpty(obj.getStatus())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Status.valid"));
            return res;
          }
          if (!StringUtils.isStringNullOrEmpty(obj.getComments())) {
            if (obj.getComments().length() > 500) {
              res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                  I18n.getLanguage("mrService.Comments.valid"));
              return res;
            }
          }
          if (StringUtils.isStringNullOrEmpty(obj.getUpdatedUser())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Updateuser.notnull"));
            return res;
          } else if (obj.getUpdatedUser().length() > 100 && !StringUtils
              .isStringNullOrEmpty(obj.getUpdatedUser())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Updateuser.valid"));
            return res;
          } else {
            ResultInSideDto resultUpdateMrNodeChecklist = mrRepository
                .updateMrNodeChecklistForVS(obj);
            updateSuccess = resultUpdateMrNodeChecklist.getMessage();
            if (RESULT.SUCCESS.equals(updateSuccess)) {
              List<MrNodeChecklistFilesDTO> lstFile = obj.getLstFile();
              if (lstFile != null && lstFile.size() > 0) {
                List<MrNodeChecklistFilesDTO> lstFileOld = mrRepository
                    .getListFileMrNodeChecklist_VS(obj.getNodeChecklistId());
                if (lstFileOld != null && lstFileOld.size() > 0) {
                  try {
                    mrRepository.deleteFileFromMrNodeChecklistForVS(obj.getNodeChecklistId());
                  } catch (Exception e) {
                    log.error(e.getMessage());
                  }
                }
                MrNodeChecklistDTO dtoFindById = mrRepository
                    .findMrNodeChecklistById(obj.getNodeChecklistId()).get(0);
                String createdUser = dtoFindById.getCreatedUser();
                List<GnocFileDto> gnocFileDtoAdds = new ArrayList<>();
                for (MrNodeChecklistFilesDTO tmp : lstFile) {
                  byte[] fileByte = tmp.getFileContent();
                  String fullPathFtp = FileUtils
                      .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), ftpFolder, tmp.getFileName(),
                          fileByte, date);
                  String filePathOld = FileUtils
                      .saveUploadFile(tmp.getFileName(), fileByte,
                          tempFolder, date);
                  tmp.setNodeChecklistId(obj.getNodeChecklistId());
                  tmp.setFileName(FileUtils.getFileName(filePathOld));
                  tmp.setFilePathFtp(fullPathFtp);
                  tmp.setFilePath(filePathOld);
                  tmp.setCreatedUser(createdUser);

                  ResultInSideDto resultInsertMrNodeChecklistFile = mrRepository
                      .insertFileMrNodeChecklistForVS(tmp);
                  insertFileSuccess = resultInsertMrNodeChecklistFile.getKey();
                  if (RESULT.SUCCESS.equals(insertFileSuccess)) {
                    GnocFileDto gnocFileDto = new GnocFileDto();
                    gnocFileDto.setBusinessId(resultUpdateMrNodeChecklist.getId());
                    gnocFileDto.setFileName(FileUtils.getFileName(filePathOld));
                    gnocFileDto.setPath(tmp.getFilePathFtp());
                    gnocFileDto.setCreateTime(date);
                    gnocFileDto.setMappingId(resultInsertMrNodeChecklistFile.getId());
                    gnocFileDtoAdds.add(gnocFileDto);
                  }
                }
                gnocFileRepository
                    .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_NODE_CHECKLIST,
                        Long.valueOf(obj.getNodeChecklistId()),
                        gnocFileDtoAdds);

                res = new ResultDTO("1", RESULT.SUCCESS, RESULT.SUCCESS);

              } else {
                res = new ResultDTO("1", RESULT.SUCCESS, RESULT.FILE_IS_NULL);//that bai
              }
            } else {
              res = new ResultDTO("0", RESULT.FAIL, RESULT.FAIL);//that bai
            }
          }
        } else {
//          String idInsert = mrRepository.getIdSequenceMrNodeChecklist();
//          obj.setNodeChecklistId(idInsert);
          if (StringUtils.isStringNullOrEmpty(obj.getMrNodeId())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.mrNodeId.notnull"));
            return res;
          }
          if (StringUtils.isStringNullOrEmpty(obj.getChecklistId())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.checklistId.notnull"));
            return res;
          }
          if (!"OK".equals(obj.getStatus()) && !"NOK".equals(obj.getStatus()) || StringUtils
              .isStringNullOrEmpty(obj.getStatus())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Status.valid"));
            return res;
          }
          if (!StringUtils.isStringNullOrEmpty(obj.getComments())) {
            if (obj.getComments().length() > 500) {
              res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                  I18n.getLanguage("mrService.Comments.valid"));
              return res;
            }
          }
          if (StringUtils.isStringNullOrEmpty(obj.getCreatedUser())) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Createuser.notnull"));
            return res;
          } else if (obj.getCreatedUser().length() > 100) {
            res = new ResultDTO(RESULT.FAIL, RESULT.FAIL,
                I18n.getLanguage("mrService.Createuser.valid"));
            return res;
          } else {
            ResultInSideDto resultInsertMrNodeChecklist = mrRepository
                .insertMrNodeChecklistForVS(obj);
            Long nodeCheckListId = resultInsertMrNodeChecklist.getId();
            insertSuccess = resultInsertMrNodeChecklist.getMessage();
            if (RESULT.SUCCESS.equals(insertSuccess)) {
              List<MrNodeChecklistFilesDTO> lstFile = obj.getLstFile();
              if (lstFile != null && lstFile.size() > 0) {
                List<GnocFileDto> gnocFileDtoAdds = new ArrayList<>();
                for (MrNodeChecklistFilesDTO tmp : lstFile) {
                  if (StringUtils.isStringNullOrEmpty(tmp.getFileContent())) {
                    return new ResultDTO("0", RESULT.FAIL,
                        I18n.getLanguage("mrService.fileContent.notnull"));
                  }
                  if (StringUtils.isStringNullOrEmpty(tmp.getFileName())) {
                    return new ResultDTO("0", RESULT.FAIL,
                        I18n.getLanguage("mrService.fileName.notnull"));
                  }
                  tmp.setNodeChecklistId(nodeCheckListId.toString());

                  byte[] fileByte = tmp.getFileContent();
                  String fullPath = FileUtils
                      .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), ftpFolder, tmp.getFileName(),
                          fileByte, date);
                  String filePathOld = FileUtils
                      .saveUploadFile(tmp.getFileName(), fileByte,
                          tempFolder, date);
                  tmp.setFileName(FileUtils.getFileName(fullPath));
                  tmp.setFilePathFtp(fullPath);
                  tmp.setFilePath(filePathOld);
                  tmp.setCreatedUser(obj.getCreatedUser());

                  ResultInSideDto resultInsertMrNodeChecklistFile = mrRepository
                      .insertFileMrNodeChecklistForVS(tmp);
                  insertFileSuccess = resultInsertMrNodeChecklistFile.getKey();
                  if (RESULT.SUCCESS.equals(insertFileSuccess)) {
                    GnocFileDto gnocFileDto = new GnocFileDto();
                    gnocFileDto.setBusinessId(nodeCheckListId);
                    gnocFileDto.setPath(tmp.getFilePathFtp());
                    gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
                    gnocFileDto.setCreateTime(date);
                    gnocFileDto.setMappingId(resultInsertMrNodeChecklistFile.getId());
                    gnocFileDtoAdds.add(gnocFileDto);
                  }
                }
                gnocFileRepository
                    .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_NODE_CHECKLIST,
                        nodeCheckListId,
                        gnocFileDtoAdds);

                res = new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
              } else {
                res = new ResultDTO("1", RESULT.SUCCESS, RESULT.FILE_IS_NULL);//that bai
              }
            } else {
              res = new ResultDTO(RESULT.FAIL, RESULT.FAIL, RESULT.FAIL);//that bai
            }
          }
        }
      }
    } catch (
        Exception e) {
      log.error(e.getMessage());
      res = new ResultDTO(e.getMessage(), RESULT.FAIL, e.getMessage());
    }
    return res;
  }

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId) {
    return mrRepository.getListMrNodeChecklistForPopUp(woId, mrNodeId);
  }

  @Override
  public List<MrNodesDTO> getWoCrNodeList_VS(String woId, String crId) {
    if (StringUtils.isStringNullOrEmpty(woId) && StringUtils.isStringNullOrEmpty(crId)) {
      return null;
    }
    List<MrNodesDTO> lstNodes = mrNodesRepository.getWoCrNodeList(woId, null);
    if (lstNodes != null && lstNodes.size() > 0) {
      for (MrNodesDTO nodes : lstNodes) {
        List<MrNodeChecklistDTO> lstChecklist = getListMrNodeChecklistForPopUp(woId,
            nodes.getMrNodeId());
        if (lstChecklist != null && lstChecklist.size() > 0) {
          nodes.setIsChecklistExisted("1");
          int checkStatus = 1;
          for (MrNodeChecklistDTO checklistDTO : lstChecklist) {
            if ("NOK".equals(checklistDTO.getStatus())) {
              checkStatus = 2;
              break;
            } else if (StringUtils.isStringNullOrEmpty(checklistDTO.getStatus())) {
              checkStatus = 0;
              break;
            }
          }
          if (checkStatus == 1) {
            nodes.setChecklistStatus("1");
          } else if (checkStatus == 2) {
            nodes.setChecklistStatus("2");
          } else {
            nodes.setChecklistStatus("0");
          }
        } else {
          nodes.setIsChecklistExisted("0");
          nodes.setChecklistStatus("0");
        }
      }
    }
    return lstNodes;
  }

  @Override
  public ResultDTO updateWoCrNodeStatus(List<MrNodesDTO> lstNodes) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      if (lstNodes != null && lstNodes.size() > 0) {
        for (MrNodesDTO nodes : lstNodes) {
          mrNodesBusiness.updateStatus(nodes, false);
        }
        resultDTO = new ResultDTO("1", "OK", "");
      } else {
        resultDTO = new ResultDTO("0", RESULT.FAIL, "lstNodes : Empty");
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      resultDTO = new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public String updateWODischargeBattery(List<MrNodesDTO> listMrNodeDTO) {
    return mrRepository.updateWODischargeBattery(listMrNodeDTO);
  }

  @Override
  public String closeWO(List<MrNodesDTO> listMrNodeDTO) {
    String resval = "";
    List<String> lsStr = new ArrayList<>();
    listMrNodeDTO.stream().filter((dto) -> (!lsStr.contains(dto.getWoId()))).forEach((dto) -> {
      lsStr.add(dto.getWoId());
    });
    System.out.println("WO_ID: " + lsStr.toString());
    for (String str : lsStr) {
      boolean check = mrRepository.checkWO(str);
      if (check) {
        try {
          WoUpdateStatusForm updateForm = new WoUpdateStatusForm();
          updateForm.setNewStatus(Long.parseLong("8"));
          updateForm.setWoCode(str);
          updateForm.setReasonChange(I18n.getLanguage("mrService.testxa.finish"));
          updateForm.setSystemChange("MR");
          updateForm.setUserChange("system");
          updateForm.setFinishTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
          updateForm.setResultClose(2L);
          ResultDTO rs = woServiceProxy.changeStatusWoProxy(updateForm);
          System.out.println("Update WO: " + rs);
        } catch (Exception ex) {
          java.util.logging.Logger.getLogger(MrBusiness.class.getName())
              .log(Level.SEVERE, null, ex);
          resval = ex.getMessage();
        }
      } else {
        resval = "WO chua NODES status = 0";
      }
    }
    if ("".equals(resval)) {
      resval = RESULT.SUCCESS;
    }
    return resval;
  }

  @Override
  public ResultDTO updateMrAndWoCDBattery(String woId, Date recentDischargeCd, String nodeName,
      String nodeIp) {
    return mrRepository.updateMrAndWoCDBattery(woId, recentDischargeCd, nodeName, nodeIp);
  }

  /**
   * @author Dunglv3
   */
  @Override
  public ResultDTO createMrDTO(MrDTO mrDTO, UsersDTO usersDTO, List<MrWoTempDTO> lstMrWoTempDTO) {
    return mrRepository.createMrDTO(mrDTO, usersDTO, lstMrWoTempDTO);
  }

  @Override
  public boolean checkNodeStatusByWo(String woId) {
    return mrRepository.checkNodeStatusByWo(woId);
  }

  @Override
  public ResultInSideDto delete(Long mrId) {
    return mrRepository.delete(mrId);
  }

  @Override
  public boolean updateMrState(String mrId, String state, ResultDTO res) {
    return mrRepository.updateMrState(mrId, state, res);
  }

  @Override
  public ResultInSideDto insertMrHis(MrHisDTO mrHisDTO) {
    return mrRepository.insertMrHis(mrHisDTO);
  }

  @Override
  public ResultDTO insertMrScheduleTelHis(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    return mrRepository.insertMrScheduleTelHis(mrScheduleTelHisDTO);
  }

  @Override
  public List<MrScheduleTelDTO> getByMrId(String mrId) {
    return mrRepository.getByMrId(mrId);
  }

  @Override
  public ResultInSideDto deleteMrScheduleTel(Long id) {
    return mrRepository.deleteMrScheduleTel(id);
  }

  @Override
  public MrDeviceDTO findDetailById(Long deviceId) {
    return mrRepository.findDetailById(deviceId);
  }

  @Override
  public ResultInSideDto updateMrDeviceDTO(MrDeviceDTO dtoUpdate) {
    return mrRepository.updateMrDeviceDTO(dtoUpdate);
  }

  @Override
  public List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId) {
    return mrRepository.getReasonWO(reasonTypeId);
  }

  @Override
  public List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId) {
    if (StringUtils.isStringNullOrEmpty(nodeChecklistId)) {
      return new ArrayList<>();
    } else {
      List<MrNodeChecklistFilesDTO> lstFile = mrRepository
          .getListFileMrNodeChecklist_VS(nodeChecklistId);
      try {
        if (lstFile != null) {
          for (MrNodeChecklistFilesDTO item : lstFile) {
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), item.getFilePath());
            item.setFileContent(bytes);
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      return lstFile;
    }
  }
}
