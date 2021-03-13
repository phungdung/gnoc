package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import com.viettel.gnoc.ws.provider.NocProWS;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({CrBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    StringUtils.class, DateUtil.class, TicketProvider.class, DataUtil.class, CommonExport.class,
    CrGeneralUtil.class, DateTimeUtils.class, JAXBContext.class, NocProWS.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrAffectedServiceDetailsBusinessImplTest {

  @InjectMocks
  CrAffectedServiceDetailsBusinessImpl crAffectedServiceDetailsBusiness;

  @Mock
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;
//
//  @Before
//  public void setUp() throws Exception {
//  }
//
//  @After
//  public void tearDown() throws Exception {
//  }

  @Test
  public void search_01() {
    List<CrAffectedServiceDetailsDTO> results = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO data = Mockito.spy(CrAffectedServiceDetailsDTO.class);
    PowerMockito.when(crAffectedServiceDetailsRepository
        .search(anyObject(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(results);
    List<CrAffectedServiceDetailsDTO> crAlarmFaultGroupDTOS = crAffectedServiceDetailsBusiness
        .search(data, 0, 1, "", "");
    Assert.assertEquals(crAlarmFaultGroupDTOS.size(), 0);
  }
}
