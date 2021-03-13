package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SRParamRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrWOTickHelpBusinessImpl implements SrWOTickHelpBusiness {

  @Autowired
  SrOutsideBusiness srOutsideBusiness;

  @Autowired
  SRParamRepository srParamRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  SROutsideRepository srOutsideRepository;

  @Autowired
  SrRepository srRepository;

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Override
  public SRDTO getDetailSRForWOTHVSmart(String srId) {
    log.info("Request to getDetailSRForWOTHVSmart : {}", srId);
    SRDTO srDTO;
    try {
      if (StringUtils.isStringNullOrEmpty(srId)) {
        srDTO = new SRDTO();
        srDTO.setKey("0");
        srDTO.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
        return srDTO;
      } else if (!StringUtils.isLong(srId)) {
        srDTO = new SRDTO();
        srDTO.setKey("0");
        srDTO.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
        return srDTO;
      }
      SrInsiteDTO srTmp = srRepository.getDetailNoOffset(Long.parseLong(srId));
      if (srTmp != null && !StringUtils.isLongNullOrEmpty(srTmp.getSrId())) {
        srDTO = srTmp.toOutsideDTO();
        List<SRParamEntity> lstParam = srParamRepository.findListSRParamBySrId(srTmp.getSrId());
        List<SRParamDTO> lstParamDTO = new ArrayList<>();
        for (SRParamEntity item : lstParam) {
          SRParamDTO dto = item.toDTO();
          if (dto != null && !StringUtils.isStringNullOrEmpty(dto.getSrParamId())) {
            lstParamDTO.add(dto);
          }
        }
        srDTO.setLstParam(lstParamDTO);

        UnitDTO unitDTO = unitRepository.findUnitById(Long.valueOf(srDTO.getSrUnit()));
        if (unitDTO != null && unitDTO.getUnitId() != null) {
          srDTO.setSrUnit(unitDTO.getUnitCode() + "(" + unitDTO.getUnitName() + ")");
        }
        srDTO.setDefaultSortField("name");
      } else {
        srDTO = new SRDTO();
        srDTO.setKey("0");
        srDTO.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
      }
      return srDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      srDTO = new SRDTO();
      srDTO.setKey("0");
      srDTO.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
    }
    return new SRDTO();
  }

  @Override
  public List<SRDTO> getListSRForWOTHVSmart(SRDTO srDTO, String woId) {
    log.info("Request to getListSRForWOTHVSmart : {}", srDTO, woId);
    List<SRDTO> lst;
    try {
      lst = new ArrayList<>();
      if (StringUtils.isStringNullOrEmpty(woId)) {
        SRDTO error = new SRDTO("0", I18n.getLanguage("sr.error.woIdNotNull"));
        error.setDefaultSortField("name");
        lst.add(error);
        return lst;
      }
      lst = srOutsideRepository.getListSRForWOTHVSmart(srDTO, woId);
      if (lst == null || lst.isEmpty()) {
        lst = new ArrayList<>();
        SRDTO error = new SRDTO("0",
            I18n.getLanguage("sr.error.listDataNull").replace("list", "SR"));
        error.setDefaultSortField("name");
        lst.add(error);
        return lst;
      }
      return lst;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lst = new ArrayList<>();
      SRDTO error = new SRDTO("0", I18n.getLanguage("sr.error.listDataNull").replace("list", "SR"));
      error.setDefaultSortField("name");
      lst.add(error);
    }
    return lst;
  }

  @Override
  public ResultDTO createSRForWOTHVSmart(List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO,
      String createUser, String serviceCode) {
    log.info("Request to createSRForWOTHVSmart : {}", lstObjKeyValueVsmartDTO, createUser,
        serviceCode);
    ResultDTO res = new ResultDTO();
    try {
      if (lstObjKeyValueVsmartDTO != null && !lstObjKeyValueVsmartDTO.isEmpty()) {
        Map<String, ObjKeyValueVsmartDTO> mapDataSR = new HashMap<>();
        Map<String, ObjKeyValueVsmartDTO> mapDataVSmart = new HashMap<>();
        for (ObjKeyValueVsmartDTO dtoObj : lstObjKeyValueVsmartDTO) {
          if ("1".equals(dtoObj.getType())) {
            mapDataSR.put(dtoObj.getKeyCode(), dtoObj);
          }
          if ("2".equals(dtoObj.getType())) {
            mapDataVSmart.put(dtoObj.getKey(), dtoObj);
          }
        }
        String srId = "";
        String woId = "";
        if (!mapDataSR.isEmpty()) {
          SRDTO srDTO = new SRDTO();
          srDTO.setCreatedUser(createUser);
          srDTO.setServiceCode(serviceCode);
          for (String keyCode : mapDataSR.keySet()) {
            if ("title".equalsIgnoreCase(keyCode)) {
              srDTO.setTitle(mapDataSR.get(keyCode).getValue());
            } else if ("description".equalsIgnoreCase(keyCode)) {
              srDTO.setDescription(mapDataSR.get(keyCode).getValue());
            } else if ("country".equalsIgnoreCase(keyCode)) {
              srDTO.setCountry(mapDataSR.get(keyCode).getValue());
            } else if ("unitId".equalsIgnoreCase(keyCode)) {
              srDTO.setSrUnit(mapDataSR.get(keyCode).getValue());
            } else if ("roleCode".equalsIgnoreCase(keyCode)) {
              srDTO.setRoleCode(mapDataSR.get(keyCode).getValue());
            } else if ("woId".equalsIgnoreCase(keyCode)) {
              woId = mapDataSR.get(keyCode).getValue();
              srDTO.setWoCode(woId);
            }
          }

          //gop ham createSRForWOTHVSmart vs createSRByConfigGroup
          ResultDTO resCreate = srOutsideBusiness
              .createSRByConfigGroup(srDTO, Constants.SR_CONFIG.DICH_VU_WO_HELP);
          if (Constants.RESULT.SUCCESS.equals(resCreate.getMessage())) {
            SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = new SRCreatedFromOtherSysDTO();
            srCreatedFromOtherSysDTO.setSrCode(resCreate.getKey());
            srCreatedFromOtherSysDTO.setSystemName("WO");
            srCreatedFromOtherSysDTO.setObjectId(woId == null ? null : Long.valueOf(woId));

            String[] srCode = resCreate.getKey().split("_");
            srId = srCode[srCode.length - 1].trim();

            ResultInSideDto resOtherSys = srCreatedFromOtherSysRepository
                .insertSRCreateFromOtherSys(srCreatedFromOtherSysDTO);
            if (!Constants.RESULT.SUCCESS.equals(resOtherSys.getKey())) {
              try {
                srRepository.deleteSR(Long.valueOf(srId));
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                res.setKey("0");
                res.setMessage(e.getMessage());
                return res;
              }
              res.setKey("0");
              res.setMessage(I18n.getLanguage("sr.error.createFromWOTHFailed"));
              return res;
            }

            res.setKey("1");
            res.setMessage(resCreate.getKey());//srCode dc gan vao key

            if (!mapDataVSmart.isEmpty() && !StringUtils.isStringNullOrEmpty(srId) && "1"
                .equals(res.getKey())) {
              List<SRParamDTO> lstParam = new ArrayList<>();
              for (String key : mapDataVSmart.keySet()) {
                if (StringUtils.isStringNullOrEmpty(mapDataVSmart.get(key).getValue())) {
                  continue;
                }
                SRParamDTO srParam = new SRParamDTO();
                srParam.setKey(key);
                srParam.setValue(mapDataVSmart.get(key).getValue());
                srParam.setSrId(srId);
                srParam.setParamType("WO_HELP");
                srParam.setUpdatedTime(DateTimeUtils.getSysDateTime());
                srParam.setUpdatedUser(createUser);
                lstParam.add(srParam);
              }
              try {
                srParamRepository.insertListSRParam(lstParam);
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                res.setKey("0");
                res.setMessage(e.getMessage());
                return res;
              }
            }
          } else {
            res = resCreate;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey("0");
      res.setMessage(e.getMessage());
    }
    return res;
  }
}
