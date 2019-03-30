# NoteForActiviti

learning activiti

### 简介

版本 `5.22.0`, 入门 `activiti` 需要了解的内容:

1.`bpmn` 规范, `activiti` 提供的 `Service` , `activiti` 内置的表;

2.流程定义,流程部署,启动流程,流程查询,任务表单

3.重构用户组,动态分配任务,集成 `modeler` ,定时任务

推荐参考: [咖啡兔](http://www.kafeitu.me/activiti.html), [官网API](https://www.activiti.org/javadocs/)

> spring boot,mybatis,activiti

### bpmn规范

`bpmn` 用于定义业务流程图, `activiti` 支持 `bpmn2.0` 规范,并加入了一些自定义的属性;

阅读 `demo` 中的 `bpmn` 文件即可大致了解;

### 流程与表之间的基本关联描述

1.deploy

流程部署成功后,打包上传的 `bpmn` 和 `png` 文件属于静态资源文件,保存在 `act_ge_bytearray` 中;

除了资源文件外,生成的部署信息和流程定义分别保存在 `act_re_deployment` 和 `act_re_procdef` 中;

2.model

流程的一种形式,以 `model` 形式保存流程,信息保存在表 `act_re_model`,`act_ge_bytearray` 中;

`model` 流程的部署同 `bpmn` 流程, 生成 `bpmn` 和 `png` 文件保存在 `act_ge_bytearray` 中

3.processInstance

流程每次启动都会生成一个流程实例,保存在 `act_hi_procint` 表中, `proc_inst_id_` 是流程实例的 `id` ;

4.execution

`execution` 指一个流程实例要执行的过程对象,保存在 `act_ru_execution` 表中;

`execution` 表的 `id_` 字段通常与 `proc_inst_id_` 字段一致,不一致时代表该 `execution` 是子流程;

5.task

流程图中的每个节点都是一个任务,上一个任务完成后,下一个任务就会被插入到 `act_hi_taskinst` 和 `act_ru_task` 表中;

6.变量

执行任务过程中产生的变量信息(也包含动态表单的信息)存储在 `act_ru_variable` 和 `act_hi_varinst` 表中;

任务完成后,历史变量信息会被存储在 `act_hi_detail` 表中(该表存储哪些信息由配置中的 `HistoryLevel` 决定)

7.执行者

任务的执行者信息存储在 `act_ru_identitylink` 和 `act_hi_identitylink` 表中;

### activiti提供的Service

`activiti` 提供了如下七个接口便于部署、启动、执行、查询流程实例、任务及表单信息;

可参考 `demo` 项目和[官方api](https://www.activiti.org/javadocs/org/activiti/engine/package-summary.html);

<table>
<tbody>
<tr>
<td>RepositoryService</td>
<td>管理流程定义,查询、部署流程</td>
</tr>
<tr>
<td>RuntimeService</td>
<td>执行管理,包括启动、推进、删除流程实例等操作,查询正在运行的流程信息</td>
</tr>
<tr>
<td>TaskService</td>
<td>任务管理,如查询某个人的代办任务</td>
</tr>
<tr>
<td>HistoryService</td>
<td>历史数据管理</td>
</tr>
<tr>
<td>IdentityService</td>
<td>组织机构管理,查询activiti内置的用户组织结构信息</td>
</tr>
<tr>
<td>FormService</td>
<td>一个可选服务，任务表单管理,如查询动态表单属性值</td>
</tr>
<tr>
<td>ManagementService</td>
<td>Service for admin and maintenance operations on the process engine. These operations will typically not be used in a workflow driven application, but are used in for example the operational console.</td>
</tr>
</tbody>
</table>

### 流程内置的表

`activiti` 内置25张表,[表字段详情参考](https://blog.csdn.net/hj7jay/article/details/51302829)

内置表以 `ACT` 开头,表名第二部分代表表的类型,如:

ACT_GE_* -> General, ACT_HI_* -> History, ACT_ID_* -> Identity, ACT_RE_ -> Repository, ACT_RU_* -> Runtime;

<table>
<tbody>
<tr>
<td>表分类</td>
<td>表名</td>
<td>解释</td>
</tr>
<tr>
<td rowspan="2">一般数据</td>
<td>ACT_GE_BYTEARRAY</td>
<td>通用的流程定义和流程资源</td>
</tr>
<tr>
<td>ACT_GE_PROPERTY</td>
<td>系统相关属性</td>
</tr>
<tr>
<td rowspan="8">流程历史记录</td>
<td>ACT_HI_ACTINST</td>
<td>历史的流程实例</td>
</tr>
<tr>
<td>ACT_HI_ATTACHMENT</td>
<td>历史的流程附件</td>
</tr>
<tr>
<td>ACT_HI_COMMENT</td>
<td>历史的说明性信息</td>
</tr>
<tr>
<td>ACT_HI_DETAIL</td>
<td>历史的流程运行中的细节信息</td>
</tr>
<tr>
<td>ACT_HI_IDENTITYLINK</td>
<td>历史的流程运行过程中用户关系</td>
</tr>
<tr>
<td>ACT_HI_PROCINST</td>
<td>历史的流程实例</td>
</tr>
<tr>
<td>ACT_HI_TASKINST</td>
<td>历史的任务实例</td>
</tr>
<tr>
<td>ACT_HI_VARINST</td>
<td>历史的流程运行中的变量信息</td>
</tr>
<tr>
<td rowspan="4">用户用户组表</td>
<td>ACT_ID_GROUP</td>
<td>身份信息-组信息</td>
</tr>
<tr>
<td>ACT_ID_INFO</td>
<td>身份信息-组信息</td>
</tr>
<tr>
<td>ACT_ID_MEMBERSHIP</td>
<td>身份信息-用户和组关系的中间表</td>
</tr>
<tr>
<td>ACT_ID_USER</td>
<td>身份信息-用户信息</td>
</tr>
<tr>
<td rowspan="3">流程定义表</td>
<td>ACT_RE_DEPLOYMENT</td>
<td>部署单元信息</td>
</tr>
<tr>
<td>ACT_RE_MODEL</td>
<td>模型信息</td>
</tr>
<tr>
<td>ACT_RE_PROCDEF</td>
<td>已部署的流程定义</td>
</tr>
<tr>
<td rowspan="6">运行实例表</td>
<td>ACT_RU_EVENT_SUBSCR</td>
<td>运行时事件</td>
</tr>
<tr>
<td>ACT_RU_EXECUTION</td>
<td>运行时流程执行实例</td>
</tr>
<tr>
<td>ACT_RU_IDENTITYLINK</td>
<td>运行时用户关系信息</td>
</tr>
<tr>
<td>ACT_RU_JOB</td>
<td>运行时作业</td>
</tr>
<tr>
<td>ACT_RU_TASK</td>
<td>运行时任务</td>
</tr>
<tr>
<td>ACT_RU_VARIABLE</td>
<td>运行时变量表</td>
</tr>
</tbody>
</table>

### 表单

bpmn文件中有两种定义表单的方式:

1.动态表单

	<userTask activiti:candidateGroups="emp" activiti:exclusive="true" id="usertask6" name="员工发起请假申请">
	    <extensionElements>
	        <activiti:formProperty datePattern="yyyy-MM-dd" id="startTime" name="开始时间" type="date"/>
	        <activiti:formProperty datePattern="yyyy-MM-dd" id="endTime" name="结束时间" type="date"/>
	        <activiti:formProperty id="restReason" name="请假原因" type="string"/>
	    </extensionElements>
	</userTask>

可通过 `formService` 获取表单属性、赋值、提交并完成当前任务;

2.外置表单(普通表单)

	<userTask activiti:candidateGroups="fz" activiti:exclusive="true" activiti:formKey="audit.jsp" id="usertask11" name="审批"/>

可通过 `formService` 获取 `formKey` 自行处理,formKey一般来说是路径+名称;

### 自定义用户组

`activiti` 内置了一套用户组以及认证管理,结构较为简单,不符合使用场景,需要替换为自定义用户组管理;

参考博客:[重构用户组数据的多种方案比较](http://www.kafeitu.me/activiti/2012/04/23/synchronize-or-redesign-user-and-role-for-activiti.html)

目前采用的是自定义 `sessionFactory` 的方式,即替换 `activiti` 原生的用户组查询模块,实现描述如下:

1.配置 `activiti` 使用自定义 `sessionFactory` 

2.自定义 `UserManagerFactory` 和 `GroupManagerFactory` 实现 `SessionFactory` 接口

3.继承 `activiti` 定义的 `UserEntityManager` 和 `GroupEntityManager` , 重写主要方法

替换后可删除表 `act_id_*` ;

该方案在使用的时候需要避免调用未重写的方法;

### 任务的动态分配

任务的动态分配与指派有多种实现方式,适用于不同场景,按需选择;

1.实现 `TaskListener` 接口,如:

 `bpmn`中的定义:

	<userTask activiti:candidateGroups="2" activiti:exclusive="true" activiti:formKey="audit_bz.jsp" id="usertask7" name="项目组长审批">
		<extensionElements>
		    <activiti:taskListener event="create" class="com.xxx.TaskListenerImpl"/>
		</extensionElements>
	</userTask>

`TaskListenerImpl` 的实现:

<pre>
	<code>
public class TaskListenerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.deleteCandidateGroup("manager");
        delegateTask.addCandidateGroup("president");
    }
}
	</code>
</pre>

2.直接指定办理人,如:

    <userTask id="hrAudit" name="人事审批" activiti:assignee="${hrUserId}" />

任务完成后传递该变量;

<pre>
<code>
Map<String, Object> variables = new HashMap<String, Object>();
variables.put("hrUserId", hrUserId);
taskService.complete(taskId, variables);
</code>
</pre>

注意 `assignee` 与 `candidateGroups` 的查询方式不同,故不能混用;

### activiti modeler

`activiti` 提供的网页绘制工具,已集成至 `demo` 项目,[参考博客](https://blog.csdn.net/h1059141989/article/details/79870043);

### 定时任务

act_ru_job,尚未在demo中尝试运用

### spring boot

采用注解取代xml配置项

配置文件application.yml

### 流程回退&撤销

需要自定义扩展

### thymeleaf

静态可以预览,跟jsp语法差不多

### vue.js

