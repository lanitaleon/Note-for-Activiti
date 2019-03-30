package com.seven.iris.service.impl;

import com.seven.iris.dao.OemGroupDao;
import com.seven.iris.model.OemGroup;
import com.seven.iris.service.OemGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("oemGroupService")
public class OemGroupServiceImpl implements OemGroupService {
    @Resource
    private OemGroupDao oemGroupDao;

    @Override
    public List<OemGroup> findByUserId(int id) {
        return oemGroupDao.findByUserId(id);
    }

    /**
     * 查询所有角色填充下拉框
     */
    @Override
    public List<OemGroup> findGroup() {
        return oemGroupDao.findGroup();
    }


    /**
     * 分页查询
     */
    @Override
    public List<OemGroup> groupPage(Map<String, Object> map) {
        return oemGroupDao.groupPage(map);
    }

    /**
     * 统计数量
     */
    @Override
    public int groupCount(Map<String, Object> map) {
        return oemGroupDao.groupCount(map);
    }

    /**
     * 批量刪除
     */
    @Override
    public int deleteGroup(List<String> list) {
        return oemGroupDao.deleteGroup(list);
    }

    /**
     * 修改
     */
    @Override
    public int updateGroup(OemGroup group) {
        return oemGroupDao.updateGroup(group);
    }

    @Override
    public int addGroup(OemGroup group) {
        return oemGroupDao.addGroup(group);
    }
}
