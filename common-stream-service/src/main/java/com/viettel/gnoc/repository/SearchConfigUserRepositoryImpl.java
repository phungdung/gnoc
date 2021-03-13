package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
import com.viettel.gnoc.commons.model.SearchConfigUserEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SearchConfigUserRepositoryImpl extends
    BaseRepository<SearchConfigUserDTO, SearchConfigUserEntity> implements
    SearchConfigUserRepository {

  @Override
  public List<SearchConfigUserDTO> getListSearchConfigUserDTO(
      SearchConfigUserDTO searchConfigUserDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(SearchConfigUserEntity.class, searchConfigUserDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<SearchConfigUserDTO> getListSearchConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new SearchConfigUserEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseSearchConfigUser(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String updateSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    SearchConfigUserEntity entity = searchConfigUserDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto insertSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    return insertByModel(searchConfigUserDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto insertOrUpdateListSearchConfigUser(
      SearchConfigUserDTO searchConfigUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    deleteByMultilParam(SearchConfigUserEntity.class,
        "userId", searchConfigUserDTO.getUserId(),
        "userName", searchConfigUserDTO.getUserName(),
        "funcKey", searchConfigUserDTO.getFuncKey());
    for (SearchConfigUserDTO item : searchConfigUserDTO.getSearchConfigUserDTOS()) {
      item.setUserId(searchConfigUserDTO.getUserId());
      item.setUserName(searchConfigUserDTO.getUserName());
      item.setFuncKey(searchConfigUserDTO.getFuncKey());
      SearchConfigUserEntity entity = item.toEntity();
      getEntityManager().merge(entity);
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public SearchConfigUserDTO findSearchConfigUserById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(SearchConfigUserEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public String deleteSearchConfigUser(Long id) {
    return deleteById(SearchConfigUserEntity.class, id, colId);
  }

  @Override
  public ResultInSideDto deleteListSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    deleteByMultilParam(SearchConfigUserEntity.class,
        "userId", searchConfigUserDTO.getUserId(),
        "userName", searchConfigUserDTO.getUserName(),
        "funcKey", searchConfigUserDTO.getFuncKey());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  private static final String colId = "searchConfigUserId";
}
