package com.seven.iris.util;

import com.seven.iris.model.OemGroup;
import com.seven.iris.model.OemUser;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

public class ActivitiUtil {

    public static GroupEntity toActivitiGroup(OemGroup role) {
        if (role == null) {
            return null;
        }
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(String.valueOf(role.getId()));
        groupEntity.setName(role.getRoleName());
        groupEntity.setType(role.getRoleDesc());
        groupEntity.setRevision(1);
        return groupEntity;
    }

    public static UserEntity toActivitiUser(OemUser user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(String.valueOf(user.getId()));
        userEntity.setFirstName(user.getRealname());
        userEntity.setLastName(user.getNickname());
        userEntity.setPassword("123");
        userEntity.setEmail(user.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }
}
