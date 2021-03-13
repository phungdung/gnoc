package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.dto.SRApproveDTO;
import com.viettel.gnoc.sr.model.SRApproveEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRApproveRepositoryImpl extends BaseRepository implements SRApproveRepository {

  @Override
  public ResultInSideDto insertSRApprove(SRApproveDTO srApproveDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    SRApproveEntity srApproveEntity = getEntityManager().merge(srApproveDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setId(srApproveEntity.getApproveId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateSRApprove(SRApproveDTO srApproveDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(srApproveDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public SRApproveDTO findSRApproveBySrId(Long srId) {
    List<SRApproveEntity> lstSRApprove = findByMultilParam(SRApproveEntity.class, "srId", srId);
    if (lstSRApprove != null && !lstSRApprove.isEmpty()) {
      return lstSRApprove.get(0).toDTO();
    }
    return null;
  }
}
