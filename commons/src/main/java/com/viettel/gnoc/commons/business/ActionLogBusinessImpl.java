package com.viettel.gnoc.commons.business;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.JsonResponseBO;
import com.viettel.gnoc.commons.dto.ParameterBO;
import com.viettel.gnoc.commons.dto.RequestInputBO;
import com.viettel.gnoc.commons.repository.ActionLogRepository;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ActionLogBusinessImpl implements ActionLogBusiness {

  @Autowired
  ActionLogRepository actionLogRepository;

  @Override
  public JsonResponseBO getDataJson(RequestInputBO requestInputBO) {
    JsonResponseBO response = new JsonResponseBO();
    try {
      //Thoi gian nhan
      response.setReceiverTime(DateTimeUtils.getSysDateTime());

      if (StringUtils.isStringNullOrEmpty(requestInputBO.getCode())) {
        response.setStatus(1);
        response.setDetailError("SQL code is null");
        return response;
      }

      BaseDto baseDto = actionLogRepository.getSQLByCode(requestInputBO);
      if (baseDto != null && StringUtils.isStringNullOrEmpty(baseDto.getSqlQuery())) {
        response.setStatus(1);
        response.setDetailError("SQL code disable or not exist");
        return response;
      }
      if (baseDto == null) {
        response.setStatus(1);
        response.setDetailError("SQL code disable or not exist");
        return response;
      }

      String content = createDataJson(response, requestInputBO, baseDto.getSqlQuery(),
          baseDto.getTimeOffset());
      if (requestInputBO.getCompressData() == 1) {
        response.setDataJson(content);
      } else {
        response.setDataJson(content);
      }
      //Thoi gian xong
      response.setSendTime(DateTimeUtils.getSysDateTime());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      response.setStatus(1);
      response.setDetailError(ex.toString());
    } finally {
      log.info("[" + requestInputBO.getCode() + "] End function getDataJson");
    }
    return response;
  }

  private String createDataJson(JsonResponseBO response, RequestInputBO requestInputBO,
      String sqlByCode, String dateFormat) throws Exception {
    //Param
    Map<String, Object> mapParam = new HashMap<>();
    if (requestInputBO.getParams() != null && !requestInputBO.getParams().isEmpty()) {
      for (ParameterBO bo : requestInputBO.getParams()) {
        if (StringUtils.isStringNullOrEmpty(bo.getValue())) {
          mapParam.put(bo.getName(), null);
        } else {
          if (StringUtils.isNotNullOrEmpty(bo.getSeparator())) {
            String[] tmps = bo.getValue().split(bo.getSeparator());
            if (tmps.length > 0) {
              List<Object> lst = new ArrayList<>();
              for (String value : tmps) {
                if (StringUtils.isNotNullOrEmpty(bo.getType()) && "NUMBER"
                    .equalsIgnoreCase(bo.getType())) {
                  if (value.matches("-?\\d+\\.?\\d*")) {
                    if (value.contains(".")) {
                      lst.add(Double.parseDouble(value.trim()));
                    } else {
                      lst.add(Long.parseLong(value.trim()));
                    }
                  }
                } else if (StringUtils.isNotNullOrEmpty(bo.getType()) && "DATE"
                    .equalsIgnoreCase(bo.getType())) {
                  if (StringUtils.isNotNullOrEmpty(bo.getFormat())) {
                    lst.add(DateTimeUtils.convertStringToTime(value.trim(), bo.getFormat()));
                  } else {
                    lst.add(new Date(Long.valueOf(value.trim())));
                  }
                } else {
                  lst.add(value.trim());
                }
              }
              mapParam.put(bo.getName(), lst);
            }
          } else {
            String value = bo.getValue();
            if (StringUtils.isNotNullOrEmpty(bo.getType()) && "NUMBER".equalsIgnoreCase(bo.getType())) {
              if (value.matches("-?\\d+\\.?\\d*")) {
                if (value.contains(".")) {
                  mapParam.put(bo.getName(), Double.parseDouble(value.trim()));
                } else {
                  mapParam.put(bo.getName(), Long.parseLong(value.trim()));
                }
              }
            } else if (StringUtils.isNotNullOrEmpty(bo.getType()) && "DATE"
                .equalsIgnoreCase(bo.getType())) {
              if (StringUtils.isNotNullOrEmpty(bo.getFormat())) {
                mapParam.put(bo.getName(),
                    DateTimeUtils.convertStringToTime(value.trim(), bo.getFormat()));
              } else {
                mapParam.put(bo.getName(), new Date(Long.valueOf(value.trim())));
              }
            } else {
              mapParam.put(bo.getName(), value.trim());
            }
          }
        }
      }
    }
    log.info("[" + requestInputBO.getCode() + "] mapParam: " + mapParam);
    log.info("[" + requestInputBO.getCode() + "] execute query ...");
    List<Map<String, Object>> lst = actionLogRepository.getDataFromSQL(sqlByCode, mapParam);
    log.info("[" + requestInputBO.getCode() + "] output size: " + lst.size());
    response.setTotalDataJson(lst.size());
    log.info("[" + requestInputBO.getCode() + "] Conver json ...");
    SimpleDateFormat formatDateJson = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    if (dateFormat != null && !dateFormat.trim().isEmpty()) {
      formatDateJson = new SimpleDateFormat(dateFormat);
    }
    StringWriter sw = new StringWriter();
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
    jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
    jsonGenerator.writeStartObject();
    jsonGenerator.writeArrayFieldStart("data");
    for (Map<String, Object> row : lst) {
      jsonGenerator.writeStartObject();
      for (String key : row.keySet()) {
        String column = key.toLowerCase();
        if (row.get(key) != null) {
          Object value = row.get(key);
          try {
            if (value instanceof String) {
              jsonGenerator.writeStringField(column, value.toString());
            } else if (value instanceof Timestamp) {
              Timestamp time = (Timestamp) value;
              java.util.Date date = new Date(time.getTime());
              jsonGenerator.writeStringField(column, formatDateJson.format(date));
            } else if (value instanceof java.sql.Date) {
              java.util.Date date = new Date(((java.sql.Date) value).getTime());
              jsonGenerator.writeStringField(column, formatDateJson.format(date));
            } else {
              if ((null != value) && value.toString().matches("-?\\d+\\.?\\d*")) {
                if (value.toString().contains(".")) {
                  jsonGenerator.writeNumberField(column, Double.parseDouble(value.toString()));
                } else {
                  jsonGenerator.writeNumberField(column, Long.parseLong(value.toString()));
                }
              }
            }
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new Exception("Error json write value: " + value + ". " + ex.toString());
          }
        }
      }
      jsonGenerator.writeEndObject();
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
    jsonGenerator.flush();
    jsonGenerator.close();

    String content = sw.getBuffer().toString();
    sw.close();
    log.info("[" + requestInputBO.getCode() + "] Convert json success: OK");
    return content;
  }
}
