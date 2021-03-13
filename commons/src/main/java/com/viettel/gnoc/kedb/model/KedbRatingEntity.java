package com.viettel.gnoc.kedb.model;

import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "KEDB_RATING")
public class KedbRatingEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "KEDB_RATING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "KEDB_ID")
  private Long kedbId;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "POINT")
  private Long point;
  @Column(name = "NOTE")
  private String note;

  public KedbRatingInsideDTO toDTO() {
    return new KedbRatingInsideDTO(id, kedbId, userName, point, note);
  }
}
