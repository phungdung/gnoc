package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import com.viettel.gnoc.wo.repository.MaterialCodienRepository;
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
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MaterialCodienBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MaterialCodienBusinessTest {

  @InjectMocks
  MaterialCodienBusinessImpl materialCodienBusiness;
  @Mock
  MaterialCodienRepository materialCodienRepository;

  @Test
  public void getListMaterialCodienByWoId_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MaterialCodienDTO> materialCodienDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(materialCodienRepository.getListMaterialCodienByWoId(anyLong()))
        .thenReturn(materialCodienDTOS);
    List<MaterialCodienDTO> materialCodienDTOS1 = materialCodienBusiness
        .getListMaterialCodienByWoId(anyLong());
    Assert.assertEquals(materialCodienDTOS.size(), materialCodienDTOS1.size());
  }

  @Test
  public void deleteMaterialCodienByWoId_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    List<MaterialCodienDTO> lst = Mockito.spy(ArrayList.class);
    MaterialCodienDTO materialCodienDTO = Mockito.spy(MaterialCodienDTO.class);
    materialCodienDTO.setId(1L);
    lst.add(materialCodienDTO);
    PowerMockito.when(materialCodienRepository.getListMaterialCodienByWoId(anyLong()))
        .thenReturn(lst);
    PowerMockito.when(materialCodienRepository.deleteMaterialCodien(anyLong())).thenReturn(result);
    ResultInSideDto result1 = materialCodienBusiness.deleteMaterialCodienByWoId(85L);
    Assert.assertEquals(result.getKey(), result1.getKey());
  }

  @Test
  public void inserOrUpdateMaterialCodien_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    MaterialCodienDTO materialCodienDTO = Mockito.spy(MaterialCodienDTO.class);
    PowerMockito.when(materialCodienRepository.inserOrUpdateMaterialCodien(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = materialCodienBusiness
        .inserOrUpdateMaterialCodien(materialCodienDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }
}
