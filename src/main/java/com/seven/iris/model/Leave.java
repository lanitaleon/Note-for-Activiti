package com.seven.iris.model;

import java.util.Date;

/**
 * 请假单实体
 *
 * @author user
 */
public class Leave {
    /**
     * 编号
     */
    private int id;
    /**
     * 请假人
     */
    private OemUser user;
    /**
     * 请假日期
     */
    private Date leaveDate;
    /**
     * 请假天数
     */
    private Integer leaveDays;
    /**
     * 请假原因
     */
    private String leaveReason;
    /**
     * 审核状态：未提交,审核中,审核通过,审核未通过
     */
    private String state;
    /**
     * 流程实例Id
     */
    private String processInstanceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OemUser getUser() {
        return user;
    }

    public void setUser(OemUser user) {
        this.user = user;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


}
