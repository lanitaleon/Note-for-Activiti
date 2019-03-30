package com.seven.iris.service.impl;

import com.seven.iris.dao.OemUserDao;
import com.seven.iris.model.OemUser;
import com.seven.iris.service.OemUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("oemUserService")
public class OemUserServiceImpl implements OemUserService {

    @Resource
    private OemUserDao oemUserDao;

    @Override
    public OemUser findById(int userId) {
        return oemUserDao.findById(userId);
    }

    /**
     * 登入
     */
    @Override
    public OemUser userLogin(OemUser user) {
        return oemUserDao.userLogin(user);
    }

    /**
     * '
     * 分页查询用户
     */
    @Override
    public List<OemUser> userPage(Map<String, Object> map) {
        return oemUserDao.userPage(map);
    }

    @Override
    public int userCount(Map<String, Object> map) {
        return oemUserDao.userCount(map);
    }

    /**
     * 批量删除用户
     */
    @Override
    public int deleteUser(List<String> id) {
        return oemUserDao.deleteUser(id);
    }

    /**
     * 修改用户
     */
    @Override
    public int updateUser(OemUser user) {
        return oemUserDao.updateUser(user);
    }

    /**
     * 新增用户
     */
    @Override
    public int addUser(OemUser user) {
        return oemUserDao.addUser(user);
    }
}
