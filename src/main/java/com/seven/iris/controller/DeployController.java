package com.seven.iris.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seven.iris.model.PageInfo;
import com.seven.iris.util.DateJsonValueProcessor;
import com.seven.iris.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * ���̲������
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/deploy")
public class DeployController {

    /**
     * ע��activitiService����
     */
    @Resource
    private RepositoryService repositoryService;

    /**
     * ��ҳ��ѯ����
     */
    @RequestMapping("/deployPage")
    public String deployPage(String rows, String page, String s_name, HttpServletResponse response) throws Exception {
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        //���ÿҳ��ʾ����
        Integer sizePage = Integer.parseInt(rows);
        pageInfo.setPageSize(sizePage);
        // �ڼ�ҳ
        String pageIndex = page;
        if (pageIndex == null || "".equals(pageIndex)) {
            pageIndex = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(pageIndex) - 1)
                * sizePage);
        //ȡ��������
        long deployCount = repositoryService.createDeploymentQuery().deploymentNameLike("%" + s_name + "%").count();
        //�������̲�ѯʵ��
        List<Deployment> deployList = repositoryService.createDeploymentQuery()
                //����
                .orderByDeploymenTime().desc()
                //����Nameģ����ѯ
                .deploymentNameLike("%" + s_name + "%")
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"resources"});
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(deployList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", deployCount);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ���ϴ����̲���ZIP�ļ�
     */
    @RequestMapping("/addDeploy")
    public String addDeploy(HttpServletResponse response, MultipartFile deployFile) throws Exception {
        //��������
        //����bpmn�ļ�-δ�������̶���
//        repositoryService.createDeployment()
//                .name(deployFile.getOriginalFilename())
//                .addInputStream("makeC.bpmn", deployFile.getInputStream())
//                .deploy();
        //����zip�ļ�-�����������̶���
        repositoryService.createDeployment()
                //��Ҫ������������
                .name(deployFile.getOriginalFilename())
                //���ZIP������
                .addZipInputStream(new ZipInputStream(deployFile.getInputStream()))
                //��ʼ����
                .deploy();
        //����classpath-�����������̶���
//        repositoryService.createDeployment()
//                .name("classpath������ʾ")
//                .addClasspathResource("processes/makeB.bpmn")
//                .addClasspathResource("processes/makeB.png")
//                .deploy();
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ����ɾ������
     */
    @RequestMapping("/delDeploy")
    public String delDeploy(HttpServletResponse response, String ids) throws Exception {
        //����ַ���
        String[] idsStr = ids.split(",");
        for (String str : idsStr) {
            repositoryService.deleteDeployment(str, true);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
}
