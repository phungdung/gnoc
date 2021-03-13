package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import com.viettel.gnoc.wo.model.MaterialCodienEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MaterialCodienRepositoryImpl extends BaseRepository implements
    MaterialCodienRepository {

  @Override
  public List<MaterialCodienDTO> getListMaterialCodienByWoId(Long woId) {
    List<MaterialCodienEntity> listEntity = findByMultilParam(MaterialCodienEntity.class, "woId",
        woId);
    List<MaterialCodienDTO> listDTO = new ArrayList<>();
    if (listEntity != null && listEntity.size() > 0) {
      for (MaterialCodienEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
    }
    return listDTO;
  }

  @Override
  public ResultInSideDto deleteMaterialCodien(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MaterialCodienEntity entity = getEntityManager().find(MaterialCodienEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto inserOrUpdateMaterialCodien(MaterialCodienDTO materialCodienDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(materialCodienDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }
}
