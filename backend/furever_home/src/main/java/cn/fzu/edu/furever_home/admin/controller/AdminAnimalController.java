package cn.fzu.edu.furever_home.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.fzu.edu.furever_home.admin.dto.AdminAnimalDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminAnimalSummaryDTO;
import cn.fzu.edu.furever_home.admin.request.AnimalReviewRequest;
import cn.fzu.edu.furever_home.admin.service.AdminAnimalService;
import cn.fzu.edu.furever_home.common.enums.AdoptionStatus;
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
@RequestMapping("/admin/animals")
@RequiredArgsConstructor
@Tag(name = "管理后台-宠物管理", description = "可领养宠物的列表与审核")
public class AdminAnimalController {

    private final AdminAnimalService adminAnimalService;

    @GetMapping("/pending")
    @SaCheckPermission("animal:read")
    @Operation(summary = "获取待审核的宠物列表", description = "分页查询所有待审核的可领养宠物（包含短期和长期），支持按宠物ID或名称搜索")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<PageResult<AdminAnimalSummaryDTO>> listPending(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "关键字：宠物ID 或 名称模糊") @RequestParam(required = false) String keyword) {
        PageResult<AdminAnimalSummaryDTO> data =
                adminAnimalService.listPending(page, pageSize, keyword);
        return Result.success(data);
    }

    @GetMapping("/short")
    @SaCheckPermission("animal:read")
    @Operation(summary = "获取已发布的短期宠物列表", description = "分页查询已发布的短期领养宠物，支持按宠物ID或名称搜索")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<PageResult<AdminAnimalSummaryDTO>> listShort(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "关键字：宠物ID 或 名称模糊") @RequestParam(required = false) String keyword) {
        PageResult<AdminAnimalSummaryDTO> data =
                adminAnimalService.listApprovedByType(page, pageSize, keyword, AdoptionStatus.SHORT_TERM);
        return Result.success(data);
    }

    @GetMapping("/long")
    @SaCheckPermission("animal:read")
    @Operation(summary = "获取已发布的长期宠物列表", description = "分页查询已发布的长期领养宠物，支持按宠物ID或名称搜索")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<PageResult<AdminAnimalSummaryDTO>> listLong(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "关键字：宠物ID 或 名称模糊") @RequestParam(required = false) String keyword) {
        PageResult<AdminAnimalSummaryDTO> data =
                adminAnimalService.listApprovedByType(page, pageSize, keyword, AdoptionStatus.LONG_TERM);
        return Result.success(data);
    }

    @GetMapping("/{id}")
    @SaCheckPermission("animal:read")
    @Operation(summary = "获取宠物详情", description = "根据宠物ID获取宠物详情")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<AdminAnimalDetailDTO> getDetail(
            @Parameter(description = "宠物ID") @PathVariable Integer id) {
        AdminAnimalDetailDTO dto = adminAnimalService.getDetail(id);
        return Result.success(dto);
    }

    @PostMapping("/{id}/approve")
    @SaCheckPermission("animal:review")
    @Operation(summary = "审核通过宠物", description = "审核通过指定宠物")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> approve(
            @Parameter(description = "宠物ID") @PathVariable Integer id,
            @RequestBody(required = false) @Valid AnimalReviewRequest body) {
        Integer reviewerId = StpUtil.getLoginIdAsInt();
        String reason = body == null ? null : body.getReason();
        adminAnimalService.approve(reviewerId, id, reason);
        return Result.success();
    }

    @PostMapping("/{id}/reject")
    @SaCheckPermission("animal:review")
    @Operation(summary = "审核拒绝宠物", description = "审核拒绝指定宠物并记录原因")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> reject(
            @Parameter(description = "宠物ID") @PathVariable Integer id,
            @RequestBody @Valid AnimalReviewRequest body) {
        Integer reviewerId = StpUtil.getLoginIdAsInt();
        adminAnimalService.reject(reviewerId, id, body.getReason());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("animal:delete")
    @Operation(summary = "删除宠物", description = "根据宠物ID删除宠物")
    @Parameter(name = "Authorization", description = "认证令牌，格式为: Bearer {token}", in = ParameterIn.HEADER, required = true, example = "Bearer xxxxxx")
    public Result<Void> delete(@Parameter(description = "宠物ID") @PathVariable Integer id) {
        adminAnimalService.delete(id);
        return Result.success();
    }
}


