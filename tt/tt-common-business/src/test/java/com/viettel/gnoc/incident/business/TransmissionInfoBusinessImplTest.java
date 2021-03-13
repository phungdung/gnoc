package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TransmissionInfoRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.WSNIMSInfraPort;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import com.viettel.nims.infra.webservice.CntCableLinkPK;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import com.viettel.nims.infra.webservice.VCntCableLinkBO;
import com.viettel.nims.infra.webservice.VInfraCableWsBO;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
@PrepareForTest({TransmissionInfoBusinessImpl.class, SpringApplicationContext.class,
    FileUtils.class, WSCxfInInterceptor.class,
    CommonExport.class, I18n.class, DataUtil.class, TroubleBccsUtils.class, TroubleBccsUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class TransmissionInfoBusinessImplTest {

  @InjectMocks
  TransmissionInfoBusinessImpl transmissionInfoBusiness;

  @Mock
  TransmissionInfoRepository transmissionInfoRepository;

  @Mock
  WSNIMSInfraPort wsnimsInfraPort;

  @Test
  public void getListCatReason() {
    List<CatReasonInSideDTO> lstReason = Mockito.spy(ArrayList.class);
    CatReasonInSideDTO reason = Mockito.spy(CatReasonInSideDTO.class);
    reason.setId(1L);
    reason.setReasonCode("EM_CAUSE_MAT LUONG DOWNCELL_TRUYEN DAN_QUANG_LOI CAP LUONG");
    lstReason.add(reason);
    PowerMockito.when(transmissionInfoRepository.getListReasonSearch(any())).thenReturn(lstReason);
    List<CatItemDTO> actual = transmissionInfoBusiness.getListCatReason("2046");
    Assert.assertEquals(actual.size(), lstReason.size());
  }

  @Test
  public void getListCableType() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    List<VInfraCableWsBO> infraCableBO = Mockito.spy(ArrayList.class);
    VInfraCableWsBO vInfraCableWsBO = Mockito.spy(VInfraCableWsBO.class);
    vInfraCableWsBO.setOpticalCableType(1L);
    vInfraCableWsBO.setCableCode("1");
    infraCableBO.add(vInfraCableWsBO);
    PowerMockito.when(wsnimsInfraPort.getCableInLanes(any())).thenReturn(infraCableBO);
    List<CatItemDTO> actual = transmissionInfoBusiness.getListCableType("1", "1");
    Assert.assertEquals(actual.size(), infraCableBO.size());
  }

  @Test
  public void getListSnippetOff() throws Exception {
    List<VInfraCableWsBO> infraCableBO = Mockito.spy(ArrayList.class);
    VInfraCableWsBO vInfraCableWsBO = Mockito.spy(VInfraCableWsBO.class);
    vInfraCableWsBO.setCableId(1L);
    vInfraCableWsBO.setCableCode("1");
    infraCableBO.add(vInfraCableWsBO);
    PowerMockito.when(wsnimsInfraPort.getCableInLanes(any())).thenReturn(infraCableBO);
    List<CatItemDTO> actual = transmissionInfoBusiness.getListSnippetOff("1");
    Assert.assertEquals(actual.size(), infraCableBO.size());
  }

  @Test
  public void getListLinkInfoDTO() throws Exception {
    List<VCntCableLinkBO> lstTmp = Mockito.spy(ArrayList.class);
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(new Date());
    VCntCableLinkBO vCntCableLinkBO = Mockito.spy(VCntCableLinkBO.class);
    vCntCableLinkBO.setLastUpdateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    vCntCableLinkBO.setPk(new CntCableLinkPK());
    vCntCableLinkBO.setStartDeviceCode("1");
    vCntCableLinkBO.setStartPort("1");
    lstTmp.add(vCntCableLinkBO);
    VCntCableLinkBO vCntCableLinkBO2 = Mockito.spy(VCntCableLinkBO.class);
    vCntCableLinkBO2.setLastUpdateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    vCntCableLinkBO2.setPk(new CntCableLinkPK());
    vCntCableLinkBO2.setStartDeviceCode("1");
    vCntCableLinkBO2.setStartPort("1");
    lstTmp.add(vCntCableLinkBO2);
    PowerMockito.when(wsnimsInfraPort.getLinkInCables(any())).thenReturn(lstTmp);
    List<LinkInfoDTO> actual = transmissionInfoBusiness.getListLinkInfoDTO("1");
    Assert.assertEquals(1, actual.size());
  }

  @Test
  public void onSearchInfraCableLaneDTO() {
    InfraCableLaneDTO infraCableLaneDTO = Mockito.spy(InfraCableLaneDTO.class);
    infraCableLaneDTO.setLaneCode("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(new ArrayList<>());
    PowerMockito.when(transmissionInfoRepository.onSearchInfraCableLaneDTO(any())).thenReturn(datatable);
    Datatable actual = transmissionInfoBusiness.onSearchInfraCableLaneDTO(infraCableLaneDTO);
    Assert.assertEquals(datatable.getData().size(), actual.getData().size());
  }

  @Test
  public void onSearchInfraSleevesDTO() throws Exception {
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(new Date());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setPage(1);
    troublesDTO.setPageSize(1);
    troublesDTO.setCreatedTime(new Date());
    List<InfraSleevesBO> lst = Mockito.spy(ArrayList.class);
    InfraSleevesBO infraSleevesBO = Mockito.spy(InfraSleevesBO.class);
    infraSleevesBO.setModifyDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    lst.add(infraSleevesBO);
    PowerMockito.when(wsnimsInfraPort.getSleevesInCable(any(), any(), any())).thenReturn(lst);
    Datatable actual = transmissionInfoBusiness.onSearchInfraSleevesDTO("1", "1", troublesDTO);
    Assert.assertEquals(lst.size(), actual.getData().size());
  }
}
