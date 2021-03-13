package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_WORK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftWorkEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_WORK_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "WORK_NAME")
  private String workName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "PROCESS")
  private String process;
  @Column(name = "REASON_EXIST")
  private String reasonExist;
  @Column(name = "CONTACT")
  private String contact;
  @Column(name = "OPINION")
  private String opinion;
  @Column(name = "COUNTRY")
  private Long country;
  @Column(name = "SHIFT_HANDOVER_ID")
  private Long shiftHandoverId;
  @Column(name = "START_TIME")
  private Date startTime;
  @Column(name = "DEADLINE")
  private Date deadLine;
  @Column(name = "OWNER")
  private String owner;
  @Column(name = "HANDLE")
  private String handle;
  @Column(name = "IMPORTANT_LEVEL")
  private String importantLevel;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "NEXT_WORK")
  private String nextWork;
  @Column(name = "WORK_STATUS")
  private String workStatus;

  public ShiftWorkDTO toDTO() {
    return new ShiftWorkDTO(id, workName, description, process, reasonExist, contact, opinion,
        country, shiftHandoverId, startTime, deadLine, owner, handle, importantLevel, result,
        nextWork, workStatus
    );
  }
}
