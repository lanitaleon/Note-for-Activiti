package com.seven.iris.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Dream
 * 2018/4/10.
 */
@Controller
@RequestMapping("model")
public class ModelTest {

    @Resource
    private RepositoryService repositoryService;

    @RequestMapping("create")
    public void createModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String modelName = "modelName";
            String modelKey = "modelKey";
            String description = "description";
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(modelName);
            modelData.setKey(modelKey);

            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
            // some
        }
    }

    @RequestMapping("export")
    public void export(HttpServletResponse response, String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            // some
        }
    }

    @RequestMapping("showPic")
    public void showModelPicture(HttpServletResponse response, String modelId) {
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode;
        try {
            modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
            ProcessEngineConfiguration processEngineConfiguration = ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration();
            InputStream inputStream = processDiagramGenerator.generateDiagram(model, "png",
                    processEngineConfiguration.getActivityFontName(),
                    processEngineConfiguration.getLabelFontName(),
                    processEngineConfiguration.getAnnotationFontName(),
                    processEngineConfiguration.getClassLoader(), 1.0);
            OutputStream out = response.getOutputStream();
            for (int b = -1; (b = inputStream.read()) != -1; ) {
                out.write(b);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            // some
        }
    }

    @RequestMapping("deploy")
    public void deployModel(HttpServletRequest request, HttpServletResponse response, String modelId) {
        try {
            //addBpmnModel
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            String processName = modelData.getName() + ".bpmn20.xml";
            repositoryService.createDeployment().name(modelData.getName()).addBpmnModel(processName, model).deploy();
            // addString
//            Model modelData = repositoryService.getModel(modelId);
//            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
//            byte[] bpmnBytes;
//            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
//            bpmnBytes = new BpmnXMLConverter().convertToXML(model);
//            String processName = modelData.getName() + ".bpmn20.xml";
//            repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();

        } catch (Exception e) {
            // err log
        }
    }

    @RequestMapping("validate")
    public void validate(HttpServletRequest request, HttpServletResponse response, String modelId) throws IOException {
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        //验证失败信息的封装ValidationError
        List<ValidationError> validate = defaultProcessValidator.validate(model);
        System.out.println(validate.size());
    }
}
