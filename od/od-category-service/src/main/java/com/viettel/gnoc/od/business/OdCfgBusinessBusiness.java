package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import java.util.List;

/**
 * @author TienNV
 */
public interface OdCfgBusinessBusiness {

  List<OdCfgBusinessDTO> getListOdCfgBusinessByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  String deleteListOdCfgBusiness(List<OdCfgBusinessDTO> odCfgBusinessListDTO);

  List<String> getSequenseOdCfgBusiness(String seqName, int... size);

  String insertOrUpdateListOdCfgBusiness(List<OdCfgBusinessDTO> odCfgBusinessDTOs);

  String updateOdCfgBusiness(OdCfgBusinessDTO odCfgBusinessDTO);

  OdCfgBusinessDTO findOdCfgBusinessById(Long id);

  List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(OdCfgBusinessDTO odCfgBusinessDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  String deleteOdCfgBusiness(Long id);

  ResultInSideDto insertOdCfgBusiness(OdCfgBusinessDTO odCfgBusinessDTO);

  List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(String oldStatus, String newStatus,
      String odPriority, String odTypeId);

  ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId);

  List<OdCfgBusinessDTO> getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO);
}
