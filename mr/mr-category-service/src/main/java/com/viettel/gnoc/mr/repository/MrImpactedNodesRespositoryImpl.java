package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import com.viettel.gnoc.maintenance.model.MrImpactedNodesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrImpactedNodesRespositoryImpl extends BaseRepository implements
    MrImpactedNodesRespository {

  @Override
  public String insertList(List<MrImpactedNodesDTO> mrImpactedNodesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (!mrImpactedNodesDTO.isEmpty() && mrImpactedNodesDTO.size() > 0) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      for (MrImpactedNodesDTO item : mrImpactedNodesDTO) {
        MrImpactedNodesEntity entity = item.toEntity();
        if (entity.getMinsId() != null && entity.getMinsId() > 0) {
          getEntityManager().merge(entity);
        } else {
          getEntityManager().persist(entity);
        }
      }
    } else {
      resultInSideDto.setKey(RESULT.FAIL);
    }
    return resultInSideDto.getKey();
  }

  @Override
  public ResultInSideDto deleteImpactNodeByMrId(String mrId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Long id = Long.valueOf(mrId);
    resultInSideDto.setKey(deleteById(MrImpactedNodesEntity.class, id, colId));
    return resultInSideDto;
  }

  @Override
  public List<MrImpactedNodesDTO> getImpactedNodesByMrId(String mrId) {
    Map<String, Object> mapParam = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_IMPACT_NODES, "get-Impacted-Nodes-By-MrId");
    mapParam.put("mrId", mrId);
    List<MrImpactedNodesDTO> lst = getNamedParameterJdbcTemplate().query(sql, mapParam,
        BeanPropertyRowMapper.newInstance(MrImpactedNodesDTO.class));
    return lst;
  }

  private static final String colId = "minsId";
}
