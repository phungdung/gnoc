package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.InsertAutoCrForSrDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryDTO;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import viettel.passport.client.UserToken;

@FeignClient(name = "cr-service")
public interface CrServiceProxy {

  @GetMapping("/CRService/findCrById/crId{crId}")
  public CrDTO findCrById(@PathVariable(value = "crId") Long crId);

  @GetMapping("/CRService/findCrByIdProxy/id{id}")
  public CrInsiteDTO findCrByIdProxy(@PathVariable(value = "id") Long id);

  @PostMapping("/CrImpactedNodesService/getListCrImpactedNodesDTO")
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(
      @RequestBody CrImpactedNodesDTO crImpactedNodesDTO);

  @GetMapping("/CrGeneralService/getListImpactSegmentCBB")
  public List<ItemDataCRInside> getListImpactSegmentCBB();

  @GetMapping("/CrGeneralService/getListImpactAffectCBB")
  public List<ItemDataCRInside> getListImpactAffectCBB();

  @GetMapping("/CrGeneralService/getListAffectedServiceCBProxy/form{form}")
  public List<ItemDataCRInside> getListAffectedServiceCBProxy(
      @PathVariable(value = "form") Long form);

  @PostMapping("/CrGeneralService/getListDutyTypeCBB")
  public List<ItemDataCRInside> getListDutyTypeCBB(@RequestBody CrImpactFrameInsiteDTO form);

  @PostMapping("/CrGeneralService/getListDeviceTypeByImpactSegmentCBB")
  public List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(@RequestBody CrInsiteDTO form);

  @PostMapping("/CrGeneralService/getCbbChildArray")
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO cfgChildArrayDTO);

  @PostMapping("/CRService/onSearch")
  public Datatable onSearch(@RequestBody CrInsiteDTO crInsiteDTO);

  @PostMapping("/CRService/getListCRFromOtherSystemOfSR")
  public List<CrInsiteDTO> getListCRFromOtherSystemOfSR(@RequestBody CrInsiteDTO crDTO);

  @GetMapping("/CrAutoServiceForSRControllers/getCrNumber/crProcessId{crProcessId}")
  public ResultInSideDto getCrNumber(@PathVariable("crProcessId") String crProcessId);

  @PostMapping("/CrAutoServiceForSRControllers/insertAutoCrForSR")
  public ResultDTO insertAutoCrForSR(@RequestBody InsertAutoCrForSrDTO insertAutoCrForSrDTO);

  @PostMapping("/CrGeneralService/insertCrCreatedFromOtherSystem")
  public ResultInSideDto insertCrCreatedFromOtherSystem(
      @RequestBody CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO);

  @PostMapping("/CrOtherSystemService/insertFile")
  public ResultDTO insertFile(@RequestBody InsertFileDTO insertFileDTO);

  @PostMapping("/CrFilesAttachService/insertListNoIDForImportForSR")
  public List<TemplateImportDTO> insertListNoIDForImportForSR(@RequestBody String dataFileProcess);

  @GetMapping("/CRService/deleteCR{crId}")
  public ResultInSideDto deleteCR(@PathVariable("crId") Long crId);

  @PostMapping("/CRService/getListCrInfo")
  public List<CrDTO> getListCrInfo(@RequestBody CrInsiteDTO crDTO);

  @GetMapping("/CrGeneralService/getListSubcategoryCBB")
  public List<ItemDataCRInside> getListSubcategoryCBB();

  @PostMapping("/CrProcessService/getListCrProcessCBB")
  public List<ItemDataCRInside> getListCrProcessCBB(@RequestBody CrProcessInsideDTO form);

  @GetMapping("/CRService/getListCRFromOtherSystem/")
  public List<CrDTO> getListCRFromOtherSystem(@RequestBody CrInsiteDTO crDTO);

  @GetMapping("/CrProcessService/findCrProcessById")
  public CrProcessInsideDTO findCrProcessById(@PathVariable(value = "id") Long id);

  @PostMapping("/CRService/insertCr")
  public ResultInSideDto insertCr(@RequestBody CrInsiteDTO form);

  @PostMapping("/CrImpactedNodesService/getLisNodeOfCRForProxy")
  public List<CrImpactedNodesDTO> getLisNodeOfCRForProxy(@RequestBody CrImpactedNodesDTO dto);

  @GetMapping("/CRService/findWorkLogCategoryById/id{id}")
  public WorkLogCategoryDTO findWorkLogCategoryById(@PathVariable(value = "id") Long id);

  @GetMapping("/CrGeneralService/getListSubcategoryCBBLocaleProxy/locale{locale}")
  public List<ItemDataCRInside> getListSubcategoryCBBLocaleProxy(
      @PathVariable(value = "locale") String locale);

  @GetMapping("/CrGeneralService/getListImpactSegmentCBBLocaleProxy/locale{locale}")
  public List<ItemDataCRInside> getListImpactSegmentCBBLocaleProxy(
      @PathVariable(value = "locale") String locale);

  @GetMapping("/CrGeneralService/getListImpactAffectCBBLocaleProxy/locale{locale}")
  public List<ItemDataCRInside> getListImpactAffectCBBLocaleProxy(
      @PathVariable(value = "locale") String locale);

  @GetMapping("/CrGeneralService/getListAffectedServiceCBBLocaleProxy/form{form}/locale{locale}")
  public List<ItemDataCRInside> getListAffectedServiceCBBLocaleProxy(
      @PathVariable(value = "form") Long form, @PathVariable(value = "locale") String locale);

  @GetMapping("/CRService/sendSMSToLstUserConfig")
  public List<ItemDataCRInside> sendSMSToLstUserConfig(@PathVariable(value = "crId") String crId,
      @PathVariable(value = "contentType") String contentType);

  @PostMapping("/CrOtherSystemService/updateWorkOrderForWOPRoxy")
  public ResultInSideDto updateWorkOrderForWOPRoxy(@RequestBody WoInsideDTO woInsideDTO);

  @GetMapping("/CrAutoServiceForSRControllers/getFilePathSrCr")
  public GnocFileDto getFilePathSrCr(@RequestBody GnocFileDto gnocFileDto);

  @PostMapping("/CRService/getListCrByCondition")
  public List<CrDTO> getListCrByCondition(
      @RequestBody BaseDto baseDto);

  @PostMapping("/CRService/insertCrOutSide")
  public ResultInSideDto insertCrOutSide(@RequestBody CrInsiteDTO form);

  @GetMapping("/CRService/getSequenseCrProxy/sequenseCr{sequenseCr}/size{size}")
  public List<String> getSequenseCrProxy(@PathVariable("sequenseCr") String sequenseCr,
      @PathVariable("size") int size);

  @PostMapping("/CRService/getCrByIdOutSide/id{id}")
  CrInsiteDTO getCrByIdOutSide(@RequestBody UserToken userToken,
      @PathVariable(value = "id") Long id);

  @PostMapping("/CRService/actionVerifyMrITOutSide")
  ResultInSideDto actionVerifyMrITOutSide(@RequestBody CrInsiteDTO crInsiteDTO);
}
