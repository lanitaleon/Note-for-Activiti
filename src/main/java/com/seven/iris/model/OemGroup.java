package com.seven.iris.model;

public class OemGroup {
    private int id;
    private int oemId;
    private String roleDesc;
    private String roleName;

    public int getOemId() {
        return oemId;
    }

    public void setOemId(int oemId) {
        this.oemId = oemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
