package com.seven.iris.service;

import com.seven.iris.model.OemGroup;

import java.util.List;
import java.util.Map;

public interface OemGroupService {

    List<OemGroup> findByUserId(int id);

    /**
     * 查询所有角色填充下拉框
     */
    List<OemGroup> findGroup();

    /**
     * 分页查询
     */
    List<OemGroup> groupPage(Map<String, Object> map);

    /**
     * 统计数量
     */
    int groupCount(Map<String, Object> map);

    /**
     * 批量刪除
     */
    int deleteGroup(List<String> list);

    /**
     * 修改
     */
    int updateGroup(OemGroup group);

    int addGroup(OemGroup group);
}
