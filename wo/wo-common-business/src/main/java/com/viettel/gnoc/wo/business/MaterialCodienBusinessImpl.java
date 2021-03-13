package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import com.viettel.gnoc.wo.repository.MaterialCodienRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MaterialCodienBusinessImpl implements MaterialCodienBusiness {

  @Autowired
  MaterialCodienRepository materialCodienRepository;

  @Override
  public List<MaterialCodienDTO> getListMaterialCodienByWoId(Long woId) {
    return materialCodienRepository.getListMaterialCodienByWoId(woId);
  }

  @Override
  public ResultInSideDto deleteMaterialCodienByWoId(Long woId) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      List<MaterialCodienDTO> lst = getListMaterialCodienByWoId(woId);
      if (lst != null && lst.size() > 0) {
        for (MaterialCodienDTO i : lst) {
          result = materialCodienRepository.deleteMaterialCodien(i.getId());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(RESULT.ERROR);
    }
    return result;
  }

  @Override
  public ResultInSideDto inserOrUpdateMaterialCodien(MaterialCodienDTO materialCodienDTO) {
    return materialCodienRepository.inserOrUpdateMaterialCodien(materialCodienDTO);
  }
}
