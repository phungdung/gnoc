package com.viettel.gnoc.cr.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_IT_SERIOUS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftItSeriousEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_IT_SERIOUS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "INFO_TICKET")
  private String infoTicket;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "AFFECT")
  private String affect;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "CORRECTIVE_ACTION")
  private String correctiveAction;
  @Column(name = "NEXT_ACTION")
  private String nextAction;
  @Column(name = "COUNTRY")
  private Long country;
  @Column(name = "SHIFT_HANDOVER_ID", nullable = false)
  private Long shiftHandoverId;

  public ShiftItSeriousDTO toDTO() {
    return new ShiftItSeriousDTO(
        id,
        infoTicket,
        description,
        affect,
        reason,
        correctiveAction,
        nextAction,
        country,
        shiftHandoverId
    );
  }
}
