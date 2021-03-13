package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import java.io.File;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrImpactedNodesBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    StringUtils.class, DateUtil.class, TicketProvider.class, DataUtil.class, CommonExport.class,
    CrGeneralUtil.class, DateTimeUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrImpactedNodesBusinessImplTest {

  @InjectMocks
  CrImpactedNodesBusinessImpl crImpactedNodesBusiness;

  @Mock
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void testActionImportAndGetNetworkNodeV2_01() {
    MultipartFile multipartFile = null;
    List<CrImpactedNodesDTO> lstImpactedNodes = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstAffectedNodes = Mockito.spy(ArrayList.class);

    ResultInSideDto actual = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(
            multipartFile,
            "1111",
            11,
            lstImpactedNodes,
            lstAffectedNodes
        );

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testActionImportAndGetNetworkNodeV2_02() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<CrImpactedNodesDTO> lstImpactedNodes = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstAffectedNodes = Mockito.spy(ArrayList.class);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    Object[] objects = new Object[]{"1"};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1001; i++) {
      lstData.add(objects);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 15, 1000
        )
    ).thenReturn(lstData);

    ResultInSideDto actual = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(
            multipartFile,
            "1111",
            11,
            lstImpactedNodes,
            lstAffectedNodes
        );

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testActionImportAndGetNetworkNodeV2_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<CrImpactedNodesDTO> lstImpactedNodes = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstAffectedNodes = Mockito.spy(ArrayList.class);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    Object[] objects = new Object[]{"1"};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 4, 0, 15, 1000
        )
    ).thenReturn(lstHeader);

    ResultInSideDto actual = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(
            multipartFile,
            "1111",
            11,
            lstImpactedNodes,
            lstAffectedNodes
        );

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testActionImportAndGetNetworkNodeV2_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<CrImpactedNodesDTO> lstImpactedNodes = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstAffectedNodes = Mockito.spy(ArrayList.class);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1"};
    Object[] objects1 = new Object[]{null, "1"};
    Object[] objects2 = new Object[]{null, null};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    lstData.add(objects1);
    lstData.add(objects2);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 4, 0, 15, 1000
        )
    ).thenReturn(lstHeader);

    ResultInSideDto actual = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(
            multipartFile,
            "1111",
            0,
            lstImpactedNodes,
            lstAffectedNodes
        );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionImportAndGetNetworkNodeV2_05() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<CrImpactedNodesDTO> lstImpactedNodes = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstAffectedNodes = Mockito.spy(ArrayList.class);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1"};
    Object[] objects1 = new Object[]{null, "1"};
    Object[] objects2 = new Object[]{null, null};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    lstData.add(objects1);
    lstData.add(objects2);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 4, 0, 15, 1000
        )
    ).thenReturn(lstHeader);

    ResultInSideDto actual = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(
            multipartFile,
            "1111",
            2,
            lstImpactedNodes,
            lstAffectedNodes
        );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testGetListInfraDeviceIpByListIP() {
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setIp("192.168.1.1");
    infraDeviceDTO.setDeviceCode("1122334455");
    List<InfraDeviceDTO> listIp = Mockito.spy(ArrayList.class);
    listIp.add(infraDeviceDTO);

    List<InfraDeviceDTO> actual = crImpactedNodesBusiness
        .getListInfraDeviceIpByListIP(listIp);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetLisNodeOfCR_01() {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);

    CrImpactedNodesDTO dto = Mockito.spy(CrImpactedNodesDTO.class);
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setEarlierStartTimeStr("14/05/2020 08:00:00");
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setSaveType("1");
    dto.setCrId("1111");

    List<CrImpactedNodesDTO> lst = Mockito.spy(ArrayList.class);

    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong()))
        .thenReturn(22D);
    PowerMockito.when(
        crImpactedNodesRepository
            .getImpactedNodesByCrIdV2(anyLong(), any(), any(), anyString())
    ).thenReturn(lst);

    List<CrImpactedNodesDTO> actual = crImpactedNodesBusiness
        .getLisNodeOfCR(dto);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetLisNodeOfCR_02() {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);

    CrImpactedNodesDTO dto = Mockito.spy(CrImpactedNodesDTO.class);
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setEarlierStartTimeStr("14/05/2020 08:00:00");
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setSaveType("2");
    dto.setCrId("1111");

    List<CrImpactedNodesDTO> lst = Mockito.spy(ArrayList.class);

    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong()))
        .thenReturn(22D);
    PowerMockito.when(
        crImpactedNodesRepository
            .getImpactedNodesByCrIdV2(anyLong(), any(), any(), anyString())
    ).thenReturn(lst);

    List<CrImpactedNodesDTO> actual = crImpactedNodesBusiness
        .getLisNodeOfCR(dto);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testSearch() {
    CrImpactedNodesDTO tForm = Mockito.spy(CrImpactedNodesDTO.class);
    tForm.setCrId("11");
    tForm.setInsertTime("14/05/2020 08:00:00");
    tForm.setType("22");

    List expected = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        crImpactedNodesRepository
            .getImpactedNodesByCrId(
                anyLong(), any(), any(), anyString(), any(), any(), any()
            )
    ).thenReturn(expected);

    List actual = crImpactedNodesBusiness
        .search(tForm, 1, 2, "1111", "2222");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListCrImpactedNodesDTO() {
    CrImpactedNodesDTO dto = Mockito.spy(CrImpactedNodesDTO.class);

    List<CrImpactedNodesDTO> expected = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        crImpactedNodesRepository.getListCrImpactedNodesDTO(any())
    ).thenReturn(expected);

    List<CrImpactedNodesDTO> actual = crImpactedNodesBusiness
        .getListCrImpactedNodesDTO(dto);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLisNodeOfCRForProxy_01() {
    CrImpactedNodesDTO dto = Mockito.spy(CrImpactedNodesDTO.class);
    dto.setCrId("1111");
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setSaveType("1");
    dto.setNodeType("2");

    List<CrImpactedNodesDTO> expected = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        crImpactedNodesRepository
            .getImpactedNodesByCrIdV2(anyLong(), any(), any(), anyString())
    ).thenReturn(expected);

    List<CrImpactedNodesDTO> actual = crImpactedNodesBusiness
        .getLisNodeOfCRForProxy(dto);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLisNodeOfCRForProxy_02() {
    CrImpactedNodesDTO dto = Mockito.spy(CrImpactedNodesDTO.class);
    dto.setCrId("1111");
    dto.setCrCreatedDateStr("14/05/2020 08:00:00");
    dto.setSaveType("2");
    dto.setNodeType("2");

    List<CrImpactedNodesDTO> expected = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        crImpactedNodesRepository
            .getImpactedNodesByCrId(anyLong(), any(), any(), anyString(), any(), any(), any())
    ).thenReturn(expected);

    List<CrImpactedNodesDTO> actual = crImpactedNodesBusiness
        .getLisNodeOfCRForProxy(dto);

    Assert.assertEquals(expected, actual);
  }

}
