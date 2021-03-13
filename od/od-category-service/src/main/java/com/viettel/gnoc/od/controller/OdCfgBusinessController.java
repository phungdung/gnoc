package com.viettel.gnoc.od.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.od.business.OdCfgBusinessBusiness;
import com.viettel.gnoc.od.business.OdChangeStatusBusiness;
import com.viettel.gnoc.od.business.OdTypeBusiness;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "odCfgBusiness")
public class OdCfgBusinessController {

  @Autowired
  private OdChangeStatusBusiness odChangeStatusBusiness;

  @Autowired
  private CatItemBusiness catItemBusiness;

  @Autowired
  private OdTypeBusiness odTypeBusiness;

  @Autowired
  private OdCfgBusinessBusiness odCfgBusinessBusiness;

  @PostMapping("/getListOdCfgBusiness")
  public ResponseEntity<Datatable> getListOdCfgBusiness(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    Datatable data = odChangeStatusBusiness.getListOdCfgBusiness(odChangeStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long Id) {
    ResultInSideDto result = odChangeStatusBusiness.deleteCfg(Id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteList")
  public ResponseEntity<ResultInSideDto> deleteList(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto result = odChangeStatusBusiness.deleteListCfg(odChangeStatusDTO.getListId());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<OdChangeStatusDTO> getDetail(Long Id) {
    OdChangeStatusDTO data = odChangeStatusBusiness.getDetailCfg(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = odChangeStatusBusiness.insertOrUpdateCfg(odChangeStatusDTO);
      return new ResponseEntity<>(data, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
      return new ResponseEntity<>(data, HttpStatus.OK);
    }
  }

  @PostMapping("/edit")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = odChangeStatusBusiness.insertOrUpdateCfg(odChangeStatusDTO);
      return new ResponseEntity<>(data, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
      return new ResponseEntity<>(data, HttpStatus.OK);
    }
  }

  @PostMapping("/getListOdType")
  public ResponseEntity<Datatable> getListOdType(@RequestBody OdTypeDTO odTypeDTO) {
    Datatable data = odTypeBusiness.getListOdType(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
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
      case OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN:
        idColName = Constants.ITEM_VALUE;
        type = MASTER_DATA.OD_CFG_BUSINESS_COLUMN;
        break;
      case CATEGORY.OD_CHANGE_STATUS_ROLE:
        idColName = Constants.ITEM_VALUE;
        type = "";
        break;
    }
    Datatable data = getDataItemMaster(categoryCode, MASTER_DATA.OD, type, idColName,
        Constants.ITEM_NAME);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO) throws Exception {
    File exportData = odChangeStatusBusiness.exportData(odChangeStatusDTO);
    return new ResponseEntity<>(exportData, HttpStatus.OK);
  }

  @PostMapping("/importData")
  public ResponseEntity<ResultInSideDto> importData(
      @RequestParam("file") MultipartFile upLoadFile) {
    ResultInSideDto resultInSideDto = odChangeStatusBusiness.importData(upLoadFile);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  private Datatable getDataItemMaster(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    return catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
  }

  @GetMapping("/getListOdCfgBusinessDTO/oldStatus/{oldStatus}/newStatus/{newStatus}/odPriority/{odPriority}/odTypeId/{odTypeId}")
  public List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(@PathVariable String oldStatus,
      @PathVariable String newStatus, @PathVariable String odPriority,
      @PathVariable String odTypeId) {
    return odCfgBusinessBusiness
        .getListOdCfgBusinessDTO(oldStatus, newStatus, odPriority, odTypeId);
  }

  @GetMapping("/deleteByOdChangeStatusId/id{id}")
  public ResponseEntity<ResultInSideDto> deleteByOdChangeStatusId(@PathVariable Long id) {
    ResultInSideDto result = odCfgBusinessBusiness.deleteByOdChangeStatusId(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/searchOdChangeStatus")
  public List<OdChangeStatusDTO> searchOdChangeStatus(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    return odChangeStatusBusiness.search(odChangeStatusDTO);
  }

  @GetMapping("/getOdChangeStatusDTOByParams/params{params}")
  public OdChangeStatusDTO getOdChangeStatusDTOByParams(@PathVariable String... params) {
    return odChangeStatusBusiness.getOdChangeStatusDTOByParams(params);
  }

  @PostMapping("/getListOdCfgBusinessToUpdateOd")
  public List<OdCfgBusinessDTO> getListOdCfgBusinessToUpdateOd(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO) {
    return odCfgBusinessBusiness.getListOdCfgBusiness(odChangeStatusDTO);
  }
}
