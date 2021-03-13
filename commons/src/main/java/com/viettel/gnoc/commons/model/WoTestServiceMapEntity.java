package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "COMMON_GNOC", name = "WO_TEST_SERVICE_MAP")
@Getter
@Setter
@NoArgsConstructor
public class WoTestServiceMapEntity {

    @Id
    @SequenceGenerator(name = "generator", sequenceName = "wo_test_service_map_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "WO_ID")
    private Long woId;

    @Column(name = "WO_CONFIG_ID")
    private Long woConfigId;

    @Column(name = "INSERT_TIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date insertTime;

    @Column(name = "CD_ID")
    private Long cdId;

    @Column(name = "WO_SUB_ID")
    private String woSubId;

    @Column(name = "FILE_ID")
    private Long fileId;

    public WoTestServiceMapEntity(Long id, Long woId, Long woConfigId, Date insertTime, Long cdId, String woSubId,Long fileId) {
        this.id = id;
        this.woId = woId;
        this.woConfigId = woConfigId;
        this.insertTime = insertTime;
        this.cdId = cdId;
        this.woSubId = woSubId;
        this.fileId = fileId;
    }

    public WoTestServiceMapDTO toDTO() {
        WoTestServiceMapDTO dto = new WoTestServiceMapDTO(
                id == null ? null : id.toString(),
                woId == null ? null : woId.toString(),
                woConfigId == null ? null : woConfigId.toString(),
                insertTime == null ? null : DateTimeUtils.convertDateToString(insertTime),
                cdId == null ? null : cdId.toString(),
                woSubId,
                fileId == null ? null : fileId.toString()
        );
        return dto;
    }
}
