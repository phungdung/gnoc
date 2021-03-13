package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "COMMON_GNOC", name = "REQUEST_SCHEDULE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestScheduleEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "REQUEST_SCHEDULE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_SCHEDULE", nullable = false)
  private Long idSchedule;
  @Column(name = "UNIT_ID", nullable = false)
  private Long unitId;
  @Column(name = "STATUS", nullable = false)
  private Long status;
  @Column(name = "START_DATE", nullable = false)
  private Date startDate;
  @Column(name = "END_DATE", nullable = false)
  private Date endDate;
  @Column(name = "CREATED_USER", nullable = false)
  private String createdUser;
  @Column(name = "CREATED_DATE", nullable = false)
  private Date createdDate;
  @Column(name = "TYPE", nullable = false)
  private String type;
  @Column(name = "DETAIL_SCHEDULE", nullable = true)
  private String detail;
  @Column(name = "WORK_TIME", nullable = false)
  private Long workTime;
  @Column(name = "COMPLICATE_WORK", nullable = true)
  private Long complicateWork;
  @Column(name = "CR_SAME_NODE", nullable = true)
  private Long sameNode;
  @Column(name = "CR_SAME_SERVICE", nullable = true)
  private Long sameService;
  @Column(name = "CR_SAME_NODE_SHIFT", nullable = true)
  private Long sameNodeShift;
  @Column(name = "CR_SAME_SERVICE_SHIFT", nullable = true)
  private Long sameServiceShift;

  public RequestScheduleDTO toDTO() {
    return new RequestScheduleDTO(
        idSchedule,
        unitId,
        status,
        startDate,
        endDate,
        createdUser,
        createdDate,
        type,
        workTime,
        complicateWork,
        sameNode,
        sameService,
        sameNodeShift,
        sameServiceShift,
        detail
    );
  }
}
