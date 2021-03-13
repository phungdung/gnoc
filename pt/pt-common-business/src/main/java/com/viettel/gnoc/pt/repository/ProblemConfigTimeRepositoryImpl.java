package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.model.ProblemConfigTimeEntity;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
@Repository
@Slf4j
public class ProblemConfigTimeRepositoryImpl extends BaseRepository implements  ProblemConfigTimeRepository{


//  @Override
//  public List<CatItemDTO> getListComboboxGroupReasonOrSolution(String codeCategory) {
//    Map<String, Object> parameters = new HashMap<>();
//    String sqlQuery = "select * from COMMON_GNOC.CATEGORY where 1 = 1 ";
//    if (id != null && !"".equals(id)) {
//      sqlQuery += "and PARENT_CATEGORY_ID = :id";
//      parameters.put("id", id);
//    }
////    sqlQuery += " order by position,NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese')";
//    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
//        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
//    return list;
//  }

  @Override
  public Datatable onSearchProbleConfigTime(ProblemConfigTimeDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(), ProblemConfigTimeDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public ResultInSideDto insertProblemConfigTime(ProblemConfigTimeDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if(checkExisted(dto.getReasonGroupId().toString(), dto.getSolutionTypeId().toString(), dto.getTypeId().toString(), dto.getSubCategoryId().toString(), null)){
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("problemConfigTime.existed"));
      return resultInSideDto;
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    ProblemConfigTimeEntity problemConfigTimeEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(problemConfigTimeEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateProblemConfigTime(ProblemConfigTimeDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if(checkExisted(dto.getReasonGroupId().toString(), dto.getSolutionTypeId().toString(), dto.getTypeId().toString(), dto.getSubCategoryId().toString(), dto.getId().toString())){
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("problemConfigTime.existed"));
      return resultInSideDto;
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    ProblemConfigTimeEntity problemConfigTimeEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(problemConfigTimeEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ProblemConfigTimeDTO checkProblemConfigTimeCreateExit(ProblemConfigTimeDTO dto) {
    List<ProblemConfigTimeEntity> dataEntity = (List<ProblemConfigTimeEntity>) findByMultilParam(
        ProblemConfigTimeEntity.class,
        "reasonGroupId", dto.getReasonGroupId(),
        "solutionTypeId", dto.getSolutionTypeId(), "typeId", dto.getTypeId(), "subCategoryId", dto.getSubCategoryId());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(ProblemConfigTimeDTO dto){
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS_CONFIG_TIME, "search-by-problem-config-time");
    Map<String, Object> params = new HashMap<>();
    if (!StringUtils.isLongNullOrEmpty(dto.getReasonGroupId())) {
      sql += " AND C1.REASON_GROUP_ID = :reason_group_id ";
      params.put("reason_group_id", dto.getReasonGroupId());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getSolutionTypeId())) {
      sql += " AND C1.SOLUTION_TYPE_ID = :solution_type_id ";
      params.put("solution_type_id", dto.getSolutionTypeId());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getTimeProcess())) {
      sql += " AND C1.TIME_PROCESS = :time_process ";
      params.put("time_process", dto.getTimeProcess());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getTypeId())) {
      sql += " AND C1.TYPE_ID = :type_id ";
      params.put("type_id", dto.getTypeId());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getSubCategoryId())) {
      sql += " AND C1.SUB_CATEGORY_ID = :sub_category_id ";
      params.put("sub_category_id", dto.getSubCategoryId());
    }

    sql += "order by C1.CREATE_DATE ASC";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public List<ProblemConfigTimeDTO> onSearchExport(ProblemConfigTimeDTO problemConfigTimeDTO) {
    BaseDto baseDto = sqlSearch(problemConfigTimeDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(ProblemConfigTimeDTO.class));
  }

  @Override
  public boolean checkExisted(String reasonGroupId, String solutionTypeId, String typeId, String subCatagoryId, String id) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("Select * from one_tm.CFG_TIME_PROBLEM_PROCESS C1 WHERE 1=1 AND C1.REASON_GROUP_ID = :reason_group_id AND C1.SOLUTION_TYPE_ID = :solution_type_id AND C1.TYPE_ID = :type_id AND C1.SUB_CATEGORY_ID = :sub_category_id ");
    parameters.put("reason_group_id", reasonGroupId);
    parameters.put("solution_type_id", solutionTypeId);
    parameters.put("type_id", typeId);
    parameters.put("sub_category_id", subCatagoryId);
    List<ProblemConfigTimeDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(ProblemConfigTimeDTO.class));
    if(!StringUtils.isStringNullOrEmpty(id) ){
      for(int i=0;i <list.size() ;i++){
        if(list.get(i).getId().toString().equals(id)){
          list.remove(i);
          break;
        }
      }
    }
    if(list != null && list.size() > 0){
      return true;
    }
    return false;
  }

  @Override
  public ProblemConfigTimeDTO findProblemConfigTimeById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ProblemConfigTimeEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public String deleteProblemConfigTime(Long id) {
    return deleteById(ProblemConfigTimeEntity.class, id, colId);
  }

  private static final String colId = "id";
}
