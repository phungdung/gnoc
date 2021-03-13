package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CategoryEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CategoryRepositoryImpl extends BaseRepository implements CategoryRepository {

  public String getSQL(CategoryDTO categoryDTO, Map<String, Object> parameters) {
    String sqlQuery = "select cg.CATEGORY_ID categoryId,"
        + "cg.CATEGORY_NAME categoryName,cg.CATEGORY_CODE categoryCode,"
        + "cg.DESCRIPTION description, cg.EDITABLE editable,"
        + "cg.PARENT_CATEGORY_ID parentCategoryId from COMMON_GNOC.CATEGORY cg where 1=1 ";

    if (StringUtils.isNotNullOrEmpty(categoryDTO.getSearchAll())) {
      sqlQuery += " AND ( "
          + " lower(cg.CATEGORY_CODE) LIKE :searchAll ESCAPE '\\' "
          + " OR lower(cg.CATEGORY_NAME) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(categoryDTO.getSearchAll()));
    }

    if (StringUtils.isNotNullOrEmpty(categoryDTO.getCategoryName())) {
      sqlQuery += " and lower(cg.CATEGORY_NAME) LIKE :categoryName ESCAPE '\\' ";
      parameters.put("categoryName",
          StringUtils.convertLowerParamContains(categoryDTO.getCategoryName()));
    }

    if (StringUtils.isNotNullOrEmpty(categoryDTO.getCategoryCode())) {
      sqlQuery += " and lower(cg.CATEGORY_CODE) LIKE :categoryCode ESCAPE '\\' ";
      parameters.put("categoryCode",
          StringUtils.convertLowerParamContains(categoryDTO.getCategoryCode()));
    }

    if (categoryDTO.getParentCategoryId() != null) {
      sqlQuery += " and cg.PARENT_CATEGORY_ID = :parentId ";
      parameters.put("parentId",
          categoryDTO.getParentCategoryId());
    }

    sqlQuery += " order by cg.CATEGORY_NAME asc";
    return sqlQuery;
  }

  @Override
  public List<CategoryDTO> getListAllCategory(CategoryDTO categoryDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(categoryDTO, parameters);
    List<CategoryDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CategoryDTO.class));
    return list;
  }

  @Override
  public Datatable getListCategoryDTO(CategoryDTO categoryDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(categoryDTO, parameters);
    Datatable datatable = getListDataTableBySqlQuery(sqlQuery, parameters,
        categoryDTO.getPage(), categoryDTO.getPageSize(), CategoryDTO.class,
        categoryDTO.getSortName(), categoryDTO.getSortType());

    String system = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    String business = Constants.COMMON_TRANSLATE_BUSINESS.CATEGORY.toString();

    List<CategoryDTO> lst = (List<CategoryDTO>) datatable.getData();
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);

    try {
      lst = setLanguage(lst, lstLanguage, "2", "CATEGORY_NAME");
      datatable.setData(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public ResultInSideDto updateCategory(CategoryDTO categoryDTO) {
    return insertOrUpdate(categoryDTO);
  }

  public ResultInSideDto insertOrUpdate(CategoryDTO categoryDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CategoryEntity entity = getEntityManager().merge(categoryDTO.toEntity());
    resultDto.setId(entity.getCategoryId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteCategory(Long id) {
    return delete(id);
  }

  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CategoryEntity entity = getEntityManager().find(CategoryEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCategory(List<CategoryDTO> categoryListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (CategoryDTO item : categoryListDTO) {
      resultInSideDto = delete(item.getCategoryId());
    }
    return resultInSideDto;
  }

  @Override
  public CategoryDTO findCategoryById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CategoryEntity entity = getEntityManager().find(CategoryEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCategory(CategoryDTO categoryDTO) {
    return insertOrUpdate(categoryDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListCategory(List<CategoryDTO> categoryDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (CategoryDTO item : categoryDTO) {
      resultInSideDto = insertOrUpdate(item);
    }
    return resultInSideDto;
  }

  @Override
  public List<CategoryDTO> getListCategory(CategoryDTO categoryDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(categoryDTO, parameters);
    List<CategoryDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CategoryDTO.class));

    String system = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    String business = Constants.COMMON_TRANSLATE_BUSINESS.CATEGORY.toString();
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);

    try {
      list = setLanguage(list, lstLanguage, "2", "CATEGORY_NAME");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public boolean checkNameExist(String name) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery =
        "select cg.CATEGORY_ID categoryId from COMMON_GNOC.CATEGORY cg where cg.CATEGORY_NAME = :p_category_name ";
    parameters.put("p_category_name", name);
    List<CategoryDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CategoryDTO.class));
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }
}
