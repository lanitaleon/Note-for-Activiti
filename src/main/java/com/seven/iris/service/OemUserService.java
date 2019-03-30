package com.seven.iris.service;

import com.seven.iris.model.OemUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface OemUserService {

    OemUser findById(int userId);

    OemUser userLogin(OemUser user);


    List<OemUser> userPage(Map<String, Object> map);

    int userCount(Map<String, Object> map);

    int deleteUser(List<String> id);

    int updateUser(OemUser user);

    int addUser(OemUser user);
}
