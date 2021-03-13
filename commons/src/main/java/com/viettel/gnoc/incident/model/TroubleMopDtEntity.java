package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
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

/**
 * @author ITSOL
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TROUBLE_MOP_DT")
public class TroubleMopDtEntity {


  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_MOP_DT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")

  @Column(name = "TROUBLE_DT_ID", nullable = false)
  private Long troubleDtId;
  @Column(name = "TROUBLE_MOP_ID")
  private Long troubleMopId;
  @Column(name = "DT_ID")
  private Long dtId;
  @Column(name = "DT_NAME")
  private String dtName;
  @Column(name = "PATH")
  private String path;
  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date createTime;
  @Column(name = "STATE")
  private Long state;
  @Column(name = "NODES")
  private String nodes;
  @Column(name = "RESULT_DETAIL")
  private String resultDetail;
  @Column(name = "DT_FILE_TYPE")
  private String dtFileType;

  public TroubleMopDtInSideDTO toDTO() {
    return new TroubleMopDtInSideDTO(troubleDtId, troubleMopId, dtId, dtName, path, createTime,
        state, nodes, resultDetail, dtFileType);
  }


}
