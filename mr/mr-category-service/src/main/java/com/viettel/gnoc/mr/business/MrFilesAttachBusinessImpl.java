package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
import com.viettel.gnoc.mr.repository.MrFilesAttachRepository;
import com.viettel.security.PassTranformer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrFilesAttachBusinessImpl implements MrFilesAttachBusiness {

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
  UserRepository userRepository;

  @Autowired
  MrFilesAttachRepository mrFilesAttachRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Override
  public List<GnocFileDto> getListMrFilesSearch(GnocFileDto dto) {
    UserToken userToken = TicketProvider.getUserToken();
    List<GnocFileDto> lstFilesAttachResultDTO = new ArrayList<>();
    dto.setBusinessCode(GNOC_FILE_BUSSINESS.MR);
    List<GnocFileDto> lst = gnocFileRepository.getListGnocFileByDto(dto);
    try {
      Double offset = userRepository.getOffsetFromUser(userToken.getUserID());
      if (lst != null && !lst.isEmpty()) {
        for (GnocFileDto mrFilesAttachResultDTO : lst) {
          //time zone
          SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          if (mrFilesAttachResultDTO.getCreateTime() != null && !""
              .equals(mrFilesAttachResultDTO.getCreateTime())) {
            Date d = spd
                .parse(DateTimeUtils.date2ddMMyyyyHHMMss(mrFilesAttachResultDTO.getCreateTime()));
            mrFilesAttachResultDTO.setCreateTime(DateTimeUtils.convertStringToDateTime(
                spd.format(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)))));
          }
          //time zone
          lstFilesAttachResultDTO.add(mrFilesAttachResultDTO);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstFilesAttachResultDTO;
  }

  @Override
  public ResultInSideDto deleteFile(GnocFileDto gnocFileDto) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      if (!StringUtils.isStringNullOrEmpty(gnocFileDto.getMappingId())) {
        result = mrFilesAttachRepository.deleteFileById(gnocFileDto.getMappingId());
      } else {
        result.setKey(RESULT.ERROR);
        result.setMessage(I18n.getLanguage("mr.file.delete.fail"));
        return result;
      }
      if (gnocFileDto != null) {
        result = gnocFileRepository.deleteGnocFileByDto(gnocFileDto);
      }
      result.setKey(RESULT.SUCCESS);
      result.setMessage(I18n.getLanguage("mr.file.delete.success"));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return result;
  }

  @Override
  public ResultInSideDto insertFile(List<MultipartFile> lstMultipartFiles,
      MrFilesAttachDTO mrFilesAttachDTO) throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (int i = 0; i < lstMultipartFiles.size(); i++) {
      MultipartFile multipartFile = lstMultipartFiles.get(i);
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), null);
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, null);
      String fileName = multipartFile.getOriginalFilename();
      //Save file MrFileAttach
      MrFilesAttachDTO filesAttachDTO = new MrFilesAttachDTO();
      filesAttachDTO.setFileName(fileName);
      filesAttachDTO.setTimeAttack(DateTimeUtils.getSysDateTime());
      filesAttachDTO.setUserId(userToken.getUserID().toString());
      filesAttachDTO.setMrId(mrFilesAttachDTO.getMrId());
      filesAttachDTO.setFilePath(fullPathOld);
      filesAttachDTO.setFileType(
          mrFilesAttachDTO.getFileType() == null ? null : mrFilesAttachDTO.getFileType());
      result = mrFilesAttachRepository.add(filesAttachDTO);

      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setFileType(
          mrFilesAttachDTO.getFileType() == null ? null : mrFilesAttachDTO.getFileType());
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(new Date());
      gnocFileDto.setMappingId(result.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    if (RESULT.SUCCESS.equals(result.getKey())) {
      result = gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR,
              Long.valueOf(mrFilesAttachDTO.getMrId()),
              gnocFileDtos);
    }

    return result;
  }
}
