package com.seven.iris.controller;

import com.seven.iris.model.Leave;
import com.seven.iris.model.OemUser;
import com.seven.iris.model.PageInfo;
import com.seven.iris.service.LeaveService;
import com.seven.iris.util.DateJsonValueProcessor;
import com.seven.iris.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ?????
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Resource
    private LeaveService leaveService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;

    /**
     * ?????????
     */
    @RequestMapping("/leavePage")
    public String leavePage(HttpServletResponse response, String rows,
                            String page, int userId) throws Exception {
        PageInfo<Leave> leavePage = new PageInfo<Leave>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", String.valueOf(userId));
        Integer pageSize = Integer.parseInt(rows);
        leavePage.setPageSize(pageSize);
        if (page == null || "".equals(page)) {
            page = "1";
        }
        leavePage.setPageIndex((Integer.parseInt(page) - 1) * pageSize);
        map.put("pageIndex", leavePage.getPageIndex());
        map.put("pageSize", leavePage.getPageSize());
        int leaveCount = leaveService.leaveCount(map);
        List<Leave> leaveList = leaveService.leavePage(map);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(leaveList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", leaveCount);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ???????
     */
    @RequestMapping("/save")
    public String save(Leave leave, HttpServletResponse response, int userId) throws Exception {
        OemUser user = new OemUser();
        user.setId(userId);
        int resultTotal = 0;
        leave.setLeaveDate(new Date());
        leave.setUser(user);
        resultTotal = leaveService.addLeave(leave);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 提交表单,启动流程
     */
    @RequestMapping("/startApply")
    public String startApply(HttpServletResponse response, String leaveId) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("leaveId", leaveId);
        // activitiemployeeProcess makeA makeC makeF
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("makeA", variables);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
        // 动态表单 - 获取属性
//        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
//        List<FormProperty> formPropertyList = taskFormData.getFormProperties();
//        Map<String, String> formValues = new HashMap<>();
//        for (FormProperty formProperty: formPropertyList) {
//            formValues.put(formProperty.getId(), "2018-10-11");
//        }
//        formService.submitTaskFormData(task.getId(), formValues);
        // 动态指定执行人
//        Map<String, Object> value = new HashMap<>();
//        value.put("hrUserId", '5');
//        taskService.complete(task.getId(), value);
        taskService.complete(task.getId());
        Leave leave = leaveService.findById(leaveId);
        leave.setState("审核中");
        leave.setProcessInstanceId(pi.getProcessInstanceId());
        leaveService.updateLeave(leave);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ??????????
     */
    @RequestMapping("/getLeaveByTaskId")
    public String getLeaveByTaskId(HttpServletResponse response, String taskId) throws Exception {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Leave leave = leaveService.getLeaveByTaskId(task.getProcessInstanceId());
        JSONObject result = new JSONObject();
        result.put("leave", JSONObject.fromObject(leave));
        ResponseUtil.write(response, result);
        return null;
    }
}
