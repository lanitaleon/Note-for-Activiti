package com.seven.iris.controller;

import com.seven.iris.model.MemberShip;
import com.seven.iris.model.OemGroup;
import com.seven.iris.model.OemUser;
import com.seven.iris.service.MemberShipService;
import com.seven.iris.util.ResponseUtil;
import com.seven.iris.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * ?????
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/memberShip")
public class MemberShipController {
    @Resource
    private MemberShipService memberShipService;

    @RequestMapping("/updateMemberShip")
    public String updateMemberShip(HttpServletResponse response, String userId, String groupsIds) throws Exception {
        //刪除全部角色
        memberShipService.deleteAllGroupsByUserId(userId);

        if (StringUtil.isNotEmpty(groupsIds)) {
            //分割字符串，以，分割
            String[] idsArr = groupsIds.split(",");
            for (String groupId : idsArr) {
                OemUser user = new OemUser();
                user.setNickname(userId);
                OemGroup group = new OemGroup();
                group.setId(Integer.parseInt(groupId));
                MemberShip memberShip = new MemberShip();
                memberShip.setOemUser(user);
                memberShip.setOemGroup(group);
                memberShipService.addMemberShip(memberShip);
            }
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
}
