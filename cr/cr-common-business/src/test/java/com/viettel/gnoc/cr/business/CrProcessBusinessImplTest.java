package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
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
@PrepareForTest({CrProcessBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrProcessBusinessImplTest {

  @InjectMocks
  CrProcessBusinessImpl crProcessBusiness;
  @Mock
  CrProcessRepository crProcessRepository;

  @Test
  public void findCrProcessById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(crProcessRepository.findCrProcessById(anyLong())).thenReturn(crProcessDTO);
    CrProcessInsideDTO crProcessDTO1 = crProcessBusiness.findCrProcessById(1L);
    Assert.assertNotNull(crProcessDTO1);
  }

  @Test
  public void getListCrProcessCBB_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(crProcessRepository.getListCrProcessCBB(any())).thenReturn(itemDataCRInsides);
    List<ItemDataCRInside> itemDataCRInsides1 = crProcessBusiness.getListCrProcessCBB(crProcessDTO);
    Assert.assertEquals(itemDataCRInsides.size(), itemDataCRInsides1.size());
  }

  @Test
  public void getListCrProcessLevel3CBB_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrProcessInsideDTO> crProcessDTOS = Mockito.spy(ArrayList.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(crProcessRepository.getListCrProcessLevel3CBB(any()))
        .thenReturn(crProcessDTOS);
    List<CrProcessInsideDTO> crProcessDTOS1 = crProcessBusiness
        .getListCrProcessLevel3CBB(crProcessDTO);
    Assert.assertEquals(crProcessDTOS.size(), crProcessDTOS1.size());
  }
}
