package cn.fzu.edu.furever_home.admin.dto;

import cn.fzu.edu.furever_home.common.enums.ApplicationStatus;
import cn.fzu.edu.furever_home.common.enums.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "管理端领养申请列表项")
public class AdminAdoptSummaryDTO {

    @Schema(description = "领养申请ID")
    private Integer adoptId;

    @Schema(description = "申请人ID")
    private Integer userId;

    @Schema(description = "申请人名称")
    private String userName;

    @Schema(description = "宠物ID")
    private Integer animalId;

    @Schema(description = "宠物名称")
    private String animalName;

    @Schema(description = "申请状态（申请中/成功/失败）")
    private ApplicationStatus applicationStatus;

    @Schema(description = "审核状态（待审核/通过/拒绝）")
    private ReviewStatus reviewStatus;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    @Schema(description = "审核完成时间")
    private LocalDateTime passTime;
}


