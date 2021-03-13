package com.viettel.gnoc.od.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.business.OdBusiness;
import com.viettel.gnoc.od.business.OdCommonBusiness;
import com.viettel.gnoc.od.business.OdParamBusiness;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusForm;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdParamDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OdServiceImpl implements OdService {

  @Autowired
  protected OdCommonBusiness odCommonBusiness;

  @Autowired
  protected OdParamBusiness odParamBusiness;

  @Autowired
  protected OdBusiness odBusiness;

  @Autowired
  protected OdCategoryServiceProxy odCategoryServiceProxy;

  @Resource
  private WebServiceContext wsContext;


  @Override
  public Integer getCountListDataSearchForOther(OdDTOSearch odDTO) {
    log.debug("Request to getCountListDataSearchForOther : {}", odDTO);
    OdSearchInsideDTO odSearchInsideDTO = odDTO.toOdSearchInsideDTO();
    return odCommonBusiness.getCountListDataSearchForOther(odSearchInsideDTO);
  }

  @Override
  public List<OdDTOSearch> getListDataSearchForOther(OdDTOSearch odDTOSearch, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListDataSearchForOther : {}", odDTOSearch);
    OdSearchInsideDTO odSearchInsideDTO = odDTOSearch.toOdSearchInsideDTO();
    odSearchInsideDTO.setPage(rowStart);
    odSearchInsideDTO.setPageSize(maxRow);
    List<OdSearchInsideDTO> listDto = odCommonBusiness.getListDataSearchForOther(odSearchInsideDTO);
    List<OdDTOSearch> listDtoOutside = new ArrayList<>();
    if (listDto != null && listDto.size() > 0) {
      for (OdSearchInsideDTO dto : listDto) {
        listDtoOutside.add(dto.toOdSearchDTO());
      }
    }
    return listDtoOutside;
  }

  @Override
  public List<OdDTOSearch> getListDataSearchVsmart(OdDTOSearch odDTOSearch) {
    try {
      OdSearchInsideDTO odSearchInsideDTO = odDTOSearch.toOdSearchInsideDTO();
      List<OdSearchInsideDTO> lstOdSearch = odCommonBusiness
          .getListDataSearchVsmart(odSearchInsideDTO);
      //them lst OdParam vao OdDTO
      OdSearchInsideDTO odDto = lstOdSearch.get(0);
      List<OdParamDTO> lstOdParam = odParamBusiness.getListOdParamByOdId(odDto.getOdId());
      List<OdDTOSearch> listOdSearchOutside = new ArrayList<>();
      odDto.setLstOdParam(lstOdParam);
      if (lstOdSearch != null && lstOdSearch.size() > 0) {
        for (OdSearchInsideDTO insideDTO : lstOdSearch) {
          listOdSearchOutside.add(insideDTO.toOdSearchDTO());
        }
      }
      return listOdSearchOutside;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO insertOdFromVsmart(List<ObjKeyValueVsmartDTO> lstObjDto, String userName,
      String odTypeCode, String woId, String insertSource, String createUnitCode,
      String crateUnitId) {
    ResultDTO result = new ResultDTO();
    I18n.setLocaleForService(wsContext);
    try {
      result = odCommonBusiness
          .insertOdFromVsmart(lstObjDto, userName, odTypeCode, woId, insertSource, createUnitCode,
              crateUnitId);
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey("");
      result.setMessage(e.getMessage());
      return result;
    }
  }

  @Override
  public ResultDTO insertOdFromOtherSystem(OdDTOSearch odDTO) {
    ResultDTO result = new ResultDTO();
    I18n.setLocaleForService(wsContext);
    //start time
    if (StringUtils.isStringNullOrEmpty(odDTO.getStartTime())) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("od.StartTime.isNotNull"));
      return result;
    } else if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getStartTime()))) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("od.StartTime.invalidFomat"));
      return result;
    }
    if (!StringUtils.isStringNullOrEmpty(odDTO.getEndTime()) && !""
        .equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getEndTime()))) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("od.EndTime.invalidFomat"));
      return result;
    }

    // create time
    if (StringUtils.isStringNullOrEmpty(odDTO.getCreateTime())) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("od.CreateTime.isNotNull"));
      return result;
    } else if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getCreateTime()))) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("od.CreateTime.invalidFomat"));
      return result;
    }
    OdSearchInsideDTO odSearchInsideDTO = odDTO.toOdSearchInsideDTO();
    try {
      result = odCommonBusiness.insertOdFromOtherSystem(odSearchInsideDTO);
      if (result != null && RESULT.SUCCESS.equals(result.getKey()) && StringUtils
          .isNotNullOrEmpty(result.getId())) {
        String[] arrOdId = result.getId().split("_");
        if (arrOdId != null && arrOdId.length > 0) {
          OdDTO dto = odBusiness.findOdById(Long.valueOf(arrOdId[arrOdId.length - 1]));
          List<OdDTO> lstOd = new ArrayList<>();
          lstOd.add(dto);
          result.setLstResult(lstOd);
        }
      }
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey("");
      result.setMessage(e.getMessage());
      return result;
    }

  }

  /*
    thuc hien cap nhat trang thai OD
    */
  @Override
  public ResultDTO changeStatusOd(OdChangeStatusForm odChangeStatusForm) {
    try {
      I18n.setLocaleForService(wsContext);
      return odBusiness.changeStatusOd(odChangeStatusForm);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public OdTypeDTO getInforByODType(String odTypeCode) {
    I18n.setLocaleForService(wsContext);
    return odCategoryServiceProxy.getInforByODType(odTypeCode);
  }

}
