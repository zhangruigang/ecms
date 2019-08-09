package com.qslion.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qslion.framework.entity.BaseEntity;
import com.qslion.framework.enums.EnableStatus;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * 实体类 - 团体基础类
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@MappedSuperclass
public abstract class PartyEntity extends BaseEntity<Long> {

    private static final long serialVersionUID = 4656704236281853404L;

    protected Long parentId;
    @JsonIgnore
    protected AuParty auParty;
    protected EnableStatus enableStatus;
    protected Date enableDate;

    @Basic
    @Column(name = "parent_id", length = 1)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 根据两张表的主键关联
     */
    @OneToOne(targetEntity = AuParty.class, fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    public AuParty getAuParty() {
        return this.auParty == null ? buildAuParty() : this.auParty;
    }

    public void setAuParty(AuParty auParty) {
        this.auParty = auParty;
    }

    @Basic
    @Column(name = "enable_status", length = 1)
    @Enumerated
    public EnableStatus getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(EnableStatus enableStatus) {
        this.enableStatus = enableStatus;
    }

    @Basic
    @Column(name = "enable_date")
    public Date getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Date enableDate) {
        this.enableDate = enableDate;
    }

    /**
     * 构建团体
     *
     * @return AuParty
     */
    public abstract AuParty buildAuParty();
}
