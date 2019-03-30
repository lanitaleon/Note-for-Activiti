package com.seven.iris.config;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomGroupEntityFactory implements SessionFactory {

    private CustomGroupEntityManager customGroupEntityManager;

    @Autowired
    public void setGroupEntityManager(CustomGroupEntityManager customGroupEntityManager) {
        this.customGroupEntityManager = customGroupEntityManager;
    }

    @Override
    public Class<?> getSessionType() {
        // 返回原始的GroupIdentityManager类型
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        // 返回自定义的GroupEntityManager实例
        return customGroupEntityManager;
    }
}
