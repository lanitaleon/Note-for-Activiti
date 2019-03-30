package com.seven.iris.dao;

import com.seven.iris.model.Leave;

import java.util.List;
import java.util.Map;

/**
 * ҵ�����
 * @author Administrator
 *
 */
public interface LeaveDao {
		public List<Leave> leavePage(Map<String, Object> map);

		public int leaveCount(Map<String, Object> map);
		
		public int addLeave(Leave leave);
		
		public Leave findById(String id);
		
		public int updateLeave(Leave leave);
		
		public Leave getLeaveByTaskId(String processInstanceId);
}
