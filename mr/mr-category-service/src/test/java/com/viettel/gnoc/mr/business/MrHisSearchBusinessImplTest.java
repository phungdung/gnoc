package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import com.viettel.gnoc.mr.repository.MrHisSearchRepository;
import java.util.ArrayList;
import java.util.List;
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

public class MrHisSearchBusinessImplTest {

  @InjectMocks
  MrHisSearchBusinessImpl mrHisSearchBusiness;

  @Mock
  MrHisSearchRepository mrHisSearchRepository;

  @Test
  public void getListMrHisSearch() {
    MrHisSearchDTO mrHisSearchDTO = Mockito.spy(MrHisSearchDTO.class);
    List<MrHisSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrHisSearchDTO);
    PowerMockito.mockStatic(I18n.class);
    when(mrHisSearchRepository.getListMrHisSearch(any())).thenReturn(lst);
    mrHisSearchBusiness.getListMrHisSearch(mrHisSearchDTO);
  }

}
