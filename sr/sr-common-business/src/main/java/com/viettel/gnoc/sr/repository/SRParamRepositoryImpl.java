package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRParamRepositoryImpl extends BaseRepository implements SRParamRepository {

  @Override
  public ResultInSideDto insertListSRParam(List<SRParamDTO> lsSrParam) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String lstId = "";
    for (SRParamDTO srParamDTO : lsSrParam) {
      try {
        SRParamEntity srParamEntity = getEntityManager().merge(srParamDTO.toEntity());
        lstId += srParamEntity.getSrParamId() + ",";
      } catch (Exception e) {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage(e.getMessage());
        return resultInSideDto;
      }
    }

    if (lstId.endsWith(",")) {
      lstId = lstId.substring(0, lstId.length() - 1);
      resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      resultInSideDto.setIdValue(lstId);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRParamEntity> findListSRParamBySrId(Long srId) {
    List<SRParamEntity> lstSrParam = findByMultilParam(SRParamEntity.class, "srId", srId);
    if (lstSrParam != null) {
      return lstSrParam;
    }
    return null;
  }
}
