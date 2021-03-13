package com.viettel.gnoc.cr.business;

import com.viettel.aam.MopInfo;
import com.viettel.aam.MopResult;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrAutoServiceForSRBusinessImpl implements CrAutoServiceForSRBusiness {

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

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  CrProcessBusiness crProcessBusiness;

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  CrApprovalDepartmentBusiness crApprovalDepartmentBusiness;

  @Autowired
  CrFileAttachBusiness crFileAttachBusiness;

  @Autowired
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Autowired
  WSVipaIpPort wSVipaPort;

  @Autowired
  WSTDTTPort wSTDTTPort;

  @Autowired
  WSVipaDdPort wsVipaDdPort;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  CrAlarmRepository crAlarmRepository;

  @Override
  public ResultDTO insertAutoCrForSR(CrDTO crDTO, List<CrFilesAttachDTO> lstFile, String system,
      String nationCode, List<WoDTO> lstWo, List<String> lstMop, List<String> lstNodeIp) {
    log.info("Request to insertAutoCrForSR : {}");
    ResultDTO rs = new ResultDTO();
    try {

      String validate = validate(crDTO, nationCode, system, lstWo);
      if (!"".equals(validate)) {
        rs.setMessage(validate);
        rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
      } else {
        //them moi don vi phe duyet
        List<CrApprovalDepartmentDTO> lstAppDept = getLstDepartmentApprove(crDTO);
        if (lstAppDept != null && !lstAppDept.isEmpty()) {
          crDTO.setLstAppDept(lstAppDept);
        }
        //them moi file
        if (lstFile != null && !lstFile.isEmpty()) {
          validate = validateAndSaveFileAttach(lstFile, crDTO);
        }
        List<CrFilesAttachDTO> lstFileTemp = new ArrayList<>();
        if (lstFile != null) {
          lstFileTemp.addAll(lstFile);
        }
        if (lstMop != null && !lstMop.isEmpty()) {
          lstFile = getListFileFromMop(crDTO, system, crDTO.getCrNumber(), lstMop, lstNodeIp);
          if (lstFile != null) {
            lstFileTemp.addAll(lstFile);
          }

        }

        if (!"".equals(validate)) {
          rs.setMessage(validate);
          rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
        } else {
          //luu thong tin db GNOC
          List<CrFilesAttachInsiteDTO> lstInsert = new ArrayList<>();
          for (CrFilesAttachDTO crFilesAttachDTO : lstFileTemp) {
            lstInsert.add(crFilesAttachDTO.toModelOutSide());
          }
          String result = crFileAttachBusiness.insertListNoID(lstInsert);
          if (result != null && result.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
            String woCode = "";
            if (lstWo != null && !lstWo.isEmpty()) {
              woCode = createWO(crDTO, lstWo);
            }

            //bo sung fill node mang tra ve tu tool tac dong vao node mang anh huong, giong voi ds node mang tac dong
            if ("AAM".equalsIgnoreCase(system) && lstMop != null && !lstMop.isEmpty()
                && crDTO.getLstNetworkNodeIdAffected() != null && !crDTO
                .getLstNetworkNodeIdAffected().isEmpty()) {
              log.info("Start create node Affect as Node Impact");
              crDTO.setIsClickedNodeAffected("1");
            } else {
              if (lstMop != null && lstMop.size() > 0 && crDTO.getLstNetworkNodeId() != null && (
                  crDTO.getLstNetworkNodeIdAffected() == null
                      || crDTO.getLstNetworkNodeIdAffected().size() < 1)) {
                List<CrAffectedNodesDTO> lstCrAffectedNodesDTOS = new ArrayList<>();
                for (CrImpactedNodesDTO crImpactedNodesDTO : crDTO.getLstNetworkNodeId()) {
                  CrAffectedNodesDTO crAffectedNodesDTO = new CrAffectedNodesDTO();
                  crAffectedNodesDTO.setCrId(crImpactedNodesDTO.getCrId());
                  crAffectedNodesDTO.setIpId(crImpactedNodesDTO.getIpId());
                  crAffectedNodesDTO.setDeviceId(crImpactedNodesDTO.getDeviceId());
                  crAffectedNodesDTO.setInsertTime(crImpactedNodesDTO.getInsertTime());
                  crAffectedNodesDTO.setDtCode(crImpactedNodesDTO.getDtCode());
                  crAffectedNodesDTO.setIp(crImpactedNodesDTO.getIp());
                  crAffectedNodesDTO.setDeviceCode(crImpactedNodesDTO.getDeviceCode());
                  crAffectedNodesDTO.setDeviceName(crImpactedNodesDTO.getDeviceName());
                  crAffectedNodesDTO.setNationCode(crImpactedNodesDTO.getNationCode());
                  lstCrAffectedNodesDTOS.add(crAffectedNodesDTO);
                }
                log.info("Start create node Affect as Node Impact");
                crDTO.setIsClickedNodeAffected("1");
                crDTO.setLstNetworkNodeIdAffected(lstCrAffectedNodesDTOS);
              }
            }

            //bo sung load them listAlarm theo quy trinh cho SR
            CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
            if ("1".equals(String.valueOf(crInsiteDTO.getIsClickedToAlarmTag()))) {
              if (crInsiteDTO.getLstAlarn() == null || crInsiteDTO.getLstAlarn().size() < 1) {
                //get alarm theo quy trinh
                List<CrAlarmInsiteDTO> lstAlarm = crAlarmRepository
                    .getListAlarmByProcess(crInsiteDTO);
                crInsiteDTO.setLstAlarn(lstAlarm);
              }
            }
            //end
            //luu Cr
            rs = (crBusiness.createObject(crInsiteDTO)).toResultDTO();
            if (!RESULT.SUCCESS.equals(rs.getMessage().toUpperCase())) {

              //goi WO
              WoDTOSearch dTOSearch = new WoDTOSearch();
              if (!StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
                dTOSearch.setUserId("256066");
                dTOSearch.setWoSystem("CR");
                dTOSearch.setWoSystemId(crDTO.getCrId());
                dTOSearch.setPage(1);
                dTOSearch.setPageSize(Integer.MAX_VALUE);
                List<WoDTOSearch> lst = woServiceProxy.getListDataSearchProxy(dTOSearch);
                if (lst != null && !lst.isEmpty()) {
                  for (WoDTOSearch search : lst) {
                    woServiceProxy
                        .deleteWOForRollbackProxy(search.getWoCode(), "rollback CR", "CR");
                  }
                }
              }
            }
            //goi sang tool
            if (lstMop != null && !lstMop.isEmpty()) {
              if ("VIPA".equals(system)) {
                wSVipaPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
                wSVipaPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
              } else if ("VMSA".equals(system)) {
                wsVipaDdPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
                wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
              } else if ("AAM".equals(system)) {
                for (String dtCode : lstMop) {
                  wSTDTTPort.linkCr(crDTO.getCrNumber(), dtCode, crDTO.getChangeOrginatorName(),
                      crDTO.getTitle(), crDTO.getEarliestStartTime(), crDTO.getLatestStartTime(),
                      Long.valueOf(crDTO.getState()));
                }
              }
            }
            rs.setId(woCode);
          } else {
            rs.setKey(result);
          }
        }
      }
    } catch (Exception ex) {
      log.info(ex.getMessage(), ex);
      rs.setMessage(ex.getMessage());
      rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
      //xoa CR
      crBusiness.delete(Long.parseLong(crDTO.getCrId()));
      //goi WO
      WoDTOSearch dTOSearch = new WoDTOSearch();
      if (!StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
        dTOSearch.setUserId("256066");
        dTOSearch.setWoSystem("CR");
        dTOSearch.setWoSystemId(crDTO.getCrId());
        dTOSearch.setPage(1);
        dTOSearch.setPageSize(Integer.MAX_VALUE);
        List<WoDTOSearch> lst = woServiceProxy.getListDataSearchProxy(dTOSearch);
        if (lst != null && !lst.isEmpty()) {
          for (WoDTOSearch search : lst) {
            woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback CR", "CR");
          }
        }
      }
    }
    return rs;
  }

  @Override
  public String getCrNumber(String crProcessId) throws Exception {
    try {
      List<String> lst = crBusiness.getSequenseCr("CR_SEQ", 1);
      if (crProcessId == null || "".equals(crProcessId.trim())) {
        return "CrProcessId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
      }

      CrProcessInsideDTO crProcess = crProcessBusiness
          .findCrProcessById(Long.parseLong(crProcessId));
      String crType = "0".equalsIgnoreCase(crProcess.getCrTypeId().toString()) ? "NORMAL"
          : "1".equalsIgnoreCase(crProcess.getCrTypeId().toString()) ? "EMERGENCY"
              : "2".equalsIgnoreCase(crProcess.getCrTypeId().toString()) ? "STANDARD" : "";
      ImpactSegmentDTO segmentDTO = crCategoryServiceProxy
          .findById(crProcess.getImpactSegmentId());

      return "CR_" + crType + "_" + segmentDTO.getImpactSegmentCode() + "_" + lst.get(0);

    } catch (Exception ex) {
      log.info(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public GnocFileDto getFilePathSrCr(GnocFileDto gnocFileDto) {
    try {
      String pathUploadProcessFtp = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder,
              gnocFileDto.getFileName(), gnocFileDto.getBytes(), gnocFileDto.getCreateTime());
      String pathUploadProcessOld = FileUtils
          .saveUploadFile(gnocFileDto.getFileName(),
              gnocFileDto.getBytes(), uploadFolder, gnocFileDto.getCreateTime());
      GnocFileDto result = new GnocFileDto();
      result.setPath(pathUploadProcessFtp);
      result.setPathTemplate(pathUploadProcessOld);
      return result;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return null;
  }

  public String createWO(CrDTO crDTO, List<WoDTO> lstWo) throws Exception {
    String woCode = "";
    try {

      for (WoDTO dTO : lstWo) {
        WoDTO woDTO = dTO;
        woDTO.setWoSystem("CR");
        woDTO.setWoSystemId(crDTO.getCrId());
        woDTO.setCreatePersonId(crDTO.getChangeOrginator());

//            String strConfigProperty = TroublesServiceImpl.getTroublesServiceImpl().getConfigProperty();
//            Type type = new TypeToken<Map<String, String>>() {
//            }.getType();
//            Gson gson = new Gson();
//            Map<String, String> mapConfigProperty = gson.fromJson(strConfigProperty, type);
//        Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
//        woDTO.setWoTypeId(mapConfigProperty.get("cr_wo_type"));
//        woDTO.setWoContent(crDTO.getCrId() + " " + crDTO.getTitle());
//        woDTO.setWoDescription(crDTO.getTitle());
        Date sysDate = new Date();
//        woDTO.setStartTime(crDTO.getEarliestStartTime());
//        woDTO.setEndTime(crDTO.getLatestStartTime());
        woDTO.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(sysDate));
//        woDTO.setPriorityId(mapConfigProperty.get("cr_wo_priority"));//muc uu tien

        ResultDTO reuslt = woServiceProxy.createWoProxy(woDTO);
        if (!RESULT.SUCCESS.equals(reuslt.getKey().toUpperCase())) {
          throw new Exception(reuslt.getMessage());
        }
        woCode = woCode + "," + reuslt.getId();

      }
    } catch (Exception ex) {
      log.info(ex.getMessage(), ex);
      throw ex;
    }

    return woCode;
  }

  public String validateAndSaveFileAttach(List<CrFilesAttachDTO> lstFilesAttach, CrDTO crDTO)
      throws Exception {
    String result = "";
    if (lstFilesAttach != null && !lstFilesAttach.isEmpty()) {
      for (CrFilesAttachDTO attachDTO : lstFilesAttach) {
        if (attachDTO.getFileContent() == null || "".equals(attachDTO.getFileContent())) {
          return "File content " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
        if (attachDTO.getFileType() == null || "".equals(attachDTO.getFileType())) {
          return "File type " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
        if (attachDTO.getFileName() == null || "".equals(attachDTO.getFileName())) {
          return "File name " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
      }
      for (CrFilesAttachDTO attachDTO : lstFilesAttach) {
        String fileName = attachDTO.getFileName();
        byte[] fileContent = Base64.decode(attachDTO.getFileContent());
        Date date = new Date();
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, fileName, fileContent, date);
        String fullPathOld = FileUtils.saveUploadFile(fileName, fileContent, uploadFolder, date);
        attachDTO.setFileName(FileUtils.getFileName(fullPath));
        attachDTO.setFilePathFtp(fullPath);
        attachDTO.setFilePath(fullPathOld);
        attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        attachDTO.setCrId(crDTO.getCrId());
      }
    } else {
      return "File attach " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    return result;
  }

  public List<CrFilesAttachDTO> getListFileFromMop(CrDTO crDTO, String system, String crNumber,
      List<String> lstMop, List<String> lstNodeIp) throws Exception {
    log.info("Request to getListFileFromMop : {}", crDTO, system, crNumber, lstMop, lstNodeIp);
    List<CrFilesAttachDTO> lstFile = new ArrayList<>();
    if (lstMop != null && !lstMop.isEmpty()) {
      log.info("lstMop size" + lstMop.size());
      com.viettel.vipa.MopDetailOutputDTO vipaIpMop = null;
      com.viettel.vmsa.MopDetailOutputDTO vipaDdMop = null;
      String mopFile = null;
      String mopFileContent = null;
      String mopFileKpi = null;
      String mopFileKpiContent = null;
      String nationCode = null;
      List<String> lstIpImpact = new ArrayList<>();
      List<String> lstAffectIps = new ArrayList<>();
      String dtFileHistory = "";
      for (String mopCode : lstMop) {
        log.info("mopCode" + mopCode);
        if (!StringUtils.isStringNullOrEmpty(mopCode)) {
          List<MopInfo> lstMopAAM = new ArrayList<>();
          if ("AAM".equalsIgnoreCase(system)) {

            MopResult mopResult = wSTDTTPort.getMopInfo(mopCode);
            if (mopResult == null || (mopResult != null && (mopResult.getMopInfos() == null
                || mopResult.getMopInfos().isEmpty()))) {
              throw new Exception(system + " not connection : " + mopCode);
            }
            if (mopResult.getMopInfos() != null && !mopResult.getMopInfos().isEmpty()) {
              for (MopInfo mopInfo : mopResult.getMopInfos()) {
                dtFileHistory = I18n.getChangeManagement("qltn.insertFile");
                lstIpImpact.addAll(mopInfo.getIps());
                lstAffectIps.addAll(mopInfo.getAffectIps());
                nationCode = mopInfo.getNationCode();
                lstMopAAM.add(mopInfo);
              }
            }
          } else if ("VMSA".equalsIgnoreCase(system)) {
            dtFileHistory = "VIPA_DD_INSERT";

            vipaDdMop = wsVipaDdPort.getMopInfo(mopCode);

            if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
              if (vipaDdMop.getMopDetailDTO() == null) {
                throw new Exception(system + " Not found MopInfo : " + mopCode);
              }
              nationCode = vipaDdMop.getMopDetailDTO().getNationCode();

              List<com.viettel.vmsa.NodeDTO> lstNode = vipaDdMop.getMopDetailDTO().getNodes();
              if (lstNode != null && !lstNode.isEmpty()) {
                List<String> lstIp = new ArrayList<>();
                for (com.viettel.vmsa.NodeDTO dto : lstNode) {
                  if (dto.getNodeIp() != null && !lstIp.contains(dto.getNodeIp())) {
                    lstIp.add(dto.getNodeIp());
                  }
                }
                lstIpImpact.addAll(lstIp);
              }
            } else {
              throw new Exception(
                  system + " Call error: " + (vipaDdMop != null ? vipaDdMop.getResultCode() : null)
                      + " " + mopCode);
            }

            mopFile = vipaDdMop.getMopDetailDTO().getMopFileName();
            mopFileContent = vipaDdMop.getMopDetailDTO().getMopFileContent();
            mopFileKpi = vipaDdMop.getMopDetailDTO().getKpiFileName();
            mopFileKpiContent = vipaDdMop.getMopDetailDTO().getKpiFileContent();

          } else {
            dtFileHistory = "VIPA_IP_INSERT";

            vipaIpMop = wSVipaPort.getMopInfo(mopCode);
            if (vipaIpMop != null && vipaIpMop.getResultCode() == 0) {
              if (vipaIpMop.getMopDetailDTO() == null) {
                throw new Exception(system + " Not found MopInfo : " + mopCode);
              }
              nationCode = vipaIpMop.getMopDetailDTO().getNationCode();
              List<com.viettel.vipa.NodeDTO> lstNode = vipaIpMop.getMopDetailDTO().getNodes();
              if (lstNode != null && !lstNode.isEmpty()) {
                List<String> lstIp = new ArrayList<>();
                for (com.viettel.vipa.NodeDTO dto : lstNode) {
                  if (dto.getNodeIp() != null && !lstIp.contains(dto.getNodeIp())) {
                    lstIp.add(dto.getNodeIp());
                  }
                }
                lstIpImpact.addAll(lstIp);
              }
            } else {
              throw new Exception(
                  system + " Call error: " + (vipaIpMop != null ? vipaIpMop.getResultCode() : null)
                      + " " + mopCode);
            }

            mopFile = vipaIpMop.getMopDetailDTO().getMopFileName();
            mopFileContent = vipaIpMop.getMopDetailDTO().getMopFileContent();
            mopFileKpi = vipaIpMop.getMopDetailDTO().getKpiFileName();
            mopFileKpiContent = vipaIpMop.getMopDetailDTO().getKpiFileContent();
          }
          // check IP
          List<String> lstIpsDB = new ArrayList<>();
          if (lstIpImpact == null || lstIpImpact.isEmpty()) {
            throw new Exception(
                I18n.getChangeManagement("cr.tdtt.ipImpactErr"));
          }

          List<CrImpactedNodesDTO> lstInfraDevice = new ArrayList<>();
          List<CrAffectedNodesDTO> lstAffectedNodes = new ArrayList<>();
          InfraDeviceDTO deviceDTO = new InfraDeviceDTO();
          deviceDTO.setLstIps(lstIpImpact);
          deviceDTO.setNationCode(nationCode);
          List<InfraDeviceDTO> lst = commonStreamServiceProxy
              .getListInfraDeviceIpV2SrProxy(deviceDTO);
          if (lst != null && !lst.isEmpty()) {
            log.info("lstIpImpactDB size" + lst.size());
            for (InfraDeviceDTO infraDeviceDTO : lst) {
              log.info("IpImpact " + infraDeviceDTO.getIp());
              lstIpsDB.add(infraDeviceDTO.getIp());
              //end
              CrImpactedNodesDTO impactedNodesDTO = new CrImpactedNodesDTO();
              impactedNodesDTO.setIpId(infraDeviceDTO.getIpId());
              impactedNodesDTO.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              impactedNodesDTO.setIp(infraDeviceDTO.getIp());
              impactedNodesDTO.setCrId(crDTO.getCrId());
              impactedNodesDTO.setDeviceId(infraDeviceDTO.getDeviceId());
              impactedNodesDTO.setDeviceName(infraDeviceDTO.getDeviceName());
              impactedNodesDTO.setDeviceCode(infraDeviceDTO.getDeviceCode());
              impactedNodesDTO.setNationCode(nationCode);
              impactedNodesDTO.setDtCode(mopCode);
              lstInfraDevice.add(impactedNodesDTO);

            }
          } else {
            throw new Exception(
                I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + lstIpImpact.get(0));
          }
          if (lstIpsDB != null && !lstIpsDB.isEmpty()) {
            for (String ipImpactExist : lstIpImpact) {
              if (!lstIpsDB.contains(ipImpactExist)) {
                throw new Exception(
                    I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + ipImpactExist);
              }
            }
          }
          lstIpsDB = new ArrayList<>();
          crDTO.setLstNetworkNodeId(new ArrayList<>());
          crDTO.getLstNetworkNodeId().addAll(lstInfraDevice);
          if ("AAM".equalsIgnoreCase(system)) {
            if (lstAffectIps != null && !lstAffectIps.isEmpty()) {
              log.info("lstAffectIps size AAM" + lstAffectIps.size());
              deviceDTO.setLstIps(new ArrayList<>());
              deviceDTO.setLstIps(lstAffectIps);
              List<InfraDeviceDTO> lstAffactIpInsert = commonStreamServiceProxy
                  .getListInfraDeviceIpV2SrProxy(deviceDTO);
              if (lstAffactIpInsert != null && !lstAffactIpInsert.isEmpty()) {
                for (InfraDeviceDTO infraDeviceDTO : lstAffactIpInsert) {
                  log.info("AffactIp " + infraDeviceDTO.getIp());
                  lstIpsDB.add(infraDeviceDTO.getIp());
                  if (lstAffectIps.contains(infraDeviceDTO.getIp())) {
                    CrAffectedNodesDTO crAffectedNodesDTO = new CrAffectedNodesDTO();
                    crAffectedNodesDTO.setCrId(crDTO.getCrId());
                    crAffectedNodesDTO.setIpId(infraDeviceDTO.getIpId());
                    crAffectedNodesDTO.setDeviceId(infraDeviceDTO.getDeviceId());
                    crAffectedNodesDTO.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                    crAffectedNodesDTO.setDtCode(mopCode);
                    crAffectedNodesDTO.setIp(infraDeviceDTO.getIp());
                    crAffectedNodesDTO.setDeviceCode(infraDeviceDTO.getDeviceCode());
                    crAffectedNodesDTO.setDeviceName(infraDeviceDTO.getDeviceName());
                    crAffectedNodesDTO.setNationCode(nationCode);
                    lstAffectedNodes.add(crAffectedNodesDTO);
                  } else {
                    throw new Exception(
                        I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + infraDeviceDTO
                            .getIp());
                  }
                }
              } else {
                throw new Exception(
                    I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + lstAffectIps.get(0));
              }
              for (String ipAffactIpExist : lstAffectIps) {
                if (!lstIpsDB.contains(ipAffactIpExist)) {
                  throw new Exception(
                      I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + ipAffactIpExist);
                }
              }
              crDTO.setLstNetworkNodeIdAffected(new ArrayList<>());
              crDTO.getLstNetworkNodeIdAffected().addAll(lstAffectedNodes);
            }
            if (lstMopAAM != null && !lstMopAAM.isEmpty()) {
              for (MopInfo mopInfo : lstMopAAM) {
                if (StringUtils.isNotNullOrEmpty(mopInfo.getMopFile()) && StringUtils
                    .isNotNullOrEmpty(mopInfo.getMopFileContent())) {
                  CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
                  attachDTO.setDtCode(mopCode);
                  attachDTO.setDtFileHistory(dtFileHistory);
                  attachDTO.setFileType("3");
                  attachDTO.setFileContent(mopInfo.getMopFileContent());
                  attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                  attachDTO.setUserId(crDTO.getChangeOrginator());
                  attachDTO.setCrId(crDTO.getCrId());
                  byte[] fileContent = Base64.decode(attachDTO.getFileContent());
                  Date date = new Date();
                  String fullPath = FileUtils
                      .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), ftpFolder, mopInfo.getMopFile(),
                          fileContent, date);
                  String fullPathOld = FileUtils
                      .saveUploadFile(mopInfo.getMopFile(), fileContent, uploadFolder, date);
                  attachDTO.setFileName(FileUtils.getFileName(fullPath));
                  attachDTO.setFilePathFtp(fullPath);
                  attachDTO.setFilePath(fullPathOld);
                  lstFile.add(attachDTO);
                }
                if (StringUtils.isNotNullOrEmpty(mopInfo.getMopRollbackFile()) && StringUtils
                    .isNotNullOrEmpty(mopInfo.getMopRollbackFileContent())) {
                  CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
                  attachDTO.setDtCode(mopCode);
                  attachDTO.setDtFileHistory(dtFileHistory);
                  attachDTO.setFileType("2");
                  attachDTO.setFileContent(mopInfo.getMopRollbackFileContent());
                  attachDTO.setCrId(crDTO.getCrId());
                  attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                  attachDTO.setUserId(crDTO.getChangeOrginator());
                  byte[] fileContent = Base64.decode(attachDTO.getFileContent());
                  Date date = new Date();
                  String fullPath = FileUtils
                      .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), ftpFolder, mopInfo.getMopRollbackFile(),
                          fileContent,
                          date);
                  String fullPathOld = FileUtils
                      .saveUploadFile(mopInfo.getMopRollbackFile(), fileContent, uploadFolder,
                          date);
                  attachDTO.setFileName(FileUtils.getFileName(fullPath));
                  attachDTO.setFilePathFtp(fullPath);
                  attachDTO.setFilePath(fullPathOld);
                  lstFile.add(attachDTO);
                }
              }
            }
          } else {
            //luu file dt
            if (StringUtils.isNotNullOrEmpty(mopFile) && StringUtils
                .isNotNullOrEmpty(mopFileContent)) {
              CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
              attachDTO.setDtCode(mopCode);
              attachDTO.setDtFileHistory(dtFileHistory);
              attachDTO.setFileType("3");
              attachDTO.setFileContent(mopFileContent);
              attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              attachDTO.setUserId(crDTO.getChangeOrginator());
              attachDTO.setCrId(crDTO.getCrId());
              byte[] fileContent = Base64.decode(attachDTO.getFileContent());
              Date date = new Date();
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, mopFile, fileContent, date);
              String fullPathOld = FileUtils
                  .saveUploadFile(mopFile, fileContent, uploadFolder, date);
              attachDTO.setFileName(FileUtils.getFileName(fullPath));
              attachDTO.setFilePathFtp(fullPath);
              attachDTO.setFilePath(fullPathOld);
              lstFile.add(attachDTO);
            } else {
              throw new Exception(
                  I18n.getLanguage("mop.is.not.Exist").replaceAll("@mopCode", mopFile));
            }
            if (StringUtils.isNotNullOrEmpty(mopFileKpi) && StringUtils
                .isNotNullOrEmpty(mopFileKpiContent)) {
              CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
              attachDTO.setDtCode(mopCode);
              attachDTO.setDtFileHistory(dtFileHistory);
              attachDTO.setFileType("5");
              attachDTO.setFileContent(mopFileKpiContent);
              attachDTO.setCrId(crDTO.getCrId());
              attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              attachDTO.setUserId(crDTO.getChangeOrginator());
              byte[] fileContent = Base64.decode(attachDTO.getFileContent());
              Date date = new Date();
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, mopFileKpi, fileContent, date);
              String fullPathOld = FileUtils
                  .saveUploadFile(mopFileKpi, fileContent, uploadFolder, date);
              attachDTO.setFileName(FileUtils.getFileName(fullPath));
              attachDTO.setFilePathFtp(fullPath);
              attachDTO.setFilePath(fullPathOld);
              lstFile.add(attachDTO);
            }
          }
        }
      }
    }
    return lstFile;
  }

  private List<CrApprovalDepartmentDTO> getLstDepartmentApprove(CrDTO crDTO) throws Exception {
    List<CrApprovalDepartmentDTO> lst = new ArrayList<>();
    if (crDTO.getRisk() == null || Constants.CR_TYPE.EMERGENCY.toString()
        .equals(crDTO.getCrType())) {
      return new ArrayList<>();
    } else {
      CrApprovalDepartmentInsiteDTO form = new CrApprovalDepartmentInsiteDTO();
      form.setCrId(crDTO.getCrId());
      form.setCreatorId(crDTO.getChangeOrginator());
      List<CrApprovalDepartmentInsiteDTO> lstAppDeptInsite = crApprovalDepartmentBusiness
          .search(form, 0, 2, "IS_EDIT", "");

      List<CrApprovalDepartmentDTO> lstAppDept = new ArrayList();
      if (lstAppDeptInsite != null) {
        for (CrApprovalDepartmentInsiteDTO dto : lstAppDeptInsite) {
          lstAppDept.add(dto.toOutSideDTO());
        }
      }
      if (lstAppDept != null && !lstAppDept.isEmpty()) {
        for (CrApprovalDepartmentDTO caddto : lstAppDept) {
          caddto.setCrId(crDTO.getCrId());
          caddto.setIncommingDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        }
        CrProcessInsideDTO crProcess = crProcessBusiness
            .findCrProcessById(Long.parseLong(crDTO.getCrProcessId()));
        String impactAffectTwoLevel = crProcess.getRiskLevel().toString();
        String approvalLever = crProcess.getApprovalLevel().toString();
        if ("4".equalsIgnoreCase(impactAffectTwoLevel.trim())) {
          if (lstAppDept.size() > 0) {
            lst.add(lstAppDept.get(0));
          }
        } else {
          if (approvalLever == null || "0".equalsIgnoreCase(approvalLever)) {
            if (("1".equalsIgnoreCase(impactAffectTwoLevel.trim())
                || "2".equalsIgnoreCase(impactAffectTwoLevel.trim())
                || "3".equalsIgnoreCase(impactAffectTwoLevel.trim()))) {
              lst.addAll(lstAppDept);
            }
          } else {
            if ("1".equalsIgnoreCase(approvalLever)) {
              if (lstAppDept.size() > 0) {
                lst.add(lstAppDept.get(0));
              }
            } else if ("2".equalsIgnoreCase(approvalLever)) {
              lst.addAll(lstAppDept);
            }
          }
        }
      }
    }
    return lst;
  }

  public String validate(CrDTO crDTO, String nationCode, String system, List<WoDTO> lstWo)
      throws Exception {
    String result = "";

    if (system == null || "".equals(system)) {
      return "System " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (crDTO.getCrId() == null || "".equals(crDTO.getCrId())) {
      return "CrId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    } else {
      try {
        Long.parseLong(crDTO.getCrId());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        return "CrId " + I18n.getChangeManagement("cr.template.notvalid");
      }
    }
    CrInsiteDTO crTemp = (crBusiness.getCrById(Long.parseLong(crDTO.getCrId()), null));
    if (crTemp != null && !StringUtils.isStringNullOrEmpty(crTemp.getCrNumber())) {
      return "CrId " + I18n.getChangeManagement("cr.msg.exists");
    }

    if (crDTO.getCrNumber() == null || "".equals(crDTO.getCrNumber())) {
      return "CrNumber " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (!StringUtils.checkMaxlength(200L, crDTO.getCrNumber())) {
      return "CrNumber " + String
          .format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 200L);
    }
    if (crDTO.getState() == null || "".equals(crDTO.getState().trim())) {
      return "State " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if ("12".equals(crDTO.getState())) {
      if (crDTO.getUserCab() == null || "".equals(crDTO.getUserCab())) {
        return "UserCab " + I18n.getChangeManagement("cr.msg.must.be.not.null");
      }
      UsersDTO usersDTO = crBusiness.getUserInfo(crDTO.getUserCab());
      if (usersDTO == null || StringUtils.isStringNullOrEmpty(usersDTO.getUserId())) {
        return "UserCab " + I18n.getLanguage("qltn.userNotExist");
      }
      crDTO.setUserCab(usersDTO.getUserId());
    }
    if (crDTO.getLstNetworkNodeId() != null && crDTO.getLstNetworkNodeId().size() > 1000L) {
      return I18n.getChangeManagement("cr.msg.node.over") + 1000L;
    }
    if (crDTO.getLstNetworkNodeIdAffected() != null
        && crDTO.getLstNetworkNodeIdAffected().size() > 1000L) {
      return I18n.getChangeManagement("cr.msg.node.over") + 1000L;
    }

    if (crDTO.getTitle() == null || "".equals(crDTO.getTitle().trim())) {
      return "Title " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.checkMaxlength(255L, crDTO.getTitle())) {
      return "Title " + String
          .format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 255);

    }
    if (crDTO.getDescription() == null || "".equals(crDTO.getDescription().trim())) {
      return "Description " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.checkMaxlength(2000L, crDTO.getDescription())) {
      return "Description " + String
          .format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 2000);
    }

    if (crDTO.getCrProcessId() == null || "".equals(crDTO.getCrProcessId().trim())) {
      return "CrProcessId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    } else {
      try {
        CrProcessInsideDTO crProcess = crProcessBusiness
            .findCrProcessById(Long.parseLong(crDTO.getCrProcessId()));
        if (crProcess == null || crProcess.getCrProcessId() == null) {
          return "CrProcessId " + I18n.getChangeManagement("cr.msg.not.exists");
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return "CrProcessId " + I18n.getChangeManagement("cr.msg.not.exists");
      }
    }
    if (crDTO.getSubcategoryId() == null || "".equals(crDTO.getSubcategoryId().trim())) {
      return "SubcategoryId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    crDTO.setSubcategory(crDTO.getSubcategoryId());
    if (crDTO.getPriority() == null || "".equals(crDTO.getPriority().trim())) {
      return "Priority " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (crDTO.getServiceAffecting() == null || "".equals(crDTO.getServiceAffecting().trim())) {
      return "ServiceAffecting " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    //co a/h dich vu
    if ("1".equals(crDTO.getServiceAffecting()) && (crDTO.getLstAffectedService() == null || crDTO
        .getLstAffectedService().isEmpty())) {
      return "LstAffectedService " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (crDTO.getImpactAffect() == null || "".equals(crDTO.getImpactAffect().trim())) {
      return "ImpactAffect " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (crDTO.getTotalAffectedCustomers() == null || ""
        .equals(crDTO.getTotalAffectedCustomers().trim())) {
      return "TotalAffectedCustomers " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    } else {
      try {
        Long.parseLong(crDTO.getTotalAffectedCustomers());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        return "TotalAffectedCustomers " + I18n.getChangeManagement("cr.template.notvalid");
      }
    }

    if (crDTO.getTotalAffectedMinutes() == null || ""
        .equals(crDTO.getTotalAffectedMinutes().trim())) {
      return "TotalAffectedMinutes " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    } else {
      try {
        Long.parseLong(crDTO.getTotalAffectedMinutes());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        return "TotalAffectedCustomers " + I18n.getChangeManagement("cr.template.notvalid");
      }
    }
    CrProcessInsideDTO crProcess = crProcessBusiness
        .findCrProcessById(Long.parseLong(crDTO.getCrProcessId()));
    crDTO.setCrType(crProcess.getCrTypeId().toString());
    crDTO.setRisk(crProcess.getRiskLevel().toString());
    crDTO.setImpactSegment(crProcess.getImpactSegmentId().toString());
    crDTO.setDeviceType(crProcess.getDeviceTypeId().toString());
    crDTO.setProcessTypeId(
        crProcess.getCrProcessId() != null ? String.valueOf(crProcess.getCrProcessId()) : null);
    crDTO.setProcessTypeLv3Id(crDTO.getProcessTypeLv3Id());
    crDTO.setDutyType(crProcess.getImpactType().toString());

    crDTO.setRelateCr("0");//ko co Cr lien quan
    crDTO.setCreatedDate(DateTimeUtils.getSysDateTime());

    if (crDTO.getEarliestStartTime() == null || "".equals(crDTO.getEarliestStartTime().trim())) {
      return "EarliestStartTime " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (crDTO.getLatestStartTime() == null || "".equals(crDTO.getLatestStartTime().trim())) {
      return "LatestStartTime " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

//    if ("1".equals(crDTO.getServiceAffecting())) {//co a/h dich vu
    crDTO.setDisturbanceStartTime(crDTO.getEarliestStartTime());
    crDTO.setDisturbanceEndTime(crDTO.getLatestStartTime());

//    }
    String temp = validateTime(crDTO, crProcess.getImpactType());
    if (!"".equals(temp)) {
      return temp;
    }
    if (crDTO.getChangeResponsibleUnit() == null || ""
        .equals(crDTO.getChangeResponsibleUnit().trim())) {
      return "ChangeResponsibleUnit " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    } else {
      UnitDTO udto = crBusiness.getUnitInfo(crDTO.getChangeResponsibleUnit());
      if (udto != null && !StringUtils.isStringNullOrEmpty(udto.getUnitId())) {
      } else {
        return "ChangeResponsibleUnit does not exist";
      }
    }

    if (!StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
      UsersDTO udto = crBusiness.getUserInfo(crDTO.getChangeResponsible());
      if (udto != null && !StringUtils.isStringNullOrEmpty(udto.getUserId()) && udto.getUnitId()
          .equals(crDTO.getChangeResponsibleUnit())) {
      } else {
        return "ChangeResponsible does not exist";
      }
      crDTO.setChangeResponsible(udto.getUserId());
      crDTO.setChangeResponsibleUnit(udto.getUnitId());

    }
    if (StringUtils.isStringNullOrEmpty(crDTO.getChangeOrginator())) {
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      String crUserOrg = mapConfigProperty.get("cr_user_org");
      crDTO.setChangeOrginator(crUserOrg);

      String crUnitOrg = mapConfigProperty.get("cr_unit_org");
      crDTO.setChangeOrginatorUnit(crUnitOrg);

      crDTO.setUserLogin(crUserOrg);
      crDTO.setUserLoginUnit(crUnitOrg);

    } else {
      UsersDTO udto = crBusiness.getUserInfo(crDTO.getChangeOrginator());
      if (udto != null && !StringUtils.isStringNullOrEmpty(udto.getUserId())) {
      } else {
        return "Users creating CR does not exist";
      }
      crDTO.setChangeOrginator(udto.getUserId());
      crDTO.setChangeOrginatorUnit(udto.getUnitId());
      crDTO.setUserLogin(udto.getUserId());
      crDTO.setUserLoginUnit(udto.getUnitId());

    }
    if (crDTO.getNotes() != null && !StringUtils.checkMaxlength(1000L, crDTO.getNotes())) {
      return "Notes " + String
          .format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 1000);
    }

    if (crDTO.getCountry() == null || "".equals(crDTO.getCountry().trim())) {
      return "Country " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition
        .add(new ConditionBean("locationCode", crDTO.getCountry(), "NAME_EQUAL", "STRING"));
    //    20210111 dungpv edit lay quoc gia co trang thai la hoat dong
    lstCondition
        .add(new ConditionBean("status", "1", "NAME_EQUAL", "NUMBER"));
    // end
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CatLocationDTO> lstLocation = catLocationBusiness
        .searchByConditionBean(lstCondition, 0, 1, "", "");
    if (lstLocation != null && !lstLocation.isEmpty()) {
      crDTO.setCountry(lstLocation.get(0).getLocationId());
    } else {
      return I18n.getChangeManagement("cr.trace.countryInvalid");
    }

    if ("281".equals(crDTO.getCountry()) && (crDTO.getRegion() == null || ""
        .equals(crDTO.getRegion().trim()))) {
      return "Region " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.isStringNullOrEmpty(crDTO.getRegion())) {
      lstCondition = new ArrayList<ConditionBean>();
      lstCondition
          .add(new ConditionBean("locationCode", crDTO.getRegion(), "NAME_EQUAL", "STRING"));
      lstCondition.add(new ConditionBean("parentId", crDTO.getCountry(), "NAME_EQUAL", "NUMBER"));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      lstLocation = catLocationBusiness.searchByConditionBean(lstCondition, 0, 1, "", "");
      if (lstLocation != null && !lstLocation.isEmpty()) {
        crDTO.setRegion(lstLocation.get(0).getLocationId());
      } else {
        return I18n.getChangeManagement("cr.trace.regionInvalid");
      }
    }
    temp = validateDeviceIp(crDTO, nationCode);
    if (!"".equals(temp)) {
      return temp;
    }
    return result;
  }

  public String validateTime(CrDTO crDTO, Long dutyType) throws Exception {
    try {
      Date startDate = null;
      Date endDate = null;
      Date startDateImpact = null;
      Date endDateImpact = null;
      try {
        startDate = DateTimeUtils
            .convertStringToTime(crDTO.getEarliestStartTime(), DateTimeUtils.patternDateTime);
      } catch (Exception e) {
        log.info(e.getMessage(), e);
        return I18n.getChangeManagement("cr.trace.earliestStartTimeInvalid");
      }

      try {
        endDate = DateTimeUtils
            .convertStringToTime(crDTO.getLatestStartTime(), DateTimeUtils.patternDateTime);
      } catch (Exception e) {
        log.info(e.getMessage(), e);
        return I18n.getChangeManagement("cr.trace.latestStartTimeInvalid");
      }

      try {
        if (crDTO.getDisturbanceStartTime() != null && !crDTO.getDisturbanceStartTime().trim()
            .isEmpty()) {
          startDateImpact = DateTimeUtils
              .convertStringToTime(crDTO.getDisturbanceStartTime(), DateTimeUtils.patternDateTime);
        }
      } catch (Exception e) {
        log.info(e.getMessage(), e);
        return I18n.getChangeManagement("cr.trace.disturbanceStartTimeInvalid");
      }

      try {
        if (crDTO.getDisturbanceEndTime() != null && !crDTO.getDisturbanceEndTime().trim()
            .isEmpty()) {
          endDateImpact = DateTimeUtils
              .convertStringToTime(crDTO.getDisturbanceEndTime(), DateTimeUtils.patternDateTime);
        }
      } catch (Exception e) {
        log.info(e.getMessage(), e);
        return I18n.getChangeManagement("cr.trace.disturbanceEndTimeInvalid");
      }

      if (startDate.compareTo(new Date()) < 0) {
        return "EarliestStartTime " + I18n.getChangeManagement("msg.must.be.greater.than") + " "
            + I18n.getChangeManagement("cr.sysdate");
      }
      if (endDate.compareTo(startDate) < 0) {
        return "LatestStartTime " + I18n.getChangeManagement("msg.must.be.greater.than")
            + " EarliestStartTime";
      }

      if (endDateImpact != null && startDateImpact != null
          && endDateImpact.compareTo(startDateImpact) < 0) {
        return "DisturbanceEndTime " + I18n.getChangeManagement("msg.must.be.greater.than")
            + " DisturbanceStartTime";
      }

      if (startDate != null && startDateImpact != null && endDate != null
          && endDateImpact != null) {
        if (startDateImpact.compareTo(startDate) < 0) {

          return "DisturbanceStartTime " + I18n.getChangeManagement("msg.must.be.greater.than")
              + " EarliestStartTime";
        }
        if (endDateImpact.compareTo(endDate) > 0) {
          return "LatestStartTime " + I18n.getChangeManagement("msg.must.be.greater.than")
              + " DisturbanceEndTime";
        }
      }

      CrImpactFrameDTO form = new CrImpactFrameDTO();
      form.setIfeId(dutyType.toString());
      List<ItemDataCRInside> lstFrame = crGeneralBusiness.getListDutyTypeCBB(form.toInSiteDTO());
      ItemDataCRInside dataCR = lstFrame.get(0);
      String[] startendarray = dataCR.getSecondValue().split(",");
      if (startendarray.length > 1) {
        String[] startDuty = startendarray[0].split(":");
        String[] endDuty = startendarray[1].split(":");
        if (startDuty.length > 2 && endDuty.length > 2) {
          Calendar startDutyCal = Calendar.getInstance();
          startDutyCal.clear();
          startDutyCal.setTime(startDate);
          if (startDate != null) {
            startDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                Integer.valueOf(startDuty[2]));
          }
          Date startDutyDate = startDutyCal.getTime();
          Calendar endDutyCal = Calendar.getInstance();
          endDutyCal.clear();
          if (startDate != null) {
            endDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                Integer.valueOf(endDuty[2]));
          }
          Date endDutyDate;
          if (Integer.valueOf(startDuty[0]) > Integer.valueOf(endDuty[0])) {//tac dong dem
            Calendar startDutyCalCheck = Calendar.getInstance();
            startDutyCalCheck.clear();
            if (startDate != null) {
              startDutyCalCheck
                  .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                      0, 0, 0);
            }
            Date checkstartDutyDate = startDutyCalCheck.getTime();
            Calendar endDutyCalCheck = startDutyCalCheck;
            endDutyCalCheck.clear();
            if (endDate != null) {
              endDutyCalCheck.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                  0, 0, 0);
            }
            Date checkendDutyDate = endDutyCalCheck.getTime();//1445014800000 | 1445014800000
            if (startDate != null) {
              if (endDate != null && checkstartDutyDate.equals(checkendDutyDate)) {//Cung ngay
                if (endDate.getHours() <= (Integer.valueOf(endDuty[0])
                    + 1)) {//sang hom sau (00h-5h)
                  startDutyCal
                      .set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate() - 1,
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                      Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                      Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                } else {//dem hom truoc (23h-24h)
                  startDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(),
                          startDate.getDate() + 1,
                          Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                          Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                }
              } else {
                endDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate() + 1,
                        Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                        Integer.valueOf(endDuty[2]));
//                        endDutyDate = endDutyCal.getTime();
              }
            }
          }
//          endDutyCal.add(Calendar.MINUTE, 1);
          endDutyDate = endDutyCal.getTime();
          if (startDate != null && endDate != null) {
            if (startDate.compareTo(startDutyDate) < 0
                || endDate.compareTo(endDutyDate) > 0) {
              return I18n.getChangeManagement("cr.msg.timecr.not.in.duty.date");
            }
          }
        }
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    }
    return "";
  }

  public String validateDeviceIp(CrDTO crDTO, String nationCode) throws Exception {
    if (crDTO.getLstNetworkNodeId() == null || crDTO.getLstNetworkNodeId().isEmpty()) {
      return "";
    }
    String impact = "";
    List<CrImpactedNodesDTO> lstImpact = new ArrayList<CrImpactedNodesDTO>();
    if (!crDTO.getLstNetworkNodeId().isEmpty()) {
      for (CrImpactedNodesDTO impactedNodesDTO : crDTO.getLstNetworkNodeId()) {
        if (!StringUtils.isStringNullOrEmpty(impactedNodesDTO.getIp())) {
          InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
          infraDeviceDTO.setIp(impactedNodesDTO.getIp());
          infraDeviceDTO.setNationCode(nationCode);
          List<InfraDeviceDTO> lst = commonStreamServiceProxy
              .getListInfraDeviceIpV2SrProxy(infraDeviceDTO);
          Boolean check = true;
          if (lst != null && !lst.isEmpty()) {
            for (InfraDeviceDTO deviceDTO : lst) {
              if (!StringUtils.isStringNullOrEmpty(deviceDTO.getDeviceCode())) {
                CrImpactedNodesDTO nodesDTO = new CrImpactedNodesDTO();
                nodesDTO.setCrId(crDTO.getCrId());
                nodesDTO.setIpId(lst.get(0).getIpId());
                nodesDTO.setIp(lst.get(0).getIp());
                nodesDTO.setDeviceId(lst.get(0).getDeviceId());
                nodesDTO.setDeviceCode(lst.get(0).getDeviceCode());
                nodesDTO.setDeviceName(lst.get(0).getDeviceName());
                nodesDTO.setInsertTime(DateTimeUtils.getSysDateTime());
                lstImpact.add(nodesDTO);
                check = false;
              }
            }
            if (check) {
              impact = impact + "," + impactedNodesDTO.getIp();
            }

          } else {
            impact = impact + "," + impactedNodesDTO.getIp();
          }
        } else {
          return "Ip in LstNetworkNodeId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
      }
    }
    crDTO.getLstNetworkNodeId().clear();
    crDTO.getLstNetworkNodeId().addAll(lstImpact);

    String affect = "";
    List<CrAffectedNodesDTO> lstAffect = new ArrayList<CrAffectedNodesDTO>();
    if (crDTO.getLstNetworkNodeIdAffected() != null && !crDTO.getLstNetworkNodeIdAffected()
        .isEmpty()) {
      for (CrAffectedNodesDTO affectedNodesDTO : crDTO.getLstNetworkNodeIdAffected()) {
        if (!StringUtils.isStringNullOrEmpty(affectedNodesDTO.getIp())) {
          InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
          infraDeviceDTO.setIp(affectedNodesDTO.getIp());
          infraDeviceDTO.setNationCode(nationCode);
          List<InfraDeviceDTO> lst = commonStreamServiceProxy
              .getListInfraDeviceIpV2SrProxy(infraDeviceDTO);

          if (lst != null && !lst.isEmpty()) {
            Boolean check = true;
            for (InfraDeviceDTO deviceDTO : lst) {
              if (!StringUtils.isStringNullOrEmpty(deviceDTO.getDeviceCode())) {
                CrAffectedNodesDTO nodesDTO = new CrAffectedNodesDTO();
                nodesDTO.setCrId(crDTO.getCrId());
                nodesDTO.setIpId(lst.get(0).getIpId());
                nodesDTO.setIp(lst.get(0).getIp());
                nodesDTO.setDeviceId(lst.get(0).getDeviceId());
                nodesDTO.setDeviceCode(lst.get(0).getDeviceCode());
                nodesDTO.setDeviceName(lst.get(0).getDeviceName());
                nodesDTO.setInsertTime(DateTimeUtils.getSysDateTime());
                lstAffect.add(nodesDTO);
                check = false;
              }
            }
            if (check) {
              impact = impact + "," + affectedNodesDTO.getIp();
            }
          } else {
            affect = affect + "," + affectedNodesDTO.getIp();
          }
        } else {
          return "Ip in LstNetworkNodeIdAffected " + I18n
              .getChangeManagement("cr.msg.must.be.not.null");
        }
      }
      crDTO.getLstNetworkNodeIdAffected().clear();
      crDTO.getLstNetworkNodeIdAffected().addAll(lstAffect);
    }

    if (!"".equals(impact)) {
      return I18n.getChangeManagement("import.node.doesnot.exists") + " Ip in LstNetworkNodeId "
          + impact.substring(1, impact.length());
    }

    if (!"".equals(affect)) {
      return I18n.getChangeManagement("import.node.doesnot.exists")
          + " Ip in LstNetworkNodeIdAffected " + affect.substring(1, affect.length());
    }
    return "";
  }

  /*
  @Override
  public ResultDTO insertCrFileForMR(CrDTO crDTO, String system, List<String> lstMop) {
    ResultDTO rs = new ResultDTO(null, RESULT.SUCCESS, RESULT.SUCCESS);
    try {
      //them moi don vi phe duyet

      List<CrFilesAttachDTO> lstFile = new ArrayList<>();

      //them moi file
      if (lstMop != null && !lstMop.isEmpty()) {
        lstFile = getListFileFromMopMR(crDTO, system, crDTO.getCrNumber(), lstMop);
      }

      //luu thong tin db GNOC
      List<CrFilesAttachInsiteDTO> lstInsert = new ArrayList<>();
      for (CrFilesAttachDTO crFilesAttachDTO : lstFile) {
        lstInsert.add(crFilesAttachDTO.toModelOutSide());
      }
      String result = crFileAttachBusiness.insertListNoID(lstInsert);
      if (result != null && result.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        //goi sang tool
        if (lstMop != null && !lstMop.isEmpty()) {
          if ("VIPA".equals(system)) {
            wSVipaPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
            wSVipaPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          } else if ("VMSA".equals(system)) {
            wsVipaDdPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
            wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          } else if ("AAM".equals(system)) {
            for (String dtCode : lstMop) {
              wSTDTTPort.linkCr(crDTO.getCrNumber(), dtCode, crDTO.getChangeOrginatorName(),
                  crDTO.getTitle(), crDTO.getEarliestStartTime(), crDTO.getLatestStartTime(),
                  Long.valueOf(crDTO.getState()));
            }
          }
        }
        rs.setKey(RESULT.SUCCESS);
      } else {
        rs.setKey(result);
      }
    } catch (Exception ex) {
      log.info(ex.getMessage(), ex);
      rs.setMessage("Insert file tool error !");
      rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
      //xoa CR
      crBusiness.delete(Long.parseLong(crDTO.getCrId()));
      //goi WO
      WoDTOSearch dTOSearch = new WoDTOSearch();
      if (!StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
        dTOSearch.setUserId("256066");
        dTOSearch.setWoSystem("CR");
        dTOSearch.setWoSystemId(crDTO.getCrId());
        dTOSearch.setPage(1);
        dTOSearch.setPageSize(Integer.MAX_VALUE);
        List<WoDTOSearch> lst = woServiceProxy.getListDataSearchProxy(dTOSearch);
        if (lst != null && !lst.isEmpty()) {
          for (WoDTOSearch search : lst) {
            woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback CR", "CR");
          }
        }
      }
    }
    return rs;
  }

  public List<CrFilesAttachDTO> getListFileFromMopMR(CrDTO crDTO, String system, String crNumber,
      List<String> lstMop) throws Exception {
    List<CrFilesAttachDTO> lstFile = new ArrayList<>();
    if (lstMop != null && !lstMop.isEmpty()) {
      com.viettel.vipa.MopDetailOutputDTO vipaIpMop = null;
      com.viettel.vmsa.MopDetailOutputDTO vipaDdMop = null;
      String mopFile = null;
      String mopFileContent = null;
      String mopFileKpi = null;
      String mopFileKpiContent = null;
      String dtFileHistory = "";
      String fileRollback = null;
      String fileRollbackContent = null;
      List<String> lstMopFile = new ArrayList<>();
      List<String> lstMopFileContent = new ArrayList<>();
      List<String> lstmopFileKpi = new ArrayList<>();
      List<String> lstmopFileKpiContent = new ArrayList<>();
      List<String> lstmopFileRollback = new ArrayList<>();
      List<String> lstMopFileRollbackContent = new ArrayList<>();
      for (String mopCode : lstMop) {
        if (!StringUtils.isStringNullOrEmpty(mopCode)) {
          lstMopFile.clear();
          lstMopFileContent.clear();
          lstmopFileKpi.clear();
          lstmopFileKpiContent.clear();
          lstmopFileRollback.clear();
          lstMopFileRollbackContent.clear();
          if ("AAM".equalsIgnoreCase(system)) {

            MopResult mopResult = wSTDTTPort.getMopInfo(mopCode);
            if (mopResult == null || (mopResult != null && (mopResult.getMopInfos() == null
                || mopResult.getMopInfos().isEmpty()))) {
              throw new Exception(system + " not connection : " + mopCode);
            }
            List<MopInfo> mopInfos = mopResult.getMopInfos();
//            MopInfo mopInfo = mopResult.getMopInfos().get(0);
            for (MopInfo mopInfo : mopInfos) {
              mopFile = mopInfo.getMopFile();
              mopFileContent = mopInfo.getMopFileContent();
              fileRollback = mopInfo.getMopRollbackFile();
              fileRollbackContent = mopInfo.getMopRollbackFileContent();
              if (StringUtils.isStringNullOrEmpty(mopFile) || StringUtils
                  .isStringNullOrEmpty(mopFileContent)
                  || StringUtils.isStringNullOrEmpty(fileRollback) || StringUtils
                  .isStringNullOrEmpty(fileRollbackContent)
              ) {
                throw new Exception(
                    I18n.getLanguage("mop.is.not.Exist").replaceAll("@mopCode", mopFile));
              }

              lstMopFile.add(mopFile);
              lstMopFileContent.add(mopFileContent);
              lstmopFileRollback.add(fileRollback);
              lstMopFileRollbackContent.add(fileRollbackContent);
            }

            dtFileHistory = I18n.getChangeManagement("qltn.insertFile");

          } else if ("VMSA".equalsIgnoreCase(system)) {
            dtFileHistory = "VIPA_DD_INSERT";

            vipaDdMop = wsVipaDdPort.getMopInfo(mopCode);

            if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
              if (vipaDdMop.getMopDetailDTO() == null) {
                throw new Exception(system + " Not found MopInfo : " + mopCode);
              }

            } else {
              throw new Exception(
                  system + " Call error: " + (vipaDdMop != null ? vipaDdMop.getResultCode() : null)
                      + " " + mopCode);
            }

            mopFile = vipaDdMop.getMopDetailDTO().getMopFileName();
            mopFileContent = vipaDdMop.getMopDetailDTO().getMopFileContent();
            mopFileKpi = vipaDdMop.getMopDetailDTO().getKpiFileName();
            mopFileKpiContent = vipaDdMop.getMopDetailDTO().getKpiFileContent();
            lstMopFile.add(mopFile);
            lstMopFileContent.add(mopFileContent);
            lstmopFileKpi.add(mopFileKpi);
            lstmopFileKpiContent.add(mopFileKpiContent);

          } else {
            dtFileHistory = "VIPA_IP_INSERT";

            vipaIpMop = wSVipaPort.getMopInfo(mopCode);
            if (vipaIpMop != null && vipaIpMop.getResultCode() == 0) {
              if (vipaIpMop.getMopDetailDTO() == null) {
                throw new Exception(system + " Not found MopInfo : " + mopCode);
              }
            } else {
              throw new Exception(
                  system + " Call error: " + (vipaIpMop != null ? vipaIpMop.getResultCode() : null)
                      + " " + mopCode);
            }

            mopFile = vipaIpMop.getMopDetailDTO().getMopFileName();
            mopFileContent = vipaIpMop.getMopDetailDTO().getMopFileContent();
            mopFileKpi = vipaIpMop.getMopDetailDTO().getKpiFileName();
            mopFileKpiContent = vipaIpMop.getMopDetailDTO().getKpiFileContent();
            lstMopFile.add(mopFile);
            lstMopFileContent.add(mopFileContent);
            lstmopFileKpi.add(mopFileKpi);
            lstmopFileKpiContent.add(mopFileKpiContent);
          }

          if (lstMopFile != null && lstMopFileContent != null && lstMopFileContent.size() > 0) {
            for (int i = 0; i < lstMopFile.size(); i++) {
              CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
              attachDTO.setDtCode(mopCode);
              attachDTO.setDtFileHistory(dtFileHistory);
              attachDTO.setFileType("3");
              attachDTO.setFileContent(lstMopFileContent.get(i));
              attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              attachDTO.setUserId(crDTO.getChangeOrginator());
              attachDTO.setCrId(crDTO.getCrId());
              byte[] fileContent = Base64.decode(attachDTO.getFileContent());
              Date date = new Date();
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, lstMopFile.get(i), fileContent,
                      date);
              String fullPathOld = FileUtils
                  .saveUploadFile(lstMopFile.get(i), fileContent, uploadFolder, date);
              attachDTO.setFileName(FileUtils.getFileName(fullPath));
              attachDTO.setFilePathFtp(fullPath);
              attachDTO.setFilePath(fullPathOld);
              lstFile.add(attachDTO);
            }
          } else {
            throw new Exception(
                I18n.getLanguage("mop.is.not.Exist").replaceAll("@mopCode", mopFile));
          }

          if (lstmopFileRollback != null && lstMopFileRollbackContent != null
              && lstmopFileRollback.size() > 0) {
            for (int i = 0; i < lstmopFileRollback.size(); i++) {
              CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
              attachDTO.setDtCode(mopCode);
              attachDTO.setDtFileHistory(dtFileHistory);
              attachDTO.setFileType("2");
              attachDTO.setFileContent(lstMopFileRollbackContent.get(i));
              attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              attachDTO.setUserId(crDTO.getChangeOrginator());
              attachDTO.setCrId(crDTO.getCrId());
              byte[] fileContent = Base64.decode(attachDTO.getFileContent());
              Date date = new Date();
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, lstMopFile.get(i), fileContent,
                      date);
              String fullPathOld = FileUtils
                  .saveUploadFile(lstmopFileRollback.get(i), fileContent, uploadFolder, date);
              attachDTO.setFileName(FileUtils.getFileName(fullPath));
              attachDTO.setFilePathFtp(fullPath);
              attachDTO.setFilePath(fullPathOld);
              lstFile.add(attachDTO);
            }
          }

          if (lstmopFileKpi != null && lstmopFileKpiContent != null && lstmopFileKpi.size() > 0) {
            for (int i = 0; i < lstmopFileKpi.size(); i++) {
              if (StringUtils.isNotNullOrEmpty(lstmopFileKpi.get(i)) && StringUtils
                  .isNotNullOrEmpty(lstmopFileKpiContent.get(i))) {
                CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
                attachDTO.setDtCode(mopCode);
                attachDTO.setDtFileHistory(dtFileHistory);
                attachDTO.setFileType("5");
                attachDTO.setFileContent(lstmopFileKpiContent.get(i));
                attachDTO.setCrId(crDTO.getCrId());
                attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                attachDTO.setUserId(crDTO.getChangeOrginator());
                byte[] fileContent = Base64.decode(attachDTO.getFileContent());
                Date date = new Date();
                String fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder, lstmopFileKpi.get(i),
                        fileContent, date);
                String fullPathOld = FileUtils
                    .saveUploadFile(lstmopFileKpi.get(i), fileContent, uploadFolder, date);
                attachDTO.setFileName(FileUtils.getFileName(fullPath));
                attachDTO.setFilePathFtp(fullPath);
                attachDTO.setFilePath(fullPathOld);
                lstFile.add(attachDTO);
              }
            }
          }
        }
      }
    }
    return lstFile;
  }
  */
}
