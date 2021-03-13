package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.*;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Transactional
public class SrNocProBusinessImpl implements SrNocProBusiness {

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  SrRepository srRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  SRConfigRepository srConfigRepository;

  @Autowired
  SRCatalogRepository2 srCatalogRepository2;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Autowired
  SRHisRepository srHisRepository;

  @Autowired
  SrOutsideBusiness srOutsideBusiness;

  @Override
  public List<SRCatalogDTO> getListServiceArraySR(String countryId) {
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    try {
      lstReturn = validateCountry(countryId);
      if (!lstReturn.isEmpty()) {
        return lstReturn;
      }
      List<SRConfigDTO> lstArray = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY);
      if (lstArray == null || lstArray.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstArray.null")));
        return lstReturn;
      }
      SRCatalogDTO catalog = new SRCatalogDTO();
      catalog.setCountry(countryId);
      List<SRCatalogDTO> lst = srCatalogRepository2.getListSRCatalogDTO(catalog);
      if (lst == null || lst.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstCatalog.null")));
        return lstReturn;
      }
      List<SRConfigDTO> lstConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC);
      if (lstConfig == null || lstConfig.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstNoc.null")));
        return lstReturn;
      }

      for (SRCatalogDTO item : lst) {
        if (countryId.equals(item.getCountry())
            && !containsArray(lstReturn, item.getServiceArray())
            && containsConfig(lstConfig, item.getServiceCode())) {

          Optional<SRConfigDTO> arrayDto = lstArray.stream()
              .filter(a -> item.getServiceArray().equals(a.getConfigCode())).findAny();
          if (arrayDto.isPresent()) {
            SRCatalogDTO itemNew = new SRCatalogDTO();
            itemNew.setServiceArray(item.getServiceArray());
            itemNew.setServiceArrayName(arrayDto.get().getConfigName());
            itemNew.setCountry(item.getCountry());
            lstReturn.add(itemNew);
          }
        }
      }
      if (lstReturn.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key,
            I18n.getLanguage("sr.error.listDataNull").replace("list", "service array")));
      }
      return lstReturn;
    } catch (Exception e) {
      lstReturn.clear();
      log.error(e.getMessage(), e);
      lstReturn.add(new SRCatalogDTO(key, e.getMessage()));
      return lstReturn;
    }
  }

  @Override
  public List<SRCatalogDTO> getListServiceGroupSR(String countryId) {
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    try {
      lstReturn = validateCountry(countryId);
      if (!lstReturn.isEmpty()) {
        return lstReturn;
      }
      List<SRConfigDTO> lstGroup = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.SERVICE_GROUP);
      if (lstGroup == null || lstGroup.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstGroup.null")));
        return lstReturn;
      }
      SRCatalogDTO catalog = new SRCatalogDTO();
      catalog.setCountry(countryId);
      List<SRCatalogDTO> lst = srCatalogRepository2.getListSRCatalogDTO(catalog);
      if (lst == null || lst.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstCatalog.null")));
        return lstReturn;
      }
      List<SRConfigDTO> lstConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC);
      if (lstConfig == null || lstConfig.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstNoc.null")));
        return lstReturn;
      }
      for (SRCatalogDTO item : lst) {
        if (countryId.equals(item.getCountry())
            && !containsGroup(lstReturn, item.getServiceGroup())
            && containsConfig(lstConfig, item.getServiceCode())) {

          Optional<SRConfigDTO> groupDto = lstGroup.stream()
              .filter(a -> item.getServiceGroup().equals(a.getConfigCode())).findAny();
          if (groupDto.isPresent()) {
            SRCatalogDTO itemNew = new SRCatalogDTO();
            itemNew.setServiceArray(groupDto.get().getParentCode());
            itemNew.setServiceGroupName(groupDto.get().getConfigName());
            itemNew.setServiceGroup(groupDto.get().getConfigCode());
            itemNew.setCountry(item.getCountry());
            lstReturn.add(itemNew);
          }
        }
      }
      if (lstReturn.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key,
            I18n.getLanguage("sr.error.listDataNull").replace("list", "service group")));
      }
      return lstReturn;
    } catch (Exception e) {
      lstReturn.clear();
      lstReturn.add(new SRCatalogDTO(key, e.getMessage()));
      log.error(e.getMessage(), e);
      return lstReturn;
    }
  }

  @Override
  public List<SRCatalogDTO> getListServiceSR(String countryId) {
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    try {
      lstReturn = validateCountry(countryId);
      if (!lstReturn.isEmpty()) {
        return lstReturn;
      }
      SRCatalogDTO catalog = new SRCatalogDTO();
      catalog.setCountry(countryId);
      List<SRCatalogDTO> lst = srCatalogRepository2.getListSRCatalogDTO(catalog);
      if (lst == null || lst.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstCatalog.null")));
        return lstReturn;
      }
      List<SRConfigDTO> lstConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC);
      if (lstConfig == null || lstConfig.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstNoc.null")));
        return lstReturn;
      }
      for (SRCatalogDTO item : lst) {
        if (countryId.equals(item.getCountry())
            && !containsService(lstReturn, item.getServiceCode())
            && containsConfig(lstConfig, item.getServiceCode())) {
          SRCatalogDTO itemNew = new SRCatalogDTO();
          itemNew.setServiceId(item.getServiceId());
          itemNew.setServiceArray(item.getServiceArray());
          itemNew.setServiceGroup(item.getServiceGroup());
          itemNew.setServiceCode(item.getServiceCode());
          itemNew.setServiceName(item.getServiceName());
          itemNew.setCountry(item.getCountry());
          lstReturn.add(itemNew);
        }
      }
      if (lstReturn.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key,
            I18n.getLanguage("sr.error.listDataNull").replace("list", "service")));
      }
      return lstReturn;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lstReturn.clear();
      lstReturn.add(new SRCatalogDTO(key, e.getMessage()));
      return lstReturn;
    }
  }

  @Override
  public List<SRCatalogDTO> getListUnitServiceSR(String countryId) {
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    try {
      lstReturn = validateCountry(countryId);
      if (!lstReturn.isEmpty()) {
        return lstReturn;
      }
      List<SRCatalogDTO> lst = srCatalogRepository2.getListCatalogWithRoleAndUnit("UNIT");
      if (lst == null || lst.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstUnit.null")));
        return lstReturn;
      }
      List<SRConfigDTO> lstConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC);
      if (lstConfig == null || lstConfig.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstNoc.null")));
        return lstReturn;
      }
      UnitDTO objSearchUnit = new UnitDTO();
      objSearchUnit.setStatus(1L);
      List<UnitDTO> lstUnit = unitRepository.getListUnit(objSearchUnit);
      if (lstUnit == null || lstUnit.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstUnitGnoc.null")));
        return lstReturn;
      }
      for (SRCatalogDTO item : lst) {
        if (countryId.equals(item.getCountry()) && containsConfig(lstConfig,
            item.getServiceCode())) {
          if (!StringUtils.isStringNullOrEmpty(item.getStatus())) {
            if (item.getStatus().equals(Constants.SR_CATALOG.ACTIVE)) {
              SRCatalogDTO catalog = new SRCatalogDTO();
              catalog.setCountry(countryId);
              catalog.setServiceCode(item.getServiceCode());
              List<UnitDTO> lstUnitCatalog = new ArrayList<>();
              String[] arrUnit = item.getExecutionUnit().split(",");
              Map<String, String> mapCheck = new HashMap<>();
              for (String unit : arrUnit) {
                unit = unit.trim();
                if (mapCheck.get(unit) == null) {
                  mapCheck.put(unit, unit);
                  for (UnitDTO uDto : lstUnit) {
                    if (unit.equals(uDto.getUnitId().toString())) {
                      UnitDTO u = new UnitDTO();
                      u.setUnitId(uDto.getUnitId());
                      u.setUnitCode(uDto.getUnitCode());
                      u.setUnitName(uDto.getUnitName());
                      lstUnitCatalog.add(u);
                      break;
                    }
                  }
                }
              }
              catalog.setLstUnit(lstUnitCatalog);
              lstReturn.add(catalog);
            }
          }
        }
      }
      if (lstReturn.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key,
            I18n.getLanguage("sr.error.listDataNull").replace("list", "unit")));
      }
      return lstReturn;
    } catch (Exception e) {
      lstReturn.clear();
      lstReturn.add(new SRCatalogDTO(key, e.getMessage()));
      return lstReturn;
    }
  }

  @Override
  public List<SRCatalogDTO> getListRoleServiceSR(String countryId) {
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    try {
      lstReturn = validateCountry(countryId);
      if (!lstReturn.isEmpty()) {
        return lstReturn;
      }
      List<SRCatalogDTO> lst = srCatalogRepository2.getListCatalogWithRoleAndUnit("ROLE");
      if (lst == null || lst.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstRole.null")));
        return lstReturn;
      }
      List<SRConfigDTO> lstConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC);
      if (lstConfig == null || lstConfig.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstNoc.null")));
        return lstReturn;
      }
      SRRoleDTO dtoRole = new SRRoleDTO();
      dtoRole.setCountry(countryId);
      List<SRRoleDTO> lstRole = srCategoryServiceProxy.getListSRRoleDTO(dtoRole);
      if (lstRole == null || lstRole.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.lstRole.error")));
        return lstReturn;
      }
      for (SRCatalogDTO item : lst) {
        if (countryId.equals(item.getCountry()) && containsConfig(lstConfig,
            item.getServiceCode())) {
          SRCatalogDTO catalog = new SRCatalogDTO();
          catalog.setCountry(countryId);
          catalog.setServiceCode(item.getServiceCode());
          if (!StringUtils.isStringNullOrEmpty(item.getRoleCode())) {
            List<SRRoleDTO> lstRoleCatalog = new ArrayList<>();
            String[] arrRole = item.getRoleCode().split(",");
            Map<String, String> mapCheck = new HashMap<>();
            for (String r : arrRole) {
              r = r.trim();
              if (mapCheck.get(r) == null) {
                mapCheck.put(r, r);
                for (SRRoleDTO rDto : lstRole) {
                  if (r.trim().equalsIgnoreCase(rDto.getRoleCode())) {
                    SRRoleDTO rDtoNew = new SRRoleDTO();
                    rDtoNew.setRoleId(rDto.getRoleId());
                    rDtoNew.setRoleCode(r);
                    rDtoNew.setRoleName(rDto.getRoleName());
                    lstRoleCatalog.add(rDtoNew);
                    break;
                  }
                }
              }
            }
            catalog.setLstRole(lstRoleCatalog);
          }
          lstReturn.add(catalog);
        }
      }
      if (lstReturn.isEmpty()) {
        lstReturn.add(new SRCatalogDTO(key,
            I18n.getLanguage("sr.error.listDataNull").replace("list", "role")));
      }
      return lstReturn;
    } catch (Exception e) {
      lstReturn.clear();
      lstReturn.add(new SRCatalogDTO(key, e.getMessage()));
      return lstReturn;
    }
  }

  @Override
  public ResultDTO getListStatusSR(List<String> lstSrCode, List<String> lstStatus, String fromDate,
      String toDate) {
    ResultDTO res = new ResultDTO();
    List<SRDTO> lstReturn = new ArrayList<>();
    String key = Constants.RESULT.FAIL;
    res.setKey(key);
    try {
      if (StringUtils.isStringNullOrEmpty(fromDate)) {
        res.setMessage(I18n.getLanguage("sr.error.fromDate.null"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(toDate)) {
        res.setMessage(I18n.getLanguage("sr.error.toDate.null"));
        return res;
      }
      SRDTO dtoSearch = new SRDTO();
      dtoSearch.setInsertSource("NOCPRO");
      SrInsiteDTO sr = dtoSearch.toInsideDTO();
      int page = (int) Math.ceil((0 + 1) * 1.0 / Integer.MAX_VALUE);
      sr.setPage(page);
      sr.setPageSize(Integer.MAX_VALUE);
      List<SrInsiteDTO> lstSR = (List<SrInsiteDTO>) srRepository.getListSR(sr).getData();
      if (lstSR == null || lstSR.isEmpty()) {
        res.setMessage(I18n.getLanguage("sr.error.listDataNull").replace("list", "SR"));
        res.setKey(Constants.RESULT.SUCCESS);
        return res;
      }
      for (SrInsiteDTO dto : lstSR) {
//        Date dUpdateTime = DateTimeUtils.convertStringToDateTime(dto.getUpdatedTime() + ":00");
        Date dUpdateTime = dto.getUpdatedTime();
        try {
          Date dFrom = DateTimeUtils.convertStringToDateTime(fromDate);
          if (DateTimeUtils.compareDateTime(dFrom, dUpdateTime) == 1) {
            //neu dFrom > dUpdateTime
            continue;
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          res.setMessage(I18n.getLanguage("sr.error.fromDate.invalid"));
          return res;
        }
        try {
          Date dTo = DateTimeUtils.convertStringToDateTime(toDate);
          if (DateTimeUtils.compareDateTime(dUpdateTime, dTo) == 1) {
            //neu dTo < dUpdateTime
            continue;
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          res.setMessage(I18n.getLanguage("sr.error.toDate.invalid"));
          return res;
        }
        if (lstStatus != null && !lstStatus.isEmpty() && !lstStatus.contains("")) {
          if (!lstStatus.contains(dto.getStatus())) {
            continue;
          }
        }
        if (lstSrCode != null && !lstSrCode.isEmpty() && !lstSrCode.contains("")) {
          Optional<String> srCode = lstSrCode.stream()
              .filter(code -> dto.getSrCode().contains(code)).findAny();
          if (!srCode.isPresent()) {
            continue;
          }
        }
        SRDTO item = new SRDTO();
        item.setSrCode(dto.getSrCode());
        item.setStatus(dto.getStatus());
        item.setSrUser(dto.getSrUser());

        SRHisDTO dtoHis = new SRHisDTO();
        dtoHis.setSrStatus(dto.getStatus());
        dtoHis.setSrId(dto.getSrId().toString());
        List<SRHisDTO> lstHis = srHisRepository.getListSRHisDTO(dtoHis, 0, 0, "asc", "createdTime");
        item.setUpdatedTime((lstHis == null || lstHis.isEmpty()) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(dto.getUpdatedTime()) : lstHis.get(0).getCreatedTime());

        lstReturn.add(item);
      }
      if (lstReturn.isEmpty()) {
        res.setMessage(I18n.getLanguage("sr.error.listDataNull").replace("list", "SR"));
        res.setKey(Constants.RESULT.SUCCESS);
        return res;
      }
      res.setLstResult(lstReturn);
      res.setKey(Constants.RESULT.SUCCESS);
      res.setMessage(Constants.RESULT.SUCCESS);
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setMessage(e.getMessage());
      return res;
    }
  }

  @Override
  public ResultDTO createSRForNoc(SRDTO srDTO) {
    ResultDTO res = new ResultDTO();
    try {
      srDTO.setInsertSource("NOCPRO");
      res = srOutsideBusiness.createSRByConfigGroup(srDTO, Constants.SR_CONFIG.DICH_VU_NOC);
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return res;
  }

  private boolean containsService(final List<SRCatalogDTO> list, final String serviceCode) {
    return list.stream().filter(o -> serviceCode.equals(o.getServiceCode())).findFirst()
        .isPresent();
  }

  private boolean containsArray(final List<SRCatalogDTO> list, final String serviceArray) {
    return list.stream().filter(o -> serviceArray.equals(o.getServiceArray())).findFirst()
        .isPresent();
  }

  private boolean containsGroup(final List<SRCatalogDTO> list, final String serviceGroup) {
    return list.stream().filter(o -> serviceGroup.equals(o.getServiceGroup())).findFirst()
        .isPresent();
  }

  private boolean containsConfig(final List<SRConfigDTO> list, final String configCode) {
    return list.stream().filter(o -> configCode.equals(o.getConfigCode())).findFirst().isPresent();
  }

  private List<SRCatalogDTO> validateCountry(String countryId) {
    String key = Constants.RESULT.FAIL;
    List<SRCatalogDTO> lstReturn = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(countryId)) {
      lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.countryIsNotNull")));
      return lstReturn;
    } else {
      if (!StringUtils.isLong(countryId)) {
        lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.country.invalid")));
        return lstReturn;
      } else {
        List<CatLocationDTO> lstLocation = catLocationRepository.getCatLocationByLevel("1");
        if (lstLocation == null || lstLocation.isEmpty()) {
          lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.cantFindListCountry5")));
          return lstReturn;
        } else {
          if (!lstLocation.stream().filter(c -> countryId.equals(c.getLocationId())).findAny()
              .isPresent()) {
            lstReturn.add(new SRCatalogDTO(key, I18n.getLanguage("sr.error.cantFindListCountry5")));
            return lstReturn;
          }
        }
      }
    }
    return lstReturn;
  }
}
