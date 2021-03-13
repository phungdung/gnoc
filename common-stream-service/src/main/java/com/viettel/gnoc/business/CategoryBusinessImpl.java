package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.CategoryRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Service
@Transactional
public class CategoryBusinessImpl implements CategoryBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Override
  public List<CategoryDTO> getListParentCategoryDTO(CategoryDTO categoryDTO) {
    List<CategoryDTO> list = categoryRepository.getListAllCategory(categoryDTO);
    for (CategoryDTO item : list) {
      item.setParentName(item.getCategoryName());
      item.setParentCategoryId(item.getCategoryId());
    }

    return list;
  }

  @Override
  public Datatable getListCategoryDTO(CategoryDTO categoryDTO) {
    Datatable datatable = categoryRepository.getListCategoryDTO(categoryDTO);
    if (datatable != null) {
      List<CategoryDTO> list = (List<CategoryDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        for (CategoryDTO item : list) {
          if (!StringUtils.isStringNullOrEmpty(item.getParentCategoryId())) {
            CategoryDTO parentCategory = categoryRepository
                .findCategoryById(item.getParentCategoryId());
            if (parentCategory != null) {
              item.setParentName(parentCategory.getCategoryName());
            }
          }
        }
      }
    }

    return datatable;
  }

  @Override
  public ResultInSideDto updateCategory(CategoryDTO categoryDTO) {
    ResultInSideDto resultInSideDto;
    CategoryDTO oldDto = findCategoryById(categoryDTO.getCategoryId());
    if (oldDto != null) {
      if (!oldDto.getCategoryName().equalsIgnoreCase(categoryDTO.getCategoryName())) {
        ResultInSideDto rs = checkName(categoryDTO);
        if (RESULT.DUPLICATE.equalsIgnoreCase(rs.getKey())) {
          return rs;
        }
      }
    }
    resultInSideDto = categoryRepository.updateCategory(categoryDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(categoryDTO.getCategoryId().toString());
        dataHistoryChange.setType("UTILITY_CATEGORY");
        //Old Object History
        dataHistoryChange.setOldObject(oldDto);
        dataHistoryChange.setActionType("update");
        //New Object History
        dataHistoryChange.setNewObject(categoryDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCategory(Long id) {
    ResultInSideDto resultInSideDto;
    CategoryDTO oldHis = findCategoryById(id);
    resultInSideDto =  categoryRepository.deleteCategory(id);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new CategoryDTO());
        dataHistoryChange.setType("UTILITY_CATEGORY");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCategory(List<CategoryDTO> categoryListDTO) {
    return categoryRepository.deleteListCategory(categoryListDTO);
  }

  @Override
  public CategoryDTO findCategoryById(Long id) {
    CategoryDTO categoryDTO = categoryRepository.findCategoryById(id);
    if (categoryDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(categoryDTO.getParentCategoryId())) {
        CategoryDTO parentCategoryDTO = categoryRepository
            .findCategoryById(categoryDTO.getParentCategoryId());
        if (parentCategoryDTO != null) {
          categoryDTO.setParentName(parentCategoryDTO.getCategoryName());
        }
      }
    }

    return categoryDTO;
  }

  public ResultInSideDto checkName(CategoryDTO categoryDTO) {
    ResultInSideDto rs = new ResultInSideDto();
    if (categoryDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(categoryDTO.getCategoryName())) {
        boolean check = categoryRepository.checkNameExist(categoryDTO.getCategoryName());
        if (check) {
          rs.setKey(RESULT.DUPLICATE);
          rs.setMessage(I18n.getValidation("CfgCategory.null.unique"));
          return rs;
        }
      }
    }

    return rs;
  }

  @Override
  public ResultInSideDto insertCategory(CategoryDTO categoryDTO) {
    ResultInSideDto resultInSideDto;
    ResultInSideDto rs = checkName(categoryDTO);
    if (RESULT.DUPLICATE.equalsIgnoreCase(rs.getKey())) {
      return rs;
    }
    resultInSideDto = categoryRepository.insertCategory(categoryDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("UTILITY_CATEGORY");
        //Old Object History
        dataHistoryChange.setOldObject(new CategoryDTO());
        dataHistoryChange.setActionType("add");
        //New Object History
        dataHistoryChange.setNewObject(categoryDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  public ResultInSideDto insertOrUpdateListCategory(CategoryDTO categoryDTO) {
    return categoryRepository.insertOrUpdateListCategory(categoryDTO.getCategoryDTO());
  }

  @Override
  public List<CategoryDTO> getListCategory(CategoryDTO categoryDTO) {
    return categoryRepository.getListCategory(categoryDTO);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CategoryDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end

  @Override
  public List<CategoryDTO> getListCategoryDTO2(CategoryDTO categoryDTO) {
    Datatable datatable = categoryRepository.getListCategoryDTO(categoryDTO);
    if (datatable != null) {
      List<CategoryDTO> list = (List<CategoryDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        for (CategoryDTO item : list) {
          if (!StringUtils.isStringNullOrEmpty(item.getParentCategoryId())) {
            CategoryDTO parentCategory = categoryRepository
                .findCategoryById(item.getParentCategoryId());
            if (parentCategory != null) {
              item.setParentName(parentCategory.getCategoryName());
            }
          }
        }
      }
      return (List<CategoryDTO>) datatable.getData();
    }
    return null;
  }

}
