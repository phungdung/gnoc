package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import com.viettel.gnoc.cr.repository.CrDtRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrDtBusinessImpl.class, FileUtils.class, I18n.class, DataUtil.class,
    DateTimeUtils.class, PassProtector.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrDtBusinessImplTest {

  @InjectMocks
  CrDtBusinessImpl crDtBusiness;

  @Mock
  CrRepository crRepository;

  @Mock
  CrDtRepository crDtRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crDtBusiness, "userConfig",
        "thanhlv12");
    ReflectionTestUtils.setField(crDtBusiness, "passConfig",
        "123456a@");
    ReflectionTestUtils.setField(crDtBusiness, "saltService",
        "Gman");
  }

  @Test
  public void testInsertVMSADT_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    Locale locale1 = new Locale("en_US");
    PowerMockito.when(I18n.getLanguageByLocale(locale1, "qltn.invalidUserNameOrPassWord"))
        .thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String systemCode = "hj";
    Long crId = 123L;
    Long validateKey = 1L;
    int createMopSuccess = 12;
    String createMopDetail = "hji";
    List<VMSAMopDetailDTO> mopDTOList = Mockito.spy(ArrayList.class);
    String nationCode = "";
    String locale = "en_US";
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("aaa");
    ResultDTO rs = crDtBusiness
        .insertVMSADT(userService, passService, systemCode, crId,
            validateKey, createMopSuccess, createMopDetail, mopDTOList, nationCode, locale);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testInsertVMSADT_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    Locale locale1 = new Locale("en_US");
    PowerMockito.when(I18n.getLanguageByLocale(locale1, "qltn.invalidUserNameOrPassWord"))
        .thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String systemCode = "hj";
    Long crId = 123L;
    Long validateKey = 1L;
    int createMopSuccess = 12;
    String createMopDetail = "hji";
    List<VMSAMopDetailDTO> mopDTOList = Mockito.spy(ArrayList.class);
    String nationCode = "";
    String locale = "en_US";
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    PowerMockito.when(crDtRepository
        .insertVMSADT(anyLong(), anyLong(), anyString(), anyInt(), anyString(), anyList(),
            anyString(), anyString())).thenReturn(resultDTO);

    ResultDTO rs = crDtBusiness
        .insertVMSADT(userService, passService, systemCode, crId,
            validateKey, createMopSuccess, createMopDetail, mopDTOList, nationCode, locale);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testGetAllActiveAffectedService_01() throws Exception {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgfgfgfg");
    itemDataCRList.add(itemDataCR);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String locale = "en_US";
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    PowerMockito.when(crDtRepository.getAllActiveAffectedService(anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> crList = crDtBusiness
        .getAllActiveAffectedService(userService, passService, locale);
    Assert.assertEquals(itemDataCRList.size(), crList.size());
  }

  @Test
  public void testGetAllActiveAffectedService_02() throws Exception {
    List<ItemDataCR> itemDataCRList = null;
    String userService = "thanhlv12";
    String passService = "123456a@";
    String locale = "en_US";
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("aaa");
    List<ItemDataCR> crList = crDtBusiness
        .getAllActiveAffectedService(userService, passService, locale);
    Assert.assertEquals(itemDataCRList, crList);
  }
}
