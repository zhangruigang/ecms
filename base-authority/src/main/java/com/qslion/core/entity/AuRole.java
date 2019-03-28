package com.qslion.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.qslion.core.enums.AuPartyType;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;

/**
 * 实体类 - 角色
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@Entity
@Table(name = "au_role")
public class AuRole extends PartyEntity implements GrantedAuthority {

    private static final long serialVersionUID = 5739472491120418264L;
    private String name;
    private String value;
    private String description;

    @JsonIgnore
    private AuRoleType roleType;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    public AuRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(AuRoleType roleType) {
        this.roleType = roleType;
    }

    @JsonIgnore
    private Set<AuUser> users = Sets.newHashSet();
    @JsonIgnore
    private Set<AuPermission> permissions = Sets.newHashSet();
    @JsonIgnore
    private Set<AuUserGroup> userGroups = Sets.newHashSet();

    @ManyToMany(targetEntity = AuUserGroup.class, mappedBy = "roles")
    public Set<AuUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<AuUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    @ManyToMany(targetEntity = AuPermission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "au_role_permission", joinColumns = {
        @JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    public Set<AuPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<AuPermission> permissions) {
        this.permissions = permissions;
    }

    @ManyToMany(targetEntity = AuUser.class, mappedBy = "roles")
    public Set<AuUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AuUser> users) {
        this.users = users;
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
    @Column(name = "value", nullable = true, length = 30)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    @Override
    public String getAuthority() {
        return value;
    }

    @Override
    public AuParty buildAuParty() {
        auParty = new AuParty();
        auParty.setAuPartyType(AuPartyType.ROLE);
        auParty.setName(getName());
        auParty.setRemark(getDescription());
        auParty.setEnableStatus(getEnableStatus());
        auParty.setInherit(true);
        auParty.setReal(true);
        return auParty;
    }
}
