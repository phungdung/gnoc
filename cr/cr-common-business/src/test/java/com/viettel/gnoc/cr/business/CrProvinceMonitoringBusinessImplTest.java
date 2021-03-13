package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.repository.CrProvinceMonitorRepository;
import java.util.ArrayList;
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
@PrepareForTest({CrProvinceMonitoringBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrProvinceMonitoringBusinessImplTest {

  @InjectMocks
  CrProvinceMonitoringBusinessImpl crProvinceMonitoringBusiness;
  @Mock
  CrProvinceMonitorRepository crProvinceMonitorRepository;

  @Test
  public void actionGetProvinceMonitoringParam_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ResultDTO> resultDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crProvinceMonitorRepository.actionGetProvinceMonitoringParam(any(), any(), any(), any()))
        .thenReturn(resultDTOS);
    List<ResultDTO> resultDTOS1 = crProvinceMonitoringBusiness
        .actionGetProvinceMonitoringParam("413314", "1", "01/01/2010 10:00:00",
            "01/02/2010 10:00:00");
    Assert.assertEquals(resultDTOS.size(), resultDTOS1.size());
  }
}
