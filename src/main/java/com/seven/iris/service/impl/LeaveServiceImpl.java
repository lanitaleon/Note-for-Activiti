package com.seven.iris.service.impl;

import com.seven.iris.dao.LeaveDao;
import com.seven.iris.model.Leave;
import com.seven.iris.service.LeaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("leaveService")
public class LeaveServiceImpl implements LeaveService{
	@Resource 
	private LeaveDao leaveDao;
	@Override
	public List<Leave> leavePage(Map<String,Object> map){
		return leaveDao.leavePage(map);
	}

	@Override
	public int leaveCount(Map<String,Object> map){
		return leaveDao.leaveCount(map);
	}

	@Override
	public int addLeave(Leave leave){
		return leaveDao.addLeave(leave);
	}

	@Override
	public Leave findById(String id){
		return leaveDao.findById(id);
	}

	@Override
	public int updateLeave(Leave leave){
		return leaveDao.updateLeave(leave);
	}

	@Override
	public Leave getLeaveByTaskId(String processInstanceId){
		return leaveDao.getLeaveByTaskId(processInstanceId);
	}
}
