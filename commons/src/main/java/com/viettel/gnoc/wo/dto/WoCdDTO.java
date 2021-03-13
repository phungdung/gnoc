package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoCdEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoCdDTO extends BaseDto {

  private Long woGroupId;
  private Long userId;
  private Long cdId;

  private String woGroupCode;
  private String woGroupName;
  private String userName;
  private String fullName;
  private String email;
  private String mobile;

  private Long assign;
  private String assignName;
  private String resultImport;

  private WoCdGroupInsideDTO woCdGroupInsideDTO;

  private List<Long> listUserIdDel;
  private List<Long> listUserIdInsert;

  public WoCdDTO(Long woGroupId, Long userId, Long cdId) {
    this.woGroupId = woGroupId;
    this.userId = userId;
    this.cdId = cdId;
  }

  public WoCdEntity toEntity() {
    return new WoCdEntity(woGroupId, userId, cdId);
  }

}
