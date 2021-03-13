package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository {

  List<CategoryDTO> getListAllCategory(CategoryDTO categoryDTO);

  Datatable getListCategoryDTO(CategoryDTO categoryDTO);

  ResultInSideDto updateCategory(CategoryDTO categoryDTO);

  ResultInSideDto deleteCategory(Long id);

  ResultInSideDto deleteListCategory(
      List<CategoryDTO> categoryListDTO);

  CategoryDTO findCategoryById(Long id);

  ResultInSideDto insertCategory(CategoryDTO categoryDTO);

  ResultInSideDto insertOrUpdateListCategory(
      List<CategoryDTO> categoryDTO);


  List<CategoryDTO> getListCategory(CategoryDTO categoryDTO);

  boolean checkNameExist(String name);
}
