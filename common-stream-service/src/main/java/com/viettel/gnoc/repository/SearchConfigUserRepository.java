package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

public interface SearchConfigUserRepository {

  List<SearchConfigUserDTO> getListSearchConfigUserDTO(SearchConfigUserDTO searchConfigUserDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<SearchConfigUserDTO> getListSearchConfigUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<String> getSequenseSearchConfigUser(String seqName, int... size);

  String updateSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO);

  ResultInSideDto insertSearchConfigUser(SearchConfigUserDTO SearchConfigUserDTO);

  ResultInSideDto insertOrUpdateListSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO);

  SearchConfigUserDTO findSearchConfigUserById(Long id);

  String deleteSearchConfigUser(Long id);

  ResultInSideDto deleteListSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO);
}
