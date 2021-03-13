package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.io.InputStream;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({SrAddOnBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, DateTimeUtils.class, InputStream.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SrAddOnBusinessImplTest {

  @InjectMocks
  SrAddOnBusinessImpl srAddOnBusiness;

  @Mock
  SrOutsideBusiness srOutsideBusiness;

  @Test
  public void createSRByConfigGroup() {
    ResultDTO expected = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        srOutsideBusiness.createSRByConfigGroup(any(), anyString())
    ).thenReturn(expected);

    SRDTO srInputDTO = Mockito.spy(SRDTO.class);
    String configGroup = "test";
    ResultDTO actual = srAddOnBusiness
        .createSRByConfigGroup(srInputDTO, configGroup);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListSRByConfigGroup() {
    List<SRDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        srOutsideBusiness.getListSRByConfigGroup(any(), anyString())
    ).thenReturn(expected);

    SRDTO srInputDTO = Mockito.spy(SRDTO.class);
    String configGroup = "test";
    List<SRDTO> actual = srAddOnBusiness
        .getListSRByConfigGroup(srInputDTO, configGroup);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListSRCatalogByConfigGroup() {
    List<SRCatalogDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        srOutsideBusiness.getListSRCatalogByConfigGroup(anyString())
    ).thenReturn(expected);

    String configGroup = "test";
    String scopeOfUse = "test";
    List<SRCatalogDTO> actual = srAddOnBusiness
        .getListSRCatalogByConfigGroup(configGroup);
    Assert.assertEquals(expected, actual);
  }
}
