package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPortFactory;
import com.viettel.gnoc.commons.incident.provider.WsGenericObjectPool;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.repository.CrCableRepository;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.JsonResponseBO;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.List;
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
import org.slf4j.Logger;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({CrCableBusinessImpl.class, DataUtil.class, WSNIMSInfraPortFactory.class,
    WsGenericObjectPool.class, PassTranformer.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrCableBusinessImplTest {

  @InjectMocks
  CrCableBusinessImpl crCableBusiness;
  @Mock
  CrCableRepository crCableRepository;
  @Mock
  WSNIMSInfraPort wsnimsInfraPort;
  @Mock
  WSNIMSInfraPortFactory wsnimsInfraPortFactory;

  @Test
  public void getLinkInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrCableDTO crCableDTO = Mockito.spy(CrCableDTO.class);
    crCableDTO.setPage(1);
    crCableDTO.setPageSize(1);
    List<String> lstCableCode = Mockito.spy(ArrayList.class);
    lstCableCode.add("AAG");
    crCableDTO.setLstCableCode(lstCableCode);
    List<LinkInfoDTO> lstLink = Mockito.spy(ArrayList.class);
    LinkInfoDTO linkInfoDTO = Mockito.spy(LinkInfoDTO.class);
    lstLink.add(linkInfoDTO);
    try {
      PowerMockito.when(wsnimsInfraPort.getLinkInfo(anyList())).thenReturn(lstLink);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    PowerMockito.mockStatic(DataUtil.class);
    List<LinkInfoDTO> crSubList = Mockito.spy(ArrayList.class);
    crSubList.add(linkInfoDTO);
    PowerMockito.when((List<LinkInfoDTO>) DataUtil.subPageList(anyList(), anyInt(), anyInt()))
        .thenReturn(crSubList);
    Datatable datatable1 = crCableBusiness.getLinkInfo(crCableDTO);
    Assert.assertEquals(datatable1.getPages(), 1L);
  }

  @Test
  public void getListCrCableByCondition_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrCableDTO> crCableDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crCableRepository.getListCrCableByCondition(any())).thenReturn(crCableDTOS);
    List<CrCableDTO> crCableDTOS1 = crCableBusiness.getListCrCableByCondition(crInsiteDTO);
    Assert.assertEquals(crCableDTOS.size(), crCableDTOS1.size());
  }

  @Test
  public void GNOC_getInfraCableLane_01() throws Exception {
   CrCableDTO args1 = PowerMockito.mock(CrCableDTO.class);
   PowerMockito.when(args1.getPage()).thenReturn(1);
   PowerMockito.when(args1.getLaneCode()).thenReturn("CODE");
   PowerMockito.when(args1.getStartPoint()).thenReturn("1");
   PowerMockito.when(args1.getEndPoint()).thenReturn("10");
   args1.setPage(1);
   args1.setPageSize(10);
   InfraWS port = createConnect();
   PowerMockito.mockStatic(PassTranformer.class);
   PowerMockito.when(PassTranformer.decrypt(any())).thenReturn("DECR");
   JsonResponseBO dataJson = PowerMockito.mock(JsonResponseBO.class);
   PowerMockito.when(port.getDataJson(any(), any(), any())).thenReturn(dataJson);
   PowerMockito.when(dataJson.getDataJson()).thenReturn("{\"data\":[{\"LANE_CODE\":\"CODE\"}]}");
   Datatable datatable = crCableBusiness.GNOC_getInfraCableLane(args1);
   Assert.assertEquals(1, datatable.getTotal());
  }

  @Test
  public void getAllCableInLane_01() throws Exception {
    CrCableDTO args1 = PowerMockito.mock(CrCableDTO.class);
    PowerMockito.when(args1.getPage()).thenReturn(1);
    PowerMockito.when(args1.getLaneCode()).thenReturn("CODE1,CODE2");
    PowerMockito.when(args1.getNationCode()).thenReturn("VNM");
    List<CrCableDTO> crCableDTOS = Mockito.spy(ArrayList.class);
    CrCableDTO item1 = PowerMockito.mock(CrCableDTO.class);
    crCableDTOS.add(item1);
    PowerMockito.when(wsnimsInfraPort.getAllCableInLane(any())).thenReturn(crCableDTOS);
    Datatable datatable = crCableBusiness.getAllCableInLane(args1);
    Assert.assertEquals(1, datatable.getTotal());
  }

  @Test
  public void getSleevesInCable_01() throws Exception {
    CrCableDTO args1 = PowerMockito.mock(CrCableDTO.class);
    PowerMockito.when(args1.getPage()).thenReturn(1);
    PowerMockito.when(args1.getLaneCode()).thenReturn("CODE1,CODE2");
    PowerMockito.when(args1.getNationCode()).thenReturn("VNM");
    InfraWS port = createConnect();
    List<InfraSleevesBO> res = Mockito.spy(ArrayList.class);
    InfraSleevesBO item1 = PowerMockito.mock(InfraSleevesBO.class);
    res.add(item1);
    PowerMockito.when(port.getSleevesInCable(any(), any(), any())).thenReturn(res);
    List<InfraSleevesBO> results = crCableBusiness.getSleevesInCable("1", 1l, "1");
    Assert.assertEquals(1, results.size());
  }

  @Test
  public void getListCrCableDTO_01() throws Exception {
    CrCableDTO args1 = PowerMockito.mock(CrCableDTO.class);
    PowerMockito.when(args1.getPage()).thenReturn(1);
    PowerMockito.when(args1.getLaneCode()).thenReturn("CODE1,CODE2");
    PowerMockito.when(args1.getNationCode()).thenReturn("VNM");
    List<CrCableDTO> res = Mockito.spy(ArrayList.class);
    CrCableDTO item1 = PowerMockito.mock(CrCableDTO.class);
    res.add(item1);
    PowerMockito.when(crCableRepository.getListCrCableDTO(args1, 0, 1, "", "")).thenReturn(res);
    List<CrCableDTO> results = crCableBusiness.getListCrCableDTO(args1, 0, 1, "", "");
    Assert.assertEquals(1, results.size());
  }

  public InfraWS createConnect() throws Exception {
    InfraWS infraWS = PowerMockito.mock(InfraWS.class);
//    Method privateMethod = CrCableBusinessImpl.class.getDeclaredMethod("createConnect", null);
    WsGenericObjectPool wsGenericObjectPool = PowerMockito.mock(WsGenericObjectPool.class);
    PowerMockito.when(wsnimsInfraPortFactory.getWsFactory()).thenReturn(wsGenericObjectPool);
    PowerMockito.when(wsGenericObjectPool.borrowObject()).thenReturn(infraWS);
//    privateMethod.setAccessible(true);
//    return (InfraWS) privateMethod.invoke(crCableBusiness);
    return infraWS;
  }
}
