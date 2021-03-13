package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import java.lang.reflect.Field;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Slf4j
public class LogChangeConfigRepositoryImpl extends BaseRepository implements
    LogChangeConfigRepository {

  @Override
  public ResultInSideDto insertLog(LogChangeConfigDTO logChangeConfigDTO) {
    String content = logChangeConfigDTO.getContent();
    if (logChangeConfigDTO.getObjNew() != null && logChangeConfigDTO.getObjOld() != null) {
      content = content + " \n " + printfObject(logChangeConfigDTO.getObjNew(),
          logChangeConfigDTO.getObjOld());
    }
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setContent(content);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  private String printfObject(Object objNew, Object objOld) {

    String temp = "";
    try {
      if (objOld != null) {
        Field[] oldF = objOld.getClass().getDeclaredFields();
        for (int i = 0; i < oldF.length; i++) {
          try {
            temp = temp + ", " + oldF[i].getName() + ": " + PropertyUtils
                .getSimpleProperty(objOld, oldF[i].getName());
          } catch (Exception e) {
            log.error("Exception:", e);
          }
        }
      }
      temp = temp + " \n ---------------------------------------- \n ";
      if (objNew != null) {
        Field[] newF = objNew.getClass().getDeclaredFields();
        for (int i = 0; i < newF.length; i++) {
          try {
            temp = temp + ", " + newF[i].getName() + ": " + PropertyUtils
                .getSimpleProperty(objNew, newF[i].getName());
          } catch (Exception e) {
            log.error("Exception:", e);
          }
        }
      }
    } catch (Exception e) {
      log.error("Exception:", e);
    }
    return temp;
  }
}
