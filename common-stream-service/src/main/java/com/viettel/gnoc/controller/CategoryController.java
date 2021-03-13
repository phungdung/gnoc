package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CategoryBusiness;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KienPV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "categoryService")
public class CategoryController {

  @Autowired
  private CategoryBusiness categoryBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @PostMapping("/getListCategoryDTO")
  public ResponseEntity<Datatable> getListCategoryDTO(
      @RequestBody CategoryDTO categoryDTO) {
    Datatable datatable = categoryBusiness.getListCategoryDTO(categoryDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListParentCategoryDTO")
  public ResponseEntity<List<CategoryDTO>> getListParentCategoryDTO(
      @RequestBody CategoryDTO categoryDTO) {
    List<CategoryDTO> list = categoryBusiness.getListParentCategoryDTO(categoryDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PutMapping("/updateCategory")
  public ResponseEntity<ResultInSideDto> updateCategory(
      @RequestBody @Valid CategoryDTO categoryDTO) {
    ResultInSideDto result = categoryBusiness.updateCategory(categoryDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/deleteCategory")
  public ResponseEntity<ResultInSideDto> deleteCategory(Long id) {
    ResultInSideDto result = categoryBusiness.deleteCategory(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/deleteListCategory")
  public ResponseEntity<ResultInSideDto> deleteListCategory(
      @RequestBody @Valid CategoryDTO categoryDTO) {
    ResultInSideDto result = categoryBusiness.deleteListCategory(categoryDTO.getCategoryDTO());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findCategoryById")
  public ResponseEntity<CategoryDTO> findCategoryById(Long id) {
    CategoryDTO categoryDTO = categoryBusiness.findCategoryById(id);
    return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
  }

  @PutMapping("/insertCategory")
  public ResponseEntity<ResultInSideDto> insertCategory(
      @RequestBody @Valid CategoryDTO categoryDTO) {
    ResultInSideDto result = categoryBusiness.insertCategory(categoryDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PutMapping("/insertOrUpdateListCategory")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListCategory(
      @RequestBody @Valid CategoryDTO categoryDTO) {
    ResultInSideDto result = categoryBusiness.insertOrUpdateListCategory(categoryDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListCategory")
  public ResponseEntity<List<CategoryDTO>> getListCategory(
      @RequestBody CategoryDTO categoryDTO) {
    List<CategoryDTO> data = categoryBusiness.getListCategory(categoryDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCategoryDTO2")
  public ResponseEntity<List<CategoryDTO>> getListCategoryDTO2(
      @RequestBody CategoryDTO categoryDTO) {
    List<CategoryDTO> categoryDTOS = categoryBusiness.getListCategoryDTO2(categoryDTO);
    return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
  }

}
