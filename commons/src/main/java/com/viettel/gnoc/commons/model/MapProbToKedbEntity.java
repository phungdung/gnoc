package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
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
@Table(schema = "COMMON_GNOC", name = "MAP_PROB_TO_KEDB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapProbToKedbEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MAP_PROB_TO_KEDB_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "PROB_TYPE_ID_LV1", nullable = false)
  private Long probTypeIdLv1;
  @Column(name = "PROB_TYPE_NAME_LV1", nullable = false)
  private String probTypeNameLv1;
  @Column(name = "PROB_TYPE_ID_LV2", nullable = false)
  private Long probTypeIdLv2;
  @Column(name = "PROB_TYPE_NAME_LV2", nullable = false)
  private String probTypeNameLv2;
  @Column(name = "PROB_TYPE_ID_LV3", nullable = false)
  private Long probTypeIdLv3;
  @Column(name = "PROB_TYPE_NAME_LV3", nullable = false)
  private String probTypeNameLv3;
  @Column(name = "KEDB_CODE", nullable = false)
  private String kedbCode;

  public MapProbToKedbDTO toDTO() {
    return new MapProbToKedbDTO(id,
        probTypeIdLv1, probTypeNameLv1,
        probTypeIdLv2, probTypeNameLv2,
        probTypeIdLv3, probTypeNameLv3,
        kedbCode);
  }
}
