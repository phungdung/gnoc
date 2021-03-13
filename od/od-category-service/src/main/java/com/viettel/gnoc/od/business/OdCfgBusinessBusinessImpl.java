package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.repository.OdCfgBusinessRepository;
import com.viettel.gnoc.od.repository.OdChangeStatusRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class OdCfgBusinessBusinessImpl implements OdCfgBusinessBusiness {

  @Autowired
  OdCfgBusinessRepository odCfgBusinessRepository;

  @Autowired
  OdChangeStatusRepository odChangeStatusRepository;

  @Override
  public List<OdCfgBusinessDTO> getListOdCfgBusinessByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return null;
  }

  @Override
  public String deleteListOdCfgBusiness(List<OdCfgBusinessDTO> odCfgBusinessListDTO) {
    List<Long> listIds = new ArrayList<>();
    odCfgBusinessListDTO.forEach(c -> {
      listIds.add(c.getId());
    });
    String checkConstraint = odCfgBusinessRepository.checkConstraint(listIds);
    if (checkConstraint == null || !checkConstraint.equals(RESULT.SUCCESS)) {
      return checkConstraint;
    }
    // Xóa key ngôn ngữ
    String deleteLocale = odCfgBusinessRepository.deleteLocaleList(listIds);
    if (deleteLocale == null || !deleteLocale.equals(RESULT.SUCCESS)) {
      return checkConstraint;
    }
    int countSuccess = odCfgBusinessRepository.deleteList(listIds);
    return (countSuccess > 0) ? RESULT.SUCCESS : RESULT.ERROR;

  }

  @Override
  public List<String> getSequenseOdCfgBusiness(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    return odCfgBusinessRepository.getListSequense(seqName, number);
  }

  @Override
  public String insertOrUpdateListOdCfgBusiness(List<OdCfgBusinessDTO> odCfgBusinessDTOs) {
    try {
      if (odCfgBusinessDTOs != null) {
        for (OdCfgBusinessDTO odCfgBusinessDTO : odCfgBusinessDTOs) {
          odCfgBusinessRepository.insertOrUpdate(odCfgBusinessDTO);
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public String updateOdCfgBusiness(OdCfgBusinessDTO odCfgBusinessDTO) {
    try {
      odCfgBusinessRepository.insertOrUpdate(odCfgBusinessDTO);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR + e.getMessage();
    }
  }

  @Override
  public ResultInSideDto insertOdCfgBusiness(OdCfgBusinessDTO odCfgBusinessDTO) {
    ResultInSideDto dto = new ResultInSideDto();
    try {
      Long id = odCfgBusinessRepository.insertOrUpdate(odCfgBusinessDTO);
      dto.setId(id);
      dto.setMessage(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      dto.setId(null);
      dto.setMessage(RESULT.ERROR);
    }
    return dto;
  }

  @Override
  public OdCfgBusinessDTO findOdCfgBusinessById(Long id) {
    if (id != null && id > 0) {
      return odCfgBusinessRepository.findOdCfgBusinessById(id);
    }
    return null;
  }

  @Override
  public List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(OdCfgBusinessDTO odCfgBusinessDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return null;
  }

  @Override
  public List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(String oldStatus, String newStatus,
      String odPriority, String odTypeId) {
    try {
      OdChangeStatusDTO odChangeStatusDTO = odChangeStatusRepository
          .getOdChangeStatusDTOByParams(oldStatus, newStatus, odPriority, odTypeId);
      List<OdCfgBusinessDTO> odCfgBusinessDTOS = odCfgBusinessRepository
          .getListOdCfgBusiness(odChangeStatusDTO);
      return odCfgBusinessDTOS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String deleteOdCfgBusiness(Long id) {
    return odCfgBusinessRepository.delete(id);
  }

  @Override
  public ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId) {
    return odCfgBusinessRepository.deleteByOdChangeStatusId(odChangeStatusId);
  }

  @Override
  public List<OdCfgBusinessDTO> getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO) {
    return odCfgBusinessRepository.getListOdCfgBusiness(odChangeStatusDTO);
  }
}
