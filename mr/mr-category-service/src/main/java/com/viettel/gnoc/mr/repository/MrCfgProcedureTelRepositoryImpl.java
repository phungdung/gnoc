package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureTelEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgProcedureTelRepositoryImpl extends BaseRepository implements
    MrCfgProcedureTelRepository {

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Override
  public Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO, String type) {
    BaseDto baseDto = sqlGetListFullNameCfgProcedureDTO(mrCfgProcedureTelDTO, type);
    List<MrCfgProcedureTelDTO> lstData = new ArrayList<>();
    Datatable datatable = new Datatable();
    if (isCustomColumnSearch(mrCfgProcedureTelDTO)) {
      lstData = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrCfgProcedureTelDTO.class));
    } else {
      datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
          mrCfgProcedureTelDTO.getPage(),
          mrCfgProcedureTelDTO.getPageSize(), MrCfgProcedureTelDTO.class,
          mrCfgProcedureTelDTO.getSortName(), mrCfgProcedureTelDTO.getSortType());
      if (datatable != null) {
        lstData = (List<MrCfgProcedureTelDTO>) datatable.getData();
      }
    }
    if (datatable != null && lstData != null) {
      setDataSearchMap(lstData, false);
      buildSortCustom(lstData, mrCfgProcedureTelDTO);
      if (isCustomColumnSearch(mrCfgProcedureTelDTO)) {
        int totalSize = lstData.size();
        int size = mrCfgProcedureTelDTO.getPageSize();
        int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
        DataUtil.subPageList(lstData, mrCfgProcedureTelDTO.getPage(),
            mrCfgProcedureTelDTO.getPageSize());
        if (lstData != null && lstData.size() > 0) {
          lstData.get(0).setTotalRow(totalSize);
          lstData.get(0).setPage(pageSize);
          lstData.get(0).setPageSize(size);
        }
        datatable.setTotal(totalSize);
        datatable.setPages(pageSize);
        datatable.setData(lstData);
      } else {
        datatable.setData(lstData);
      }
    }
    return datatable;
  }


  @Override
  public List<MrCfgProcedureTelDTO> onSearchExport(MrCfgProcedureTelDTO mrCfgProcedureBtsDTO,
      String type) {
    BaseDto baseDto = sqlGetListFullNameCfgProcedureDTO(mrCfgProcedureBtsDTO, type);
    List<MrCfgProcedureTelDTO> lstData = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrCfgProcedureTelDTO.class));
    if (lstData != null && lstData.size() > 0) {
      setDataSearchMap(lstData, true);
    }
    return lstData;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
        .merge(mrCfgProcedureTelDTO.toEntity());
    resultInSideDto.setId(mrCfgProcedureTelEntity.getProcedureId());
    return resultInSideDto;
  }

  @Override
  public String insertOrUpdateList(List<MrCfgProcedureTelDTO> listDTO) {
    if (listDTO != null && listDTO.size() > 0) {
      for (MrCfgProcedureTelDTO dto : listDTO) {
        insertOrUpdate(dto);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto delete(Long procedureId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
        .find(MrCfgProcedureTelEntity.class, procedureId);
    getEntityManager().remove(mrCfgProcedureTelEntity);
    return resultInSideDto;
  }

  @Override
  public MrCfgProcedureTelDTO getDetail(Long procedureId) {
    MrCfgProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
        .find(MrCfgProcedureTelEntity.class, procedureId);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = mrCfgProcedureTelEntity.toDTO();
    return mrCfgProcedureTelDTO;
  }

  @Override
  public List<MrDeviceDTO> getListNetworkType() {
    String sqlQuery = " SELECT DISTINCT ARRAY_CODE arrayCode, NETWORK_TYPE networkType "
        + " FROM MR_DEVICE WHERE NETWORK_TYPE IS NOT NULL ORDER BY NETWORK_TYPE ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }

  @Override
  public List<MrDeviceDTO> getListDeviceType2() {
    String sqlQuery =
        " SELECT DISTINCT ARRAY_CODE arrayCode, NETWORK_TYPE networkType, DEVICE_TYPE deviceType "
            + " FROM MR_DEVICE WHERE DEVICE_TYPE IS NOT NULL ORDER BY ARRAY_CODE ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }

  private BaseDto sqlGetListFullNameCfgProcedureDTO(MrCfgProcedureTelDTO cfgProcedureDTO,
      String type) {
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_PROCEDURE_TEL, "on-search"));
    BaseDto baseDto = new BaseDto();
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    params.put("bussiness", APPLIED_BUSSINESS.CAT_ITEM);
    params.put("p_leeLocale", I18n.getLocale());
    if (cfgProcedureDTO != null) {
      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getSearchAll())) {
        if ("H".equalsIgnoreCase(type)) {
          sql.append(
              " AND ( LOWER(CP.procedureName) LIKE :searchAll ESCAPE '\\' OR LOWER(CP.CYCLE) LIKE :searchAll ESCAPE '\\' ) ");
        } else {
          sql.append(" AND ( LOWER(CP.procedureName) LIKE :searchAll ESCAPE '\\' ) ");
        }
        params
            .put("searchAll",
                StringUtils.convertLowerParamContains(cfgProcedureDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getMarketName())) {
        sql.append(" AND LOWER(CP.marketName) like :marketName ESCAPE '\\' ");
        params.put("marketName",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getMarketName()));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getCycleType())) {
        sql.append(" AND CP.cycleType = :cycleType ");
        params.put("cycleType", cfgProcedureDTO.getCycleType());
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getMarketCode())) {
        sql.append(" AND UPPER(CP.marketCode) = :marketCode ");
        params.put("marketCode", cfgProcedureDTO.getMarketCode().toUpperCase());
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getArrayCode())) {
        sql.append(" AND UPPER(CP.arrayCodeName) = :arrayCode ");
        params.put("arrayCode", cfgProcedureDTO.getArrayCode().toUpperCase());
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getProcedureName())) {
        sql.append(" AND LOWER(CP.procedureName) like :procedureName ESCAPE '\\' ");
        params.put("procedureName",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getProcedureName()));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getStatus())) {
        sql.append(" AND UPPER(CP.STATUS) = :status ");
        params.put("status", cfgProcedureDTO.getStatus().toUpperCase());
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getDeviceType())) {
        sql.append(" AND UPPER(CP.deviceType) = :deviceType ");
        params.put("deviceType", cfgProcedureDTO.getDeviceType().toUpperCase());
      }
      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getNetworkType())) {
        sql.append(" AND UPPER(CP.networkType) = :networkType ");
        params.put("networkType", cfgProcedureDTO.getNetworkType().toUpperCase());
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getMrMode())) {
        sql.append(" AND UPPER(CP.mrMode) = :mrMode ");
        params.put("mrMode", cfgProcedureDTO.getMrMode().toUpperCase());
      }
      if (cfgProcedureDTO.getCycle() != null) {
        sql.append(" AND CP.CYCLE = :cycle ");
        params.put("cycle", cfgProcedureDTO.getCycle());
      }

      //duongnt edit
      if (cfgProcedureDTO.getArrayChild() != null) {
        sql.append("AND CP.arrayChild = :arrayChild ");
        params.put("arrayChild", cfgProcedureDTO.getArrayChild());
      }
      sql.append(" ORDER BY marketCode,arrayCode,mrMode ");
    }
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(params);
    return baseDto;
  }

  private void buildSortCustom(List<MrCfgProcedureTelDTO> lstData,
      MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    if ("ArrayChild".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getArrayChild()));
      } else {
        lstData.sort(Comparator.comparing(o -> o.getArrayChild(), Comparator.reverseOrder()));
      }
    } else if ("MrWorksName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getMrWorksName()));
      } else {
        lstData.sort(Comparator.comparing(o -> o.getMrWorksName(), Comparator.reverseOrder()));
      }
    } else if ("ArrayActorName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getArrayActorName()));
      } else {
        lstData.sort(Comparator.comparing(o -> o.getArrayActorName(), Comparator.reverseOrder()));
      }
    } else if ("SubcategoryName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getSubcategoryName()));
      } else {
        lstData.sort(Comparator.comparing(o -> o.getSubcategoryName(), Comparator.reverseOrder()));
      }
    } else if ("DutyTypeName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getDutyTypeName()));
      } else {
        lstData.sort(Comparator.comparing(o -> o.getDutyTypeName(), Comparator.reverseOrder()));
      }
    } else if ("ServiceEffectName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getServiceEffectName()));
      } else {
        lstData
            .sort(Comparator.comparing(o -> o.getServiceEffectName(), Comparator.reverseOrder()));
      }
    } else if ("TypeCrName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getTypeCrName()));
      } else {
        lstData
            .sort(Comparator.comparing(o -> o.getTypeCrName(), Comparator.reverseOrder()));
      }
    } else if ("PriorityCrName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getPriorityCrName()));
      } else {
        lstData
            .sort(Comparator.comparing(o -> o.getPriorityCrName(), Comparator.reverseOrder()));
      }
    } else if ("LevelEffectName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      if ("ASC".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortType())) {
        lstData.sort(Comparator.comparing(o -> o.getLevelEffectName()));
      } else {
        lstData
            .sort(Comparator.comparing(o -> o.getLevelEffectName(), Comparator.reverseOrder()));
      }
    }
  }

  private boolean isCustomColumnSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    return "ArrayChild".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "MrWorksName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "ArrayActorName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "SubcategoryName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "DutyTypeName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "ServiceEffectName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "TypeCrName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "PriorityCrName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())
        || "LevelEffectName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName());
  }

  private void setDataSearchMap(List<MrCfgProcedureTelDTO> lstData, boolean isExport) {
    try {
      String locale = I18n.getLocale();
      List<ItemDataCRInside> lstCbbImpactSegment = crServiceProxy
          .getListImpactSegmentCBBLocaleProxy(locale);
      CrImpactFrameInsiteDTO crImpactFrameInsiteDTO = new CrImpactFrameInsiteDTO();
      crImpactFrameInsiteDTO.setProxyLocale(locale);
      List<ItemDataCRInside> lstCbbDutyType = crServiceProxy
          .getListDutyTypeCBB(crImpactFrameInsiteDTO);
      List<ItemDataCRInside> lstServiceEffect = crServiceProxy
          .getListAffectedServiceCBBLocaleProxy(null, locale);
      List<ItemDataCRInside> lstCbbSubcategory = crServiceProxy
          .getListSubcategoryCBBLocaleProxy(locale);
      List<CatItemDTO> lstCatItem = catItemBusiness
          .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_WORKS, null);
      List<ItemDataCRInside> lstImpactAffect = crServiceProxy
          .getListImpactAffectCBBLocaleProxy(locale);
      CfgChildArrayDTO cfgChildArrayDTO = new CfgChildArrayDTO();
      cfgChildArrayDTO.setProxyLocale(locale);
      List<CfgChildArrayDTO> lstArrayChild = crServiceProxy.getCbbChildArray(cfgChildArrayDTO);
      Map<Long, CfgChildArrayDTO> mapArrayChild = new HashMap<>();
      Map<Long, ItemDataCRInside> mapImpactSegment = new HashMap<>();
      Map<Long, ItemDataCRInside> mapDutyType = new HashMap<>();
      Map<Long, ItemDataCRInside> mapServiceEffect = new HashMap<>();
      Map<Long, ItemDataCRInside> mapSubcategory = new HashMap<>();
      Map<String, ItemDataCRInside> mapImpactAffect = new HashMap<>();

      if (lstCbbImpactSegment != null) {
        for (ItemDataCRInside item2 : lstCbbImpactSegment) {
          mapImpactSegment.put(item2.getValueStr(), item2);
        }
      }
      if (lstCbbDutyType != null && !lstCbbDutyType.isEmpty()) {
        for (ItemDataCRInside item3 : lstCbbDutyType) {
          mapDutyType.put(item3.getValueStr(), item3);
        }
      }
      if (lstServiceEffect != null) {
        for (ItemDataCRInside item4 : lstServiceEffect) {
          mapServiceEffect.put(item4.getValueStr(), item4);
        }
      }

      if (lstCbbSubcategory != null) {
        for (ItemDataCRInside item : lstCbbSubcategory) {
          mapSubcategory.put(item.getValueStr(), item);
        }
      }

      if (lstArrayChild != null) {
        for (CfgChildArrayDTO item : lstArrayChild) {
          mapArrayChild.put(item.getChildrenId(), item);
        }
      }

      if (lstImpactAffect != null) {
        for (ItemDataCRInside item : lstImpactAffect) {
          mapImpactAffect.put(item.getValueStr().toString(), item);
        }
      }

      for (MrCfgProcedureTelDTO dto2 : lstData) {
        Long arrayName2 = dto2.getArrayChild();
        if (arrayName2 != null) {
          if (mapArrayChild.get(arrayName2) != null) {
            dto2.setArrayChildName(mapArrayChild.get(arrayName2).getChildrenName());
          } else {
            dto2.setArrayChildName("");
          }
        }

        if (StringUtils.isNotNullOrEmpty(dto2.getMrWorks())) {
          if (lstCatItem != null && !lstCatItem.isEmpty()) {
            String strCate = "";
            String[] arrRoleCode = dto2.getMrWorks().split(",");
            for (String rCode : arrRoleCode) {
              for (CatItemDTO catDTO : lstCatItem) {
                if (rCode.equalsIgnoreCase(String.valueOf(catDTO.getItemId()))) {
                  strCate += catDTO.getItemName() + ", ";
                  break;
                }
              }
            }
            if (strCate.trim().endsWith(",")) {
              strCate = strCate.trim().substring(0, strCate.trim().length() - 1);
            }
            dto2.setMrWorksName(strCate);
          } else {
            dto2.setMrWorksName(dto2.getMrWorks());
          }
        }

        if (dto2.getGenCr() != null) {
          if ("1".equals(dto2.getGenCr())) {
            dto2.setGenCrName(I18n.getLanguage("common.yes"));
          } else if ("0".equals(dto2.getGenCr())) {
            dto2.setGenCrName(I18n.getLanguage("common.no"));
          }
        }

        Long subCategory = dto2.getCr();
        if (subCategory != null) {
          if (mapSubcategory.get(subCategory) != null) {
            dto2.setSubcategoryName(mapSubcategory.get(subCategory).getDisplayStr());
          } else {
            dto2.setSubcategoryName("");
          }
        }

        if (dto2.getTypeCr() != null) {
          if (0L == dto2.getTypeCr()) {
            dto2.setTypeCrName(I18n.getLanguage("cfgProcedure.crType.often"));
          } else if (1L == dto2.getTypeCr()) {
            dto2.setTypeCrName(I18n.getLanguage("cfgProcedure.crType.urgent"));
          } else if (2L == dto2.getTypeCr()) {
            dto2.setTypeCrName(I18n.getLanguage("cfgProcedure.crType.standard"));
          }
        }

        //Muc uu tien CR
        if (dto2.getPriorityCr() != null) {
          if (dto2.getPriorityCr() == 0L) {
            dto2.setPriorityCrName(I18n.getLanguage("cfgProcedure.priorityCr.now"));
          } else if (dto2.getPriorityCr() == 1L) {
            dto2.setPriorityCrName(I18n.getLanguage("monitor.wo.medium"));
          } else if (dto2.getPriorityCr() == 2L) {
            dto2.setPriorityCrName(I18n.getLanguage("cfgProcedure.priorityCr.lower"));
          }
        }

        //Muc do anh huong
        String levelEffect = dto2.getLevelEffect();
        if (levelEffect != null) {
          if (mapImpactAffect.get(levelEffect) != null) {
            dto2.setLevelEffectName(mapImpactAffect.get(levelEffect).getDisplayStr());
          } else {
            dto2.setLevelEffectName("");
          }
//          if ("141".equals(dto2.getLevelEffect())) {
//            dto2.setLevelEffectName(I18n.getLanguage("cfgProcedure.priorityCr.lower"));
//          } else if ("142".equals(dto2.getLevelEffect())) {
//            dto2.setLevelEffectName(I18n.getLanguage("monitor.wo.medium"));
//          } else if ("143".equals(dto2.getLevelEffect())) {
//            dto2.setLevelEffectName(I18n.getLanguage("cfgProcedure.level"));
//          }
        }

        Long arrayActorName = dto2.getArrayAction();
        if (arrayActorName != null) {
          if (mapImpactSegment.get(arrayActorName) != null) {
            dto2.setArrayActorName(mapImpactSegment.get(arrayActorName).getDisplayStr());
          } else {
            dto2.setArrayActorName("");
          }
        }

        //Loai thiet bi CR NOK
        Long impactSegment = dto2.getArrayAction();
        CrInsiteDTO crDTO = new CrInsiteDTO();
        crDTO.setImpactSegment(impactSegment == null ? null : impactSegment.toString());
        List<ItemDataCRInside> lstCbbDeviceType = crServiceProxy
            .getListDeviceTypeByImpactSegmentCBB(crDTO);
        if (dto2.getDeviceTypeCR() != null) {
          for (ItemDataCRInside itemCR : lstCbbDeviceType) {
            if (dto2.getDeviceTypeCR().equals(itemCR.getValueStr())) {
              dto2.setDeviceTypeCRName(itemCR.getDisplayStr());
              break;
            } else {
              dto2.setDeviceTypeCRName("");
            }
          }
        }

        //Loai tac dong
        Long dutyTypeName = dto2.getDutyType();
        if (dutyTypeName != null) {
          if (mapDutyType.get(dutyTypeName) != null) {
            dto2.setDutyTypeName(mapDutyType.get(dutyTypeName).getDisplayStr());
          } else {
            dto2.setDutyTypeName("");
          }
        }

        //Cac dich vu anh huong
        if (StringUtils.isNotNullOrEmpty(dto2.getServiceEffectId())) {
          if (lstServiceEffect != null && !lstServiceEffect.isEmpty()) {
            String strCate = "";
            String[] arrService = dto2.getServiceEffectId().split(",");
            for (String rCode : arrService) {
              for (ItemDataCRInside catDTO : lstServiceEffect) {
                if (rCode.equalsIgnoreCase(String.valueOf(catDTO.getValueStr()))) {
                  strCate += catDTO.getDisplayStr() + ", ";
                  break;
                }
              }
            }
            if (strCate.trim().endsWith(",")) {
              strCate = strCate.trim().substring(0, strCate.trim().length() - 1);
            }
            dto2.setServiceEffectName(strCate);
          } else {
            dto2.setServiceEffectName(dto2.getServiceEffectId());
          }
        }

//        if (isExport) {
        if ("M".equalsIgnoreCase(dto2.getCycleType())) {
          dto2.setCycleName(dto2.getCycle() + " " + I18n
              .getLanguage("cfgProcedureView.procedureTab.combobox.month"));
        } else {
          dto2.setCycleName(dto2.getCycle() + " " + I18n
              .getLanguage("cfgProcedureView.procedureTab.combobox.day"));
        }
        if ("I".equalsIgnoreCase(dto2.getStatus())) {
          dto2.setStatusName(I18n.getLanguage("common.inActive"));
        } else {
          dto2.setStatusName(I18n.getLanguage("common.active"));
        }
        if ("H".equalsIgnoreCase(dto2.getMrMode())) {
          dto2.setMrModeName(I18n.getLanguage("cfgProcedureView.procedureTab.combobox.Hard"));
        } else {
          dto2.setMrModeName(I18n.getLanguage("cfgProcedureView.procedureTab.combobox.Soft"));
        }
        if (dto2.getExpDate() != null) {
//          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//          SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//          Date date = dateFormat.parse(item.getExpDate());
//          item.setExpDate(df.format(date));
          dto2.setStrExpDate(DateTimeUtils.date2ddMMyyyyHHMMss(dto2.getExpDate()));
        }
//        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public MrCfgProcedureTelDTO findMrCfgProcedureTelById(Long procedureId) {
    List<MrCfgProcedureTelEntity> lst = findByMultilParam(MrCfgProcedureTelEntity.class,
        "procedureId", procedureId);
    return lst.isEmpty() ? null : lst.get(0).toDTO();
  }

  @Override
  public List<MrScheduleTelDTO> getScheduleInProcedure(MrScheduleTelDTO mrScheduleTelDTO) {
    try {
      StringBuilder sql = new StringBuilder(SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_PROCEDURE_TEL, "get-schedule-in-progress"));
      Map<String, Object> params = new HashMap<>();
      if (mrScheduleTelDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getProcedureId())) {
          sql.append(" AND T1.PROCEDURE_ID = :procedureId ");
          params.put("procedureId", mrScheduleTelDTO.getProcedureId());
        }
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<MrCfgProcedureTelDTO> onSearchEntity(MrCfgProcedureTelDTO mrCfgProcedureTelDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(MrCfgProcedureTelEntity.class, mrCfgProcedureTelDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  //TrungDuong them check trùng
  @Override
  public List<MrCfgProcedureTelDTO> checkMrCfgProcedureTelHardExist2(
      MrCfgProcedureTelDTO cfgProcedureDTO) {
    Map<String, Object> params = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "SELECT T.PROCEDURE_ID as procedureId, T.MARKET_CODE as marketCode,T.ARRAY_CODE as arrayCode, T.NETWORK_TYPE networkType, T.DEVICE_TYPE deviceType"
            + " , T.MR_MODE as mrMode, T.CYCLE_TYPE as cycleType, T.CYCLE as cycle FROM MR_CFG_PROCEDURE_TEL T WHERE 1 = 1");
    if (cfgProcedureDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getMarketCode())) {
        sql.append(" AND T.MARKET_CODE =:marketCode ");
        params.put("marketCode", cfgProcedureDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getArrayCode())) {
        sql.append(" AND T.ARRAY_CODE =:arrayCode ");
        params.put("arrayCode", cfgProcedureDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getNetworkType())) {
        sql.append(" AND UPPER(T.NETWORK_TYPE) =:networkType ");
        params.put("networkType", cfgProcedureDTO.getNetworkType().toUpperCase());
      } else {
        sql.append(" AND T.NETWORK_TYPE is null ");
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getDeviceType())) {
        sql.append(" AND UPPER(T.DEVICE_TYPE) =:deviceType");
        params.put("deviceType", cfgProcedureDTO.getDeviceType().toUpperCase());
      } else {
        sql.append(" AND T.DEVICE_TYPE is null ");
      }

      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getMrMode())) {
        sql.append(" AND T.MR_MODE =:mrMode ");
        params.put("mrMode", cfgProcedureDTO.getMrMode());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getCycleType())) {
        sql.append(" AND T.CYCLE_TYPE =:cycleType ");
        params.put("cycleType", cfgProcedureDTO.getCycleType());
      } else {
        sql.append(" AND T.CYCLE_TYPE is null ");
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getCycle())) {
        sql.append(" AND T.CYCLE =:cycle ");
        params.put("cycle", cfgProcedureDTO.getCycle());
      } else {
        sql.append(" AND T.CYCLE is null ");
      }

      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getProcedureId())) {
        sql.append(" AND UPPER(T.PROCEDURE_ID) <> :procedureId ");
        params.put("procedureId", cfgProcedureDTO.getProcedureId());
      }
    }
    List<MrCfgProcedureTelDTO> mrCfgProcedureTelDTOS = getNamedParameterJdbcTemplate()
        .query(sql.toString(), params,
            BeanPropertyRowMapper.newInstance(MrCfgProcedureTelDTO.class));
    if (mrCfgProcedureTelDTOS != null && !mrCfgProcedureTelDTOS.isEmpty()) {
      return mrCfgProcedureTelDTOS;
    }
    return null;
  }

  @Override
  public MrCfgProcedureTelDTO findMrCfgProcedureTelHard(String marketCode, String arrayCode,
      String networkType, String deviceType, String cycleType, Long cycle) {

    List<MrCfgProcedureTelEntity> dataEntity = (List<MrCfgProcedureTelEntity>) findByMultilParam(
        MrCfgProcedureTelEntity.class,
        "marketCode", marketCode,
        "arrayCode", arrayCode,
        "networkType", networkType,
        "deviceType", deviceType,
        "cycleType", cycleType,
        "cycle", cycle);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }
}
