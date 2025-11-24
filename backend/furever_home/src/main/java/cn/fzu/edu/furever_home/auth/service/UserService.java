package cn.fzu.edu.furever_home.auth.service;

import java.util.Set;

public interface UserService {
    Set<String> getPermissionCodes(Long userId);
    Set<String> getRoleCodes(Long userId);
}