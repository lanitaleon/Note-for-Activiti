package com.seven.iris.config;

import com.seven.iris.model.OemGroup;
import com.seven.iris.model.OemUser;
import com.seven.iris.service.OemGroupService;
import com.seven.iris.service.OemUserService;
import com.seven.iris.util.ActivitiUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomGroupEntityManager extends GroupEntityManager {
    @Autowired
    private OemUserService oemUserService;
    //系统的用户服务

    @Autowired
    private OemGroupService oemGroupService;
    //系统的角色服务

    @Override
    public Group createNewGroup(String groupId) {
        return new GroupEntity(groupId);
    }

    @Override
    public void insertGroup(Group group) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> list = new ArrayList<>();
        OemUser user = oemUserService.findById(Integer.parseInt(userId));
        if (user != null) {
            List<OemGroup> roles = oemGroupService.findByUserId(Integer.parseInt(userId));
            for (OemGroup role : roles) {
                list.add(ActivitiUtil.toActivitiGroup(role));
            }
        }
        return list;
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }
}
