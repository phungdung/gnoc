package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsRepository;
import org.apache.commons.math3.analysis.function.Power;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleBtsBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, FileInputStream.class, ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrScheduleBtsBusinessImplTest {

  @InjectMocks
  MrScheduleBtsBusinessImpl mrScheduleBtsBusiness;

  @Mock
  MrScheduleBtsRepository mrScheduleBtsRepository;

  @Mock
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Test
  public void onSearch() {
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsDTO dto = Mockito.spy(MrScheduleBtsDTO.class);
    dto.setWoStatus("1");
    dto.setSortName("woStatusName");
    MrScheduleBtsDTO mrScheduleBtsDTO = Mockito.spy(MrScheduleBtsDTO.class);
    List<MrScheduleBtsDTO> lstMrSch = Mockito.spy(ArrayList.class);
    lstMrSch.add(mrScheduleBtsDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstMrSch);
    PowerMockito.when(mrScheduleBtsRepository.onSearch(dto)).thenReturn(datatable);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setProvinceCode("test");
    mrDeviceBtsDTO.setProvinceName("test1");
    List<MrDeviceBtsDTO> lstProvince = Mockito.spy(ArrayList.class);
    lstProvince.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListProvince(null)).thenReturn(lstProvince);

    dto.setSortName("woStatusName");
    mrScheduleBtsBusiness.onSearch(dto);
    dto.setSortName("ProvinceCode");
    mrScheduleBtsBusiness.onSearch(dto);
    dto.setSortName("DeviceTypeName");
    mrScheduleBtsBusiness.onSearch(dto);
  }

  @Test
  public void exportSearchData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsDTO mrScheduleBtsDTO = Mockito.spy(MrScheduleBtsDTO.class);
    File file = PowerMockito.mock(File.class);
    PowerMockito.when(
        CommonExport
            .exportFileWithTemplateXLSX(anyString(), anyString(), any(), anyString(), anyList(),
                any(), anyString())).thenReturn(file);
    mrScheduleBtsBusiness.exportSearchData(mrScheduleBtsDTO);
  }

  @Test
  public void getFileTemplate() {
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsDTO mrScheduleBtsDTO = Mockito.spy(MrScheduleBtsDTO.class);

    MrScheduleBtsDTO dto = Mockito.spy(MrScheduleBtsDTO.class);
    dto.setWoStatus("1");
    List<MrScheduleBtsDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(dto);
    PowerMockito.when(mrScheduleBtsRepository.onSearchExport(any())).thenReturn(lstData);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setProvinceCode("test");
    mrDeviceBtsDTO.setProvinceName("test1");
    List<MrDeviceBtsDTO> lstProvince = Mockito.spy(ArrayList.class);
    lstProvince.add(mrDeviceBtsDTO);


    mrScheduleBtsBusiness.getFileTemplate(mrScheduleBtsDTO);
  }

  @Test
  public void importMrScheduleBTS() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = null;
    mrScheduleBtsBusiness.importMrScheduleBTS(multipartFile);
  }

  @Test
  public void importMrScheduleBTS1() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(File.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);

    String filePath = "/temp.xls";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);


    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      Object[] objects = new Object[12];
      for (int j = 0; j < objects.length; j++) {
        objects[j] = "";
      }
      lstData.add(objects);
    }

    File fileImp = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcel(fileImp, 0, 8, 0, 11, 2))
        .thenReturn(lstData);

    mrScheduleBtsBusiness.importMrScheduleBTS(multipartFile);
  }

  @Test
  public void exportFileImport() {
  }

  @Test
  public void validateFileImport() {
  }
}
