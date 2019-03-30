package com.seven.iris.config;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomUserEntityFactory implements SessionFactory {
    private CustomUserEntityManager customUserEntityManager;

    @Autowired
    public void setUserEntityManager(CustomUserEntityManager customUserEntityManager) {
        this.customUserEntityManager = customUserEntityManager;
    }

    @Override
    public Class<?> getSessionType() {
        // 返回原始的UserIdentityManager类型
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        // 返回自定义的GroupEntityManager实例
        return customUserEntityManager;
    }
}
