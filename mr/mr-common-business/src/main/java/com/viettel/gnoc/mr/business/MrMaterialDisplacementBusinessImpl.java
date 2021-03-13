package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.mr.repository.MrMaterialDisplacementRepository;
import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrMaterialDisplacementBusinessImpl implements MrMaterialDisplacementBusiness {

  @Autowired
  MrMaterialDisplacementRepository mrMaterialDisplacementRepository;

  @Override
  public List<MrMaterialDTO> getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO, String userManager,
      String woCode, int rowStart, int maxRow) {
    return mrMaterialDisplacementRepository.getListMrMaterialDTO2(mrMaterialDTO, userManager, woCode, rowStart, maxRow);
  }

  @Override
  public String insertOrUpdateListMrMaterialDisplacement(
      List<MrMaterialDisplacementDTO> lstDTO) {
    ResultDTO resultDTO = new ResultDTO();
    String result = RESULT.FAIL;
    if (lstDTO != null && !lstDTO.isEmpty()) {
      for (MrMaterialDisplacementDTO mrMaterialDisplacementDTO : lstDTO) {
        if (StringUtils.isNotNullOrEmpty(mrMaterialDisplacementDTO.getDateTime())
            && StringUtils.isNotNullOrEmpty(
            DataUtil.validateDateTimeDdMmYyyy_HhMmSs(mrMaterialDisplacementDTO.getDateTime()))) {
          return result + "_" + I18n.getLanguage("mrMaterialDisplacementDTO.dateTime");
        }
        resultDTO = mrMaterialDisplacementRepository
            .insertOrUpdateListMrMaterialDisplacement(mrMaterialDisplacementDTO);
      }
    }
    if (resultDTO != null && RESULT.SUCCESS.equals(resultDTO.getKey())) {
      result = RESULT.SUCCESS;
    }
    return result;
  }

  @Override
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(
      MrMaterialDisplacementDTO dto) {
    return mrMaterialDisplacementRepository.getListMrMaterialDisplacementDTO2(dto);
  }
}
