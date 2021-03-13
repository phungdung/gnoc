package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class GnocFileBusinessImpl implements GnocFileBusiness {

  @Autowired
  private GnocFileRepository gnocFileRepository;

  @Override
  public GnocFileEntity getGnocFileById(Long id) {
    log.debug("Request to getGnocFileById : {}");
    return gnocFileRepository.getGnocFileById(id);
  }

  @Override
  public GnocFileDto getGnocFileByDto(GnocFileDto gnocFileDto) {
    log.debug("Request to getGnocFileByDto : {}");
    return gnocFileRepository.getGnocFileByDto(gnocFileDto);
  }

  @Override
  public List<GnocFileDto> getListGnocFileByDto(GnocFileDto gnocFileDto) {
    log.debug("Request to getListGnocFileByDto : {}");
    return gnocFileRepository.getListGnocFileByDto(gnocFileDto);
  }

  @Override
  public ResultInSideDto insertGnocFile(GnocFileDto gnocFileDto) {
    log.debug("Request to insertGnocFile : {}");
    return gnocFileRepository.insertGnocFile(gnocFileDto);
  }

  @Override
  public ResultInSideDto updateGnocFile(GnocFileDto gnocFileDto) {
    log.debug("Request to updateGnocFile : {}");
    return gnocFileRepository.updateGnocFile(gnocFileDto);
  }

  @Override
  public ResultInSideDto deleteGnocFile(String businessCode, Long businessId) {
    log.debug("Request to deleteGnocFile : {}");
    return gnocFileRepository.deleteGnocFile(businessCode, businessId);
  }

  @Override
  public List<GnocFileDto> findListGnocFile(GnocFileDto gnocFileDto){
    log.debug("Request to getGnocFile : {}");
    return gnocFileRepository.findListGnocFile(gnocFileDto);
  }

  @Override
  public  ResultInSideDto uploadFileCommon(List<MultipartFile> fileAttacks){
    log.debug("Request to uploadFileCommon");
    return gnocFileRepository.uploadFileCommon(fileAttacks);
  }
}
