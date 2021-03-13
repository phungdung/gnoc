package com.viettel.gnoc.wo.business;

import com.viettel.bccs.inventory.service.BaseMessage;
import com.viettel.bccs.inventory.service.StockTotalResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_MASTER_CODE;
import com.viettel.gnoc.commons.utils.ErrorUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.repository.MaterialThresRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoMaterialDeducteRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.utils.ResultQlctkt;
import com.viettel.gnoc.wo.utils.WSUtils;
import com.viettel.gnoc.wo.utils.WS_IM_Direction;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTotalFullDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTransDetailDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

//import com.viettel.bccs.inventory.service.StockTotalFullDTO;
//import com.viettel.bccs.inventory.service.StockTransDetailDTO;

@Service
@Transactional
@Slf4j
public class WoMaterialDeducteBusinessImpl implements WoMaterialDeducteBusiness {

  @Value("${application.ws.qlctkt.bccs.url:null}")
  private String urlSource;

  @Autowired
  WoDetailRepository woDetailRepository;

  @Autowired
  WoRepository woRepository;

  @Autowired
  WoMaterialDeducteRepository woMaterialDeducteRepository;

  @Autowired
  WSUtils wsUtils;
//
//  @Autowired
//  WSIMInventoryPort port;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  MaterialThresRepository materialThresRepository;

  @Autowired
  WS_IM_Direction ws_im_direction;

  @Override
  public ResultInSideDto putMaterialDeducteToIM(Long woId, Boolean isRollback) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    WoDetailDTO woDetail = woDetailRepository.findWoDetailById(woId);
    WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
    List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO = woMaterialDeducteRepository
        .getListWoMaterialDeducteByWoId(woId);
    if (listWoMaterialDeducteInsideDTO == null || listWoMaterialDeducteInsideDTO.isEmpty()) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      return resultInSideDto;
    }
    //thuc hien tru kho rieng re voi tung nhan vien
    Map<String, List<StockTransDetailDTO>> mapUserTruKho = new HashMap<>();
    Map<String, List<WoMaterialDeducteInsideDTO>> mapMatterialDeducte = new HashMap<>();
    Map<String, String> mapName = new HashMap<>();
    for (WoMaterialDeducteInsideDTO i : listWoMaterialDeducteInsideDTO) {
      // neu chua tru thi moi thuc hien tru
      if (!RESULT.SUCCESS.equals(i.getSendImResult())) {
        if (i.getUserId() == null) {
          resultInSideDto.setMessage(I18n.getLanguage("woMaterialDeducte.noUserInfo"));
          return resultInSideDto;
        }
        String account = i.getUserName();
        String identify = null;
        try {
          identify = getAccountIdentify(account);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        if (account == null || "".equals(account) || StringUtils.isStringNullOrEmpty(identify)) {
          resultInSideDto.setMessage(
              I18n.getLanguage("woMaterialDeducte.noGetIdentifyForAccount") + " " + account);
          return resultInSideDto;
        }
        if (mapUserTruKho.containsKey(identify)) {
          List<StockTransDetailDTO> lst = mapUserTruKho.get(identify);
          StockTransDetailDTO obj = new StockTransDetailDTO();
//          obj.setProdOfferId(i.getMaterialId());
          if (i.getNationId() != null) {
            obj.setProdOfferId(i.getNationId());
          } else {
            obj.setProdOfferId(i.getMaterialId());
          }
          obj.setQuantity(i.getQuantity().longValue());
          obj.setType(i.getType());
          obj.setSerial(i.getSerial());
          lst.add(obj);
          mapUserTruKho.put(identify, lst);
          // add vao map dto
          List<WoMaterialDeducteInsideDTO> lstDto = mapMatterialDeducte.get(identify);
          lstDto.add(i);
          mapMatterialDeducte.put(identify, lstDto);
        } else {
          List<StockTransDetailDTO> lst = new ArrayList<>();
          StockTransDetailDTO obj = new StockTransDetailDTO();
          obj.setProdOfferId(i.getMaterialId());
          obj.setQuantity(i.getQuantity().longValue());
          obj.setSerial(i.getSerial());
          obj.setType(i.getType());
          lst.add(obj);
          mapUserTruKho.put(identify, lst);
          // add vao map dto
          List<WoMaterialDeducteInsideDTO> lstDto = new ArrayList<>();
          lstDto.add(i);
          mapMatterialDeducte.put(identify, lstDto);
          mapName.put(identify, account);
        }
      }
    }
    String description = "";
    try {
      for (Map.Entry<String, List<StockTransDetailDTO>> entry : mapUserTruKho.entrySet()) {
        String transactionId = mapName.get(entry.getKey()) + wo.getWoCode();
//        BaseMessage result = port
//            .saveAPDeploymentWithWODatetime(entry.getKey(), woDetail.getAccountIsdn(), "GNOC",
//                entry.getValue(), transactionId, wo.getCreateDate());
        BaseMessage result = ws_im_direction
            .saveAPDeploymentWithWODatetime(entry.getKey(), woDetail.getAccountIsdn(), "GNOC",
                entry.getValue(), transactionId, wo.getCreateDate(), wo.getDeptCode(),
                wo.getNationCode());
        //thuc hien update bang vat tu
        if (!result.isSuccess()) {
//          throw new RuntimeException(result.getDescription() + "; User: " + mapName.get(entry.getKey()));
          if (isRollback) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          }
          resultInSideDto
              .setMessage(result.getDescription() + "; User: " + mapName.get(entry.getKey()));
          resultInSideDto.setKey(RESULT.FAIL);
          return resultInSideDto;
          // bo rollback luu lai danh dach thong bao loi
        } else {
          List<WoMaterialDeducteInsideDTO> lst = mapMatterialDeducte.get(entry.getKey());
          for (WoMaterialDeducteInsideDTO i : lst) {
            i.setSendImResult(RESULT.SUCCESS);
            i.setSendImTime(new Date());
            woMaterialDeducteRepository.updateWoMaterialDeducte(i);
          }
        }
        description =
            "CMT:" + entry.getKey() + " ket qua" + description + result.getDescription() + ",";
      }
      resultInSideDto.setMessage(description);
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception ex) {
      resultInSideDto.setMessage("Loi tru kho:" + ex.getMessage());
      log.error(ex.getMessage(), ex);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto rollBackDeducteToIM(Long woId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    WoDetailDTO woDetail = woDetailRepository.findWoDetailById(woId);
    WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
    List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO = woMaterialDeducteRepository
        .getListWoMaterialDeducteByWoId(woId);
    if (listWoMaterialDeducteInsideDTO == null || listWoMaterialDeducteInsideDTO.isEmpty()) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      return resultInSideDto;
    }
    //thuc hien tru kho rieng re voi tung nhan vien
    Map<String, List<StockTransDetailDTO>> mapUserTruKho = new HashMap<>();
    Map<String, List<WoMaterialDeducteInsideDTO>> mapMatterialDeducte = new HashMap<>();
    Map<String, String> mapName = new HashMap<>();
    for (WoMaterialDeducteInsideDTO i : listWoMaterialDeducteInsideDTO) {
      // neu tru roi thi moi thuc hien rollback
      if (RESULT.SUCCESS.equals(i.getSendImResult())) {
        String account = i.getUserName();
        String identify = null;
        try {
          identify = getAccountIdentify(account);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (account == null || "".equals(account) || StringUtils.isStringNullOrEmpty(identify)) {
          resultInSideDto.setMessage(
              I18n.getLanguage("woMaterialDeducte.noGetIdentifyForAccountRollback") + " "
                  + account);
          return resultInSideDto;
        }
        if (mapUserTruKho.containsKey(identify)) {
          List<StockTransDetailDTO> lst = mapUserTruKho.get(identify);
          StockTransDetailDTO obj = new StockTransDetailDTO();
          obj.setProdOfferId(i.getMaterialId());
          obj.setQuantity(i.getQuantity().longValue());
          lst.add(obj);
          mapUserTruKho.put(identify, lst);
          // add vao map dto
          List<WoMaterialDeducteInsideDTO> lstDto = mapMatterialDeducte.get(identify);
          lstDto.add(i);
          mapMatterialDeducte.put(identify, lstDto);
        } else {
          List<StockTransDetailDTO> lst = new ArrayList<StockTransDetailDTO>();
          StockTransDetailDTO obj = new StockTransDetailDTO();
          obj.setProdOfferId(i.getMaterialId());
          obj.setQuantity(i.getQuantity().longValue());
          lst.add(obj);
          mapUserTruKho.put(identify, lst);
          // add vao map dto
          List<WoMaterialDeducteInsideDTO> lstDto = new ArrayList<>();
          lstDto.add(i);
          mapMatterialDeducte.put(identify, lstDto);
          mapName.put(identify, account);
        }
      }
    }
    String description = "";
    try {
      for (Map.Entry<String, List<StockTransDetailDTO>> entry : mapUserTruKho.entrySet()) {
        String transactionId = mapName.get(entry.getKey()) + wo.getWoCode();
//        BaseMessage result = port
//            .restoreAPDeploymentByIdNo(entry.getKey(), woDetail.getAccountIsdn(), "GNOC",
//                entry.getValue(), transactionId);
        BaseMessage result = ws_im_direction
            .restoreAPDeploymentByIdNo(entry.getKey(), woDetail.getAccountIsdn(), "GNOC",
                entry.getValue(), transactionId, wo.getDeptCode(), wo.getNationCode());
        if (result != null && result.getDescription() != null) {
          description =
              "CMT:" + entry.getKey() + " ket qua" + description + result.getDescription() + ",";
        }
      }
      resultInSideDto.setMessage(description);
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception ex) {
      resultInSideDto.setMessage("Rollback fail:" + ex.getMessage());
      log.error(ex.getMessage(), ex);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto putMaterialDeducte(
      List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setMessage(RESULT.FAIL);
    try {
      if (listWoMaterialDeducteInsideDTO == null || listWoMaterialDeducteInsideDTO.isEmpty()) {
        resultInSideDto.setKey(RESULT.SUCCESS);
        return resultInSideDto;
      }
      String validate = validateAdd(listWoMaterialDeducteInsideDTO);
      if (StringUtils.isNotNullOrEmpty(validate)) {
        resultInSideDto.setMessage(validate);
        return resultInSideDto;
      }
      // xoa vat tu cu
      WoMaterialDeducteInsideDTO dto = listWoMaterialDeducteInsideDTO.get(0);
      Long woId = dto.getWoId();
      Long userId = dto.getUserId();
      List<WoMaterialDeducteInsideDTO> lstDelete = woMaterialDeducteRepository
          .getListWoMaterialDeducteDeleteByWoIdAndUserId(woId, userId);
      if (lstDelete != null) {
        for (WoMaterialDeducteInsideDTO del : lstDelete) {
          woMaterialDeducteRepository.deleteWoMaterialDeducte(del.getWoMaterialDeducteId());
        }
      }
      // them vat tu moi
      for (WoMaterialDeducteInsideDTO deducte : listWoMaterialDeducteInsideDTO) {
        resultInSideDto = woMaterialDeducteRepository.insertWoMaterialDeducte(deducte);
        if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          return resultInSideDto;
        }
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(ErrorUtils.printLog(e));
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMaterialDeducte(Long woId, Long userId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      List<WoMaterialDeducteInsideDTO> lstDelete = woMaterialDeducteRepository
          .getListWoMaterialDeducteDeleteByWoIdAndUserId(woId, userId);
      if (lstDelete != null) {
        for (WoMaterialDeducteInsideDTO del : lstDelete) {
          woMaterialDeducteRepository.deleteWoMaterialDeducte(del.getWoMaterialDeducteId());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(ErrorUtils.printLog(e));
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    }
    return resultInSideDto;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> getMaterialDeducteKeyByWO(Long woId) {
    return woMaterialDeducteRepository.getMaterialDeducteKeyByWO(woId);
  }

  @Override
  public String validateMaterialCompleted(List<WoMaterialDeducteInsideDTO> listMaterial) {
    String res = "";
    WoInsideDTO wo = woRepository.findWoByIdNoOffset(Long.valueOf(listMaterial.get(0).getWoId()));
    for (WoMaterialDeducteInsideDTO o : listMaterial) {
      String identify = null;
      try {
        // neu la thi truong ko dung cmt ma dung ma to doi
        if (StringUtils.isStringNullOrEmpty(wo.getNationCode())
            || "MYT".equalsIgnoreCase(wo.getNationCode())
            || "VNM".equalsIgnoreCase(wo.getNationCode())
        ) {
          identify = getAccountIdentify(o.getUserName());
        } else {
          identify = wo.getDeptCode();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (o.getUserName() == null || "".equals(o.getUserName()) || StringUtils
          .isStringNullOrEmpty(identify)) {
        return I18n.getLanguage("woMaterialDeducte.noGetIdentifyForAccount") + " " + o
            .getUserName();
      }
      List<StockTotalFullDTO> lstS = new ArrayList<>();
      StockTotalFullDTO oo = new StockTotalFullDTO();
      WoMaterialDTO mateDto = woCategoryServiceProxy.findWoMaterialById(o.getMaterialId());
      Double d = o.getQuantity();
      oo.setCurrentQuantity(d.longValue());
      oo.setProdOfferCode(mateDto.getMaterialCode());
      if (mateDto.getNationCode() != null && !"VNM".equals(mateDto.getNationCode())) {
        oo.setProdOfferId(mateDto.getNationId());
      } else {
        oo.setProdOfferId(mateDto.getMaterialId());
      }
      oo.setType(o.getType() == null ? null : o.getType().toString());
      oo.setSerial(o.getSerial());
      lstS.add(oo);
      StockTotalResult rs = ws_im_direction
          .validateStockTotalByStaffIdNo(identify, lstS, wo.getNationCode());
      if (!WO_MASTER_CODE.WO_OK.equals(rs.getErrorCode()) || !rs.isSuccess()) {
        if (rs.getLstError() != null) {
          for (String i : rs.getLstError()) {
            res = res + i + ";";
          }
//          return res;
        } else {
          return I18n.getLanguage("woMaterialDeducte.ErrCallIm") + " " + rs.getErrorCode();
        }
      }
    }
    return res;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> onSearch(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return woMaterialDeducteRepository
        .onSearch(woMaterialDeducteInsideDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  private String validateAdd(List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO) {
    Map<Long, List<WoMaterialDeducteInsideDTO>> mapMaterialActionTypeByWo = new HashMap<>();
    Map<String, MaterialThresDTO> mapMaterialThres = new HashMap<>();
    Map<Long, WoDetailDTO> mapWoDetailDTO = new HashMap<>();
    Map<Long, CatItemDTO> mapAction = new HashMap<>();
    Map<Long, WoMaterialDTO> mapMaterial = new HashMap<>();
    for (WoMaterialDeducteInsideDTO dto : listWoMaterialDeducteInsideDTO) {
      WoDetailDTO woDetail;
      if (dto.getWoId() == null) {
        return I18n.getLanguage("woMaterialDeducte.woIdNull");
      }
      if (dto.getActionId() == null) {
        return I18n.getLanguage("woMaterialDeducte.actionIdNull");
      }
      if (dto.getMaterialId() == null) {
        return I18n.getLanguage("woMaterialDeducte.materialIdNull");
      }
      if (dto.getQuantity() == null) {
        return I18n.getLanguage("woMaterialDeducte.quantityNull");
      }
      if (StringUtils.isStringNullOrEmpty(dto.getUserName())) {
        return I18n.getLanguage("woMaterialDeducte.usernameNull");
      }
      Double quantity = dto.getQuantity();
      if (quantity == null || quantity.equals(0D)) {
        return I18n.getLanguage("woMaterialDeducte.quantityInvalid");
      }
      CatItemDTO actionDTO = mapAction.get(dto.getActionId());
      if (actionDTO == null) {
        actionDTO = catItemRepository.getCatItemById(dto.getActionId());
        if (actionDTO == null) {
          return I18n.getLanguage("woMaterialDeducte.actionIdInvalid");
        }
        if (actionDTO.getParentItemId() == null) {
          return I18n.getLanguage("woMaterialDeducte.actionIdInvalid");
        }
        mapAction.put(dto.getActionId(), actionDTO);
      }
      WoMaterialDTO materialDTO = mapMaterial.get(dto.getMaterialId());
      if (materialDTO == null) {
        WoMaterialDTO model = woCategoryServiceProxy.findWoMaterialById(dto.getMaterialId());
        if (model == null) {
          return I18n.getLanguage("woMaterialDeducte.materialIdNotExist");
        }
        materialDTO = model;
        mapMaterial.put(dto.getActionId(), materialDTO);
      }
      //<editor-fold defaultstate="collapsed" desc="CHECK WO">
      Long woId = Long.valueOf(dto.getWoId());
      if (mapWoDetailDTO.containsKey(woId)) {
        woDetail = mapWoDetailDTO.get(woId);
      } else {
        woDetail = woDetailRepository.findWoDetailById(woId);
      }
      if (woDetail == null) {
        return I18n.getLanguage("woMaterialDeducte.woNotExist");
      }
      mapWoDetailDTO.put(woId, woDetail);
      if (woDetail.getInfraType() == null) {
        return I18n.getLanguage("woMaterialDeducte.woInfraTypeNull");
      }
      String materialThresKey = dto.getMaterialId() + "_" + dto.getActionId() + "_" //
          + woDetail.getServiceId() + "_" + woDetail.getInfraType();
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="Check Thres">
      MaterialThresInsideDTO dtoSearch;
      MaterialThresDTO materialThresDTO;
      if (mapMaterialThres.containsKey(materialThresKey)) {
        materialThresDTO = mapMaterialThres.get(materialThresKey);
      } else {
        dtoSearch = new MaterialThresInsideDTO();
        dtoSearch.setMaterialId(dto.getMaterialId());
        dtoSearch.setActionId(dto.getActionId());
        dtoSearch.setServiceId(woDetail.getServiceId());
        dtoSearch.setInfraType(woDetail.getInfraType());
        List<MaterialThresDTO> list = materialThresRepository.getDataList(dtoSearch);
        if (list.isEmpty()) {
          return I18n.getLanguage("woMaterialDeducte.woThresNull");
        } else {
          materialThresDTO = list.get(0);
        }
      }
      mapMaterialThres.put(materialThresKey, materialThresDTO);
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="Check Quantity">
      Double techThres;
      Double warningThres;
      if (materialThresDTO.getTechDistanctThres() != null //
          || materialThresDTO.getWarningDistanctThres() != null) {
        Double distance = getAccountDistance(woDetail.getAccountIsdn());
        if (distance != null && !distance.equals(0D)) {
          Integer ratio;
          try {
            ratio = Integer.valueOf(materialThresDTO.getTechDistanctThres());
            if (ratio != null && !ratio.equals(0)) {
              techThres = distance / ratio;
            } else {
              techThres = NumberUtils.toDouble(materialThresDTO.getTechThres());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            techThres = NumberUtils.toDouble(materialThresDTO.getTechThres());
          }
          try {
            ratio = Integer.valueOf(materialThresDTO.getWarningDistanctThres());
            if (ratio != null && !ratio.equals(0)) {
              warningThres = distance / ratio;
            } else {
              warningThres = NumberUtils.toDouble(materialThresDTO.getWarningThres());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            warningThres = NumberUtils.toDouble(materialThresDTO.getWarningThres());
          }
        } else {
          techThres = NumberUtils.toDouble(materialThresDTO.getTechThres());
          warningThres = NumberUtils.toDouble(materialThresDTO.getWarningThres());
        }
      } else {
        techThres = NumberUtils.toDouble(materialThresDTO.getTechThres());
        warningThres = NumberUtils.toDouble(materialThresDTO.getWarningThres());
      }
      if (quantity > techThres) {
        return I18n.getLanguage("woMaterialDeducte.quantityOverTechThres")
            .replace("[material]", materialThresDTO.getMaterialName())
            .replace("[quantity]", String.valueOf(techThres - quantity));
      }
      if (quantity > warningThres && (dto.getReasonId() == null)
          || "-1".equals(String.valueOf(dto.getReasonId()))) {
        return I18n.getLanguage("woMaterialDeducte.reasonIdNull")
            .replace("[material]", materialThresDTO.getMaterialName())
            .replace("[quantity]", String.valueOf(warningThres - quantity));
      }
      //</editor-fold>
      //<editor-fold defaultstate="collapsed" desc="check exist Materia & Action">
      List<WoMaterialDeducteInsideDTO> listDeducteWO = mapMaterialActionTypeByWo.get(dto.getWoId());
      if (listDeducteWO == null) {
        listDeducteWO = woMaterialDeducteRepository.getMaterialDeducteKeyByWO(woId);
      }
      for (WoMaterialDeducteInsideDTO deducte : listDeducteWO) {
        if (deducte.getParentActionId().equals(actionDTO.getParentItemId())
            && !deducte.getActionId().equals(actionDTO.getItemId())) {
          return I18n.getLanguage("woMaterialDeducte.actionIdDuplicateGroup");
        }
        if (deducte.getParentActionId().equals(actionDTO.getParentItemId())
            && deducte.getActionId().equals(actionDTO.getItemId())
            && StringUtils.isNotNullOrEmpty(deducte.getMaterialGroupCode())
            && StringUtils.isNotNullOrEmpty(materialDTO.getMaterialGroupCode())
            && deducte.getMaterialGroupCode().equals(materialDTO.getMaterialGroupCode())
            && deducte.getType().equals(dto.getType())
        ) {
          return I18n.getLanguage("woMaterialDeducte.materialIdDuplicateGroup");
        }
      }
      //</editor-fold>
      WoMaterialDeducteInsideDTO deducte = new WoMaterialDeducteInsideDTO();
      deducte.setParentActionId(actionDTO.getParentItemId());
      deducte.setActionId(actionDTO.getItemId());
      deducte.setMaterialGroupCode(materialDTO.getMaterialGroupCode());
      deducte.setMaterialId(materialDTO.getMaterialId());
      listDeducteWO.add(deducte);
      mapMaterialActionTypeByWo.put(dto.getWoId(), listDeducteWO);
    }
    return "";
  }

  private Double getAccountDistance(String account) {
    return null;
  }

  private String getAccountIdentify(String account) throws Exception {
    String url = urlSource;
    String contenRequet =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.qlctkt.viettel.com/\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "      <web:getIdentityCard>\n"
            + "         <!--Optional:-->\n"
            + "         <userName>" + account + "</userName>\n"
            + "      </web:getIdentityCard>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";
    String str = wsUtils.sendRequest(contenRequet, url, 100000, 100000);
    ResultQlctkt rq = wsUtils.convertNodesFromXml(str);
    if (rq != null) {
      return rq.getIdentityCard();
    }
    return "";
  }

  @Override
  public List<WoMaterialDeducteDTO> getMaterialDeducteKeyByWOOutSide(Long woId) {
    return woMaterialDeducteRepository.getMaterialDeducteKeyByWOOutSide(woId);
  }
}
