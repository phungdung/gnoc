package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.repository.CrHisRepository;
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
@PrepareForTest({CrHisBusinessImpl.class, I18n.class, DataUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrHisBusinessImplTest {

  @InjectMocks
  CrHisBusinessImpl crHisBusiness;
  @Mock
  CrHisRepository crHisRepository;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Test
  public void searchSql_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    CrHisDTO crhisdto = Mockito.spy(CrHisDTO.class);
    crhisdto.setCrId("1");
    crhisdto.setPage(1);
    crhisdto.setPageSize(1);
    crhisdto.setActionCode("1");
    crhisdto.setStatus("1");
    List<CrHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crhisdto);
    PowerMockito.when(crHisRepository.getListCrHis(any())).thenReturn(lst);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    LanguageExchangeDTO dto = Mockito.spy(LanguageExchangeDTO.class);
    dto.setBussinessId(1L);
    dto.setLeeLocale("vi_VN");
    lstLanguage.add(dto);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), anyMap(), anyMap(), any()))
        .thenReturn(lstLanguage);
    PowerMockito.mockStatic(DataUtil.class);
    try {
      PowerMockito.when(DataUtil.setLanguage(any(), any(), any(), any()))
          .thenReturn(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    PowerMockito.when(I18n.getChangeManagement(any(), any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    Datatable datatable1 = crHisBusiness.searchSql(crhisdto);
    Assert.assertEquals(datatable1.getPages(), 1L);
  }
}
