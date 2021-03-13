package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
@Repository
public interface GnocFileRepository {

  GnocFileEntity getGnocFileById(Long id);

  GnocFileDto getGnocFileByDto(GnocFileDto gnocFileDto);

  List<GnocFileDto> getListGnocFileByDto(GnocFileDto gnocFileDto);

  List<GnocFileDto> getListGnocFileForSR(GnocFileDto gnocFileDto);

  ResultInSideDto saveListGnocFile(String businessCode, Long businessId,
      List<GnocFileDto> gnocFileDtos);

  ResultInSideDto saveListGnocFileNotDeleteAll(String businessCode, Long businessId,
      List<GnocFileDto> gnocFileDtos);

  ResultInSideDto insertGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto updateGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto deleteGnocFile(String businessCode, Long businessId);

  ResultInSideDto deleteListGnocFile(String businessCode, Long businessId, List<Long> mappingIds);

  ResultInSideDto deleteGnocFileByDto(GnocFileDto gnocFileDto);

  ResultInSideDto deleteGnocFileByMapping(String businessCode, Long mappingId);

  List<GnocFileEntity> getLstGnocFileByDto(GnocFileDto gnocFileDto);

  List<GnocFileDto> findListGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto uploadFileCommon(List<MultipartFile> fileAttacks);
}
