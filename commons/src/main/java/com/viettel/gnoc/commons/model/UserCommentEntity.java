package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.viettel.gnoc.commons.dto.UserCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "USER_COMMENT")
public class UserCommentEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.USER_COMMENT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "SUBJECT")
  private String subject;

  @Column(name = "CREATE_TIME")
  private Date createTime;

  public UserCommentDTO toDTO() {
    return new UserCommentDTO(id, userName, content, subject, createTime);
  }
}
