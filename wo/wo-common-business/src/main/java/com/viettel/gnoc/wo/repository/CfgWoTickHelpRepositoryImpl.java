package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpInsideDTO;
import com.viettel.gnoc.wo.model.CfgWoTickHelpEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgWoTickHelpRepositoryImpl extends BaseRepository implements CfgWoTickHelpRepository {

  @Override
  public ResultInSideDto add(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgWoTickHelpEntity cfgWoTickHelpEntity = getEntityManager()
        .merge(cfgWoTickHelpInsideDTO.toEntity());
    resultInSideDTO.setId(cfgWoTickHelpEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<CfgWoTickHelpEntity> search(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO) {
    return findByMultilParam(CfgWoTickHelpEntity.class
        , "id", cfgWoTickHelpInsideDTO.getId()
        , "status", cfgWoTickHelpInsideDTO.getStatus()
        , "systemName", cfgWoTickHelpInsideDTO.getSystemName()
        , "tickHelpId", cfgWoTickHelpInsideDTO.getTickHelpId()
        , "woId", cfgWoTickHelpInsideDTO.getWoId()
    );
  }

  @Override
  public List<CfgWoTickHelpInsideDTO> searchEntity(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO) {
    return onSearchEntity(CfgWoTickHelpEntity.class, cfgWoTickHelpInsideDTO,
        cfgWoTickHelpInsideDTO.getPage(), cfgWoTickHelpInsideDTO.getPageSize(),
        cfgWoTickHelpInsideDTO.getSortType(),
        cfgWoTickHelpInsideDTO.getSortName());
  }

  @Override
  public List<CfgWoTickHelpEntity> searchTwoParam(CfgWoTickHelpInsideDTO cfgWoTickHelpInsideDTO) {
    return findByMultilParam(CfgWoTickHelpEntity.class
        , "status", cfgWoTickHelpInsideDTO.getStatus()
        , "woId", cfgWoTickHelpInsideDTO.getWoId()
    );
  }
}
