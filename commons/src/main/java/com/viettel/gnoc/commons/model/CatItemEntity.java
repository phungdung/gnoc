/**
 * @(#)CatItemBO.java 8/7/2015 8:15 AM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CAT_ITEM")
public class CatItemEntity {

  // long serialVersionUID = 2L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cat_item_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ITEM_ID", unique = true)
  private Long itemId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME", nullable = false)
  @Nationalized
  private Date updateTime;

  @Column(name = "ITEM_CODE")
  @Nationalized
  private String itemCode;

  @Column(name = "ITEM_NAME", nullable = false)
  @Nationalized
  private String itemName;

  @Column(name = "ITEM_VALUE")
  @Nationalized
  private String itemValue;

  @Column(name = "CATEGORY_ID", nullable = false, columnDefinition = "Category")
  private Long categoryId;

  @Column(name = "POSITION")
  private Long position;

  @Column(name = "DESCRIPTION")
  @Nationalized
  private String description;

  @Column(name = "EDITABLE")
  private Long editable;

  @Column(name = "PARENT_ITEM_ID")
  private Long parentItemId;

  @Column(name = "STATUS")
  private Long status;

  public CatItemDTO toDTO() {
    return new CatItemDTO(itemId, updateTime, itemCode, itemName, itemValue, categoryId,
        position, description, editable, parentItemId, status);
  }


}
