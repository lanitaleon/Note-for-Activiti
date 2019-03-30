package com.seven.iris.dao;

import com.seven.iris.model.OemUser;

import java.util.List;
import java.util.Map;

public interface OemUserDao {

    OemUser findById(int userid);

    /**
     * 登入
     */
    OemUser userLogin(OemUser user);

    /**
     * '
     * 分页查询用户
     */
    List<OemUser> userPage(Map<String, Object> map);

    int userCount(Map<String, Object> map);

    /**
     * 批量删除用户
     */
    int deleteUser(List<String> id);

    /**
     * 修改用户
     */
    int updateUser(OemUser user);

    /**
     * 新增用户
     */
    int addUser(OemUser user);
}
