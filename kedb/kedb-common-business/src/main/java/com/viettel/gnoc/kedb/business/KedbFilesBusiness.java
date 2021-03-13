package com.viettel.gnoc.kedb.business;

import java.util.List;

public interface KedbFilesBusiness {

  List<com.viettel.gnoc.ws.dto.KedbFilesDTO> onSearch(
      com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO,int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
