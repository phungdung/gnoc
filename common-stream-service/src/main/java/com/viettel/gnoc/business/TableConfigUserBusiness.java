package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

public interface TableConfigUserBusiness {

  List<TableConfigUserDTO> getListTableConfigUserDTO(TableConfigUserDTO tableConfigUserDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<TableConfigUserDTO> getListTableConfigUserByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<String> getSequenseTableConfigUser(String seqName, int... size);

  String updateTableConfigUser(TableConfigUserDTO tableConfigUserDTO);

  ResultInSideDto insertTableConfigUser(TableConfigUserDTO tableConfigUserDTO);

  String insertOrUpdateListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO);

  TableConfigUserDTO findTableConfigUserById(Long id);

  ResultInSideDto deleteTableConfigUser(Long id);

  String deleteListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO);
}
