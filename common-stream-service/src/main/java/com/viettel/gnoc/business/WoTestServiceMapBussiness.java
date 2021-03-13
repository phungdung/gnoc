package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;

public interface WoTestServiceMapBussiness {

  List<WoTestServiceMapDTO> search(WoTestServiceMapDTO tDTO,
      int start, int maxResult, String sortType, String sortField);

  ResultDTO insertWoTestServiceMap(WoTestServiceMapDTO woTestServiceMapDTO);

  String insertOrUpdateListWoTestServiceMap(List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS);

}

