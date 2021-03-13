package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.SessionResponse;
import com.viettel.gnoc.cr.repository.CrGeneralForMobileRepository;
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
@PrepareForTest({CrGeneralForMobileBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrGeneralForMobileBusinessImplTest {

  @InjectMocks
  CrGeneralForMobileBusinessImpl crGeneralForMobileBusiness;
  @Mock
  CrGeneralForMobileRepository crGeneralForMobileRepository;

  @Test
  public void getSessionInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    SessionResponse sessionResponse = Mockito.spy(SessionResponse.class);
    PowerMockito.when(crGeneralForMobileRepository.getSessionInfo(anyString()))
        .thenReturn(sessionResponse);
    SessionResponse sessionResponse1 = crGeneralForMobileBusiness.getSessionInfo("1");
    Assert.assertNotNull(sessionResponse1);
  }

  @Test
  public void getLocationByUnitId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(crGeneralForMobileRepository.getLocationByUnitId(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = crGeneralForMobileBusiness.getLocationByUnitId("1");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void checkSession_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(crGeneralForMobileRepository.checkSession(anyString()))
        .thenReturn(RESULT.SUCCESS);
    String res = crGeneralForMobileBusiness.checkSession("1");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }
}
