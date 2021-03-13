package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.bccs.inventory.service.BaseMessage;
import com.viettel.bccs.inventory.service.StockTotalResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.repository.MaterialThresRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoMaterialDeducteRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.utils.ResultQlctkt;
import com.viettel.gnoc.wo.utils.WSUtils;
import com.viettel.gnoc.wo.utils.WS_IM_Direction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WoMaterialDeducteBusinessImpl.class, I18n.class, WSUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoMaterialDeducteBusinessTest {

  @InjectMocks
  WoMaterialDeducteBusinessImpl woMaterialDeducteBusiness;
  @Mock
  WoDetailRepository woDetailRepository;
  @Mock
  WoRepository woRepository;
  @Mock
  WoMaterialDeducteRepository woMaterialDeducteRepository;
  @Mock
  CatItemRepository catItemRepository;
  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;
  @Mock
  MaterialThresRepository materialThresRepository;
  @Mock
  WS_IM_Direction ws_im_direction;

  @Test
  public void putMaterialDeducteToIM_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(WSUtils.class);
    ResultQlctkt resultQlctkt = Mockito.spy(ResultQlctkt.class);
    resultQlctkt.setIdentityCard("123456");
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setAccountIsdn("h004_gftth_linhvt40");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(woDetail);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("WO_HOAN_CONG_20180607_394743");
    wo.setCreateDate(new Date());
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO = Mockito.spy(ArrayList.class);
    WoMaterialDeducteInsideDTO i = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    i.setSendImResult(RESULT.ERROR);
    i.setUserId(69696969L);
    i.setUserName("tms_caugiay");
    i.setMaterialId(45624L);
    i.setQuantity(4D);
    listWoMaterialDeducteInsideDTO.add(i);
    PowerMockito.when(woMaterialDeducteRepository.getListWoMaterialDeducteByWoId(anyLong()))
        .thenReturn(listWoMaterialDeducteInsideDTO);
    PowerMockito.when(WSUtils.sendRequest(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn("abc");
    PowerMockito.when(WSUtils.convertNodesFromXml(any())).thenReturn(resultQlctkt);
    BaseMessage result = Mockito.spy(BaseMessage.class);
    result.setSuccess(true);
    result.setDescription("pass");
    PowerMockito.when(
        ws_im_direction
            .saveAPDeploymentWithWODatetime(anyString(), anyString(), anyString(), anyList(),
                anyString(), any(), anyString(), anyString())).thenReturn(result);
    ResultInSideDto resultInSideDto1 = woMaterialDeducteBusiness.putMaterialDeducteToIM(85L, false);
    Assert.assertNotNull(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void rollBackDeducteToIM_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(WSUtils.class);
    ResultQlctkt resultQlctkt = Mockito.spy(ResultQlctkt.class);
    resultQlctkt.setIdentityCard("123456");
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setAccountIsdn("h004_gftth_linhvt40");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(woDetail);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("WO_HOAN_CONG_20180607_394743");
    wo.setDeptCode("413314");
    wo.setNationCode("VNM");
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO = Mockito.spy(ArrayList.class);
    WoMaterialDeducteInsideDTO i = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    i.setSendImResult(RESULT.SUCCESS);
    i.setUserId(69696969L);
    i.setUserName("tms_caugiay");
    i.setMaterialId(45624L);
    i.setQuantity(4D);
    listWoMaterialDeducteInsideDTO.add(i);
    PowerMockito.when(woMaterialDeducteRepository.getListWoMaterialDeducteByWoId(anyLong()))
        .thenReturn(listWoMaterialDeducteInsideDTO);
    PowerMockito.when(WSUtils.sendRequest(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn("abc");
    PowerMockito.when(WSUtils.convertNodesFromXml(any())).thenReturn(resultQlctkt);
    BaseMessage result = Mockito.spy(BaseMessage.class);
    result.setSuccess(true);
    result.setDescription("pass");
    PowerMockito.when(
        ws_im_direction.restoreAPDeploymentByIdNo(anyString(), anyString(), anyString(), anyList(),
            anyString(), anyString(), anyString())).thenReturn(result);
    ResultInSideDto resultInSideDto = woMaterialDeducteBusiness.rollBackDeducteToIM(85L);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void putMaterialDeducte_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Vật tư [material] nhập quá chỉ tiêu kỹ thuật [quantity] [unit]");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO = Mockito.spy(ArrayList.class);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    woMaterialDeducteInsideDTO.setWoId(400007L);
    woMaterialDeducteInsideDTO.setActionId(426L);
    woMaterialDeducteInsideDTO.setMaterialId(45624L);
    woMaterialDeducteInsideDTO.setQuantity(4D);
    woMaterialDeducteInsideDTO.setUserName("tms_caugiay");
    listWoMaterialDeducteInsideDTO.add(woMaterialDeducteInsideDTO);
    CatItemDTO actionDTO = Mockito.spy(CatItemDTO.class);
    actionDTO.setParentItemId(268L);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(actionDTO);
    WoMaterialDTO model = Mockito.spy(WoMaterialDTO.class);
    PowerMockito.when(woCategoryServiceProxy.findWoMaterialById(anyLong())).thenReturn(model);
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setInfraType(1L);
    woDetail.setServiceId(6L);
    woDetail.setAccountIsdn("h004_gftth_linhvt40");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(woDetail);
    List<MaterialThresDTO> list = Mockito.spy(ArrayList.class);
    MaterialThresDTO materialThresDTO = Mockito.spy(MaterialThresDTO.class);
    materialThresDTO.setTechDistanctThres("9");
    materialThresDTO.setWarningDistanctThres("9");
    materialThresDTO.setTechThres("150");
    materialThresDTO.setWarningThres("100");
    materialThresDTO.setMaterialName("abc");
    list.add(materialThresDTO);
    PowerMockito.when(materialThresRepository.getDataList(any())).thenReturn(list);
    PowerMockito.when(woMaterialDeducteRepository.insertWoMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woMaterialDeducteBusiness
        .putMaterialDeducte(listWoMaterialDeducteInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteMaterialDeducte_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDeducteInsideDTO> lstDelete = Mockito.spy(ArrayList.class);
    WoMaterialDeducteInsideDTO del = Mockito.spy(WoMaterialDeducteInsideDTO.class);
    del.setWoMaterialDeducteId(1814L);
    lstDelete.add(del);
    PowerMockito.when(woMaterialDeducteRepository
        .getListWoMaterialDeducteDeleteByWoIdAndUserId(any(), any())).thenReturn(lstDelete);
    ResultInSideDto resultInSideDto1 = woMaterialDeducteBusiness
        .deleteMaterialDeducte(anyLong(), anyLong());
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getMaterialDeducteKeyByWO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woMaterialDeducteRepository.getMaterialDeducteKeyByWO(anyLong()))
        .thenReturn(woMaterialDeducteInsideDTOS);
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS1 = woMaterialDeducteBusiness
        .getMaterialDeducteKeyByWO(anyLong());
    Assert.assertEquals(woMaterialDeducteInsideDTOS.size(), woMaterialDeducteInsideDTOS1.size());
  }

  @Test
  public void validateMaterialCompleted_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(WSUtils.class);
    PowerMockito.when(WSUtils.sendRequest(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn("abc");
    ResultQlctkt rq = Mockito.spy(ResultQlctkt.class);
    rq.setIdentityCard("123456");
    PowerMockito.when(WSUtils.convertNodesFromXml(any())).thenReturn(rq);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Không lấy được số chứng minh thư cho account");
    WoMaterialDeducteInsideDTO o = Mockito.spy(WoMaterialDeducteInsideDTO.class);
    o.setUserName("tms_caugiay");
    o.setQuantity(4D);
    o.setMaterialId(45624L);
    WoMaterialDTO mateDto = Mockito.spy(WoMaterialDTO.class);
    mateDto.setMaterialCode("TREO_ADSS_KV100");
    PowerMockito.when(woCategoryServiceProxy.findWoMaterialById(o.getMaterialId()))
        .thenReturn(mateDto);
    StockTotalResult rs = Mockito.spy(StockTotalResult.class);
    rs.setErrorCode("404");
    PowerMockito
        .when(ws_im_direction.validateStockTotalByStaffIdNo(anyString(), anyList(), anyString()))
        .thenReturn(rs);
    List<WoMaterialDeducteInsideDTO> listMaterial = Mockito.spy(ArrayList.class);
    listMaterial.add(o);
//    String result = woMaterialDeducteBusiness.validateMaterialCompleted(listMaterial);
//    Assert.assertEquals("", result);
  }

  @Test
  public void onSearch_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        woMaterialDeducteRepository.onSearch(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woMaterialDeducteInsideDTOS);
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS1 = woMaterialDeducteBusiness
        .onSearch(any(), anyInt(), anyInt(), anyString(), anyString());
    Assert.assertEquals(woMaterialDeducteInsideDTOS.size(), woMaterialDeducteInsideDTOS1.size());
  }

}
