package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleBtsHisFileRepository {

  ResultInSideDto deleteMrScheduleBtsHisFileByID(Long idFile);

  List<MrScheduleBtsHisFileDTO> getListMrScheduleBtsHisFileDTO(MrScheduleBtsHisFileDTO dto,
      int rowStart, int maxRow);

  MrScheduleBtsHisFileDTO findById(Long id);
}
