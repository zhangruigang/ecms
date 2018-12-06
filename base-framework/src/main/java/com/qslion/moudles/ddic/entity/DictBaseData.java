package com.qslion.moudles.ddic.entity;

import com.qslion.framework.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 实体类 - 数据字典基础表
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@Entity
@Table(name = "dict_base_data")
public class DictBaseData extends BaseEntity<Long> {

    /**
     * 编码
     */
    private String code;

    private String name;
    private String status;

    private String description;
    /**
     * 显示序号
     */
    private Short orderNo;

    private DictBaseDataType dictBaseDataType;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    public DictBaseDataType getDictBaseDataType() {
        return dictBaseDataType;
    }

    public DictBaseData setDictBaseDataType(DictBaseDataType dictBaseDataType) {
        this.dictBaseDataType = dictBaseDataType;
        return this;
    }

    @Column(name = "CODE", length = 64)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "NAME", nullable = false, length = 64)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "STATUS", nullable = false, length = 10)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "DESCRIPTION", length = 256)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDER_NO", nullable = false)
    public Short getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Short orderNo) {
        this.orderNo = orderNo;
    }
}