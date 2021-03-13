package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class ResultInSideDto {

  Long id;
  String key;
  String message;
  Timestamp systemDate;
  Object object;
  File file;
  String filePath;
  String authToken;
  String link;
  String requestTime;
  String finishTime;
  String idValue;
  Boolean check;

  private int quantitySucc;
  private int quantityFail;
  private Double amount;
  private Double amountIssue;
  private List lstResult;
  private String returnCode;
  private String description;
  private String fileName;
  private Map<String, Object> map;
  private String validateKey;
  private String processId;
  private SrWsToolCrDTO srWsToolCrDTO;
  public ResultInSideDto(Long id, String key, String message) {
    this.id = id;
    this.key = key;
    this.message = message;
  }

  public ResultDTO toResultDTO() {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setId(id == null ? null : String.valueOf(id));
    resultDTO.setKey(key);
    resultDTO.setFinishTime(finishTime);
    resultDTO.setRequestTime(requestTime);
    resultDTO.setMessage(message);
    resultDTO.setQuantityFail(quantityFail);
    resultDTO.setQuantitySucc(quantitySucc);
    resultDTO.setAmount(amount);
    resultDTO.setAmountIssue(amountIssue);
    return resultDTO;
  }
}
