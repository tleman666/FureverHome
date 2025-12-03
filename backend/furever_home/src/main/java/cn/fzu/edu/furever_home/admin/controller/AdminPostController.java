package cn.fzu.edu.furever_home.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.fzu.edu.furever_home.admin.dto.AdminPostDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminPostSummaryDTO;
import cn.fzu.edu.furever_home.admin.request.PostReviewRequest;
import cn.fzu.edu.furever_home.admin.service.AdminPostService;
import cn.fzu.edu.furever_home.common.result.PageResult;
import cn.fzu.edu.furever_home.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
@Tag(name = "管理后台-帖子管理", description = "帖子列表、审核与删除")
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping("/pending")
    @SaCheckPermission("post:read")
    @Operation(summary = "获取待审核帖子列表", description = "分页查询待审核帖子，支持按帖子ID或标题搜索")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<PageResult<AdminPostSummaryDTO>> listPending(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "关键字：帖子ID 或 标题模糊") @RequestParam(required = false) String keyword) {
        PageResult<AdminPostSummaryDTO> data = adminPostService.listPending(page, pageSize, keyword);
        return Result.success(data);
    }

    @GetMapping("/published")
    @SaCheckPermission("post:read")
    @Operation(summary = "获取已发布帖子列表", description = "分页查询已发布帖子，支持按帖子ID或标题搜索")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<PageResult<AdminPostSummaryDTO>> listPublished(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "关键字：帖子ID 或 标题模糊") @RequestParam(required = false) String keyword) {
        PageResult<AdminPostSummaryDTO> data = adminPostService.listPublished(page, pageSize, keyword);
        return Result.success(data);
    }

    @GetMapping("/{id}")
    @SaCheckPermission("post:read")
    @Operation(summary = "获取帖子详情（含评论）", description = "根据帖子ID获取帖子详情和评论列表")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<AdminPostDetailDTO> getDetail(@Parameter(description = "帖子ID") @PathVariable Integer id) {
        AdminPostDetailDTO dto = adminPostService.getDetail(id);
        return Result.success(dto);
    }

    @PostMapping("/{id}/approve")
    @SaCheckPermission("post:review")
    @Operation(summary = "审核通过帖子", description = "审核通过指定帖子")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> approve(
            @Parameter(description = "帖子ID") @PathVariable Integer id,
            @RequestBody(required = false) @Valid PostReviewRequest body) {
        Integer reviewerId = StpUtil.getLoginIdAsInt();
        String reason = body == null ? null : body.getReason();
        adminPostService.approve(reviewerId, id, reason);
        return Result.success();
    }

    @PostMapping("/{id}/reject")
    @SaCheckPermission("post:review")
    @Operation(summary = "审核拒绝帖子", description = "审核拒绝指定帖子并记录原因")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> reject(
            @Parameter(description = "帖子ID") @PathVariable Integer id,
            @RequestBody @Valid PostReviewRequest body) {
        Integer reviewerId = StpUtil.getLoginIdAsInt();
        adminPostService.reject(reviewerId, id, body.getReason());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("post:delete")
    @Operation(summary = "删除帖子", description = "根据帖子ID删除帖子")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> delete(@Parameter(description = "帖子ID") @PathVariable Integer id) {
        adminPostService.delete(id);
        return Result.success();
    }
}


