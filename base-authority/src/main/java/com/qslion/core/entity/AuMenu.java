package com.qslion.core.entity;

import com.google.common.collect.Sets;
import com.qslion.framework.entity.BaseEntity;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 实体类 - 菜单
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@Entity
@Table(name = "au_menu")
public class AuMenu extends BaseEntity<Long> {

    private String name;
    private String url;
    private Short level;
    private Long parentId;
    private String icon;
    private Short orderNo;
    private Short status;
    private String enableStatus;

    private Set<AuPermission> permissions = Sets.newHashSet();

    @ManyToMany(targetEntity = AuPermission.class, mappedBy = "menus")
    public Set<AuPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<AuPermission> permissions) {
        this.permissions = permissions;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "level", nullable = true)
    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    @Basic
    @Column(name = "parent_id", nullable = true)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "icon", nullable = true, length = 30)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "order_no", nullable = true)
    public Short getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Short orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @Basic
    @Column(name = "enable_status", nullable = true, length = 1)
    public String getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuMenu auMenu = (AuMenu) o;

        if (id != auMenu.id) {
            return false;
        }
        if (name != null ? !name.equals(auMenu.name) : auMenu.name != null) {
            return false;
        }
        if (url != null ? !url.equals(auMenu.url) : auMenu.url != null) {
            return false;
        }
        if (level != null ? !level.equals(auMenu.level) : auMenu.level != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(auMenu.parentId) : auMenu.parentId != null) {
            return false;
        }
        if (icon != null ? !icon.equals(auMenu.icon) : auMenu.icon != null) {
            return false;
        }
        if (orderNo != null ? !orderNo.equals(auMenu.orderNo) : auMenu.orderNo != null) {
            return false;
        }
        if (status != null ? !status.equals(auMenu.status) : auMenu.status != null) {
            return false;
        }
        if (enableStatus != null ? !enableStatus.equals(auMenu.enableStatus)
            : auMenu.enableStatus != null) {
            return false;
        }
        if (createDate != null ? !createDate.equals(auMenu.createDate) : auMenu.createDate != null) {
            return false;
        }
        if (modifyDate != null ? !modifyDate.equals(auMenu.modifyDate) : auMenu.modifyDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (orderNo != null ? orderNo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (enableStatus != null ? enableStatus.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}