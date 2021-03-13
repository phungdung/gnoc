package com.viettel.gnoc.cr.controller;

import com.viettel.aam.AppGroup;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.business.CrForOtherSystemBusiness;
import com.viettel.gnoc.cr.business.UserReceiveMsgBusiness;
import com.viettel.gnoc.cr.dto.AppGroupInsite;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CRService")
@Slf4j
public class CrController {

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Autowired
  UserReceiveMsgBusiness userReceiveMsgBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody CrInsiteDTO crDTO) {
    Datatable data = crBusiness.getListCRBySearchTypePagging(crDTO, null);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSequenseCr")
  public ResponseEntity<List<String>> getSequenseCr(String sequenseCr, int size) {
    List<String> data = crBusiness.getSequenseCr(sequenseCr, size);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCrById")
  public ResponseEntity<CrInsiteDTO> findCrById(Long id, String actionRight) {
    CrInsiteDTO data = crBusiness.findCrById(id);
    crBusiness.processDayOff(data, actionRight);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCrByIdProxy/id{id}")
  public CrInsiteDTO findCrByIdProxy(@PathVariable Long id) {
    CrInsiteDTO data = crBusiness.findCrById(id);
    return data;
  }

  @PostMapping("/getListScopeOfUserForAllRole")
  public ResponseEntity<List<ItemDataCRInside>> getListScopeOfUserForAllRole(
      @RequestBody CrInsiteDTO form) {
    List<ItemDataCRInside> data = crBusiness.getListScopeOfUserForAllRole(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getNetworkNodeFromQLTN")
  public ResponseEntity<Datatable> getNetworkNodeFromQLTN(@RequestBody AppGroupInsite appGroupInsite) {
    Datatable datatable= crBusiness.getNetworkNodeFromQLTN(appGroupInsite);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/insertCr")
  public ResponseEntity<ResultInSideDto> insertCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = crBusiness.addNewCrClient(form);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCr")
  public ResponseEntity<ResultInSideDto> updateCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.onUpdateCrClient(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionApproveCR")
  public ResponseEntity<ResultInSideDto> actionApproveCR(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionApproveCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionAppraiseCr")
  public ResponseEntity<ResultInSideDto> actionAppraiseCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionAppraisCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionVerify")
  public ResponseEntity<ResultInSideDto> actionVerify(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionVerifyCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionScheduleCr")
  public ResponseEntity<ResultInSideDto> actionScheduleCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionScheduleCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionReceiveCr")
  public ResponseEntity<ResultInSideDto> actionReceiveCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionReceiveCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionResolveCr")
  public ResponseEntity<ResultInSideDto> actionResolveCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionResolveCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionCloseCr")
  public ResponseEntity<ResultInSideDto> actionCloseCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto resultInSideDto = crBusiness.actionCloseGeneralCr(form, I18n.getLocale());
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/actionAssignCab")
  public ResponseEntity<ResultInSideDto> actionAssignCab(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionAssignCabCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionCab")
  public ResponseEntity<ResultInSideDto> actionCab(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionCabCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionEditCr")
  public ResponseEntity<ResultInSideDto> actionEditCr(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = crBusiness.actionEditCRGeneral(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionCancelCr")
  public ResponseEntity<ResultInSideDto> actionCancelCr(@RequestBody CrInsiteDTO form) {
    String msg = crBusiness.actionCancelCrGeneral(form);
    return new ResponseEntity<>(new ResultInSideDto(null, msg, msg), HttpStatus.OK);
  }

  @PostMapping("/getListSecondaryCr")
  public ResponseEntity<List<CrInsiteDTO>> getListSecondaryCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> data = crBusiness.getListSecondaryCr(crInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListPreApprovedCr")
  public ResponseEntity<List<CrInsiteDTO>> getListPreApprovedCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> data = crBusiness.getListPreApprovedCr(crInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/searchParentCr")
  public ResponseEntity<Datatable> searchParentCr(String system, String code, int page,
      int pageSize) {
    Datatable data = crBusiness.searchParentCr(system, code, page, pageSize);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/importCheckCr")
  public ResponseEntity<ResultInSideDto> importCheckCr(MultipartFile multipartFile) {
    ResultInSideDto data = crBusiness.importCheckCr(multipartFile);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/loadWorkOrder")
  public ResponseEntity<Datatable> loadWorkOrder(WoSearchDTO woSearchDTO) {
    Datatable data = crBusiness.loadWorkOrder(woSearchDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeDTO")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeDTO(
      @RequestBody WoTypeInsideDTO woTypeDTO) {
    List<WoTypeInsideDTO> lst = crBusiness
        .getListWoTypeDTO(woTypeDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListCrForRelateOrPreApprove")
  public ResponseEntity<Datatable> getListCrForRelateOrPreApprove(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    crInsiteDTO.setUserLogin(userToken.getUserID().toString());
    crInsiteDTO.setUserLoginUnit(userToken.getDeptId().toString());
    Datatable datatable = crBusiness
        .getListCrForRelateOrPreApprove(crInsiteDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListWorklogSearch")
  public ResponseEntity<Datatable> getListWorklogSearch(
      @RequestBody WorkLogInsiteDTO workLogDTO) {
    Datatable lst = crBusiness
        .getListWorklogSearch(workLogDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListUserGroupBySystem")
  public ResponseEntity<List<UserGroupCategoryDTO>> getListUserGroupBySystem(
      @RequestBody UserGroupCategoryDTO userGroupCategoryDTO, String step) {
    List<UserGroupCategoryDTO> lst = crBusiness
        .getListUserGroupBySystem(userGroupCategoryDTO, step);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListWorkLogCategoryDTO")
  public ResponseEntity<List<WorkLogCategoryInsideDTO>> getListWorkLogCategoryDTO(
      @RequestBody WorkLogCategoryInsideDTO workLogCategoryDTO) {
    List<WorkLogCategoryInsideDTO> lst = crBusiness
        .getListWorkLogCategoryDTO(workLogCategoryDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/insertWorkLog")
  public ResponseEntity<ResultInSideDto> insertWorkLog(@RequestBody WorkLogInsiteDTO form) {
    ResultInSideDto data = crBusiness.insertWorkLog(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCdGroupByUser")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListCdGroupByUser(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    List<WoCdGroupInsideDTO> lst = crBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListWoCdGroupTypeDTO")
  public ResponseEntity<List<WoCdGroupTypeDTO>> getListWoCdGroupTypeDTO(
      @RequestBody WoCdGroupTypeDTO woCdGroupTypeDTO) {
    List<WoCdGroupTypeDTO> lst = crBusiness
        .getListWoCdGroupTypeDTO(woCdGroupTypeDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListWoPriorityDTO")
  public ResponseEntity<List<WoPriorityDTO>> getListWoPriorityDTO(
      @RequestBody WoPriorityDTO woPriorityDTO) {
    List<WoPriorityDTO> lst = crBusiness
        .getListWoPriorityDTO(woPriorityDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListDataSearch")
  public ResponseEntity<Datatable> getListDataSearch(
      @RequestBody WoSearchDTO woSearchDTO) {
    Datatable datatable = crBusiness
        .getListDataSearch(woSearchDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/loadCRRelated")
  public ResponseEntity<Datatable> loadCRRelated(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    Datatable datatable = crBusiness
        .loadCRRelated(crInsiteDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/loadMop")
  public ResponseEntity<ResultInSideDto> loadMop(
      @RequestBody CrInsiteDTO dto) throws Exception {
    ResultInSideDto resultInSideDto = crBusiness
        .loadMop(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/actionAssignCabMulti")
  public ResponseEntity<ResultInSideDto> actionAssignCabMulti(@RequestBody List<CrInsiteDTO> form) {
    ResultInSideDto result = crBusiness.actionAssignCabMulti(form, I18n.getLocale());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getCrCreatedFromOtherSysDTO")
  public ResponseEntity<CrCreatedFromOtherSysDTO> getCrCreatedFromOtherSysDTO(Long crId) {
    CrCreatedFromOtherSysDTO data = crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(crId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/actionGetListDuplicateCRImpactedNode")
  public ResponseEntity<List<CrInsiteDTO>> actionGetListDuplicateCRImpactedNode(
      @RequestBody CrInsiteDTO form) {
    List<CrInsiteDTO> result = crBusiness.actionGetListDuplicateCRImpactedNode(form);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
//
//  @GetMapping("/findWorkLogCategoryById")
//  public ResponseEntity<WorkLogCategoryDTO> findWorkLogCategoryById(Long id) {
//    WorkLogCategoryDTO data = crBusiness.findWorkLogCategoryById(id);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//  }

  @PostMapping("/getListCRFromOtherSystem")
  public ResponseEntity<Datatable> getListCRFromOtherSystem(@RequestBody CrInsiteDTO crDTO) {
    Datatable data = crBusiness.getListCRFromOtherSystem(crDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCRFromOtherSystemOfSR")
  public List<CrInsiteDTO> getListCRFromOtherSystemOfSR(
      @RequestBody CrInsiteDTO crDTO) {
    List<CrInsiteDTO> list = new ArrayList<>();
    Datatable data = crBusiness.getListCRFromOtherSystem(crDTO);
    list = (List<CrInsiteDTO>) data.getData();
    if (list != null && list.size() > 0) {
      list.get(0).setTotalRow(data.getTotal());
    }
    return list;
  }

  @PostMapping("/insertUserReceiveMsg")
  public ResponseEntity<ResultInSideDto> insertUserReceiveMsg(
      @RequestBody UserReceiveMsgDTO userReceiveMsgDTO) {
    ResultInSideDto data = userReceiveMsgBusiness.insertOrUpdate(userReceiveMsgDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListUserReceiveMsgDTO")
  public ResponseEntity<List<UserReceiveMsgDTO>> getListUserReceiveMsgDTO(
      @RequestBody UserReceiveMsgDTO userReceiveMsgDTO) {
    List<UserReceiveMsgDTO> data = userReceiveMsgBusiness
        .getListUserReceiveMsgDTO(userReceiveMsgDTO, 0, Integer.MAX_VALUE, "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListAppGroup")
  public ResponseEntity<List<AppGroup>> getListAppGroup() {
    List<AppGroup> data = crBusiness.getListAppGroup();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/changeCheckboxAction")
  public ResponseEntity<ResultInSideDto> changeCheckboxAction(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto resultInSideDto = crBusiness.changeCheckboxAction(crInsiteDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/doAssignHandoverCa")
  public ResponseEntity<ResultInSideDto> doAssignHandoverCa(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") CrInsiteDTO crDTO) throws IOException {
    ResultInSideDto resultInSideDto = crBusiness.doAssignHandoverCa(crDTO, files);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/processWOTabAdd")
  public ResponseEntity<CrProcessWoDTO> processWOTabAdd(@RequestBody CrInsiteDTO crDTO) {
    CrProcessWoDTO data = crBusiness.processWOTabAdd(crDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWorkLogDTO")
  public ResponseEntity<List<WorkLogInsiteDTO>> getListWorkLogDTO(
      @RequestBody WorkLogInsiteDTO workLogDTO) {
    List<WorkLogInsiteDTO> data = crBusiness.getListWorkLogDTO(workLogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getDataTableSecondaryCr")
  public ResponseEntity<Datatable> getDataTableSecondaryCr(@RequestBody CrInsiteDTO crInsiteDTO) {
    Datatable data = crBusiness.getDataTableSecondaryCr(crInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getDataTablePreApprovedCr")
  public ResponseEntity<Datatable> getDataTablePreApprovedCr(@RequestBody CrInsiteDTO crInsiteDTO) {
    Datatable data = crBusiness.getDataTablePreApprovedCr(crInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/checkCreateWOWhenCloserCr")
  public ResponseEntity<ResultInSideDto> checkCreateWOWhenCloserCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto data = crBusiness.checkCreateWOWhenCloserCr(crInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteCR{crId}")
  public ResponseEntity<ResultInSideDto> deleteCR(@PathVariable("crId") Long crId) {
    ResultInSideDto data = crBusiness.delete(crId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCrInfo")
  public List<CrDTO> getListCrInfo(@RequestBody CrInsiteDTO crDTO) {
    return crBusiness.getListCrInfo(crDTO);
  }

  @GetMapping("/sendSMSToLstUserConfig")
  public ResponseEntity<String> sendSMSToLstUserConfig(@PathVariable("crId") String crId,
      @PathVariable("contentType") String contentType) {
    String data = crBusiness.sendSMSToLstUserConfig(crId, contentType);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCrByCondition")
  public List<CrDTO> getListCrByCondition(
      @RequestBody BaseDto baseDto) {
    List<CrDTO> dtoList = new ArrayList<>();
    List<CrInsiteDTO> list = crBusiness
        .getListCrByCondition(baseDto.getLstCondition(), baseDto.getPage(), baseDto.getPageSize(),
            baseDto.getSortType(), baseDto.getSortName());
    if (list != null && list.size() > 0) {
      for (CrInsiteDTO dto : list) {
        dtoList.add(dto.toCrDTO());
      }
    }
    return dtoList;
  }

  @PostMapping("/insertCrOutSide")
  public ResponseEntity<ResultInSideDto> insertCrOutSide(@RequestBody CrInsiteDTO form) {
    ResultInSideDto data = new ResultInSideDto();
    try {
      data = crBusiness.createObject(form);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      data.setKey(RESULT.ERROR);
      data.setMessage(e.getMessage());
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSequenseCrProxy/sequenseCr{sequenseCr}/size{size}")
  public List<String> getSequenseCrProxy(@PathVariable("sequenseCr") String sequenseCr,
      @PathVariable("size") int size) {
    List<String> data = crBusiness.getSequenseCr(sequenseCr, size);
    return data;
  }

  @PostMapping("/getCrByIdOutSide/id{id}")
  public CrInsiteDTO getCrByIdOutSide(@RequestBody UserToken userToken,
      @PathVariable(value = "id") Long id) {
    CrInsiteDTO data = crBusiness.getCrById(id, userToken);
    return data;
  }

  @PostMapping("/actionVerifyMrITOutSide")
  public ResultInSideDto actionVerifyMrITOutSide(@RequestBody CrInsiteDTO crInsiteDTO) {
    try {
      return crForOtherSystemBusiness
          .actionVerifyMrIT(crInsiteDTO,
              StringUtils.isStringNullOrEmpty(crInsiteDTO.getProxyLocale()) ? I18n.getLocale()
                  : crInsiteDTO.getProxyLocale());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultInSideDto(null, RESULT.ERROR, e.getMessage());
    }
  }

  @PostMapping("/approveAssign")
  public ResponseEntity<ResultInSideDto> approveAssign(@RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto resultInSideDto = crBusiness.approveAssign(crInsiteDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/rejectAssign")
  public ResponseEntity<ResultInSideDto> rejectAssign(@RequestBody CrInsiteDTO crInsiteDTO) {
      ResultInSideDto resultInSideDto = crBusiness.rejectAssign(crInsiteDTO);
      return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //TrungDuong them getList CR voi CR co IP tac đong = IP vua click tuong ung
  @PostMapping("/getListCrByIp")
  public ResponseEntity<Datatable> getListCrByIp(@RequestBody CrInsiteDTO crInsiteDTO) {
    List<CrDTO> dtoList = new ArrayList<>();
    Datatable data = crBusiness.getListCrByIp(crInsiteDTO);
    List<CrInsiteDTO> list = (List<CrInsiteDTO>)data.getData();
    if (list != null && list.size() > 0) {
      for (CrInsiteDTO dto : list) {
        dtoList.add(dto.toCrDTO());
      }
    }
    data.setData(dtoList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCrTimeOverdue")
  public ResponseEntity<ResultInSideDto> updateCrTimeOverdue(@RequestBody CrInsiteDTO form) {
    try {
      ResultInSideDto data = crBusiness.updateCrTimeOverdue(form);
      if (data != null && RESULT.SUCCESS.equals(data.getKey()) && data.getObject() != null) {
        ResultInSideDto result = crBusiness
            .updateCrTimeOverdueToMop(form, (CrInsiteDTO) data.getObject());
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(new ResultInSideDto(null, RESULT.ERROR, "have some error, please try again!"), HttpStatus.OK);
  }
}
