package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.mr.business.MrMaterialDisplacementBusiness;
import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TrungDuong
 */
@Service
@Slf4j
public class MrMaterialDisplacementServiceImpl implements MrMaterialDisplacementService {

  @Autowired
  MrMaterialDisplacementBusiness mrMaterialDisplacementBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Override
  public List<MrMaterialDTO> getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO, String userManager,
      String woCode, int rowStart, int maxRow) {
    return mrMaterialDisplacementBusiness
        .getListMrMaterialDTO2(mrMaterialDTO, userManager, woCode, rowStart, maxRow);
  }

  @Override
  public String insertOrUpdateListMrMaterialDisplacement(
      List<MrMaterialDisplacementDTO> mrMaterialDisplacementDTO) {
    I18n.setLocaleForService(wsContext);
    if (mrMaterialDisplacementDTO != null && !mrMaterialDisplacementDTO.isEmpty()) {
      return mrMaterialDisplacementBusiness
          .insertOrUpdateListMrMaterialDisplacement(mrMaterialDisplacementDTO);
    }
    return RESULT.FAIL;
  }

  @Override
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(
      MrMaterialDisplacementDTO mrMaterialDisplacementDTO, int rowStart, int maxRow,
      List<MrMaterialDisplacementDTO> lstExclude) {
    I18n.setLocaleForService(wsContext);
    if (lstExclude != null && !lstExclude.isEmpty()) {
      List<Long> lstId = new ArrayList<>();
      for (MrMaterialDisplacementDTO id : lstExclude) {
        if (StringUtils.isNotNullOrEmpty(id.getMaterialId())) {
          lstId.add(Long.valueOf(id.getMaterialId()));
        }
      }
      if (lstId != null && !lstId.isEmpty()) {
        mrMaterialDisplacementDTO.setListId(lstId);
      }
    }
    List<MrMaterialDisplacementDTO> lst = mrMaterialDisplacementBusiness
        .getListMrMaterialDisplacementDTO2(mrMaterialDisplacementDTO);
    if (maxRow > 0) {
      if (lst != null && lst.size() >= rowStart) {
        List<MrMaterialDisplacementDTO> lstResult = new ArrayList<>();
        if (lst.size() < maxRow) {
          for (int i = rowStart; i < lst.size(); i++) {
            lstResult.add(lst.get(i));
          }
        } else {
          maxRow = maxRow + rowStart;
          for (int i = rowStart; i < maxRow; i++) {
            lstResult.add(lst.get(i));
          }
        }
        return lstResult;
      }
    }
    return lst;
  }


}
