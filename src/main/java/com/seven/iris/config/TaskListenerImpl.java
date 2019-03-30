package com.seven.iris.config;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener {
    /**
     * 可以设置任务的办理人（个人组人和组任务）
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.deleteCandidateGroup("2");
        delegateTask.addCandidateGroup("5");
    }
}
