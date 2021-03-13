package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTFilesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrUngCuuTTRepository {

  Datatable getListMrUctt(MrUngCuuTTDTO mrUngCuuTTDTO);

  ResultInSideDto insertMrUcttFilesDTO(MrUngCuuTTFilesDTO mrUngCuuTTFilesDTO);

  ResultInSideDto insertMrUcttDTO(MrUngCuuTTDTO mrUngCuuTTDTO);

  String getConfigUCTTForCreateWo(String configGroup, String configCode);

  List<MrUngCuuTTDTO> getDataExport(MrUngCuuTTDTO dto);
}
