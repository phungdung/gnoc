package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<CategoryDTO> getListCategoryDTO(CategoryDTO categoryDTO) {
    log.info("Request to getListCategoryDTO : {}", categoryDTO);
    I18n.setLocaleForService(wsContext);
    return commonStreamServiceProxy.getListCategoryDTO2(categoryDTO);
  }
}
