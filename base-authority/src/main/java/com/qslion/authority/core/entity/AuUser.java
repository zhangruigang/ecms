package com.qslion.authority.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.qslion.framework.bean.DisplayField;
import com.qslion.framework.entity.BaseEntity;
import com.qslion.framework.enums.EnableStatus;
import com.qslion.framework.enums.SexEnum;
import com.qslion.framework.util.ValidatorUtils.AddGroup;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * 用户实体类
 *
 * @author Gray.Z
 * @date 2018/4/21 13:43.
 */
@Entity
@Table(name = "au_user")
public class AuUser extends BaseEntity<Long> implements UserDetails {

    private static final long serialVersionUID = -8685902226854146300L;
    @DisplayField(order = 1, title = "用户名")
    private String username;
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$", groups = AddGroup.class, message = "{custom.pwd.invalid}")
    private String password;

    private String pwdConfirm;

    @DisplayField(order = 2, title = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    @DisplayField(order = 3, title = "手机号码")
    //@Pattern(regexp = "^1([345789])\\d{9}$", message = "手机号码格式错误")
    private String mobile;
    @DisplayField(order = 4, title = "昵称")
    private String nickname;
    private Integer age;
    @DisplayField(order = 5, title = "出生日期")
    private Date birthday;
    private String loginId;
    private String loginIp;
    @DisplayField(order = 6, title = "状态")
    private EnableStatus enableStatus;
    private Date lockedDate;
    private Integer loginFailureCount = 0;
    /**
     * 头像图片地址
     */
    private String avatar;

    private SexEnum sex;

    private String idNumber;

    private String tel;

    private String address;

    private String remark;

    @JsonIgnore
    private Set<AuRole> roles = Sets.newHashSet();

    @JsonIgnore
    private Set<AuUserGroup> userGroups = Sets.newHashSet();

    /**
     * @see org.springframework.security.core.userdetails.User
     */
    private Set<GrantedAuthority> authorities = Sets.newHashSet();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    @ManyToMany(targetEntity = AuUserGroup.class, mappedBy = "users")
    public Set<AuUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<AuUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    @ManyToMany(targetEntity = AuRole.class)
    @JoinTable(name = "au_user_role", joinColumns = {
            @JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<AuRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<AuRole> roles) {
        this.roles = roles;
    }

    @Override
    @Basic
    @Column(name = "username", nullable = false, length = 30)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Basic
    @Column(name = "password", nullable = false, length = 128)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    @JsonIgnore
    public String getPwdConfirm() {
        return pwdConfirm;
    }

    public void setPwdConfirm(String pwdConfirm) {
        this.pwdConfirm = pwdConfirm;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 64)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "mobile", nullable = true, length = 11)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "nickname", nullable = true, length = 30)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "birthday", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "login_id", nullable = true, length = 30)
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Basic
    @Column(name = "login_ip", nullable = true, length = 255)
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Basic
    @Enumerated
    @Column(name = "enable_status", nullable = true, length = 1)
    public EnableStatus getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(EnableStatus enableStatus) {
        this.enableStatus = enableStatus;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "locked_date")
    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    @Basic
    @Column(name = "login_failure_count")
    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }


    @Enumerated
    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "id_number", length = 64)
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    @Basic
    @Column(name = "tel", length = 32)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Transient
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        for (AuRole role : roles) {
            authorities.addAll(role.getPermissions());
        }
        //用户组角色
        userGroups.forEach(auUserGroup -> auUserGroup.getRoles().forEach(role -> authorities.addAll(role.getPermissions())));
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ALL"));
        }
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuUser auUser = (AuUser) o;

        if (id != auUser.id) {
            return false;
        }
        if (username != null ? !username.equals(auUser.username) : auUser.username != null) {
            return false;
        }
        if (password != null ? !password.equals(auUser.password) : auUser.password != null) {
            return false;
        }
        if (email != null ? !email.equals(auUser.email) : auUser.email != null) {
            return false;
        }
        if (mobile != null ? !mobile.equals(auUser.mobile) : auUser.mobile != null) {
            return false;
        }
        if (nickname != null ? !nickname.equals(auUser.nickname) : auUser.nickname != null) {
            return false;
        }
        if (age != null ? !age.equals(auUser.age) : auUser.age != null) {
            return false;
        }
        if (birthday != null ? !birthday.equals(auUser.birthday) : auUser.birthday != null) {
            return false;
        }
        if (loginId != null ? !loginId.equals(auUser.loginId) : auUser.loginId != null) {
            return false;
        }
        if (loginIp != null ? !loginIp.equals(auUser.loginIp) : auUser.loginIp != null) {
            return false;
        }
        if (enableStatus != null ? !enableStatus.equals(auUser.enableStatus)
                : auUser.enableStatus != null) {
            return false;
        }
        if (createDate != null ? !createDate.equals(auUser.createDate) : auUser.createDate != null) {
            return false;
        }
        if (modifyDate != null ? !modifyDate.equals(auUser.modifyDate) : auUser.modifyDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (loginId != null ? loginId.hashCode() : 0);
        result = 31 * result + (loginIp != null ? loginIp.hashCode() : 0);
        result = 31 * result + (enableStatus != null ? enableStatus.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }

    @Transient
    @JsonIgnore
    public boolean isAdmin() {
        return getRoles().stream().anyMatch(auRole -> "ROLE_ADMIN".equals(auRole.getValue()));
    }
}
