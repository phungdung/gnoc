package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrUnitsScopeDeviceTypeDTO;
import com.viettel.gnoc.cr.model.CrUnitsScopeDeviceTypeEntity;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DungPV
 */
@Repository
@Transactional
public class CrUnitsScopeDeviceTypeRepositoryImpl extends BaseRepository implements
    CrUnitsScopeDeviceTypeRepository {

  @Override
  public ResultInSideDto addCrUnitsScopeDeviceType(
      CrUnitsScopeDeviceTypeDTO crUnitsScopeDeviceTypeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrUnitsScopeDeviceTypeEntity entity = getEntityManager()
        .merge(crUnitsScopeDeviceTypeDTO.toDTO());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long crUnitsScopeDeviceTypeId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrUnitsScopeDeviceTypeEntity entity = getEntityManager()
        .find(CrUnitsScopeDeviceTypeEntity.class, crUnitsScopeDeviceTypeId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListUnitsScopeDeviceType(List<Long> lstUnitsScopeDeviceTypeId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (Long id : lstUnitsScopeDeviceTypeId) {
      resultInSideDTO = delete(id);
    }
    return resultInSideDTO;
  }
}
