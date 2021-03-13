package com.viettel.gnoc.cr.util;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.WorkLogCategoryRepository;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MrCategoryUtil {

  @Autowired
  WorkLogCategoryRepository workLogCategoryRepository;

  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO) {
    List<WorkLogCategoryInsideDTO> lst = new ArrayList<>();
    try {
      int rowStart = 0;
      int maxRow = 200;
      String sortType = "";
      String sortFieldList = "";
      if (workLogCategoryDTO != null) {
        lst = workLogCategoryRepository
            .getListWorkLogCategoryDTO(workLogCategoryDTO, rowStart, maxRow, sortType,
                sortFieldList);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public Datatable getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    return workLogCategoryRepository.getListWorklogSearch(workLogInsiteDTO);
  }

  public List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto) {
    List<WorkLogInsiteDTO> list = workLogCategoryRepository.getListWorkLogDTO(dto);
    try {
      Double offset = dto.getOffset();
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (list != null && offset != null) {
        for (WorkLogInsiteDTO work : list) {
          if (work.getCreatedDate() != null) {
            Date d = work.getCreatedDate();
            String timeTmp = spd.format(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
            work.setCreatedDate(DateTimeUtils.convertStringToDate(timeTmp));
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition) {
    return workLogCategoryRepository.getListUserGroupBySystem(lstCondition);
  }
}
