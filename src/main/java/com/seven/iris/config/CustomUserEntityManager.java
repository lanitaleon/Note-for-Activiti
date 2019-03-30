package com.seven.iris.config;

import com.seven.iris.model.OemGroup;
import com.seven.iris.model.OemUser;
import com.seven.iris.service.OemUserService;
import com.seven.iris.service.OemGroupService;
import com.seven.iris.util.ActivitiUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserEntityManager extends UserEntityManager {

    @Autowired
    private OemUserService oemUserService;

    @Autowired
    private OemGroupService oemGroupService;

    @Override
    public User createNewUser(String userId) {
        return new UserEntity(userId);
    }

    @Override
    public void insertUser(User user) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void updateUser(User updatedUser) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public UserEntity findUserById(String userId) {
        return ActivitiUtil.toActivitiUser(oemUserService.findById(Integer.parseInt(userId)));
    }

    @Override
    public void deleteUser(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
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
    public UserQuery createNewUserQuery() {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        throw new RuntimeException("not implement method.");

    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }
}
