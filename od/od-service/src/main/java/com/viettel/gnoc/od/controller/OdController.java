package com.viettel.gnoc.od.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.business.OdBusiness;
import com.viettel.gnoc.od.business.OdCommonBusiness;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @author NamTN
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "od")
@Slf4j
public class OdController {

  @Autowired
  OdBusiness odBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  OdCategoryServiceProxy odCategoryServiceProxy;

  @Autowired
  OdCommonBusiness odCommonBusiness;

  @Autowired
  SrServiceProxy srServiceProxy;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/getListDataSearch")
  public ResponseEntity<Datatable> getListDataSearch(
      @RequestBody OdSearchInsideDTO odSearchInsideDTO) {
    Datatable data = odBusiness.getListDataSearch(odSearchInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findOdById")
  public ResponseEntity<OdDTO> findOdById(Long odId) {
    OdDTO data = odBusiness.findOdById(odId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailById")
  public ResponseEntity<OdDTO> getDetailById(Long odId) {
    OdDTO data = odBusiness.getDetailOdDTOById(odId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getConfigPropertyOd")
  public ResponseEntity<ConfigPropertyDTO> getConfigPropertyOd(String key) {
    ConfigPropertyDTO data = odBusiness.getConfigPropertyOd(key);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(@RequestBody OdSearchInsideDTO odSearchInsideDTO)
      throws Exception {
    File data = odBusiness.exportData(odSearchInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListDataItem")
  public ResponseEntity<List<String>> getSeqOdType(int size) {
    List<String> list = odBusiness.getSeqOd(size);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getItemMaster")
  public ResponseEntity<Datatable> getItemMaster(String categoryCode) {
    String idColName = "";
    String type = "";
    switch (categoryCode) {
      case CATEGORY.OD_PRIORITY:
        idColName = Constants.ITEM_ID;
        type = MASTER_DATA.OD_PRIORITY;
        break;
      case CATEGORY.OD_STATUS:
        idColName = Constants.ITEM_VALUE;
        type = MASTER_DATA.OD_STATUS;
        break;
      case CATEGORY.OD_GROUP_TYPE:
        idColName = Constants.ITEM_ID;
        type = MASTER_DATA.OD_GROUP_TYPE;
        break;
      case CATEGORY.OD_REASON_GROUP:
        idColName = Constants.ITEM_ID;
        break;
      case CATEGORY.OD_SOLUTION_GROUP:
        idColName = Constants.ITEM_ID;
        break;
    }
    Datatable data = getDataItemMaster(categoryCode, MASTER_DATA.OD, type, idColName,
        Constants.ITEM_NAME);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListStatusNext")
  public ResponseEntity<Datatable> getListStatusNext(Long odId, String userName) {
    Datatable data = odBusiness.getListStatusNext(odId, userName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOdFromWeb")
  public ResponseEntity<ResultInSideDto> insertOdFromWeb(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") OdSearchInsideDTO odSearchInsideDTO) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = odBusiness.insertOdFromWeb(odSearchInsideDTO, files);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateOdFromWeb")
  public ResponseEntity<ResultInSideDto> updateOdFromWeb(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") OdSearchInsideDTO odSearchInsideDTO,
      @RequestPart("userName") String userName) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = odBusiness.updateOdFromWeb(odSearchInsideDTO, files, userName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/searchRelationToUpdate")
  public ResponseEntity<Datatable> searchRelationToUpdate(
      @RequestBody OdRelationDTO odRelationDTO) {
    Datatable data = new Datatable();
    UserToken userToken = ticketProvider.getUserToken();
    Double offSet = userRepository.getOffsetFromUser(userToken.getUserName());
    try {
      List<OdRelationDTO> odRelationDTOS = new ArrayList<>();
      int totalRow = 0;
      if (odRelationDTO.getSystem() != null) {
        if ("WO".equalsIgnoreCase(odRelationDTO.getSystem().trim())) {
          odRelationDTO.setOffset(TimezoneContextHolder.getOffsetDouble().longValue());
          List<WoInsideDTO> woSearchWebDTOS = woServiceProxy
              .getListDataSearchWebProxy(odRelationDTO.toWoInsideDTO());
          totalRow = woSearchWebDTOS.get(0).getTotalRow();
          if (woSearchWebDTOS != null && woSearchWebDTOS.size() > 0) {
            totalRow = woSearchWebDTOS.get(0).getTotalRow();
            for (WoInsideDTO woInsideDTO : woSearchWebDTOS) {
              OdRelationDTO tempOdRelationDTO = new OdRelationDTO(woInsideDTO.getWoContent(),
                  woInsideDTO.getCreatePersonName(),
                  woInsideDTO.getCreateDate(), woInsideDTO.getEndTime(),
                  "WO", woInsideDTO.getStatusName(), woInsideDTO.getWoCode(), woInsideDTO.getWoId(),
                  woInsideDTO.getCdName(),
                  woInsideDTO.getCreatePersonId(),
                  woInsideDTO.getCdId(),
                  totalRow);
              odRelationDTOS.add(tempOdRelationDTO);
            }
          }
        } else if ("CR".equalsIgnoreCase(odRelationDTO.getSystem())) {
          List<CrDTO> crDTOS = crServiceProxy
              .getListCrInfo(odRelationDTO.toCrDTO().toModelInsiteDTO());
          totalRow = crDTOS.size();
          crDTOS = (List<CrDTO>) DataUtil
              .subPageList(crDTOS, odRelationDTO.getPage(), odRelationDTO.getPageSize());
          for (CrDTO crDTO : crDTOS) {
            OdRelationDTO tempOdRelationDTO = new OdRelationDTO(crDTO.getTitle(),
                crDTO.getChangeOrginatorName(),
                convertServerTimeToClientTime(crDTO.getCreatedDate(), offSet),
                convertServerTimeToClientTime(crDTO.getLatestStartTime(), offSet),
                "CR", convertStatusCR(crDTO.getState()), crDTO.getCrNumber(),
                Long.parseLong(crDTO.getCrId()),
                crDTO.getChangeResponsibleUnitName(),
                StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator()) ? Long
                    .parseLong(crDTO.getChangeOrginator()) : null,
                StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorUnit()) ? Long
                    .parseLong(crDTO.getChangeOrginatorUnit()) : null
                , totalRow);
            odRelationDTOS.add(tempOdRelationDTO);

          }
        } else if ("RDM".equalsIgnoreCase(odRelationDTO.getSystem())) {
          data = odBusiness
              .getListRDMRelationToUpdate(
                  odRelationDTO.getSystemCode(),
                  odRelationDTO.getStartTimeFrom(),
                  odRelationDTO.getStartTimeTo(),
                  odRelationDTO.getPage(),
                  odRelationDTO.getPageSize(),
                  offSet
              );
          return new ResponseEntity<>(data, HttpStatus.OK);
        } else if ("SR".equalsIgnoreCase(odRelationDTO.getSystem())) {
          List<SRDTO> srdtos = srServiceProxy.getListSRForWO(odRelationDTO.toSrDTO());
          totalRow = srdtos.size();
          srdtos = (List<SRDTO>) DataUtil
              .subPageList(srdtos, odRelationDTO.getPage(), odRelationDTO.getPageSize());
          for (SRDTO srdto : srdtos) {
            OdRelationDTO tempOdRelationDTO = new OdRelationDTO(srdto.getTitle(),
                srdto.getCreatedUser(),
                convertServerTimeToClientTime(srdto.getCreatedTime(), offSet),
                convertServerTimeToClientTime(srdto.getEndTime(), offSet),
                "SR", srdto.getStatusName(), srdto.getSrCode(), Long.parseLong(srdto.getSrId()),
                srdto.getUnitName(),
                StringUtils.isNotNullOrEmpty(srdto.getUserId()) ? Long.valueOf(srdto.getUserId()) : null,
                StringUtils.isNotNullOrEmpty(srdto.getSrUnit()) ? Long.valueOf(srdto.getSrUnit()) : null,
                totalRow
            );
            odRelationDTOS.add(tempOdRelationDTO);
          }
        }
      }
      int pages = (int) Math.ceil(totalRow * 1.0 / odRelationDTO.getPageSize());
      data.setTotal(totalRow);
      data.setPages(pages);
      data.setData(odRelationDTOS);
      return new ResponseEntity<>(data, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    data.setData(new ArrayList<String>());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/searchRelationToUpdateById")
  public ResponseEntity<Datatable> searchRelationToUpdateById(
      @RequestBody OdRelationDTO odRelationDTO) {
    Datatable data = new Datatable();
    try {
      List<OdRelationDTO> odRelationDTOS = new ArrayList<>();
      int totalRow = 1;
      if (odRelationDTO.getSystem() != null) {
        if ("CR".equalsIgnoreCase(odRelationDTO.getSystem())) {
          CrInsiteDTO crDTO = crServiceProxy.findCrByIdProxy(odRelationDTO.getSystemId());
          OdRelationDTO tempOdRelationDTO = new OdRelationDTO(crDTO.getTitle(),
              crDTO.getChangeOrginatorName(),
              crDTO.getCreatedDate() != null ? crDTO.getCreatedDate() : null,
              crDTO.getLatestStartTime() != null ? crDTO.getLatestStartTime() : null,
              "CR", convertStatusCR(crDTO.getState()), crDTO.getCrNumber(),
              Long.parseLong(crDTO.getCrId()),
              crDTO.getChangeResponsibleUnitName(),
              StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator()) ? Long
                  .parseLong(crDTO.getChangeOrginator()) : null,
              StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorUnit()) ? Long
                  .parseLong(crDTO.getChangeOrginatorUnit()) : null
              , totalRow);
          odRelationDTOS.add(tempOdRelationDTO);
        }
        if ("SR".equalsIgnoreCase(odRelationDTO.getSystem())) {
          SrInsiteDTO srInsiteDTO = srServiceProxy.findSrFromOdByProxyId(odRelationDTO.getSystemId());
          OdRelationDTO tempOdRelationDTO = new OdRelationDTO(srInsiteDTO.getTitle(),
              srInsiteDTO.getCreatedUser(),
              srInsiteDTO.getCreatedTime() != null ? srInsiteDTO.getCreatedTime() : null,
              srInsiteDTO.getEndTime() != null ? srInsiteDTO.getEndTime() : null,
              "SR", srInsiteDTO.getStatus(), srInsiteDTO.getSrCode(), srInsiteDTO.getSrId(),
              srInsiteDTO.getUnitName(),
              null,
              null,
              totalRow
          );
          odRelationDTOS.add(tempOdRelationDTO);
        }
        if ("WO".equalsIgnoreCase(odRelationDTO.getSystem())) {
          WoInsideDTO woInsideDTO = woServiceProxy.findWoByIdProxy(odRelationDTO.getSystemId());
          OdRelationDTO tempOdRelationDTO = new OdRelationDTO(woInsideDTO.getWoContent(),
              woInsideDTO.getCreatePersonName(),
              woInsideDTO.getCreateDate(), woInsideDTO.getEndTime(),
              "WO", woInsideDTO.getStatusName(), woInsideDTO.getWoCode(), woInsideDTO.getWoId(),
              woInsideDTO.getCdName(),
              woInsideDTO.getCreatePersonId(),
              woInsideDTO.getCdId(),
              totalRow);
          odRelationDTOS.add(tempOdRelationDTO);
        }
      }
      int pages = (int) Math.ceil(totalRow * 1.0 / odRelationDTO.getPageSize());
      data.setTotal(totalRow);
      data.setPages(pages);
      data.setData(odRelationDTOS);
      return new ResponseEntity<>(data, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    data.setData(new ArrayList<String>());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListColumnCheck")
  public ResponseEntity<List<OdCfgBusinessDTO>> getListColumnCheck(String oldStatus,
      String newStatus, String priority, String odTypeId) {
    List<OdCfgBusinessDTO> odCfgBusinessDTOS = odCategoryServiceProxy
        .getListOdCfgBusinessDTO(oldStatus, newStatus, priority, odTypeId);
    return new ResponseEntity<>(odCfgBusinessDTOS, HttpStatus.OK);
  }


  public String convertStatusCR(String stateId) {
    if (stateId != null) {
      return I18n.getLanguage("cr.state." + stateId);
    }
    return "";
  }

  private Datatable getDataItemMaster(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    return catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
  }

  @PostMapping("/getListDataSearchForOther")
  public ResponseEntity<List<OdSearchInsideDTO>> getListDataSearchForOther(
      @RequestBody OdSearchInsideDTO odDTOSearch) {
    List<OdSearchInsideDTO> data = odCommonBusiness.getListDataSearchForOther(odDTOSearch);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertLstRelation")
  public ResponseEntity<ResultInSideDto> insertLstRelation(
      @RequestBody OdDTO odDTO) {
    ResultInSideDto resultDTO = odBusiness.insertLstRelation(odDTO);
    return new ResponseEntity<>(resultDTO, HttpStatus.OK);
  }

  @GetMapping("/getRelationsByOdId")
  public ResponseEntity<List<OdRelationDTO>> getRelationsByOdId(Long odId) {
    List<OdRelationDTO> odRelationDTOS = odBusiness.getRelationsByOdId(odId);
    UserToken userToken = ticketProvider.getUserToken();
    Double offSet = userRepository.getOffsetFromUser(userToken.getUserName());
    for (OdRelationDTO dto : odRelationDTOS) {
      if(dto.getCreateTime() != null) {
        dto.setCreateTime(convertServerTimeToClientTime(dto.getCreateTime(), offSet));
      }
      if(dto.getEndTime() != null) {
        dto.setEndTime(convertServerTimeToClientTime(dto.getEndTime(), offSet));
      }
    }
    return new ResponseEntity<>(odRelationDTOS, HttpStatus.OK);
  }

  private Date convertServerTimeToClientTime(String serverTime, Double offSet) {
    Calendar cal = Calendar.getInstance();
    if (StringUtils.validString(serverTime)) {
      Date convertTime =  DateTimeUtils
          .convertStringToDate(serverTime);
      cal.setTime(convertTime);
      cal.add(Calendar.HOUR_OF_DAY, offSet.intValue());
      return cal.getTime();
    }
    return null;
  }

  private Date convertServerTimeToClientTime(Date serverTime, Double offSet) {
    Calendar cal = Calendar.getInstance();
    if (serverTime != null) {
      cal.setTime(serverTime);
      cal.add(Calendar.HOUR_OF_DAY, offSet.intValue());
      return cal.getTime();
    }
    return null;
  }

  // thangdt updateOdOtherSystem
  @PostMapping("/updateOdOtherSystem")
  public ResponseEntity<ResultInSideDto> updateOdOtherSystem(
      @RequestBody OdDTO odDTO) {
    ResultInSideDto resultDTO = odBusiness.updateOdOtherSystem(odDTO);
    return new ResponseEntity<>(resultDTO, HttpStatus.OK);
  }
}
