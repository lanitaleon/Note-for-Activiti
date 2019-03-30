package com.seven.iris.service.impl;

import com.seven.iris.dao.MemberShipDao;
import com.seven.iris.model.MemberShip;
import com.seven.iris.service.MemberShipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("memberShipService")
public class MemberShipServiceImpl implements MemberShipService {

    @Resource
    private MemberShipDao memberShipDao;

    /**
     * s
     */
    @Override
    public MemberShip userLogin(Map<String, Object> map) {
        return memberShipDao.userLogin(map);
    }

    @Override
    public int deleteAllGroupsByUserId(String userId) {
        return memberShipDao.deleteAllGroupsByUserId(userId);
    }

    @Override
    public int addMemberShip(MemberShip memberShip) {
        return memberShipDao.addMemberShip(memberShip);
    }
}
