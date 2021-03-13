package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.business.MrCDWorkItemBusiness;
import com.viettel.gnoc.mr.business.MrCfgProcedureCDBusiness;
import com.viettel.gnoc.mr.business.MrChecklistBtsBusiness;
import com.viettel.gnoc.mr.business.MrDeviceBtsBusiness;
import com.viettel.gnoc.mr.business.MrDeviceBusiness;
import com.viettel.gnoc.mr.business.MrDeviceCDBusiness;
import com.viettel.gnoc.mr.business.MrITHardScheduleBusiness;
import com.viettel.gnoc.mr.business.MrITSoftScheduleBusiness;
import com.viettel.gnoc.mr.business.MrScheduleBtsBusiness;
import com.viettel.gnoc.mr.business.MrSynItSoftDevicesBusiness;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TienNV
 */
@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "mrCbbCommon")
public class MrCbbCommonController {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  protected MrDeviceCDBusiness mrDeviceCDBusiness;

  @Autowired
  protected MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  protected MrCDWorkItemBusiness mrCDWorkItemBusiness;

  @Autowired
  MrScheduleBtsBusiness mrScheduleBtsBusiness;

  @Autowired
  MrChecklistBtsBusiness mrChecklistBtsBusiness;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;


  @GetMapping("/getListSupplierBtsByDeviceType")
  public ResponseEntity<List<MrDeviceBtsDTO>> getListSupplierBtsCBB(String deviceType,
      String marketCode)
      throws Exception {
    List<MrDeviceBtsDTO> data = mrDeviceBtsBusiness
        .getListSupplierBtsByDeviceType(deviceType, marketCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrCDWorkItemDTO")
  public ResponseEntity<List<MrCDWorkItemDTO>> getListMrCDWorkItemDTO(
      @RequestBody MrCDWorkItemDTO cDWorkItemDTO)
      throws Exception {
    List<MrCDWorkItemDTO> data = mrCDWorkItemBusiness.getListMrCDWorkItemDTO(cDWorkItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB Mảng
  @GetMapping("/getMrSubCategory")
  public ResponseEntity<List<CatItemDTO>> getMrSubCategory() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrSubCategory();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListDeviceTypeByNetworkType")
  public ResponseEntity<List<MrDeviceDTO>> getListDeviceTypeByNetworkType(String arrayCode,
      String networkType) {
    List<MrDeviceDTO> data = mrDeviceBusiness
        .getListDeviceTypeByNetworkType(arrayCode, networkType);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB Loại mạng
  @GetMapping("/getListNetworkTypeByArrayCode")
  public ResponseEntity<List<MrDeviceDTO>> getListNetworkTypeByArrayCode(String arrayCode) {
    List<MrDeviceDTO> data = mrDeviceBusiness.getListNetworkTypeByArrayCode(arrayCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB khu vực theo quốc gia
  @GetMapping("/getListRegionByMarketCode")
  public ResponseEntity<List<MrDeviceDTO>> getListRegionByMarketCode(String marketCode) {
    List<MrDeviceDTO> data = mrDeviceBusiness.getListRegionByMarketCode(marketCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB Loai hoat dong
  @GetMapping("/getListMrType")
  public ResponseEntity<List<CatItemDTO>> getListMrType() {
    List<CatItemDTO> data = catItemBusiness
        .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB muc uu tien
  @GetMapping("/getMrPriority")
  public ResponseEntity<List<CatItemDTO>> getMrPriority() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrPriority();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB muc anh huong
  @GetMapping("/getMrImpact")
  public ResponseEntity<List<CatItemDTO>> getMrImpact() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrImpact();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB kieu CR
  @GetMapping("/getListSubcategoryCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListSubcategoryCBB() {
    List<ItemDataCRInside> data = crServiceProxy.getListSubcategoryCBBLocaleProxy(I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB khung gio tac dong CR
  @GetMapping("/getListDutyTypeCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDutyTypeCBB() {
    CrImpactFrameInsiteDTO dto = new CrImpactFrameInsiteDTO();
    dto.setProxyLocale(I18n.getLocale());
    List<ItemDataCRInside> data = crServiceProxy.getListDutyTypeCBB(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB mang tac dong CR
  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactSegmentCBB() {
    List<ItemDataCRInside> data = crServiceProxy
        .getListImpactSegmentCBBLocaleProxy(I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB muc do anh huong CR
  @GetMapping("/getListImpactAffectCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactAffectCBB() {
    List<ItemDataCRInside> data = crServiceProxy
        .getListImpactAffectCBBLocaleProxy(I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB dich vu anh huong CR
  @GetMapping("/getListAffectedServiceCBProxy")
  public ResponseEntity<List<ItemDataCRInside>> getListAffectedServiceCBProxy() {
    List<ItemDataCRInside> data = crServiceProxy
        .getListAffectedServiceCBBLocaleProxy(null, I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB loai thiet bi CR
  @GetMapping("/getListDeviceTypeByImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDeviceTypeByImpactSegmentCBB(
      Long impactSegment) {
    CrInsiteDTO crDTO = new CrInsiteDTO();
    crDTO.setProxyLocale(I18n.getLocale());
    crDTO.setImpactSegment(impactSegment == null ? null : impactSegment.toString());
    List<ItemDataCRInside> data = crServiceProxy.getListDeviceTypeByImpactSegmentCBB(crDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB load dau viec
  @GetMapping("/getOgListWorks")
  public ResponseEntity<List<CatItemDTO>> getOgListWorks(
      Long parentItemId) {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getOgListWorks(parentItemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB load quy trinh
  @PostMapping("/getListCrProcessCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListCrProcessCBB(
      @RequestBody CrProcessInsideDTO form) {
    if (form != null) {
      form.setProxyLocale(I18n.getLocale());
    }
    List<ItemDataCRInside> data = crServiceProxy.getListCrProcessCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB load mang con
  @GetMapping("/getChildArrayCBB")
  public ResponseEntity<List<CfgChildArrayDTO>> getChildArrayCBB() {
    CfgChildArrayDTO dto = new CfgChildArrayDTO();
    dto.setProxyLocale(I18n.getLocale());
    List<CfgChildArrayDTO> data = crServiceProxy.getCbbChildArray(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB load dau viec
  @GetMapping("/getOgListWorksByCode")
  public ResponseEntity<List<CatItemDTO>> getOgListWorksByCode(
      String parentItemCode) {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrSubCategory();
    Long itemId = null;
    if (data != null) {
      for (CatItemDTO dto : data) {
        if (dto.getItemValue().equals(parentItemCode)) {
          itemId = dto.getItemId();
          break;
        }
      }
    }
    List<CatItemDTO> dataReturn = mrCfgProcedureCDBusiness.getOgListWorks(itemId);
    return new ResponseEntity<>(dataReturn, HttpStatus.OK);
  }

  //CBB lay station code trong bang thiet bi Mr_Device
  @GetMapping("/getDeviceStationCodeCbb")
  public ResponseEntity<List<MrDeviceDTO>> getDeviceStationCodeCbb() {
    List<MrDeviceDTO> data = mrDeviceBusiness.getDeviceStationCodeCbb();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB lấy danh sách thiết bị theo mảng (IT mềm )
  @GetMapping("/getListDeviceTypeByArrayCode")
  public ResponseEntity<List<MrSynItDevicesDTO>> getListDeviceTypeByArrayCode(String arrayCode) {
    List<MrSynItDevicesDTO> data = mrSynItSoftDevicesBusiness
        .getListDeviceTypeByArrayCode(arrayCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB lấy danh sách khu vực theo country (IT mềm )
  @GetMapping("/getListRegionByMrSynItDevices")
  public ResponseEntity<List<MrITSoftScheduleDTO>> getListRegionByMrSynItDevices(String country) {
    List<MrITSoftScheduleDTO> data = mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(country);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  //CBB lay station code trong bang thiet bi Mr_SYN_IT_DEVICE
  @GetMapping("/getDeviceITStationCodeCbb")
  public ResponseEntity<List<MrSynItDevicesDTO>> getDeviceITStationCodeCbb() {
    List<MrSynItDevicesDTO> data = mrSynItSoftDevicesBusiness.getDeviceITStationCodeCbb();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB lấy danh sách khu vực theo country (IT cứng)
  @GetMapping("/getListRegionByMrSynItDevicesHard")
  public ResponseEntity<List<MrITHardScheduleDTO>> getListRegionByMrSynItDevicesHard(
      String country) {
    List<MrITHardScheduleDTO> data = mrITHardScheduleBusiness
        .getListRegionByMrSynItDevices(country);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
