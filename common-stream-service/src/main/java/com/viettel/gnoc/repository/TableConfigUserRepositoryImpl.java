package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import com.viettel.gnoc.commons.model.TableConfigUserEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TableConfigUserRepositoryImpl extends
    BaseRepository<TableConfigUserDTO, TableConfigUserEntity> implements TableConfigUserRepository {

  @Override
  public List<TableConfigUserDTO> getListTableConfigUserDTO(TableConfigUserDTO tableConfigUserDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(TableConfigUserEntity.class, tableConfigUserDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<TableConfigUserDTO> getListTableConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new TableConfigUserEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseTableConfigUser(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String updateTableConfigUser(TableConfigUserDTO tableConfigUserDTO) {
    TableConfigUserEntity entity = tableConfigUserDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto insertTableConfigUser(TableConfigUserDTO tableConfigUserDTO) {
    return insertByModel(tableConfigUserDTO.toEntity(), colId);
  }

  @Override
  public String insertOrUpdateListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO) {
    for (TableConfigUserDTO item : tableConfigUserDTO) {
      TableConfigUserEntity entity = item.toEntity();
      if (entity.getTableConfigUserId() != null && entity.getTableConfigUserId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public TableConfigUserDTO findTableConfigUserById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TableConfigUserEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteTableConfigUser(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(deleteById(TableConfigUserEntity.class, id, colId));
    return resultInSideDto;
  }

  @Override
  public String deleteListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO) {
    return deleteByListDTO(tableConfigUserDTO, TableConfigUserEntity.class, colId);
  }

  private static final String colId = "tableConfigUserId";
}
