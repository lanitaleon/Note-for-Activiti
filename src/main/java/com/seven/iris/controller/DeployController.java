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
 * 流程部署管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/deploy")
public class DeployController {

    /**
     * 注入activitiService服务
     */
    @Resource
    private RepositoryService repositoryService;

    /**
     * 分页查询流程
     */
    @RequestMapping("/deployPage")
    public String deployPage(String rows, String page, String s_name, HttpServletResponse response) throws Exception {
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        //填充每页显示数量
        Integer sizePage = Integer.parseInt(rows);
        pageInfo.setPageSize(sizePage);
        // 第几页
        String pageIndex = page;
        if (pageIndex == null || "".equals(pageIndex)) {
            pageIndex = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(pageIndex) - 1)
                * sizePage);
        //取得总数量
        long deployCount = repositoryService.createDeploymentQuery().deploymentNameLike("%" + s_name + "%").count();
        //创建流程查询实例
        List<Deployment> deployList = repositoryService.createDeploymentQuery()
                //降序
                .orderByDeploymenTime().desc()
                //根据Name模糊查询
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
     * 添上传流程部署ZIP文件
     */
    @RequestMapping("/addDeploy")
    public String addDeploy(HttpServletResponse response, MultipartFile deployFile) throws Exception {
        //创建部署
        //部署bpmn文件-未生成流程定义
//        repositoryService.createDeployment()
//                .name(deployFile.getOriginalFilename())
//                .addInputStream("makeC.bpmn", deployFile.getInputStream())
//                .deploy();
        //部署zip文件-可以生成流程定义
        repositoryService.createDeployment()
                //需要部署流程名称
                .name(deployFile.getOriginalFilename())
                //添加ZIP输入流
                .addZipInputStream(new ZipInputStream(deployFile.getInputStream()))
                //开始部署
                .deploy();
        //部署classpath-可以生成流程定义
//        repositoryService.createDeployment()
//                .name("classpath部署演示")
//                .addClasspathResource("processes/makeB.bpmn")
//                .addClasspathResource("processes/makeB.png")
//                .deploy();
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 批量删除流程
     */
    @RequestMapping("/delDeploy")
    public String delDeploy(HttpServletResponse response, String ids) throws Exception {
        //拆分字符串
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
