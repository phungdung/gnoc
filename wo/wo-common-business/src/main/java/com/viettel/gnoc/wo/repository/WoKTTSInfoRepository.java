package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoKTTSInfoRepository {

  ResultInSideDto insertWoKTTSInfo(WoKTTSInfoDTO woKTTSInfoDTO);

  ResultInSideDto inserOrUpdateWoKTTSInfo(WoKTTSInfoDTO woKTTSInfoDTO);

  List<WoKTTSInfoDTO> getListWoKTTSInfoByWoId(Long woId);

}
