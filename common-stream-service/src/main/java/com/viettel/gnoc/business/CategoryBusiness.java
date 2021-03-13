package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;

public interface CategoryBusiness {

  List<CategoryDTO> getListParentCategoryDTO(CategoryDTO categoryDTO);

  Datatable getListCategoryDTO(CategoryDTO categoryDTO);

  ResultInSideDto updateCategory(CategoryDTO categoryDTO);

  ResultInSideDto deleteCategory(Long id);

  ResultInSideDto deleteListCategory(List<CategoryDTO> categoryListDTO);

  CategoryDTO findCategoryById(Long id);

  ResultInSideDto insertCategory(CategoryDTO categoryDTO);

  ResultInSideDto insertOrUpdateListCategory(
      CategoryDTO categoryDTO);

  List<CategoryDTO> getListCategory(CategoryDTO categoryDTO);

  List<CategoryDTO> getListCategoryDTO2(CategoryDTO categoryDTO);

}
