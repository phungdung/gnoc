package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.SearchConfigUserEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchConfigUserDTO extends BaseDto {

  //Fields
  private Long searchConfigUserId;
  private Long userId;
  private String userName;
  private String funcKey;
  private String fieldName;
  private String dataType;
  private String fieldValue;
  private Long fieldIndex;
  private List<SearchConfigUserDTO> searchConfigUserDTOS;

  public SearchConfigUserEntity toEntity() {
    return new SearchConfigUserEntity(
        searchConfigUserId, userId, userName, funcKey, fieldName, dataType, fieldValue, fieldIndex
    );
  }

  public SearchConfigUserDTO(Long searchConfigUserId, Long userId, String userName,
      String funcKey, String fieldName, String dataType, String fieldValue, Long fieldIndex) {
    this.searchConfigUserId = searchConfigUserId;
    this.userId = userId;
    this.userName = userName;
    this.funcKey = funcKey;
    this.fieldName = fieldName;
    this.dataType = dataType;
    this.fieldValue = fieldValue;
    this.fieldIndex = fieldIndex;
  }
}
