package com.seven.iris.controller;

import com.seven.iris.model.PageInfo;
import com.seven.iris.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 流程定义管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    /**
     * 流程定义分页查询
     */
    @RequestMapping("/processDefinitionPage")
    public String processDefinitionPage(String rows, String s_name, String page, HttpServletResponse response) throws Exception {
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        Integer sizePage = Integer.parseInt(rows);
        pageInfo.setPageSize(sizePage);
        if (page == null || "".equals(page)) {
            page = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page) - 1)
                * sizePage);
        long count = repositoryService.createProcessDefinitionQuery()
                .processDefinitionNameLike("%" + s_name + "%")
                .count();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentId().desc()
                .processDefinitionNameLike("%" + s_name + "%")
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"identityLinks", "processDefinition"});
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(processDefinitionList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", count);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查看流程图
     *
     * @param deploymentId 流程ID
     */
    @RequestMapping("/showView")
    public String showView(String deploymentId, String diagramResourceName, HttpServletResponse response) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
        OutputStream out = response.getOutputStream();
        for (int b=-1;(b = inputStream.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        inputStream.close();
        return null;
    }

}
