package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import java.util.List;

public interface KedbFilesRepository {

  List<KedbFilesDTO> onSearch(com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList);

}
