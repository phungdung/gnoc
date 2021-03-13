package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_STAFT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftStaftEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_STAFT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "ASSIGN_USER_ID")
  private Long assignUserId;
  @Column(name = "ASSIGN_USER_NAME")
  private String assignUserName;
  @Column(name = "RECEIVE_USER_ID")
  private Long receiveUserId;
  @Column(name = "RECEIVE_USER_NAME")
  private String receiveUserName;
  @Column(name = "SHIFT_HANDOVER_ID")
  private Long shiftHandoverId;

  public ShiftStaftDTO toDTO() {
    return new ShiftStaftDTO(
        id,
        assignUserId,
        assignUserName,
        receiveUserId,
        receiveUserName,
        shiftHandoverId
    );
  }
}
