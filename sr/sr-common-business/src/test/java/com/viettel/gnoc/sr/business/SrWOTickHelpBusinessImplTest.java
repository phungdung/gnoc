package com.viettel.gnoc.sr.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SRParamRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.ResultCreateDtByFileInput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
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
@PrepareForTest({SrWOTickHelpBusinessImplTest.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, ObjectMapper.class, Workbook.class, ResultCreateDtByFileInput.class, ExcelWriterUtils.class, CommonExport.class,
    DateUtil.class, DataUtil.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SrWOTickHelpBusinessImplTest {

  @InjectMocks
  SrWOTickHelpBusinessImpl srWOTickHelpBusiness;
  @Mock
  SrOutsideBusiness srOutsideBusiness;

  @Mock
  SRParamRepository srParamRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  SROutsideRepository srOutsideRepository;

  @Mock
  SrRepository srRepository;

  @Mock
  SrBusiness srBusiness;

  @Mock
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Test
  public void getDetailSRForWOTHVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srWOTickHelpBusiness.getDetailSRForWOTHVSmart(null);
  }

  @Test
  public void getDetailSRForWOTHVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srWOTickHelpBusiness.getDetailSRForWOTHVSmart("a");
  }

  @Test
  public void getDetailSRForWOTHVSmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srWOTickHelpBusiness.getDetailSRForWOTHVSmart("1");
  }

  @Test
  public void getDetailSRForWOTHVSmart_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SrInsiteDTO srTmp = Mockito.spy(SrInsiteDTO.class);
    srTmp.setSrCode("1");
    srTmp.setCountry("1");
    srTmp.setStartTime(new Date());
    srTmp.setEndTime(new Date());
    srTmp.setUpdatedTime(new Date());
    srTmp.setUpdatedUser("1");
    srTmp.setSrId(1L);
    srTmp.setSrUnit(1L);
    SRParamEntity srParamEntity = Mockito.spy(SRParamEntity.class);
    srParamEntity.setSrParamId(1L);
    List<SRParamEntity> lstParam = Mockito.spy(ArrayList.class);
    lstParam.add(srParamEntity);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(srParamRepository.findListSRParamBySrId(anyLong())).thenReturn(lstParam);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srTmp);
    srWOTickHelpBusiness.getDetailSRForWOTHVSmart("1");
  }

  @Test
  public void getListSRForWOTHVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srWOTickHelpBusiness.getListSRForWOTHVSmart(srDTO, null);
  }

  @Test
  public void getListSRForWOTHVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    List<SRDTO> list = Mockito.spy(ArrayList.class);
//    list.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForWOTHVSmart(any(), anyString())).thenReturn(list);
    srWOTickHelpBusiness.getListSRForWOTHVSmart(srDTO, "1");
  }

  @Test
  public void getListSRForWOTHVSmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    List<SRDTO> list = Mockito.spy(ArrayList.class);
    list.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForWOTHVSmart(any(), anyString())).thenReturn(list);
    srWOTickHelpBusiness.getListSRForWOTHVSmart(srDTO, "1");
  }


  @Test
  public void createSRForWOTHVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    ObjKeyValueVsmartDTO objKeyValueVsmartDTO = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objKeyValueVsmartDTO.setKeyCode("woId");
    objKeyValueVsmartDTO.setKey("1");
    objKeyValueVsmartDTO.setType("1");
    List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO = Mockito.spy(ArrayList.class);
    lstObjKeyValueVsmartDTO.add(objKeyValueVsmartDTO);
    ResultDTO resCreate = Mockito.spy(ResultDTO.class);
    resCreate.setMessage("SUCCESS");
    resCreate.setKey("1_1_1_1");
    ResultInSideDto resOtherSys = Mockito.spy(ResultInSideDto.class);
    resOtherSys.setKey("ERROR");
    PowerMockito.when(srCreatedFromOtherSysRepository.insertSRCreateFromOtherSys(any())).thenReturn(resOtherSys);
    PowerMockito.when(srOutsideBusiness.createSRByConfigGroup(any(), anyString())).thenReturn(resCreate);
    srWOTickHelpBusiness.createSRForWOTHVSmart(lstObjKeyValueVsmartDTO, "1", "1");
  }

  @Test
  public void createSRForWOTHVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    ObjKeyValueVsmartDTO objKeyValueVsmartDTO = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objKeyValueVsmartDTO.setKeyCode("woId");
    objKeyValueVsmartDTO.setKey("1");
    objKeyValueVsmartDTO.setType("1");
    List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO = Mockito.spy(ArrayList.class);
    lstObjKeyValueVsmartDTO.add(objKeyValueVsmartDTO);
    ResultDTO resCreate = Mockito.spy(ResultDTO.class);
    resCreate.setMessage("SUCCESS");
    resCreate.setKey("1_1_1_1");
    ResultInSideDto resOtherSys = Mockito.spy(ResultInSideDto.class);
    resOtherSys.setKey("SUCCESS");
    PowerMockito.when(srCreatedFromOtherSysRepository.insertSRCreateFromOtherSys(any())).thenReturn(resOtherSys);
    PowerMockito.when(srOutsideBusiness.createSRByConfigGroup(any(), anyString())).thenReturn(resCreate);
    srWOTickHelpBusiness.createSRForWOTHVSmart(lstObjKeyValueVsmartDTO, "1", "1");
  }

  @Test
  public void createSRForWOTHVSmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    ObjKeyValueVsmartDTO objKeyValueVsmartDTO = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objKeyValueVsmartDTO.setKeyCode("woId");
    objKeyValueVsmartDTO.setKey("1");
    objKeyValueVsmartDTO.setType("1");

    ObjKeyValueVsmartDTO objKeyValueVsmartDTO1 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objKeyValueVsmartDTO1.setKeyCode("woId");
    objKeyValueVsmartDTO1.setKey("1");
    objKeyValueVsmartDTO1.setType("2");
    objKeyValueVsmartDTO1.setValue("1");

    List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO = Mockito.spy(ArrayList.class);
    lstObjKeyValueVsmartDTO.add(objKeyValueVsmartDTO);
    lstObjKeyValueVsmartDTO.add(objKeyValueVsmartDTO1);
    ResultDTO resCreate = Mockito.spy(ResultDTO.class);
    resCreate.setMessage("SUCCESS");
    resCreate.setKey("1_1_1_1");
    ResultInSideDto resOtherSys = Mockito.spy(ResultInSideDto.class);
    resOtherSys.setKey("SUCCESS");
    PowerMockito.when(srCreatedFromOtherSysRepository.insertSRCreateFromOtherSys(any())).thenReturn(resOtherSys);
    PowerMockito.when(srOutsideBusiness.createSRByConfigGroup(any(), anyString())).thenReturn(resCreate);
    srWOTickHelpBusiness.createSRForWOTHVSmart(lstObjKeyValueVsmartDTO, "1", "1");
  }
}
