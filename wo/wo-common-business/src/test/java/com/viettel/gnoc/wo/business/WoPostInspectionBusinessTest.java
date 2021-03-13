package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants.AP_PARAM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.repository.WoPostInspectionRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@PrepareForTest({WoPostInspectionBusinessImpl.class, I18n.class, Gson.class, TypeToken.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoPostInspectionBusinessTest {

  @InjectMocks
  WoPostInspectionBusinessImpl woPostInspectionBusiness;
  @Mock
  CommonBusiness commonBusiness;
  @Mock
  WoPostInspectionRepository woPostInspectionRepository;
  @Mock
  UserBusiness userBusiness;
  @Mock
  WoBusiness woBusiness;
  @Mock
  UnitRepository unitRepository;
  @Mock
  GnocFileRepository gnocFileRepository;

  @Test
  public void insertWOPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("\\ không đạt. Điểm/Target: ");
    List<String> arrFileName = Mockito.spy(ArrayList.class);
//    arrFileName.add("junit.doc");
    List<byte[]> fileDocumentByteArray = Mockito.spy(ArrayList.class);
//    byte[] myvar = "Any String you want".getBytes();
//    fileDocumentByteArray.add(myvar);
    List<WoPostInspectionDTO> lstInspectionDTO = Mockito.spy(ArrayList.class);
    WoPostInspectionDTO inspectionDTO = Mockito.spy(WoPostInspectionDTO.class);
    inspectionDTO.setWoId("383002");
    inspectionDTO.setAccount("h004_gftth_sonvt19");
    inspectionDTO.setNote("rui");
    inspectionDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    inspectionDTO.setReceiveUserName("tms_caugiay");
    inspectionDTO.setResult("NOK");
    inspectionDTO.setWoTypeName(AP_PARAM.WO_TYPE_NAME_HKSC);
    inspectionDTO.setPoint("10");
    inspectionDTO.setNote("abc");
    inspectionDTO.setArrFileName(arrFileName);
    inspectionDTO.setFileDocumentByteArray(fileDocumentByteArray);
    inspectionDTO.setId("1");
    lstInspectionDTO.add(inspectionDTO);
    PowerMockito.when(woPostInspectionRepository.getSeqPostInspection(anyString())).thenReturn("1");
    UsersEntity users = Mockito.spy(UsersEntity.class);
    users.setUserId(999999L);
    users.setUnitId(413314L);
    users.setUsername("thanhlv12");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(userBusiness.getUserByUserName(anyString())).thenReturn(users);
    Map<String, String> map = new HashMap<>();
    map.put("VTT_KHAC_PHUC_HAU_KIEM", "viettel");
    map.put("PRIORITY_HAU_KIEM", "vtnet");
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(map);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.createWoVsmart(any())).thenReturn(res);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDtos.add(gnocFileDto);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woPostInspectionRepository.insertList(anyList())).thenReturn(resultInSideDto);
    String result = woPostInspectionBusiness.insertWOPostInspection(lstInspectionDTO);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void getListWOPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoPostInspectionInsideDTO woPostInspectionDTO = Mockito.spy(WoPostInspectionInsideDTO.class);
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS = Mockito.spy(ArrayList.class);
    woPostInspectionInsideDTOS.add(woPostInspectionDTO);
    PowerMockito.when(woPostInspectionRepository
        .getListWOPostInspection(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woPostInspectionInsideDTOS);
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS1 = woPostInspectionBusiness
        .getListWOPostInspection(woPostInspectionDTO, 1, 1, "abc", "abc");
    Assert.assertEquals(woPostInspectionInsideDTOS.size(), woPostInspectionInsideDTOS1.size());
  }

  @Test
  public void delete_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woPostInspectionRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woPostInspectionBusiness.delete(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void loadWoPostInspectionChecklist_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String woId = "111111";
    String accountName = "thanhlv12";
    List<WoPostInspectionInsideDTO> listInspectionDTO = Mockito.spy(ArrayList.class);
    WoPostInspectionInsideDTO woPostInspectionInsideDTO = Mockito
        .spy(WoPostInspectionInsideDTO.class);
    woPostInspectionInsideDTO.setDataJson(
        "[{\"key\":\"- Ưu tiên đặt thiết bị ONT, STB, AP mở rộng trên kệ ti vi hoặc bàn.\\n- Trong trường hợp không có kệ, bàn thì đóng đinh bê tông hoặc bắn vít treo trên tường (yêu cầu độ cao treo thiết bị AP mở rộng ≥1.5m, khuyến nghị treo AP ở vị trí cao nhất có thể để vùng phủ rộng, hạn chế vật che chắn)\",\"value\":\"1\",\"col\":0,\"row\":0,\"defaulValue\":\"3\"},{\"key\":\"Xác định góc xoay anten thiết bị ONT và AP mở rộng:\\n  + Phủ cho 1 mặt sàn, 1 tầng - Phủ ngang và phủ rộng: thiết lập góc 90° so với mặt sàn\\n  + Phủ cho 2 tầng, phủ cả phương ngang và đứng: Loại 1 râu thiết lập góc 45° so với mặt sàn, Loại 2 râu thiết lập 1 anten 90° và 1 anten 0° so với mặt sàn.\",\"value\":\"1\",\"col\":0,\"row\":0,\"defaulValue\":\"3\"},{\"key\":\"Vật tư/Dây cáp mạng LAN: Đóng trên hệ thống đúng số lượng sử dụng thực tế\",\"value\":\"Đúng\",\"col\":0,\"row\":0,\"defaulValue\":\"Đúng|Sai\"}]");
    listInspectionDTO.add(woPostInspectionInsideDTO);
    PowerMockito.when(woPostInspectionRepository
        .getListWOPostInspection(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(listInspectionDTO);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFileName("abc.docx");
    wo.setCreateDate(new Date());
    PowerMockito.when(woBusiness.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    List<ObjKeyValue> objKeyValues = woPostInspectionBusiness
        .loadWoPostInspectionChecklist(woId, accountName);
    Assert.assertEquals(objKeyValues.size(), 3L);
  }


}
