package com.seven.iris.controller;

import com.seven.iris.model.*;
import com.seven.iris.service.LeaveService;
import com.seven.iris.util.DateJsonValueProcessor;
import com.seven.iris.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * ��ʷ������ע����
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    // ����activiti�Դ���Service�ӿ�
    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private FormService formService;

    @Resource
    private LeaveService leaveService;

    @Resource
    private HistoryService historyService;


    /**
     * ��ѯ��ʷ������ע
     *
     * @param response
     * @param processInstanceId ����ID
     * @return
     * @throws Exception
     */
    @RequestMapping("/listHistoryCommentWithProcessInstanceId")
    public String listHistoryCommentWithProcessInstanceId(
            HttpServletResponse response, String processInstanceId) throws Exception {
        if (processInstanceId == null) {
            return null;
        }
        List<Comment> commentList = taskService
                .getProcessInstanceComments(processInstanceId);
        // �ı�˳�򣬰�ԭ˳��ķ���˳�򷵻�list
        Collections.reverse(commentList); //����Ԫ�ط�ת
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                //ʱ���ʽת��
                new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * �ض�����˴���ҳ��
     */
    @RequestMapping("/redirectPage")
    public String redirectPage(String taskId, HttpServletResponse response) throws Exception {
        TaskFormData formData = formService.getTaskFormData(taskId);
        String url = formData.getFormKey();
        JSONObject result = new JSONObject();
        result.put("url", url);
        ResponseUtil.write(response, result);
        return null;
    }


    /**
     * �������̷�ҳ��ѯ
     *
     * @param page   ��ǰҳ��
     * @param rows   ÿҳ��ʾҳ��
     * @param s_name ��������
     * @param userId ����ID
     */
    @RequestMapping("/taskPage")
    public String taskPage(HttpServletResponse response, String page, String rows, String s_name, String userId) throws Exception {
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        Integer pageSize = Integer.parseInt(rows);
        pageInfo.setPageSize(pageSize);
        if (page == null || "".equals(page)) {
            page = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page) - 1) * pageInfo.getPageSize());
        // ��ȡ�ܼ�¼��
        long total = taskService.createTaskQuery()
                .taskCandidateUser(userId)
//                .taskAssignee(userId)
                .taskNameLike("%" + s_name + "%")
                .count(); // ��ȡ�ܼ�¼��
        //���뷨�Ļ�������ȥ���ݿ�۲�  ACT_RU_TASK �ı仯
        List<Task> taskList = taskService.createTaskQuery()
                // �����û�id��ѯ
                .taskCandidateUser(userId)
//                .taskAssignee(userId)
                // �����������Ʋ�ѯ
                .taskNameLike("%" + s_name + "%")
                // ���ش���ҳ�Ľ������
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());
        //������Ҫʹ��һ����������ת��һ����Ҫ��ת��JSON��ʽ
        List<MyTask> myTaskList = new ArrayList<>();
        for (Task t : taskList) {
            MyTask myTask = new MyTask();
            myTask.setId(t.getId());
            myTask.setName(t.getName());
            myTask.setCreateTime(t.getCreateTime());
            myTaskList.add(myTask);
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(myTaskList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ��ѯ��ǰ����ͼ
     */
    @RequestMapping("/showCurrentView")
    public ModelAndView showCurrentView(HttpServletResponse response, String taskId) {
        //��ͼ
        ModelAndView mav = new ModelAndView();
        // ��������id��ѯ
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        // ��ȡ���̶���id
        String processDefinitionId = task.getProcessDefinitionId();
        // �������̶����ѯ -> �������̶���id��ѯ
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        // ����id
        mav.addObject("deploymentId", processDefinition.getDeploymentId());
        // ͼƬ��Դ�ļ�����
        mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName());

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
                repositoryService.getProcessDefinition(processDefinitionId);
        // ��ȡ����ʵ��id
        String processInstanceId = task.getProcessInstanceId();
        // ��������ʵ��id��ȡ����ʵ��
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // ���ݻid��ȡ�ʵ��
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());
        //�����View��ͼ���ص���ʾҳ��
        // x����
        mav.addObject("x", activityImpl.getX());
        // y����
        mav.addObject("y", activityImpl.getY());
        // ���
        mav.addObject("width", activityImpl.getWidth());
        // �߶�
        mav.addObject("height", activityImpl.getHeight());
        mav.setViewName("page/currentView");
        return mav;
    }

    /**
     * ��ѯ��ʷ��ע
     *
     * @param taskId ����ID
     */
    @RequestMapping("/listHistoryComment")
    public String listHistoryComment(HttpServletResponse response, String taskId) throws Exception {
        if (taskId == null) {
            taskId = "";
        }
        HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
        JsonConfig jsonConfig = new JsonConfig();
        JSONObject result = new JSONObject();
        List<Comment> commentList = null;
        if (hti != null) {
            commentList = taskService.getProcessInstanceComments(hti.getProcessInstanceId());
            // ����Ԫ�ط�ת
            Collections.reverse(commentList);

            //���ڸ�ʽת��
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        }
        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ����
     *
     * @param taskId    ����id
     * @param leaveDays �������
     * @param comment   ��ע��Ϣ
     * @param state     ���״̬ 1 ͨ�� 2 ����
     */
    @RequestMapping("/audit_bz")
    public String audit_bz(String taskId, Integer leaveDays, String comment, Integer state, HttpServletResponse response, HttpSession session) throws Exception {
        //���ȸ���ID��ѯ����
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        //ȡ�ý�ɫ�û������session����
        MemberShip currentMemberShip = (MemberShip) session.getAttribute("currentMemberShip");
        //ȡ���û�����ɫ��Ϣ
        OemUser currentUser = currentMemberShip.getOemUser();
        OemGroup currentGroup = currentMemberShip.getOemGroup();
        if ("�ܲ�".equals(currentGroup.getRoleDesc()) || "���ܲ�".equals(currentGroup.getRoleDesc())) {
            if (state == 1) {
                String leaveId = (String) taskService.getVariable(taskId, "leaveId");
                Leave leave = leaveService.findById(leaveId);
                leave.setState("���ͨ��");
                // ���������Ϣ
                leaveService.updateLeave(leave);
                variables.put("msg", "ͨ��");
            } else {
                String leaveId = (String) taskService.getVariable(taskId, "leaveId");
                Leave leave = leaveService.findById(leaveId);
                leave.setState("���δͨ��");
                // ���������Ϣ
                leaveService.updateLeave(leave);
                variables.put("msg", "δͨ��");
            }
        }
        if (state == 1) {
            variables.put("msg", "ͨ��");
        } else {
            String leaveId = (String) taskService.getVariable(taskId, "leaveId");
            Leave leave = leaveService.findById(leaveId);
            leave.setState("���δͨ��");
            // ���������Ϣ
            leaveService.updateLeave(leave);
            variables.put("msg", "δͨ��");
        }
        // �������̱���
        variables.put("dasy", leaveDays);
        // ��ȡ����ʵ��id
        String processInstanceId = task.getProcessInstanceId();
        // �����û�id
        Authentication.setAuthenticatedUserId(currentUser.getRealname() + currentUser.getMobileNumber()
                + "[" + currentGroup.getRoleDesc() + "]");
        // �����ע��Ϣ
        taskService.addComment(taskId, processInstanceId, comment);
        // �������
        taskService.complete(taskId, variables);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ��ԃ���������������ʷ���̱� :  act_hi_actinst
     */
    @RequestMapping("/finishedList")
    public String finishedList(HttpServletResponse response, String rows, String page, String s_name, String groupId) throws Exception {
        //ΪʲôҪ�����أ���Ϊ�����״����н����̨ʱ��
        //s_name�ض��ǵ���null�ģ����ֱ��������д����ѯ����оͻ����  % null %�����ͻᵼ�²�ѯ�������
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        Integer pageSize = Integer.parseInt(rows);
        pageInfo.setPageSize(pageSize);
        if (page == null || "".equals(page)) {
            page = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page) - 1) * pageSize);
        //����������ʷʵ����ѯ
        List<HistoricTaskInstance> histList = historyService.createHistoricTaskInstanceQuery()
                //���ݽ�ɫ���Ʋ�ѯ
                .taskCandidateGroup(groupId)
                .taskNameLike("%" + s_name + "%")
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());

        long histCount = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateGroup(groupId)
                .taskNameLike("%" + s_name + "%")
                .count();
        List<MyTask> taskList = new ArrayList<MyTask>();
        //����ݳ�û���õ��ֶΣ���ø�ǰ��ҳ�����ɼ���ѹ��
        for (HistoricTaskInstance hti : histList) {
            MyTask myTask = new MyTask();
            myTask.setId(hti.getId());
            myTask.setName(hti.getName());
            myTask.setCreateTime(hti.getCreateTime());
            myTask.setEndTime(hti.getEndTime());
            taskList.add(myTask);
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(taskList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", histCount);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ��������id��ѯ����ʵ���ľ���ִ�й���
     */
    @RequestMapping("/listAction")
    public String listAction(String taskId, HttpServletResponse response) throws Exception {
        HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        // ��ȡ����ʵ��id
        String processInstanceId = hti.getProcessInstanceId();
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(haiList, jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }
}
