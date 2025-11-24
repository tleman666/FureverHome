package cn.fzu.edu.furever_home.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import cn.fzu.edu.furever_home.common.Result;
import cn.fzu.edu.furever_home.user.dto.UserDTO;
import cn.fzu.edu.furever_home.user.request.UpdateUserRequest;
import cn.fzu.edu.furever_home.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "个人信息查看与修改、他人信息查看")
public class UserController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    @Operation(summary = "获取我的信息")
    public Result<?> me() {
        Integer uid = StpUtil.getLoginIdAsInt();
        UserDTO dto = userProfileService.getMe(uid);
        return Result.success(dto);
    }

    @PutMapping("/me")
    @Operation(summary = "更新我的信息")
    public Result<?> updateMe(@RequestBody @Valid UpdateUserRequest req) {
        Integer uid = StpUtil.getLoginIdAsInt();
        userProfileService.updateMe(uid, req);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取他人信息")
    public Result<?> getById(@PathVariable Integer id) {
        UserDTO dto = userProfileService.getById(id);
        return Result.success(dto);
    }
}