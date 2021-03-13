package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoMopInfoDTO;
import com.viettel.gnoc.wo.model.WoMopInfoEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoMopInfoRepository {


  ResultInSideDto saveOrUpdate(WoMopInfoDTO woMopInfoDTO);

  List<WoMopInfoEntity> getWoMopInfoByWoIdAndMopId(WoMopInfoDTO woMopInfoDTO);
}
