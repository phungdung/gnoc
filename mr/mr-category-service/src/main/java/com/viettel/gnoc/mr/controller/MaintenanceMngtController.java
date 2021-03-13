package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.mr.business.MaintenanceMngtBusiness;
import com.viettel.gnoc.mr.business.MrFilesAttachBusiness;
import com.viettel.gnoc.mr.business.MrHisSearchBusiness;
import com.viettel.gnoc.mr.business.MrImpactedNodesBusiness;
import com.viettel.gnoc.mr.business.MrServiceBusiness;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MaintenanceMngt")
public class MaintenanceMngtController {


  @Autowired
  MaintenanceMngtBusiness maintenanceMngtBusiness;

  @Autowired
  MrServiceBusiness mrServiceBusiness;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  MrFilesAttachBusiness mrFilesAttachBusiness;

  @Autowired
  MrHisSearchBusiness mrHisSearchBusiness;

  @Autowired
  MrImpactedNodesBusiness mrImpactedNodesBusiness;

  @PostMapping("/getListMrDTOSearch")
  public ResponseEntity<Datatable> getListMrDTOSearch(@RequestBody MrSearchDTO mrSearchDTO) {
    Datatable data = maintenanceMngtBusiness.getListMrDTOSearchDatatable(mrSearchDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(@RequestBody MrInsideDTO mrInsideDTO) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      result = maintenanceMngtBusiness.onInsert(mrInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onEdit")
  public ResponseEntity<ResultInSideDto> onEdit(@RequestBody MrInsideDTO mrDTO) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      result = maintenanceMngtBusiness.onEdit(mrDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // initTabApprove
  @PostMapping("/initTabApprove")
  public ResponseEntity<List<MrApproveSearchDTO>> initTabApprove(@RequestBody MrDTO mrDTO) {
    List<MrApproveSearchDTO> lst = new ArrayList<>();
    try {
      lst = maintenanceMngtBusiness.initTabApprove(mrDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getIdSequence")
  public ResponseEntity<List<String>> getIdSequence() {
    List<String> data = mrServiceBusiness.getIdSequence();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWorklogSearch")
  public List<WorkLogInsiteDTO> getListWorklogSearch(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO) {
    List<WorkLogInsiteDTO> data = mrServiceBusiness.getListWorklogSearch(workLogInsiteDTO);
    return data;
  }

  @GetMapping("/getListWoDTO")
  public ResponseEntity<List<WoInsideDTO>> getListWoDTO(String mrCode) {
    List<WoInsideDTO> lstTemp = woServiceProxy.getListWoDTOByWoSystemId(mrCode);
    return new ResponseEntity<>(lstTemp, HttpStatus.OK);
  }
//
//  @GetMapping("/findWorkLogCategoryById")
//  public WorkLogCategoryDTO findWorkLogCategoryById(Long id) {
//    WorkLogCategoryDTO result = crServiceProxy.findWorkLogCategoryById(id);
//    return result;
//  }

  @PostMapping("/getListMrFilesSearch")
  public ResponseEntity<List<GnocFileDto>> getListMrFilesSearch(@RequestBody GnocFileDto dto) {
    List<GnocFileDto> lstTemp = mrFilesAttachBusiness.getListMrFilesSearch(dto);
    return new ResponseEntity<>(lstTemp, HttpStatus.OK);
  }

  @GetMapping("/getListMrHisSearch")
  public ResponseEntity<List<MrHisSearchDTO>> getListMrHisSearch(Long mrId) {
    MrHisSearchDTO his = new MrHisSearchDTO();
    his.setMrId(mrId == null ? null : String.valueOf(mrId));
    List<MrHisSearchDTO> lst = mrHisSearchBusiness.getListMrHisSearch(his);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListUserGroupBySystem")
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(
      @RequestBody UserGroupCategoryDTO userGroupCategoryDTO) {
    if (StringUtils.isNotNullOrEmpty(userGroupCategoryDTO.getProxyLocale())) {
      I18n.setLocale(userGroupCategoryDTO.getProxyLocale());
    }
    List<UserGroupCategoryDTO> mrDTOS = maintenanceMngtBusiness
        .getListUserGroupBySystem(userGroupCategoryDTO);
    return mrDTOS;

  }

  @PostMapping("/getListWorkLogCategoryDTO")
  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      @RequestBody WorkLogCategoryInsideDTO workLogCategoryDTO) {
    if (StringUtils.isNotNullOrEmpty(workLogCategoryDTO.getProxyLocale())) {
      I18n.setLocale(workLogCategoryDTO.getProxyLocale());
    }
    List<WorkLogCategoryInsideDTO> mrDTOS = maintenanceMngtBusiness
        .getListWorkLogCategoryDTO(workLogCategoryDTO);
    return mrDTOS;
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrInsideDTO> getDetail(Long id) {
    MrInsideDTO data = maintenanceMngtBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/isCheckEdit")
  public ResponseEntity<Boolean> isCheckEdit(MrInsideDTO mrInsideDTO, String userId) {
    Boolean data = maintenanceMngtBusiness.isCheckEdit(mrInsideDTO, userId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertWorkLog")
  public ResultInSideDto insertWorkLog(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO) {
    ResultInSideDto data = maintenanceMngtBusiness.insertWorkLog(workLogInsiteDTO);
    return data;
  }

  @PostMapping("/insertWorkLogProxy")
  public ResultInSideDto insertWorkLogProxy(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO) {
    ResultInSideDto data = maintenanceMngtBusiness.insertWorkLogProxy(workLogInsiteDTO);
    return data;
  }

  @PostMapping("/deleteFile")
  public ResponseEntity<ResultInSideDto> deleteFile(@RequestBody GnocFileDto gnocFileDto) {
    ResultInSideDto result = mrFilesAttachBusiness
        .deleteFile(gnocFileDto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/insertFile")
  public ResponseEntity<ResultInSideDto> insertFile(
      @RequestPart List<MultipartFile> lstMultipartFiles,
      @RequestPart("formDataJson") MrFilesAttachDTO mrFilesAttachDTO
  ) throws Exception {
    ResultInSideDto data = mrFilesAttachBusiness
        .insertFile(lstMultipartFiles, mrFilesAttachDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getImpactedNodesByMrId")
  public ResponseEntity<List<MrImpactedNodesDTO>> getImpactedNodesByMrId(String mrId) {
    List<MrImpactedNodesDTO> mrDTOS = mrImpactedNodesBusiness.getImpactedNodesByMrId(mrId);
    return new ResponseEntity<>(mrDTOS, HttpStatus.OK);
  }

  @PostMapping("/getListCdGroupByUser")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListCdGroupByUser(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    List<WoCdGroupInsideDTO> lst = maintenanceMngtBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListWorkLogDTO")
  public List<WorkLogInsiteDTO> getListWorkLogDTO(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO) {
    List<WorkLogInsiteDTO> lst = maintenanceMngtBusiness
        .getListWorkLogDTO(workLogInsiteDTO);
    return lst;
  }

  //dung de call proxy
  @GetMapping("/updateMrStatus/crId{crId}/woId{woId}")
  public ResultDTO updateMrStatus(@PathVariable("crId") String crId,
      @PathVariable("woId") String woId) {
    ResultDTO resultDTO = maintenanceMngtBusiness.updateMrStatus(crId, woId);
    return resultDTO;
  }

  //dung de call proxy
  @GetMapping("/reCreatedOrCloseCr/crId{crId}/status{status}")
  public ResultDTO reCreatedOrCloseCr(@PathVariable("crId") String crId,
      @PathVariable("status") String status) {
    ResultDTO resultDTO = maintenanceMngtBusiness.reCreatedOrCloseCr(crId, status);
    return resultDTO;
  }

  @PostMapping("/getMrChartInfoForNOC")
  public MrClientDetail getMrChartInfoForNOC(
      @RequestBody MrForNocSearchDTO mrSearchDTO) {
    return maintenanceMngtBusiness
        .getMrChartInfoForNOC(mrSearchDTO);
  }


  @PostMapping("/getListMrForMobile")
  public List<MrDTO> getListMrForMobile(
      @RequestBody MrMobileDTO dto) {
    return maintenanceMngtBusiness
        .getListMrForMobile(dto);
  }

  @GetMapping("/getWoCrNodeList")
  public ResponseEntity<List<MrNodesDTO>> getWoCrNodeList(String woId, String crId) {
    return new ResponseEntity<>(maintenanceMngtBusiness.getWoCrNodeList(woId, crId), HttpStatus.OK);
  }

  @GetMapping("/getListMrNodeChecklistForPopUp")
  public ResponseEntity<List<MrNodeChecklistDTO>> getListMrNodeChecklistForPopUp(String woId,
      String mrNodeId) {
    return new ResponseEntity<>(
        maintenanceMngtBusiness.getListMrNodeChecklistForPopUp(woId, mrNodeId), HttpStatus.OK);
  }

  @PostMapping("/updateMrNodeChecklistForPopUp")
  public ResponseEntity<ResultInSideDto> updateMrNodeChecklistForPopUp(
      @RequestBody List<MrNodeChecklistDTO> lstMrNodeChecklistDTO) {
    return new ResponseEntity<>(
        maintenanceMngtBusiness.updateMrNodeChecklistForPopUp(lstMrNodeChecklistDTO),
        HttpStatus.OK);
  }

  @PostMapping("/updateWoCrNodeStatus")
  public ResponseEntity<ResultInSideDto> updateWoCrNodeStatus(
      @RequestBody List<MrNodesDTO> lstNodes) {
    return new ResponseEntity<>(maintenanceMngtBusiness.updateWoCrNodeStatus(lstNodes),
        HttpStatus.OK);
  }

  @GetMapping("/getListFileMrNodeChecklist_VS")
  public ResponseEntity<List<MrNodeChecklistFilesDTO>> getListFileMrNodeChecklist_VS(
      String nodeChecklistId) {
    return new ResponseEntity<>(maintenanceMngtBusiness.getListFileMrNodeChecklist_VS(nodeChecklistId), HttpStatus.OK);
  }

  @PostMapping("/getWorklogFromWo")
  public ResponseEntity<List<MrDTO>> getWorklogFromWo(
      @RequestBody MrSearchDTO mrSearchDTO) {
    if (StringUtils.isNotNullOrEmpty(mrSearchDTO.getProxyLocale())) {
      I18n.setLocale(mrSearchDTO.getProxyLocale());
    }
    return new ResponseEntity<>(mrServiceBusiness.getWorklogFromWo(mrSearchDTO),
        HttpStatus.OK);
  }

}
