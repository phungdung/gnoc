package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.repository.CategoryRepository;
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
@PrepareForTest({CategoryBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, DateTimeUtils.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CategoryBusinessImplTest {

  @InjectMocks
  CategoryBusinessImpl categoryBusiness;

  @Mock
  CategoryRepository categoryRepository;


  @Test
  public void testGetListParentCategoryDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    List<CategoryDTO> list = Mockito.spy(ArrayList.class);
    categoryDTO.setCategoryName("xxx");
    categoryDTO.setCategoryId(1L);
    list.add(categoryDTO);
    PowerMockito.when(categoryRepository.getListAllCategory(any())).thenReturn(list);
    List<CategoryDTO> categoryDTOList = categoryBusiness.getListParentCategoryDTO(categoryDTO);
    Assert.assertEquals(categoryDTOList.size(), list.size());
  }

  @Test
  public void testGetListCategoryDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setParentCategoryId(69L);
    categoryDTO.setCategoryName("xxx");
    List<CategoryDTO> list = Mockito.spy(ArrayList.class);
    list.add(categoryDTO);
    datatable.setData(list);
    PowerMockito.when(categoryRepository.getListCategoryDTO(any())).thenReturn(datatable);
    PowerMockito.when(categoryRepository.findCategoryById(anyLong())).thenReturn(categoryDTO);
    Datatable result = categoryBusiness.getListCategoryDTO(categoryDTO);
    Assert.assertEquals(result.getTotal(), datatable.getTotal());
  }

  @Test
  public void testGetListCategoryDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = null;
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    PowerMockito.when(categoryRepository.getListCategoryDTO(any())).thenReturn(datatable);
    Datatable datatable1 = categoryBusiness.getListCategoryDTO(categoryDTO);
    Assert.assertNull(datatable1);
  }

  @Test
  public void testUpdateCategory_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.DUPLICATE);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("CfgCategory.null.unique")).thenReturn("xxx");
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryId(1L);
    categoryDTO.setCategoryName("yyyy");

    CategoryDTO oldDto = Mockito.spy(CategoryDTO.class);
    oldDto.setCategoryName("xxx");
    oldDto.setCategoryId(2L);
    oldDto.setParentCategoryId(3L);
    PowerMockito.when(categoryRepository.findCategoryById(anyLong())).thenReturn(oldDto);
    //true la DUPLICATE
    PowerMockito.when(categoryRepository.checkNameExist(anyString())).thenReturn(true);
    ResultInSideDto result = categoryBusiness.updateCategory(categoryDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testUpdateCategory_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryId(1L);
    categoryDTO.setCategoryName("yyyy");
    categoryDTO.setParentCategoryId(4L);

    CategoryDTO oldDto = null;
    PowerMockito.when(categoryRepository.findCategoryById(anyLong())).thenReturn(oldDto);
    PowerMockito.when(categoryRepository.updateCategory(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = categoryBusiness.updateCategory(categoryDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDeleteCategory_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(categoryRepository.deleteCategory(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = categoryBusiness.deleteCategory(1L);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testDeleteListCategory_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CategoryDTO dto = Mockito.spy(CategoryDTO.class);
    List<CategoryDTO> categoryListDTO = Mockito.spy(ArrayList.class);
    categoryListDTO.add(dto);
    PowerMockito.when(categoryRepository.deleteListCategory(anyList())).thenReturn(resultInSideDto);
    ResultInSideDto result = categoryBusiness.deleteListCategory(categoryListDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testFindCategoryById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setParentCategoryId(1L);

    PowerMockito.when(categoryRepository.findCategoryById(anyLong())).thenReturn(categoryDTO);
    CategoryDTO dto = categoryBusiness.findCategoryById(1L);
    Assert.assertEquals(dto, categoryDTO);
  }

  @Test
  public void testCheckName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    ResultInSideDto result = categoryBusiness.checkName(categoryDTO);
    Assert.assertEquals(result.getKey(), null);
  }

  @Test
  public void testCheckName_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto rs = Mockito.spy(ResultInSideDto.class);
    rs.setKey(RESULT.DUPLICATE);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("CfgCategory.null.unique")).thenReturn("xxx");

    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryName("qwe");
    PowerMockito.when(categoryRepository.checkNameExist(anyString())).thenReturn(true);
    ResultInSideDto result = categoryBusiness.checkName(categoryDTO);
    Assert.assertEquals(result.getKey(), rs.getKey());
  }

  @Test
  public void testCheckName_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryName("qwe");
    PowerMockito.when(categoryRepository.checkNameExist(anyString())).thenReturn(false);
    ResultInSideDto result = categoryBusiness.checkName(categoryDTO);
    Assert.assertEquals(result.getKey(), null);
  }

  @Test
  public void testInsertCategory_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryName("qwe");
    ResultInSideDto rs = Mockito.spy(ResultInSideDto.class);
    rs.setKey(RESULT.DUPLICATE);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("CfgCategory.null.unique")).thenReturn("xxx");
    PowerMockito.when(categoryRepository.checkNameExist(anyString())).thenReturn(true);
    ResultInSideDto result = categoryBusiness.insertCategory(categoryDTO);
    Assert.assertEquals(result.getKey(), rs.getKey());
  }

  @Test
  public void testInsertCategory_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryName("qwe");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("CfgCategory.null.unique")).thenReturn("xxx");
    PowerMockito.when(categoryRepository.checkNameExist(anyString())).thenReturn(false);
    PowerMockito.when(categoryRepository.insertCategory(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = categoryBusiness.insertCategory(categoryDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void insertOrUpdateListCategory() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CategoryDTO dto = Mockito.spy(CategoryDTO.class);
    List<CategoryDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    categoryDTO.setCategoryDTO(list);
    PowerMockito.when(categoryRepository.insertOrUpdateListCategory(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = categoryBusiness.insertOrUpdateListCategory(categoryDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testGetListCategory_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CategoryDTO> list = Mockito.spy(ArrayList.class);
    CategoryDTO categoryDTO = Mockito.spy(CategoryDTO.class);
    list.add(categoryDTO);
    PowerMockito.when(categoryRepository.getListCategory(any())).thenReturn(list);
    List<CategoryDTO> categoryDTOS = categoryBusiness.getListCategory(categoryDTO);
    Assert.assertEquals(categoryDTOS.size(), list.size());
  }
}
