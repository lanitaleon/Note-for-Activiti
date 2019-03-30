package com.seven.iris.model;

public class MemberShip {

	private OemUser oemUser; // ÓÃ»§
	private OemGroup oemGroup; // ½ÇÉ«
	private String userId;
	private String groupId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public OemUser getOemUser() {
		return oemUser;
	}
	public void setOemUser(OemUser oemUser) {
		this.oemUser = oemUser;
	}
	public OemGroup getOemGroup() {
		return oemGroup;
	}
	public void setOemGroup(OemGroup oemGroup) {
		this.oemGroup = oemGroup;
	}
}
