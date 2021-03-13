package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.repository.UserReceiveMsgRepository;
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
@PrepareForTest({UserReceiveMsgBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class UserReceiveMsgBusinessImplTest {

  @InjectMocks
  UserReceiveMsgBusinessImpl userReceiveMsgBusiness;
  @Mock
  UserReceiveMsgRepository userReceiveMsgRepository;

  @Test
  public void insertOrUpdate_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(userReceiveMsgRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    UserReceiveMsgDTO userReceiveMsgDTO = Mockito.spy(UserReceiveMsgDTO.class);
    ResultInSideDto resultInSideDto1 = userReceiveMsgBusiness.insertOrUpdate(userReceiveMsgDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListUserReceiveMsgDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<UserReceiveMsgDTO> userReceiveMsgDTOS = Mockito.spy(ArrayList.class);
    UserReceiveMsgDTO userReceiveMsgDTO = Mockito.spy(UserReceiveMsgDTO.class);
    PowerMockito.when(userReceiveMsgRepository
        .getListUserReceiveMsgDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(userReceiveMsgDTOS);
    List<UserReceiveMsgDTO> userReceiveMsgDTOS1 = userReceiveMsgBusiness
        .getListUserReceiveMsgDTO(userReceiveMsgDTO, 1, 1, "asc", "ID");
    Assert.assertEquals(userReceiveMsgDTOS.size(), userReceiveMsgDTOS1.size());
  }

  @Test
  public void deleteUserReceiveMsg_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(userReceiveMsgRepository.deleteUserReceiveMsg(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = userReceiveMsgBusiness.deleteUserReceiveMsg(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }
}
