package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.CategoryDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "CategoryService")
@XmlSeeAlso({CategoryDTO.class})
public interface CategoryService {

  @WebMethod(operationName = "getListCategoryDTO")
  public List<CategoryDTO> getListCategoryDTO(
      @WebParam(name = "categoryDTO") CategoryDTO categoryDTO
  );
}
