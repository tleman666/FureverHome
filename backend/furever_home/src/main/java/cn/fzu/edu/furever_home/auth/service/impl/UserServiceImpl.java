package cn.fzu.edu.furever_home.auth.service.impl;

import cn.fzu.edu.furever_home.auth.entity.Permission;
import cn.fzu.edu.furever_home.auth.entity.Role;
import cn.fzu.edu.furever_home.auth.entity.RolePermission;
import cn.fzu.edu.furever_home.auth.entity.UserRole;
import cn.fzu.edu.furever_home.auth.mapper.PermissionMapper;
import cn.fzu.edu.furever_home.auth.mapper.RoleMapper;
import cn.fzu.edu.furever_home.auth.mapper.RolePermissionMapper;
import cn.fzu.edu.furever_home.auth.mapper.UserRoleMapper;
import cn.fzu.edu.furever_home.auth.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public Set<String> getPermissionCodes(Long userId) {
        Integer uid = userId.intValue();
        List<UserRole> urs = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, uid));
        if (urs.isEmpty()) return new HashSet<>();
        List<Integer> roleIds = urs.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rps = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds));
        if (rps.isEmpty()) return new HashSet<>();
        List<Integer> permIds = rps.stream().map(RolePermission::getPermissionId).distinct().collect(Collectors.toList());
        List<Permission> perms = permissionMapper.selectBatchIds(permIds);
        return perms.stream().map(Permission::getPermCode).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRoleCodes(Long userId) {
        Integer uid = userId.intValue();
        List<UserRole> urs = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, uid));
        if (urs.isEmpty()) return new HashSet<>();
        List<Integer> roleIds = urs.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream().map(Role::getRoleCode).collect(Collectors.toSet());
    }
}