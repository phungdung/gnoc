package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.TroubleMopDtEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroubleMopDtInSideDTO extends BaseDto {


  private Long troubleDtId;
  private Long troubleMopId;
  private Long dtId;
  private String dtName;
  private String path;
  private Date createTime;
  private Long state;
  private String nodes;
  private String resultDetail;
  private String dtFileType;
  private String stateName;
  private byte[] fileDocumentByteArray;
  private String troubleId;

  public TroubleMopDtEntity toEntity() {
    return new TroubleMopDtEntity(troubleDtId, troubleMopId, dtId, dtName, path, createTime, state,
        nodes, resultDetail, dtFileType);
  }

  public TroubleMopDtInSideDTO(Long troubleDtId, Long troubleMopId, Long dtId, String dtName,
      String path, Date createTime, Long state, String nodes, String resultDetail,
      String dtFileType) {
    this.troubleDtId = troubleDtId;
    this.troubleMopId = troubleMopId;
    this.dtId = dtId;
    this.dtName = dtName;
    this.path = path;
    this.createTime = createTime;
    this.state = state;
    this.nodes = nodes;
    this.resultDetail = resultDetail;
    this.dtFileType = dtFileType;
  }
}
