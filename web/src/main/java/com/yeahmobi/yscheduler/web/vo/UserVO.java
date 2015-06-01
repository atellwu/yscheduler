package com.yeahmobi.yscheduler.web.vo;

import java.util.Date;

import com.yeahmobi.yscheduler.model.User;

/**
 * @author Leo Liang
 */
public class UserVO {

    private User   user;
    private String teamName;

    public UserVO(User user) {
        this.user = user;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return this.user.getTeamId();
    }

    public Long getId() {
        return this.user.getId();
    }

    public String getName() {
        return this.user.getName();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public String getTelephone() {
        return this.user.getTelephone();
    }

    public String getToken() {
        return this.user.getToken();
    }

    public Date getCreateTime() {
        return this.user.getCreateTime();
    }

    public Date getUpdateTime() {
        return this.user.getUpdateTime();
    }

}
