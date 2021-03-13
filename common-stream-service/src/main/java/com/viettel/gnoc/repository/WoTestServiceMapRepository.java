package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTestServiceMapRepository {

  List<WoTestServiceMapDTO> getListWoTestServiceMapDTO(WoTestServiceMapDTO woTestServiceMapDTO, int rowStart, int maxRow, String sortType, String sortFieldList);

  ResultDTO createObject(WoTestServiceMapDTO woTestServiceMapDTO);

  String insertList(List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS);
}
