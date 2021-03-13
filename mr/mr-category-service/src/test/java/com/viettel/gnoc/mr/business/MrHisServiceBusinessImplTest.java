package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.mr.repository.MrHisServiceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrHisServiceBusinessImplTest {

  @InjectMocks
  MrHisServiceBusinessImpl mrHisServiceBusiness;

  @Mock
  MrHisServiceRepository mrHisServiceRepository;

  @Test
  public void insertMrHis() {
    MrHisDTO mrHisDTO = Mockito.spy(MrHisDTO.class);
    mrHisDTO.setComments("aaaa");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(mrHisServiceRepository.insertMrHis(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrHisServiceBusiness.insertMrHis(mrHisDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

  }
}
