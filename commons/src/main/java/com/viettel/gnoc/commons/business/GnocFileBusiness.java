package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
@Service
public interface GnocFileBusiness {

  GnocFileEntity getGnocFileById(Long id);

  GnocFileDto getGnocFileByDto(GnocFileDto gnocFileDto);

  List<GnocFileDto> getListGnocFileByDto(GnocFileDto gnocFileDto);

  ResultInSideDto insertGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto updateGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto deleteGnocFile(String businessCode, Long businessId);

  List<GnocFileDto> findListGnocFile(GnocFileDto gnocFileDto);

  ResultInSideDto uploadFileCommon(List<MultipartFile> fileAttacks);
}
