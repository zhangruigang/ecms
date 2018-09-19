package com.qslion.core.entity;

import com.qslion.core.enums.AuPartyRelationType;
import com.qslion.framework.entity.NestTreeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 实体类 - 团体关系
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@Entity
@Table(name = "au_party_relation")
public class AuPartyRelation extends NestTreeEntity {

    private AuPartyRelationType auPartyRelationType;
    private AuParty auParty;

    private String parentPartyId;
    private String code;
    private String name;

    private Short typeLevel;
    private String orderCode;
    private String isLeaf;
    private String typeIsLeaf;
    private String isInherit;
    private String isChief;
    private String email;

    private String parentPartyName;


    @Transient
    public String getParentPartyName() {
        return parentPartyName;
    }

    public void setParentPartyName(String parentPartyName) {
        this.parentPartyName = parentPartyName;
    }


    @Column(name = "relation_type_id")
    public AuPartyRelationType getAuPartyRelationType() {
        return this.auPartyRelationType;
    }

    public void setAuPartyRelationType(AuPartyRelationType auPartyRelationType) {
        this.auPartyRelationType = auPartyRelationType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    public AuParty getAuParty() {
        return this.auParty;
    }

    public void setAuParty(AuParty auParty) {
        this.auParty = auParty;
    }


    @Column(name = "parent_party_id", length = 32)
    public String getParentPartyId() {
        return this.parentPartyId;
    }

    public void setParentPartyId(String parentPartyId) {
        this.parentPartyId = parentPartyId;
    }

    @Column(name = "code", length = 300)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "type_level", precision = 3, scale = 0)
    public Short getTypeLevel() {
        return this.typeLevel;
    }

    public void setTypeLevel(Short typeLevel) {
        this.typeLevel = typeLevel;
    }

    @Column(name = "order_code", length = 300)
    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Column(name = "is_leaf", nullable = false, length = 1)
    public String getIsLeaf() {
        return this.isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Column(name = "type_is_leaf", nullable = false, length = 1)
    public String getTypeIsLeaf() {
        return this.typeIsLeaf;
    }

    public void setTypeIsLeaf(String typeIsLeaf) {
        this.typeIsLeaf = typeIsLeaf;
    }

    @Column(name = "is_inherit", nullable = false, length = 1)
    public String getIsInherit() {
        return this.isInherit;
    }

    public void setIsInherit(String isInherit) {
        this.isInherit = isInherit;
    }

    @Column(name = "is_chief", nullable = false, length = 1)
    public String getIsChief() {
        return this.isChief;
    }

    public void setIsChief(String isChief) {
        this.isChief = isChief;
    }

    @Column(name = "email")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}