package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_IMPACTED_NODES")
public class MrImpactedNodesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_IMPACTED_NODES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MINS_ID", unique = true, nullable = false)
  private Long minsId;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "IP_ID")
  private Long ipId;

  @Column(name = "DEVICE_ID")
  private Long deviceId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  public MrImpactedNodesDTO toDTO() {
    MrImpactedNodesDTO dto = new MrImpactedNodesDTO(
        minsId == null ? null : minsId.toString(), mrId == null ? null : mrId.toString(),
        ipId == null ? null : ipId.toString(), deviceId == null ? null : deviceId.toString(),
        insertTime == null ? null
            : DateTimeUtils.convertDateToString(insertTime, "dd/MM/yyyy HH:mm:ss"), null, null,
        null, null
    );
    return dto;
  }

}
