package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import com.viettel.gnoc.incident.dto.TroubleFileIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.dto.UnitIbmDTO;
import com.viettel.gnoc.incident.repository.ProductIbmRepository;
import com.viettel.gnoc.incident.repository.TroublesIbmRepository;
import com.viettel.gnoc.incident.repository.UnitIbmRepository;
import com.viettel.security.PassTranformer;
import java.io.IOException;
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
public class TroublesIbmBusinessImpl implements TroublesIbmBusiness {

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
  TroublesIbmRepository troublesIbmRepository;

  @Autowired
  UnitIbmRepository unitIbmRepository;

  @Autowired
  ProductIbmRepository productIbmRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Override
  public Datatable getListTroublesIbmDTO(TroublesIbmDTO troublesIbmDTO) {
    log.debug("Request to getListTroublesIbmDTO: {}", troublesIbmDTO);
    return troublesIbmRepository.getListTroublesIbmDTO(troublesIbmDTO);
  }

  @Override
  public List<UnitIbmDTO> getListUnitIbmDTOCombobox() {
    log.debug("Request to getListUnitIbmDTOCombobox: {}");
    return unitIbmRepository.getListUnitIbmDTOCombobox();
  }

  @Override
  public List<ProductIbmDTO> getListProductIbmDTOCombobox() {
    log.debug("Request to getListProductIbmDTOCombobox: {}");
    return productIbmRepository.getListProductIbmDTOCombobox();
  }

  @Override
  public ResultInSideDto insertTroublesIbm(List<MultipartFile> files, TroublesIbmDTO troublesIbmDTO)
      throws IOException {
    UserToken userToken = ticketProvider.getUserToken();
    troublesIbmDTO.setCreateUserId(userToken.getUserID());
    troublesIbmDTO.setCreatedTime(new Date());
    ResultInSideDto resultInSideDto = troublesIbmRepository.insertTroublesIbm(troublesIbmDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (MultipartFile multipartFile : files) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), troublesIbmDTO.getCreatedTime());
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, troublesIbmDTO.getCreatedTime());
        TroubleFileIbmDTO troubleFileIbmDTO = new TroubleFileIbmDTO();
        String pathOld = FileUtils.getFilePath(fullPathOld);
        if (!pathOld.endsWith("/")) {
          pathOld = pathOld + "/";
        }
        troubleFileIbmDTO.setPath(pathOld);
        troubleFileIbmDTO.setFileName(FileUtils.getFileName(fullPathOld));
        troubleFileIbmDTO.setCreateTime(troublesIbmDTO.getCreatedTime());
        troubleFileIbmDTO.setCreateUserId(troublesIbmDTO.getCreateUserId());
        troubleFileIbmDTO.setTroubleIdIbm(resultInSideDto.getId());
        ResultInSideDto resultFileDataOld = troublesIbmRepository
            .insertTroubleFileIbm(troubleFileIbmDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(troublesIbmDTO.getCreatedTime());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES_IBM, resultInSideDto.getId(),
              gnocFileDtos);
    }
    return resultInSideDto;
  }

  @Override
  public TroublesIbmDTO findById(Long id) {
    TroublesIbmDTO dto = troublesIbmRepository.findById(id).toDTO();
    dto.setGnocFileDtos(troublesIbmRepository.getGnocFileIBM(id));
    return dto;
  }
}
