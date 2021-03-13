package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrFilesAttachRepository {

  List<GnocFileDto> getListMrFilesSearch(GnocFileDto dto);

  ResultInSideDto add(MrFilesAttachDTO mrFilesAttachDTO);

  ResultInSideDto deleteFileById(Long fileId);
}
