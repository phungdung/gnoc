package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "MAP_FLOW_TEMPLATES")
public class MapFlowTemplatesEntity implements Serializable {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "map_flow_templates_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;
  @Column(name = "TYPE_ID")
  private Long typeId;
  @Column(name = "ALARM_GROUP_ID")
  private Long alarmGroupId;
  @Column(name = "LAST_UPDATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastUpdateTime;
  @Column(name = "USER_ID")
  private Long userID;


  public MapFlowTemplatesDTO toDTO() {
    MapFlowTemplatesDTO dto = new MapFlowTemplatesDTO(
        id == null ? null : id.toString(),
        typeId == null ? null : typeId.toString(),
        alarmGroupId == null ? null : alarmGroupId.toString(),
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, Constants.ddMMyyyyHHmmss),
        userID == null ? null : userID.toString()
    );
    return dto;
  }
}
