package com.seven.iris.config;

import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Configuration
@AutoConfigureBefore(org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class ActivitiConfiguration implements ProcessEngineConfigurationConfigurer {
    @Resource(name = "customGroupEntityFactory")
    private SessionFactory groupManagerFactory;

    @Resource(name = "customUserEntityFactory")
    private SessionFactory userManagerFactory;

    @Override
    public void configure(SpringProcessEngineConfiguration pec) {
        // 权限设置
        // 禁用activiti默认的权限表
        pec.setDbIdentityUsed(false);
        // model png 中文乱码
        pec.setActivityFontName("Microsoft YaHei UI");
        // 替换activiti的默认权限功能
        List<SessionFactory> customSessionFactories = new ArrayList<>();
        customSessionFactories.add(userManagerFactory);
        customSessionFactories.add(groupManagerFactory);
        if (pec.getCustomSessionFactories() == null) {
            pec.setCustomSessionFactories(customSessionFactories);
        } else {
            pec.getCustomSessionFactories().addAll(customSessionFactories);
        }
    }
}
