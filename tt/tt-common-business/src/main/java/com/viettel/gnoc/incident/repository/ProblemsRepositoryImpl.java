package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemsRepositoryImpl extends BaseRepository implements ProblemsRepository {

  @Override
  public Datatable getListProblems(ProblemsInsideDTO problemsInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_PROBLEMS, "get_list_problems");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("relatedTt", problemsInsideDTO.getRelatedTt());
    parameters.put("page", problemsInsideDTO.getPage());
    parameters.put("pageSize", problemsInsideDTO.getPageSize());
    return getListDataTableBySqlQuery(sql, parameters, problemsInsideDTO.getPage(),
        problemsInsideDTO.getPageSize(), ProblemsInsideDTO.class, problemsInsideDTO.getSortName(),
        problemsInsideDTO.getSortType());
  }
}
