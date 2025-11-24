package cn.fzu.edu.furever_home.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.fzu.edu.furever_home.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        return new ArrayList<>(userService.getPermissionCodes(userId));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        return new ArrayList<>(userService.getRoleCodes(userId));
    }
}