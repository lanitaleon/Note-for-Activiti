package com.seven.iris.service;

import com.seven.iris.model.MemberShip;

import java.util.Map;

public interface MemberShipService {
    /**
     * @return
     */
    public MemberShip userLogin(Map<String, Object> map);

    public int deleteAllGroupsByUserId(String userId);

    public int addMemberShip(MemberShip memberShip);
}
