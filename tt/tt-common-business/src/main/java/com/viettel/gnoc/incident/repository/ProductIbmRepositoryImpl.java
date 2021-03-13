package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProductIbmRepositoryImpl extends BaseRepository implements ProductIbmRepository {

  @Override
  public List<ProductIbmDTO> getListProductIbmDTOCombobox() {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_PRODUCT_IBM,
        "get-List-Product-Ibm-DTO-Combobox");
    return getNamedParameterJdbcTemplate()
        .query(sql, (Map<String, ?>) null, BeanPropertyRowMapper.newInstance(ProductIbmDTO.class));
  }
}
