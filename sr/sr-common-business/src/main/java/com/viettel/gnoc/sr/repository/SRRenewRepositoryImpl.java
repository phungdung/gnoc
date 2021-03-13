package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.model.SRRenewEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRRenewRepositoryImpl extends BaseRepository implements SRRenewRepository {

  @Override
  public ResultInSideDto insertSRRenew(SRRenewDTO srRenewDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    SRRenewEntity srRenewEntity = getEntityManager().merge(srRenewDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setId(srRenewEntity.getRenewId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateSRRenew(SRRenewDTO srRenewDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(srRenewDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public SRRenewDTO findSRRenewBySrId(Long srId) {
    List<SRRenewEntity> lstSRRenew = findByMultilParam(SRRenewEntity.class, "srId", srId);
    if (lstSRRenew != null && !lstSRRenew.isEmpty()) {
      return lstSRRenew.get(0).toDTO();
    }
    return null;
  }
}
