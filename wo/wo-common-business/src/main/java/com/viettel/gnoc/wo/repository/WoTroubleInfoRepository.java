package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTroubleInfoRepository {

  WoTroubleInfoDTO getWoTroubleInfoByWoId(Long woId);

  ResultInSideDto insertWoTroubleInfo(WoTroubleInfoDTO woTroubleInfoDTO);

  List<WoTroubleInfoDTO> getListWoTroubleInfoByWoId(Long woId);

}
