package com.seven.iris.model;

import java.util.Date;

/**
 * ��ٵ�ʵ��
 *
 * @author user
 */
public class Leave {
    /**
     * ���
     */
    private int id;
    /**
     * �����
     */
    private OemUser user;
    /**
     * �������
     */
    private Date leaveDate;
    /**
     * �������
     */
    private Integer leaveDays;
    /**
     * ���ԭ��
     */
    private String leaveReason;
    /**
     * ���״̬��δ�ύ,�����,���ͨ��,���δͨ��
     */
    private String state;
    /**
     * ����ʵ��Id
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
