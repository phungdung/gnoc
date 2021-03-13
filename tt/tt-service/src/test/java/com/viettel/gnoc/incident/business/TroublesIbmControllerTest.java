package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.dto.UnitIbmDTO;
import com.viettel.gnoc.incident.repository.ProductIbmRepository;
import com.viettel.gnoc.incident.repository.TroublesIbmRepository;
import com.viettel.gnoc.incident.repository.UnitIbmRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TroublesServiceForCCBusinessImpl.class, FileUtils.class, I18n.class,
    CommonImport.class, CommonExport.class, WebServiceContext.class, TicketProvider.class,
    DataUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class TroublesIbmControllerTest {

  @InjectMocks
  TroublesIbmBusinessImpl troublesIbmBusiness;

  @Mock
  TroublesIbmRepository troublesIbmRepository;

  @Mock
  UnitIbmRepository unitIbmRepository;

  @Mock
  ProductIbmRepository productIbmRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Test
  public void getListTroublesIbmDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    TroublesIbmDTO troublesIbmDTO = PowerMockito.mock(TroublesIbmDTO.class);
    PowerMockito.when(troublesIbmRepository.getListTroublesIbmDTO(any())).thenReturn(datatable);
    Datatable data = troublesIbmBusiness.getListTroublesIbmDTO(troublesIbmDTO);
    Assert.assertEquals(datatable, data);
  }

  @Test
  public void getListUnitIbmDTOCombobox() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitIbmDTO unitIbmDTO = Mockito.spy(UnitIbmDTO.class);
    List<UnitIbmDTO> unitIbmDTOS = Mockito.spy(ArrayList.class);
    unitIbmDTO.setUnitCode("test");
    unitIbmDTOS.add(unitIbmDTO);
    PowerMockito.when(unitIbmRepository.getListUnitIbmDTOCombobox()).thenReturn(unitIbmDTOS);
    List<UnitIbmDTO> data = troublesIbmBusiness.getListUnitIbmDTOCombobox();
    Assert.assertEquals(unitIbmDTOS, data);
  }

  @Test
  public void getListProductIbmDTOCombobox() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProductIbmDTO productIbmDTO = Mockito.spy(ProductIbmDTO.class);
    List<ProductIbmDTO> productIbmDTOS = Mockito.spy(ArrayList.class);
    productIbmDTO.setCode("test");
    productIbmDTOS.add(productIbmDTO);
    PowerMockito.when(productIbmRepository.getListProductIbmDTOCombobox())
        .thenReturn(productIbmDTOS);
    List<ProductIbmDTO> data = troublesIbmBusiness.getListProductIbmDTOCombobox();
    Assert.assertEquals(productIbmDTOS, data);
  }

  @Test
  public void insertTroublesIbm() throws IOException {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    TroublesIbmDTO troublesIbmDTO = Mockito.spy(TroublesIbmDTO.class);
    List<MultipartFile> multipartFiles = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    userToken.setFullName("Le Van Thanh");
    troublesIbmDTO.setIbmId(1l);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("A");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(troublesIbmRepository.insertTroublesIbm(any())).thenReturn(resultInSideDto);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto data = troublesIbmBusiness.insertTroublesIbm(multipartFiles, troublesIbmDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());

  }
}
