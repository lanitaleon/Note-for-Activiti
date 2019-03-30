package com.seven.iris.controller;

import com.seven.iris.model.MemberShip;
import com.seven.iris.model.OemGroup;
import com.seven.iris.model.OemUser;
import com.seven.iris.model.PageInfo;
import com.seven.iris.service.MemberShipService;
import com.seven.iris.service.OemGroupService;
import com.seven.iris.service.OemUserService;
import com.seven.iris.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ???????
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private OemUserService oemUserService;

    @Resource
    private MemberShipService memberShipService;

    @Resource
    private OemGroupService oemGroupService;

    /**
     * ????
     */
    @RequestMapping("/userLogin")
    public String userLogin(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", request.getParameter("userName"));
        map.put("password", request.getParameter("password"));
        map.put("groupId", request.getParameter("groupId"));
        MemberShip memberShip = memberShipService.userLogin(map);
        JSONObject result = new JSONObject();
        if (memberShip == null) {
            result.put("success", false);
            result.put("errorInfo", "用户不存在");
        } else {
            result.put("success", true);
            request.getSession().setAttribute("currentMemberShip", memberShip);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * ?????????
     */
    @RequestMapping("/userPage")
    public String userPage(HttpServletResponse response,
                           @RequestParam(value = "page", required = false) String page,
                           @RequestParam(value = "rows", required = false) String rows,
                           OemUser user) throws Exception {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("id", user.getId());

        PageInfo<OemUser> userPage = new PageInfo<OemUser>();
        Integer pageSize = Integer.parseInt(rows);
        userPage.setPageSize(pageSize);

        String pageIndex = page;
        if (pageIndex == null || pageIndex == "") {
            pageIndex = "1";
        }
        userPage.setPageIndex((Integer.parseInt(pageIndex) - 1)
                * pageSize);
        int userCount = oemUserService.userCount(userMap);
        userPage.setCount(userCount);
        userMap.put("pageIndex", userPage.getPageIndex());
        userMap.put("pageSize", userPage.getPageSize());

        List<OemUser> cusDevPlanList = oemUserService.userPage(userMap);
        JSONObject json = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(cusDevPlanList);
        json.put("rows", jsonArray);
        json.put("total", userCount);
        ResponseUtil.write(response, json);
        return null;
    }

    /**
     * ??????
     */
    @RequestMapping("/updateUser")
    public String updateUser(HttpServletResponse response, OemUser uses) throws Exception {
        int result = oemUserService.updateUser(uses);
        JSONObject json = new JSONObject();
        if (result > 0) {
            json.put("success", true);
        } else {
            json.put("success", false);
        }
        ResponseUtil.write(response, json);
        return null;
    }

    /**
     * ?????h?????
     */
    @RequestMapping("/deleteUser")
    public String deleteUser(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String id = request.getParameter("ids");
        JSONObject json = new JSONObject();
        List<String> list = new ArrayList<String>();
        String[] strs = id.split(",");
        for (String str : strs) {
            list.add(str);
        }
        try {
            int userResult = oemUserService.deleteUser(list);
            if (userResult > 0) {
                json.put("success", true);
            } else {
                json.put("success", false);
            }
        } catch (Exception e) {
            json.put("success", false);
            e.printStackTrace();
        }
        ResponseUtil.write(response, json);
        return null;
    }

    /**
     * ???????
     */
    @RequestMapping("/usefrSave")
    public String userSave(HttpServletResponse response, OemUser user) throws Exception {
        int userResult = oemUserService.addUser(user);
        JSONObject json = new JSONObject();
        if (userResult > 0) {
            json.put("success", true);
        } else {
            json.put("success", false);
        }
        ResponseUtil.write(response, json);
        return null;
    }

    @RequestMapping("/listWithGroups")
    public String listWithGroups(HttpServletResponse response, String rows, String page, OemUser user) throws Exception {
        PageInfo<OemUser> userPage = new PageInfo<OemUser>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("id", user.getId());
        Integer pageSize = Integer.parseInt(rows);
        userPage.setPageSize(pageSize);

        String pageIndex = page;
        if (pageIndex == null || "".equals(pageIndex)) {
            pageIndex = "1";
        }
        userPage.setPageIndex((Integer.parseInt(pageIndex) - 1)
                * pageSize);
        int userCount = oemUserService.userCount(userMap);
        userPage.setCount(userCount);
        userMap.put("pageIndex", userPage.getPageIndex());
        userMap.put("pageSize", userPage.getPageSize());

        List<OemUser> userList = oemUserService.userPage(userMap);
        for (OemUser users : userList) {
            StringBuffer buffer = new StringBuffer();
            List<OemGroup> groupList = oemGroupService.findByUserId(users.getId());
            for (OemGroup g : groupList) {
                buffer.append(g.getRoleName() + ",");
            }
            if (buffer.length() > 0) {
                users.setGroups(buffer.deleteCharAt(buffer.length() - 1).toString());
            } else {
                user.setGroups(buffer.toString());
            }
        }
        JSONArray jsonArray = JSONArray.fromObject(userList);
        JSONObject result = new JSONObject();
        result.put("rows", jsonArray);
        result.put("total", userCount);
        ResponseUtil.write(response, result);
        return null;
    }

}
